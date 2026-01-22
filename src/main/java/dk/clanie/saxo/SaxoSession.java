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

@Data
@Setter(AccessLevel.PACKAGE)
public class SaxoSession {

	private volatile String accessToken;
	private volatile Instant accessTokenExpiryTime;
	private volatile String refreshToken;
	private volatile Instant refreshTokenExpiryTime;

	private volatile SaxoUserDetails userDetails;
	
	/**
	 * The tenant ID associated with this session, if any.
	 * 
	 * Notice, that it is possible to log in on Saxo without being
	 * logged in to a specific tenant, in that case this field will be null!
	 */
	private volatile @Nullable UUID tenantId;


	void registerTokens(SaxoTokens saxoTokens) {
		accessToken = saxoTokens.getAccessToken();
		accessTokenExpiryTime = Instant.now().plusSeconds(saxoTokens.getExpiresIn());
		refreshToken = saxoTokens.getRefreshToken();
		refreshTokenExpiryTime = Instant.now().plusSeconds(saxoTokens.getRefreshTokenExpiresIn());
	}


	void invalidate() {
		accessToken = null;
		accessTokenExpiryTime = null;
		refreshToken = null;
		refreshTokenExpiryTime = null;
		userDetails = null;
		tenantId = null;
	}


}
