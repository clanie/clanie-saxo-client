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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SaxoClientDetails extends SaxoDto {

	@JsonProperty("AllowedNettingProfiles")
	private List<String> allowedNettingProfiles;

	@JsonProperty("AllowedTradingSessions")
	private String allowedTradingSessions;

	@JsonProperty("ClientId")
	private String clientId;

	@JsonProperty("ClientKey")
	private String clientKey;

	@JsonProperty("ClientType")
	private String clientType;

	@JsonProperty("ContractOptionsTradingProfile")
	private String contractOptionsTradingProfile;

	@JsonProperty("CountryOfResidence")
	private String countryOfResidence;

	@JsonProperty("CurrencyDecimals")
	private int currencyDecimals;

	@JsonProperty("DefaultAccountId")
	private String defaultAccountId;

	@JsonProperty("DefaultAccountKey")
	private String defaultAccountKey;

	@JsonProperty("DefaultCurrency")
	private String defaultCurrency;

	@JsonProperty("ForceOpenDefaultValue")
	private boolean forceOpenDefaultValue;

	@JsonProperty("IsMarginTradingAllowed")
	private boolean isMarginTradingAllowed;

	@JsonProperty("IsVariationMarginEligible")
	private boolean isVariationMarginEligible;

	@JsonProperty("LegalAssetTypes")
	private List<SaxoAssetType> legalAssetTypes;

	@JsonProperty("LegalAssetTypesAreIndicative")
	private boolean legalAssetTypesAreIndicative;

	@JsonProperty("MarginCalculationMethod")
	private String marginCalculationMethod;

	@JsonProperty("MutualFundsCashAmountOrderCurrency")
	private String mutualFundsCashAmountOrderCurrency;

	@JsonProperty("Name")
	private String name;

	@JsonProperty("PartnerPlatformId")
	private String partnerPlatformId;

	@JsonProperty("PositionNettingMethod")
	private String positionNettingMethod;

	@JsonProperty("PositionNettingMode")
	private String positionNettingMode;

	@JsonProperty("PositionNettingProfile")
	private String positionNettingProfile;

	@JsonProperty("ReduceExposureOnly")
	private boolean reduceExposureOnly;

	@JsonProperty("SupportsAccountValueProtectionLimit")
	private boolean supportsAccountValueProtectionLimit;

}
