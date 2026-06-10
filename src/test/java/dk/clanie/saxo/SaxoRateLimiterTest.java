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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import dk.clanie.saxo.SaxoRateLimiter.RateLimitState;
import dk.clanie.saxo.dto.SaxoUserDetails;

class SaxoRateLimiterTest {


	@Test
	void parses_the_per_minute_dimension_and_ignores_the_daily_appday_budget() {
		// Header names taken verbatim from a real Saxo chart-sync response.
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-RateLimit-AppDay-Limit", "10000000");
		headers.add("X-RateLimit-AppDay-Remaining", "9998941");
		headers.add("X-RateLimit-AppDay-Reset", "19243");
		headers.add("X-RateLimit-ChartMinute-Limit", "120");
		headers.add("X-RateLimit-ChartMinute-Remaining", "7");
		headers.add("X-RateLimit-ChartMinute-Reset", "41");

		Optional<RateLimitState> state = SaxoRateLimiter.mostConstrainedPerMinute(headers);

		// The per-minute dimension wins, not AppDay's millions-remaining budget.
		assertThat(state).isPresent();
		assertThat(state.get().remaining()).isEqualTo(7);
	}


	@Test
	void returns_empty_when_only_the_daily_budget_header_is_present() {
		// Saxo omits per-minute headers when the per-minute quota is ample.
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-RateLimit-AppDay-Remaining", "9998941");
		headers.add("X-RateLimit-AppDay-Reset", "19243");

		assertThat(SaxoRateLimiter.mostConstrainedPerMinute(headers)).isEmpty();
	}


	@Test
	void returns_empty_when_there_are_no_rate_limit_headers() {
		assertThat(SaxoRateLimiter.mostConstrainedPerMinute(new HttpHeaders())).isEmpty();
	}


	@Test
	void picks_the_most_constrained_of_several_per_minute_dimensions() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-RateLimit-TradeInfoPricesMinute-Remaining", "212");
		headers.add("X-RateLimit-TradeInfoPricesMinute-Reset", "53");
		headers.add("X-RateLimit-ChartMinute-Remaining", "3");
		headers.add("X-RateLimit-ChartMinute-Reset", "12");

		Optional<RateLimitState> state = SaxoRateLimiter.mostConstrainedPerMinute(headers);

		assertThat(state).isPresent();
		assertThat(state.get().remaining()).isEqualTo(3);
	}


	@Test
	void does_not_flag_the_known_minute_and_day_dimensions() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-RateLimit-AppDay-Remaining", "9998941");
		headers.add("X-RateLimit-AppDay-Reset", "19243");
		headers.add("X-RateLimit-TradeInfoPricesMinute-Remaining", "212");
		headers.add("X-RateLimit-TradeInfoPricesMinute-Reset", "53");
		headers.add("X-RateLimit-ChartMinute-Remaining", "7");
		headers.add("X-RateLimit-ChartMinute-Reset", "41");

		assertThat(SaxoRateLimiter.unrecognizedRateLimitDimensions(headers)).isEmpty();
	}


	@Test
	void flags_a_rate_limit_dimension_that_no_longer_matches_our_assumptions() {
		// If Saxo renamed its per-minute chart limit to a window we don't track,
		// pacing silently stops working — the canary must surface that dimension.
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-RateLimit-AppDay-Remaining", "9998941");
		headers.add("X-RateLimit-AppDay-Reset", "19243");
		headers.add("X-RateLimit-ChartSecond-Remaining", "2");
		headers.add("X-RateLimit-ChartSecond-Reset", "1");

		assertThat(SaxoRateLimiter.unrecognizedRateLimitDimensions(headers))
				.containsExactly("ChartSecond");
	}


	private static SaxoRateLimiter limiterForUser(SaxoSessionHolder holder, String userId) {
		SaxoSession session = new SaxoSession();
		SaxoUserDetails userDetails = new SaxoUserDetails();
		userDetails.setUserId(userId);
		session.setUserDetails(userDetails);
		holder.setSession(session);
		return new SaxoRateLimiter(holder);
	}


	@Test
	void throttle_spaces_successive_requests_by_at_least_the_interval() throws Exception {
		SaxoRateLimiter limiter = limiterForUser(new SaxoSessionHolder(), "user-1");
		Duration interval = Duration.ofMillis(40);
		int requests = 5;

		long startNanos = System.nanoTime();
		for (int i = 0; i < requests; i++) {
			limiter.throttle("chart", interval);
		}
		Duration elapsed = Duration.ofNanos(System.nanoTime() - startNanos);

		// The first request fires immediately; the remaining (requests - 1) are
		// each paced one interval apart, so total elapsed >= (requests-1)*interval.
		assertThat(elapsed).isGreaterThanOrEqualTo(interval.multipliedBy(requests - 1L));
	}


	@Test
	void throttle_is_a_no_op_when_there_is_no_session_user() throws Exception {
		// Fresh holder with no session user set -> nothing to pace, returns at once.
		SaxoRateLimiter limiter = new SaxoRateLimiter(new SaxoSessionHolder());

		long startNanos = System.nanoTime();
		limiter.throttle("chart", Duration.ofSeconds(30));
		Duration elapsed = Duration.ofNanos(System.nanoTime() - startNanos);

		assertThat(elapsed).isLessThan(Duration.ofSeconds(1));
	}


	@Test
	void throttle_is_a_no_op_for_non_positive_or_null_interval() throws Exception {
		SaxoRateLimiter limiter = limiterForUser(new SaxoSessionHolder(), "user-1");

		long startNanos = System.nanoTime();
		limiter.throttle("chart", Duration.ZERO);
		limiter.throttle("chart", Duration.ofMillis(-100));
		limiter.throttle("chart", null);
		Duration elapsed = Duration.ofNanos(System.nanoTime() - startNanos);

		assertThat(elapsed).isLessThan(Duration.ofSeconds(1));
	}


	@Test
	void throttle_serialises_concurrent_callers_onto_an_evenly_spaced_schedule() throws Exception {
		SaxoSessionHolder holder = new SaxoSessionHolder();
		// Each thread gets its own holder-bound session (ThreadLocal), but all
		// share the same userId, so they contend for the same paced schedule.
		Duration interval = Duration.ofMillis(30);
		int threads = 6;
		SaxoRateLimiter limiter = new SaxoRateLimiter(holder);
		CountDownLatch ready = new CountDownLatch(threads);
		CountDownLatch go = new CountDownLatch(1);
		List<Thread> workers = new ArrayList<>();

		long startNanos = System.nanoTime();
		for (int i = 0; i < threads; i++) {
			Thread t = new Thread(() -> {
				SaxoSession session = new SaxoSession();
				SaxoUserDetails userDetails = new SaxoUserDetails();
				userDetails.setUserId("shared-user");
				session.setUserDetails(userDetails);
				holder.setSession(session);
				ready.countDown();
				try {
					go.await();
					limiter.throttle("chart", interval);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			});
			workers.add(t);
			t.start();
		}
		ready.await();
		go.countDown();
		for (Thread t : workers) {
			t.join();
		}
		Duration elapsed = Duration.ofNanos(System.nanoTime() - startNanos);

		// All callers must be spaced out rather than passing through at once:
		// the last of N gets scheduled at >= (N-1) intervals.
		assertThat(elapsed).isGreaterThanOrEqualTo(interval.multipliedBy(threads - 1L));
	}


}
