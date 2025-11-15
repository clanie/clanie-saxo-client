/**
 * Copyright (C) 2025, Claus Nielsen, clausn999@gmail.com
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

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import dk.clanie.saxo.dto.SaxoTokens;
import dk.clanie.saxo.dto.SaxoUserDetails;

@Service
public class SaxoSessionHolder {


	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;


	private ThreadLocal<SaxoSession> threadLocalSession = ThreadLocal.withInitial(SaxoSession::new);


	public void setSession(@NonNull SaxoSession session) {
		threadLocalSession.set(session);
	}


	public SaxoSession getSession() {
		return threadLocalSession.get();
	}


	public void registerSaxoTokens(SaxoTokens tokens) {
		SaxoSession saxoSession = threadLocalSession.get();
		saxoSession.registerTokens(tokens);
	}


	public String getAccessToken() {
		return threadLocalSession.get().getAccessToken();
	}


	/**
	 * Checks if the access token has expired or will expire very soon.
	 */
	public boolean accessTokenHasExpired() {
		Instant expiryTime = threadLocalSession.get().getAccessTokenExpiryTime();
		if (expiryTime == null) return true;
		return expiryTime.isBefore(Instant.now().plusSeconds(10));
	}


	/**
	 * Checks if the refresh token has expired or will expire very soon.
	 */
	public boolean refreshTokenHasExpired() {
		Instant expiryTime = threadLocalSession.get().getRefreshTokenExpiryTime();
		if (expiryTime == null) return true;
		return expiryTime.isBefore(Instant.now().plusSeconds(10));
	}


	public Object getRefreshToken() {
		return threadLocalSession.get().getRefreshToken();
	}


	public SaxoUserDetails getUserDetails() {
		return threadLocalSession.get().getUserDetails();
	}


	void loggedIn(SaxoUserDetails userDetails) {
		SaxoSession saxoSession = threadLocalSession.get();
		saxoSession.setUserDetails(userDetails);
		applicationEventPublisher.publishEvent(new SaxoLoginEvent(saxoSession));
	}


	/**
	 * Removes the SaxoSession from the current thread.
	 */
	public void removeFromThread() {
		threadLocalSession.remove();
	}


	/**
	 * Removes tokens from SaxoSession and removes it from the current thread.
	 */
	public void logOut() {
		SaxoSession saxoSession = threadLocalSession.get();
		SaxoUserDetails userDetails = saxoSession.getUserDetails();
		saxoSession.invalidate();
		removeFromThread();
		applicationEventPublisher.publishEvent(new SaxoLogoutEvent(userDetails));
	}


}
