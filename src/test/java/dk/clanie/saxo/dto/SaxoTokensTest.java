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
package dk.clanie.saxo.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SaxoTokensTest {

	private static final String ACCESS_TOKEN = "eyJhbGciOiJFUzI1NiJ9.access-token-value";
	private static final String REFRESH_TOKEN = "refresh-token-value";


	/**
	 * The whole point of the class's hand-tuned toString: this object used to be logged
	 * whole, which put live credentials in the log store. Asserting on the token
	 * <em>values</em> rather than on the field names is what makes the test bite - a
	 * masked-but-still-printing implementation would pass a name-only check.
	 */
	@Test
	void toStringDoesNotContainTheTokens() {
		SaxoTokens tokens = tokens();

		String rendered = tokens.toString();

		assertThat(rendered).doesNotContain(ACCESS_TOKEN);
		assertThat(rendered).doesNotContain(REFRESH_TOKEN);
	}


	@Test
	void toStringReportsTheTokensAsRedactedWithTheirLength() {
		SaxoTokens tokens = tokens();

		String rendered = tokens.toString();

		assertThat(rendered).contains("accessToken=<redacted, " + ACCESS_TOKEN.length() + " chars>");
		assertThat(rendered).contains("refreshToken=<redacted, " + REFRESH_TOKEN.length() + " chars>");
	}


	@Test
	void toStringKeepsTheFieldsThatAreNotSecret() {
		SaxoTokens tokens = tokens();

		String rendered = tokens.toString();

		assertThat(rendered).contains("tokenType=Bearer");
		assertThat(rendered).contains("expiresIn=1200");
		assertThat(rendered).contains("refreshTokenExpiresIn=3600");
	}


	@Test
	void toStringHandlesMissingTokens() {
		String rendered = new SaxoTokens().toString();

		assertThat(rendered).contains("accessToken=null");
		assertThat(rendered).contains("refreshToken=null");
	}


	private SaxoTokens tokens() {
		SaxoTokens tokens = new SaxoTokens();
		tokens.setAccessToken(ACCESS_TOKEN);
		tokens.setTokenType("Bearer");
		tokens.setExpiresIn(1200);
		tokens.setRefreshToken(REFRESH_TOKEN);
		tokens.setRefreshTokenExpiresIn(3600);
		tokens.setBaseUrl("https://gateway.saxobank.com/openapi/");
		return tokens;
	}


}
