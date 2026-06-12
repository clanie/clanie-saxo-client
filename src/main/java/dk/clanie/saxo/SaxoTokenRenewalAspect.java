/*
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

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dk.clanie.web.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class SaxoTokenRenewalAspect {


	@Autowired
	private SaxoSessionHolder saxoSessionHolder;

	@Autowired
	private SaxoLoginClient saxoLoginClient;


	@Pointcut("execution(* dk.clanie.saxo.SaxoClient.*(..))")
	public void allSaxoClientMethods() {}

	@Before("allSaxoClientMethods() && !@annotation(dk.clanie.saxo.SaxoSkipTokenRenewal)")
	public void refreshTokenIfRequired() {
		if (!saxoSessionHolder.accessTokenHasExpired()) {
			return;
		}
		SaxoSession session = saxoSessionHolder.getSession();
		synchronized (session) {
			if (!saxoSessionHolder.accessTokenHasExpired()) {
				return;
			}
			log.debug("Saxo access token has expired. Refreshing.");
			try {
				saxoLoginClient.refreshTokens();
			} catch (UnauthorizedException e) {
				String userId = session.getUserDetails() != null ? session.getUserDetails().getUserId() : "unknown";
				log.warn("Saxo token refresh failed with 401 for userId='{}'. Logging out Saxo session; re-authentication is required.", userId);
				saxoSessionHolder.logOut();
				throw e;
			}
		}
	}


}
