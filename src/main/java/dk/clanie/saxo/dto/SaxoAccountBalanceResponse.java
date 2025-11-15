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

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class SaxoAccountBalanceResponse {

    @JsonProperty("CalculationReliability")
    private String calculationReliability;

    @JsonProperty("CashAvailableForTrading")
    private double cashAvailableForTrading;

    @JsonProperty("CashBalance")
    private double cashBalance;

    @JsonProperty("CashBlocked")
    private double cashBlocked;

    @JsonProperty("ChangesScheduled")
    private boolean changesScheduled;

    @JsonProperty("ClosedPositionsCount")
    private int closedPositionsCount;

    @JsonProperty("CollateralAvailable")
    private double collateralAvailable;

    @JsonProperty("CorporateActionUnrealizedAmounts")
    private double corporateActionUnrealizedAmounts;

    @JsonProperty("CostToClosePositions")
    private double costToClosePositions;

    @JsonProperty("Currency")
    private String currency;

    @JsonProperty("CurrencyDecimals")
    private int currencyDecimals;

    @JsonProperty("InitialMargin")
    private SaxoInitialMargin initialMargin;

    @JsonProperty("IntradayMarginDiscount")
    private double intradayMarginDiscount;

    @JsonProperty("IsPortfolioMarginModelSimple")
    private boolean isPortfolioMarginModelSimple;

    @JsonProperty("MarginAndCollateralUtilizationPct")
    private double marginAndCollateralUtilizationPct;

    @JsonProperty("MarginAvailableForTrading")
    private double marginAvailableForTrading;

    @JsonProperty("MarginCollateralNotAvailable")
    private double marginCollateralNotAvailable;

    @JsonProperty("MarginExposureCoveragePct")
    private double marginExposureCoveragePct;

    @JsonProperty("MarginNetExposure")
    private double marginNetExposure;

    @JsonProperty("MarginUsedByCurrentPositions")
    private double marginUsedByCurrentPositions;

    @JsonProperty("MarginUtilizationPct")
    private double marginUtilizationPct;

    @JsonProperty("NetEquityForMargin")
    private double netEquityForMargin;

    @JsonProperty("NetPositionsCount")
    private int netPositionsCount;

    @JsonProperty("NonMarginPositionsValue")
    private double nonMarginPositionsValue;

    @JsonProperty("OpenIpoOrdersCount")
    private int openIpoOrdersCount;

    @JsonProperty("OpenPositionsCount")
    private int openPositionsCount;

    @JsonProperty("OptionPremiumsMarketValue")
    private double optionPremiumsMarketValue;

    @JsonProperty("OrdersCount")
    private int ordersCount;

    @JsonProperty("OtherCollateral")
    private double otherCollateral;

    @JsonProperty("SettlementValue")
    private double settlementValue;

    @JsonProperty("SpendingPowerDetail")
    private SaxoSpendingPowerDetail spendingPowerDetail;

    @JsonProperty("TotalValue")
    private double totalValue;

    @JsonProperty("TransactionsNotBooked")
    private double transactionsNotBooked;

    @JsonProperty("TriggerOrdersCount")
    private int triggerOrdersCount;

    @JsonProperty("UnrealizedMarginClosedProfitLoss")
    private double unrealizedMarginClosedProfitLoss;

    @JsonProperty("UnrealizedMarginOpenProfitLoss")
    private double unrealizedMarginOpenProfitLoss;

    @JsonProperty("UnrealizedMarginProfitLoss")
    private double unrealizedMarginProfitLoss;

    @JsonProperty("UnrealizedPositionsValue")
    private double unrealizedPositionsValue;


}
