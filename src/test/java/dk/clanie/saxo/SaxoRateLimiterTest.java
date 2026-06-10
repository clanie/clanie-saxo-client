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
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;

import dk.clanie.saxo.dto.SaxoUserDetails;

class SaxoRateLimiterTest {


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
