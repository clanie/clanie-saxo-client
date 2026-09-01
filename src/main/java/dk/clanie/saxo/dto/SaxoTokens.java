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
package dk.clanie.saxo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import dk.clanie.saxo.TokenRedaction;
import lombok.Data;
import lombok.ToString;

/**
 * The tokens Saxo hands back in exchange for an authorization code.
 *
 * Two of these fields are live credentials, and this object used to be logged whole.
 * They are kept out of {@link #toString()} and shown redacted instead, so the class
 * cannot leak them however it is logged. <b>Any secret added here must be excluded the
 * same way</b> - Lombok includes new fields in {@code toString()} automatically.
 */
@Data
public class SaxoTokens {
	

	@ToString.Exclude
	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("token_type")
	private String tokenType;
	
	@JsonProperty("expires_in")
	private int expiresIn;
	
	@ToString.Exclude
	@JsonProperty("refresh_token")
	private String refreshToken;
	
	@JsonProperty("refresh_token_expires_in")
	private int refreshTokenExpiresIn;
	
	@JsonProperty("base_uri")
	private String baseUrl;


	@ToString.Include(name = "accessToken")
	private String redactedAccessToken() {
		return TokenRedaction.redact(accessToken);
	}


	@ToString.Include(name = "refreshToken")
	private String redactedRefreshToken() {
		return TokenRedaction.redact(refreshToken);
	}


}
