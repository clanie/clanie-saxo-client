/*
 * Copyright (C) 2026, Claus Nielsen, clausn999@gmail.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package dk.clanie.saxo;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import dk.clanie.saxo.dto.SaxoUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Tracks Saxo API rate-limit state from response headers and allows callers to
 * voluntarily pause before low-priority requests.
 *
 * <p>Saxo reports each rate-limit "dimension" in its own header set, named
 * {@code X-RateLimit-<Dimension>-Limit/-Remaining/-Reset} — e.g.
 * {@code X-RateLimit-ChartMinute-*} for chart requests, alongside the app-wide
 * {@code X-RateLimit-AppDay-*} budget. When a service group has ample quota the
 * per-minute headers may be absent, so missing headers mean "plenty left" — not
 * a bug.
 *
 * <p>Per-minute ({@code *Minute}) and per-list-call ({@code *List}) dimensions
 * are independent per service group: chart quota and trade (price) quota are
 * separate. We track the most constrained short-window dimension and ignore the
 * large daily {@code AppDay} budget.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SaxoRateLimiter {

    private final SaxoSessionHolder sessionHolder;

    record RateLimitState(int remaining, Instant resetAt) {}

    /** Key: "userId:serviceGroup" */
    private final ConcurrentHashMap<String, RateLimitState> states = new ConcurrentHashMap<>();

    /**
     * Earliest instant at which the next request for a given service group may
     * be sent, used by {@link #throttle} to pace outbound requests. Key:
     * "userId:serviceGroup".
     */
    private final ConcurrentHashMap<String, AtomicReference<Instant>> nextSlots = new ConcurrentHashMap<>();

    /** Dimensions already warned about, so the header-drift canary logs at most once each. */
    private final Set<String> warnedDimensions = ConcurrentHashMap.newKeySet();

    /**
     * {@code *List} dimensions whose reset window turned out to be long: warned
     * once, then skipped from pacing to avoid multi-hour waits.
     */
    private static final int LIST_MAX_RESET_SECONDS = 300;
    private static final Set<String> warnedLongListResets = ConcurrentHashMap.newKeySet();


    /**
     * Returns a WebClient filter that updates rate-limit state from response
     * headers. Add this as an inner filter in the WebClient chain so it fires
     * before the error-mapping filter converts 429s to exceptions.
     */
    public ExchangeFilterFunction trackingFilter() {
        return (request, next) -> {
            // Capture session identity on the calling virtual thread (ThreadLocal).
            // doOnNext runs on a Netty thread, so the session must be captured here.
            String userId = Optional.ofNullable(sessionHolder.getSession())
                    .map(SaxoSession::getUserDetails)
                    .map(SaxoUserDetails::getUserId)
                    .orElse(null);
            String serviceGroup = extractServiceGroup(request.url().getPath());
            return next.exchange(request).doOnNext(response -> {
                if (userId != null) {
                    updateState(userId + ":" + serviceGroup, response.headers().asHttpHeaders());
                }
            });
        };
    }

    private static String extractServiceGroup(String path) {
        String[] parts = path.replaceFirst("^/", "").split("/", 2);
        return parts.length > 0 ? parts[0] : "unknown";
    }

    private void updateState(String key, HttpHeaders headers) {
        mostConstrained(headers).ifPresent(state -> {
            states.put(key, state);
            log.debug("Saxo rate limit [{}]: {} remaining, resets {}.", key, state.remaining(), state.resetAt());
        });
        // Canary: if Saxo reports a rate-limit dimension we neither pace against
        // ("*Minute" and short-window "*List") nor knowingly ignore (coarse
        // day/hour budgets like AppDay), our header-name assumptions have likely
        // drifted and the pacing for that group has silently stopped working.
        for (String dimension : unrecognizedRateLimitDimensions(headers)) {
            if (warnedDimensions.add(dimension)) {
                log.warn("Unrecognized Saxo rate-limit dimension '{}' (service group '{}'). SaxoRateLimiter only paces against '*Minute' and short-window '*List' dimensions; if Saxo renamed its rate-limit headers this class must be updated, otherwise rate limiting for this dimension is no longer effective.", dimension, key);
            }
        }
    }

    /**
     * Returns the most constrained short-window rate-limit dimension present in
     * {@code headers} (the one with the fewest requests remaining), or empty
     * when Saxo omitted those headers because there is ample quota.
     *
     * <p>Saxo names rate-limit headers per dimension, e.g.
     * {@code X-RateLimit-ChartMinute-Remaining} / {@code -Reset}. We consider
     * both {@code *Minute} dimensions and {@code *List} dimensions (per-list-call
     * limits such as {@code TradeInfoPricesList}). The large daily
     * {@code X-RateLimit-AppDay-*} budget is deliberately ignored, since its
     * multi-hour reset window must never drive a per-cycle pause.
     *
     * <p>As a guard, any {@code *List} dimension whose reset window exceeds
     * {@value #LIST_MAX_RESET_SECONDS}s is treated as long-window and skipped —
     * a one-time WARN is logged if that ever happens.
     */
    static Optional<RateLimitState> mostConstrained(HttpHeaders headers) {
        RateLimitState constraining = null;
        for (String name : headers.headerNames()) {
            String lower = name.toLowerCase();
            if (!lower.startsWith("x-ratelimit-")) continue;
            boolean isMinute = lower.endsWith("minute-remaining");
            boolean isList = lower.endsWith("list-remaining");
            if (!isMinute && !isList) continue;
            String dimension = name.substring("X-RateLimit-".length(), name.length() - "-Remaining".length());
            String remaining = headers.getFirst(name);
            String reset = headers.getFirst("X-RateLimit-" + dimension + "-Reset");
            if (remaining == null || reset == null) continue;
            try {
                int remainingCount = Integer.parseInt(remaining.trim());
                int resetSeconds = Integer.parseInt(reset.trim());
                if (isList && resetSeconds > LIST_MAX_RESET_SECONDS) {
                    if (warnedLongListResets.add(dimension)) {
                        log.warn("Saxo rate-limit dimension '{}' is a *List dimension but has reset={}s > {}s. " +
                                 "Assumption was that *List dimensions are short-window. " +
                                 "SaxoRateLimiter will not pace against it to avoid unexpectedly long waits. " +
                                 "Update the code if this is wrong.",
                                 dimension, resetSeconds, LIST_MAX_RESET_SECONDS);
                    }
                    continue;
                }
                if (constraining == null || remainingCount < constraining.remaining()) {
                    constraining = new RateLimitState(remainingCount, Instant.now().plusSeconds(resetSeconds));
                }
            } catch (NumberFormatException e) {
                log.warn("Unparseable Saxo rate-limit headers for dimension {}: remaining='{}', reset='{}'.", dimension, remaining, reset);
            }
        }
        return Optional.ofNullable(constraining);
    }

    /**
     * Returns the rate-limit dimensions Saxo reported that this class does not
     * understand: neither a short-window {@code *Minute} or {@code *List}
     * dimension (which we pace against) nor a coarse day/hour budget such as
     * {@code AppDay} (which we deliberately ignore). A non-empty result is a red
     * flag that Saxo's header naming has changed and pacing has silently gone
     * stale.
     */
    static Set<String> unrecognizedRateLimitDimensions(HttpHeaders headers) {
        Set<String> unrecognized = new HashSet<>();
        for (String name : headers.headerNames()) {
            String lower = name.toLowerCase();
            if (!lower.startsWith("x-ratelimit-") || !lower.endsWith("-remaining")) continue;
            String dimension = name.substring("X-RateLimit-".length(), name.length() - "-Remaining".length());
            String dimensionLower = dimension.toLowerCase();
            if (dimensionLower.endsWith("minute") || dimensionLower.endsWith("list")
                    || dimensionLower.endsWith("hour") || dimensionLower.endsWith("day")) continue;
            unrecognized.add(dimension);
        }
        return unrecognized;
    }


    /**
     * If the remaining quota for {@code serviceGroup} is at or below
     * {@code minRemaining}, sleeps until the quota window resets.
     *
     * <p>Call this before low-priority requests (e.g. chart data fetches) to
     * leave headroom for higher-priority requests in the same service group.
     */
    public void waitIfNeeded(String serviceGroup, int minRemaining) throws InterruptedException {
        String userId = currentUserId();
        if (userId == null) return;
        RateLimitState state = states.get(userId + ":" + serviceGroup);
        if (state == null || state.remaining() > minRemaining) return;
        Duration wait = Duration.between(Instant.now(), state.resetAt());
        if (!wait.isPositive()) return;
        log.debug("Saxo chart rate limit low ({} remaining). Pausing {}s for quota reset.", state.remaining(), wait.toSeconds());
        Thread.sleep(wait.plusSeconds(1)); // +1s buffer against clock skew
    }


    /**
     * Proactively paces requests for {@code serviceGroup} so that successive
     * calls are spaced at least {@code minInterval} apart. This smooths out the
     * request bursts (e.g. paginating an instrument's full chart history) that
     * would otherwise exhaust Saxo's per-minute limit in a few seconds.
     *
     * <p>This complements {@link #waitIfNeeded}: pacing keeps us under the limit
     * up front, whereas {@code waitIfNeeded} only reacts <em>after</em> Saxo
     * reports the quota is nearly gone — and only when it chooses to emit the
     * {@code X-RateLimit-*} headers, which is too late to prevent the first
     * burst from being rejected.
     *
     * <p>Slots are reserved lock-free via a compare-and-set on a per-group
     * "next allowed" instant, so concurrent callers on the same service group
     * are serialised onto an evenly-spaced schedule rather than all racing
     * through at once.
     *
     * @param serviceGroup the Saxo service group to pace (e.g. {@code "chart"})
     * @param minInterval  minimum spacing between requests; non-positive or
     *                     {@code null} disables pacing
     */
    public void throttle(String serviceGroup, Duration minInterval) throws InterruptedException {
        if (minInterval == null || !minInterval.isPositive()) return;
        String userId = currentUserId();
        if (userId == null) return;
        AtomicReference<Instant> slot = nextSlots.computeIfAbsent(userId + ":" + serviceGroup, _ -> new AtomicReference<>(Instant.EPOCH));
        Instant scheduledAt;
        while (true) {
            Instant now = Instant.now();
            Instant earliest = slot.get();
            scheduledAt = earliest.isAfter(now) ? earliest : now;
            if (slot.compareAndSet(earliest, scheduledAt.plus(minInterval))) break;
        }
        Duration wait = Duration.between(Instant.now(), scheduledAt);
        if (wait.isPositive()) Thread.sleep(wait);
    }


    private String currentUserId() {
        return Optional.ofNullable(sessionHolder.getSession())
                .map(SaxoSession::getUserDetails)
                .map(SaxoUserDetails::getUserId)
                .orElse(null);
    }


    @EventListener
    void onLogout(SaxoLogoutEvent event) {
        String userId = event.getUserDetails().getUserId();
        states.keySet().removeIf(key -> key.startsWith(userId + ":"));
        nextSlots.keySet().removeIf(key -> key.startsWith(userId + ":"));
    }


}
