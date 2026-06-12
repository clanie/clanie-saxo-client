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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import dk.clanie.saxo.dto.SaxoUserDetails;
import dk.clanie.web.exception.UnauthorizedException;

class SaxoTokenRenewalAspectTest {


	@Test
	void logsOutAndRethrowsWhenRefreshReturnsUnauthorized() {
		SaxoSession session = session("user-1");
		AtomicBoolean accessTokenExpired = new AtomicBoolean(true);
		AtomicInteger refreshCalls = new AtomicInteger();
		TestSaxoSessionHolder sessionHolder = new TestSaxoSessionHolder(session, accessTokenExpired);
		TestSaxoLoginClient loginClient = new TestSaxoLoginClient(() -> {
			refreshCalls.incrementAndGet();
			throw new UnauthorizedException("Unauthorized");
		});
		SaxoTokenRenewalAspect aspect = aspect(sessionHolder, loginClient);

		assertThatThrownBy(() -> aspect.refreshTokenIfRequired())
				.isInstanceOf(UnauthorizedException.class);

		assertThat(refreshCalls.get()).isEqualTo(1);
		assertThat(sessionHolder.logOutCalls.get()).isEqualTo(1);
	}


	@Test
	void refreshIsSerializedAndOnlyOneConcurrentCallerRefreshes() throws Exception {
		SaxoSession session = session("user-1");
		AtomicBoolean accessTokenExpired = new AtomicBoolean(true);
		AtomicInteger refreshCalls = new AtomicInteger();
		TestSaxoSessionHolder sessionHolder = new TestSaxoSessionHolder(session, accessTokenExpired);
		TestSaxoLoginClient loginClient = new TestSaxoLoginClient(() -> {
			refreshCalls.incrementAndGet();
			try {
				Thread.sleep(75);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			accessTokenExpired.set(false);
		});
		SaxoTokenRenewalAspect aspect = aspect(sessionHolder, loginClient);

		CountDownLatch ready = new CountDownLatch(2);
		CountDownLatch go = new CountDownLatch(1);
		Thread t1 = startRefreshThread(aspect, ready, go);
		Thread t2 = startRefreshThread(aspect, ready, go);
		ready.await();
		go.countDown();
		t1.join();
		t2.join();

		assertThat(refreshCalls.get()).isEqualTo(1);
		assertThat(sessionHolder.logOutCalls.get()).isZero();
	}


	private static SaxoTokenRenewalAspect aspect(SaxoSessionHolder sessionHolder, SaxoLoginClient loginClient) {
		SaxoTokenRenewalAspect aspect = new SaxoTokenRenewalAspect();
		ReflectionTestUtils.setField(aspect, "saxoSessionHolder", sessionHolder);
		ReflectionTestUtils.setField(aspect, "saxoLoginClient", loginClient);
		return aspect;
	}


	private static Thread startRefreshThread(SaxoTokenRenewalAspect aspect, CountDownLatch ready, CountDownLatch go) {
		Thread thread = new Thread(() -> {
			ready.countDown();
			try {
				go.await();
				aspect.refreshTokenIfRequired();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});
		thread.start();
		return thread;
	}


	private static SaxoSession session(String userId) {
		SaxoSession session = new SaxoSession();
		SaxoUserDetails userDetails = new SaxoUserDetails();
		userDetails.setUserId(userId);
		session.setUserDetails(userDetails);
		return session;
	}


	private static final class TestSaxoSessionHolder extends SaxoSessionHolder {

		private final SaxoSession session;
		private final AtomicBoolean accessTokenExpired;
		private final AtomicInteger logOutCalls = new AtomicInteger();


		private TestSaxoSessionHolder(SaxoSession session, AtomicBoolean accessTokenExpired) {
			this.session = session;
			this.accessTokenExpired = accessTokenExpired;
		}


		@Override
		public SaxoSession getSession() {
			return session;
		}


		@Override
		public boolean accessTokenHasExpired() {
			return accessTokenExpired.get();
		}


		@Override
		public void logOut() {
			logOutCalls.incrementAndGet();
			accessTokenExpired.set(true);
			session.invalidate();
		}


	}


	private static final class TestSaxoLoginClient extends SaxoLoginClient {

		private final Runnable refreshBehavior;


		private TestSaxoLoginClient(Runnable refreshBehavior) {
			this.refreshBehavior = refreshBehavior;
		}


		@Override
		public void refreshTokens() {
			refreshBehavior.run();
		}


	}


}
