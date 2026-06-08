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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
 * <p>Saxo only emits {@code X-RateLimit-*} headers when a limit is close to
 * being reached (per their documentation), so absent headers mean "plenty of
 * quota left" — not a bug.
 *
 * <p>Limits are 120 requests/minute per session per service group. Service
 * groups are independent: chart quota and trade (price) quota are separate.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SaxoRateLimiter {

    private final SaxoSessionHolder sessionHolder;

    private record RateLimitState(int remaining, Instant resetAt) {}

    /** Key: "userId:serviceGroup" */
    private final ConcurrentHashMap<String, RateLimitState> states = new ConcurrentHashMap<>();


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
        if (log.isDebugEnabled()) {
            headers.forEach((name, values) -> {
                if (name.toLowerCase().startsWith("x-ratelimit-")) {
                    log.debug("Saxo rate limit header [{}]: {}", name, String.join(", ", values));
                }
            });
        }
        String remaining = headers.getFirst("X-RateLimit-Session-Remaining");
        String reset = headers.getFirst("X-RateLimit-Session-Reset");
        if (remaining == null || reset == null) return;
        try {
            int remainingCount = Integer.parseInt(remaining.trim());
            int resetSeconds = Integer.parseInt(reset.trim());
            states.put(key, new RateLimitState(remainingCount, Instant.now().plusSeconds(resetSeconds)));
            log.debug("Saxo rate limit [{}]: {} remaining, resets in {}s.", key, remainingCount, resetSeconds);
        } catch (NumberFormatException e) {
            log.warn("Unparseable Saxo rate-limit headers: X-RateLimit-Session-Remaining='{}', X-RateLimit-Session-Reset='{}'.", remaining, reset);
        }
    }


    /**
     * If the remaining quota for {@code serviceGroup} is at or below
     * {@code minRemaining}, sleeps until the quota window resets.
     *
     * <p>Call this before low-priority requests (e.g. chart data fetches) to
     * leave headroom for higher-priority requests in the same service group.
     */
    public void waitIfNeeded(String serviceGroup, int minRemaining) throws InterruptedException {
        String userId = Optional.ofNullable(sessionHolder.getSession())
                .map(SaxoSession::getUserDetails)
                .map(SaxoUserDetails::getUserId)
                .orElse(null);
        if (userId == null) return;
        RateLimitState state = states.get(userId + ":" + serviceGroup);
        if (state == null || state.remaining() > minRemaining) return;
        Duration wait = Duration.between(Instant.now(), state.resetAt());
        if (!wait.isPositive()) return;
        log.debug("Saxo chart rate limit low ({} remaining). Pausing {}s for quota reset.", state.remaining(), wait.toSeconds());
        Thread.sleep(wait.plusSeconds(1)); // +1s buffer against clock skew
    }


    @EventListener
    void onLogout(SaxoLogoutEvent event) {
        String userId = event.getUserDetails().getUserId();
        states.keySet().removeIf(key -> key.startsWith(userId + ":"));
    }


}
