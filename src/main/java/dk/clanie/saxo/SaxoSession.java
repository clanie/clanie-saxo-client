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

import java.time.Instant;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

import dk.clanie.saxo.dto.SaxoTokens;
import dk.clanie.saxo.dto.SaxoUserDetails;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

/**
 * A logged-in Saxo session: the live tokens, and who they belong to.
 *
 * The tokens are credentials, so they are kept out of {@link #toString()} and shown
 * redacted instead - see {@link dk.clanie.saxo.dto.SaxoTokens}, where the same rule and
 * the reason for it are spelled out.
 */
@Data
@Setter(AccessLevel.PACKAGE)
public class SaxoSession {

	@ToString.Exclude
	private volatile String accessToken;
	private volatile Instant accessTokenExpiryTime;
	@ToString.Exclude
	private volatile String refreshToken;
	private volatile Instant refreshTokenExpiryTime;
	private volatile @Nullable String redirectUri;

	private volatile SaxoUserDetails userDetails;
	
	/**
	 * The tenant ID associated with this session, if any.
	 * 
	 * Notice, that it is possible to log in on Saxo without being
	 * logged in to a specific tenant, in that case this field will be null!
	 */
	private volatile @Nullable UUID tenantId;
	
	/**
	 * The Portfolio user ID associated with this session, if any.
	 * 
	 * This is used to track which Portfolio user owns accounts created
	 * during Saxo synchronization.
	 */
	private volatile @Nullable UUID userId;


	void registerTokens(SaxoTokens saxoTokens, String redirectUri) {
		accessToken = saxoTokens.getAccessToken();
		accessTokenExpiryTime = Instant.now().plusSeconds(saxoTokens.getExpiresIn());
		refreshToken = saxoTokens.getRefreshToken();
		refreshTokenExpiryTime = Instant.now().plusSeconds(saxoTokens.getRefreshTokenExpiresIn());
		this.redirectUri = redirectUri;
	}


	@ToString.Include(name = "accessToken")
	private String redactedAccessToken() {
		return TokenRedaction.redact(accessToken);
	}


	@ToString.Include(name = "refreshToken")
	private String redactedRefreshToken() {
		return TokenRedaction.redact(refreshToken);
	}


	void invalidate() {
		accessToken = null;
		accessTokenExpiryTime = null;
		refreshToken = null;
		refreshTokenExpiryTime = null;
		redirectUri = null;
		userDetails = null;
		tenantId = null;
		userId = null;
	}


}
