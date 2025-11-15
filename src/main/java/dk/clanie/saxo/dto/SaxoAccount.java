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
package dk.clanie.saxo.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SaxoAccount extends SaxoDto {

	@JsonProperty("AccountGroupKey")
	public String accountGroupKey;

	@JsonProperty("AccountGroupName")
	public String accountGroupName;

	@JsonProperty("AccountId")
	public String accountId;

	@JsonProperty("AccountKey")
	public String accountKey;

	@JsonProperty("AccountSubType")
	public String accountSubType;

	@JsonProperty("AccountType")
	public String accountType;

	@JsonProperty("AccountValueProtectionLimit")
	public double accountValueProtectionLimit;

	@JsonProperty("Active")
	public boolean active;

	@JsonProperty("CanUseCashPositionsAsMarginCollateral")
	public boolean canUseCashPositionsAsMarginCollateral;

	@JsonProperty("ClientId")
	public String clientId;
	
	@JsonProperty("ClientKey")
	public String clientKey;
	
	@JsonProperty("CreationDate")
	public Instant creationDate;
	
	@JsonProperty("Currency")
	public String currency;
	
	@JsonProperty("CurrencyDecimals")
	public int currencyDecimals;
	
	@JsonProperty("DirectMarketAccess")
	public boolean directMarketAccess;
	
	@JsonProperty("DisplayName")
	public String displayName;
	
	@JsonProperty("ExternalReference")
	public String externalReference;
	
	@JsonProperty("FractionalOrderEnabled")
	public boolean fractionalOrderEnabled;
	
	@JsonProperty("FractionalOrderEnabledAssetTypes")
	public List<SaxoAssetType> fractionalOrderEnabledAssetTypes;
	
	@JsonProperty("IndividualMargining")
	public boolean individualMargining;
	
	@JsonProperty("IsCurrencyConversionAtSettlementTime")
	public boolean isCurrencyConversionAtSettlementTime;
	
	@JsonProperty("IsMarginTradingAllowed")
	public boolean isMarginTradingAllowed;
	
	@JsonProperty("IsShareable")
	public boolean isShareable;
	
	@JsonProperty("IsTrialAccount")
	public boolean isTrialAccount;
	
	@JsonProperty("LegalAssetTypes")
	public List<SaxoAssetType> legalAssetTypes;
	
	@JsonProperty("ManagementType")
	public String managementType;
	
	@JsonProperty("MarginCalculationMethod")
	public String marginCalculationMethod;
	
	@JsonProperty("MarginLendingEnabled")
	public String marginLendingEnabled;
	
	@JsonProperty("PortfolioBasedMarginEnabled")
	public boolean portfolioBasedMarginEnabled;
	
	@JsonProperty("Sharing")
	public List<String> sharing;
	
	@JsonProperty("SupportsAccountValueProtectionLimit")
	public boolean supportsAccountValueProtectionLimit;
	
	@JsonProperty("UseCashPositionsAsMarginCollateral")
	public boolean useCashPositionsAsMarginCollateral;
	
	@JsonProperty("CfdBorrowingCostsActive")
	public boolean cfdBorrowingCostsActive;

}