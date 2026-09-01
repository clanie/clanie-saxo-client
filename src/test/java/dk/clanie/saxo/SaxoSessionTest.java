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

import org.junit.jupiter.api.Test;

import dk.clanie.saxo.dto.SaxoTokens;

class SaxoSessionTest {


	@Test
	void registerTokensStoresRedirectUriForLaterRefreshes() {
		SaxoSession session = new SaxoSession();
		SaxoTokens tokens = new SaxoTokens();
		tokens.setAccessToken("access-token");
		tokens.setRefreshToken("refresh-token");
		tokens.setExpiresIn(1200);
		tokens.setRefreshTokenExpiresIn(2400);
		String redirectUri = "https://portfolio.clanie.dk/saxo/login/code";

		session.registerTokens(tokens, redirectUri);

		assertThat(session.getRedirectUri()).isEqualTo(redirectUri);
		assertThat(session.getAccessToken()).isEqualTo("access-token");
		assertThat(session.getRefreshToken()).isEqualTo("refresh-token");
	}


	@Test
	void invalidateClearsRedirectUri() {
		SaxoSession session = new SaxoSession();
		SaxoTokens tokens = new SaxoTokens();
		tokens.setAccessToken("access-token");
		tokens.setRefreshToken("refresh-token");
		tokens.setExpiresIn(1200);
		tokens.setRefreshTokenExpiresIn(2400);
		session.registerTokens(tokens, "https://portfolio.clanie.dk/saxo/login/code");

		session.invalidate();

		assertThat(session.getRedirectUri()).isNull();
	}


	/**
	 * The session holds the same credentials the tokens DTO does, so it has to keep them
	 * out of its toString for the same reason - see SaxoTokensTest.
	 */
	@Test
	void toStringDoesNotContainTheTokens() {
		SaxoSession session = new SaxoSession();
		SaxoTokens tokens = new SaxoTokens();
		tokens.setAccessToken("access-token");
		tokens.setRefreshToken("refresh-token");
		tokens.setExpiresIn(1200);
		tokens.setRefreshTokenExpiresIn(2400);
		session.registerTokens(tokens, "https://portfolio.clanie.dk/saxo/login/code");

		String rendered = session.toString();

		assertThat(rendered).doesNotContain("access-token");
		assertThat(rendered).doesNotContain("refresh-token");
		assertThat(rendered).contains("accessToken=<redacted, 12 chars>");
		assertThat(rendered).contains("refreshToken=<redacted, 13 chars>");
	}


}
