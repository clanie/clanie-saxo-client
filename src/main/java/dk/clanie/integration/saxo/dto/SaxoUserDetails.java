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
package dk.clanie.integration.saxo.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SaxoUserDetails extends SaxoDto {

	/**
	 * Unique key identifying the client that owns the user.
	 */
	@JsonProperty("ClientKey")
	private String clientKey;

	/**
	 * Selected culture for this user. Five letter language culture name. Eg. en-GB.
	 */
	@JsonProperty("Culture")
	private String culture;

	/**
	 * Selected language for this user. The two letter ISO 639-1 language code. See Reference Data Languages endpoint for supported languages.
	 */
	@JsonProperty("Language")
	private String language;

	/**
	 * Status of last login or login attempt.
	 */
	@JsonProperty("LastLoginStatus")
	private String lastLoginStatus;

	/**
	 * Time of last login or login attempt.
	 */
	@JsonProperty("LastLoginTime")
	private Instant lastLoginTime;

	/**
	 * Asset Types that can be traded on all accounts by this user.
	 */
	@JsonProperty("LegalAssetTypes")
	private List<String> legalAssetTypes;

	/**
	 * Indicates whether the user has accepted terms for market data via OpenApi access.
	 */
	@JsonProperty("MarketDataViaOpenApiTermsAccepted")
	private Boolean MarketDataViaOpenApiTermsAccepted;

	/**
	 * The name of the user.
	 */
	@JsonProperty("Name")
	private String name;

	/**
	 * Selected Time Zone for this user. See Reference Data TimeZones endpoint for supported time zones.
	 */
	@JsonProperty("TimeZoneId")
	private Integer timeZoneId;

	/**
	 * Unique ID of the user.
	 */
	@JsonProperty("UserId")
	private String userId;

	/**
	 * The unique key for the user.
	 */
	@JsonProperty("UserKey")
	private String userKey;
	
}
