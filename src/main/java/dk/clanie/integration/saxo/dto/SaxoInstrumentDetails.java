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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SaxoInstrumentDetails extends SaxoDto {


	/**
	 * The trading amount supported decimals.
	 */
	@JsonProperty("AmountDecimals")
	Integer amountDecimals;

	/**
	 * AssetType (Note: OptionRoots also have an asset type (FuturesOption, StockOption, StockIndexOption)).
	 */
	@JsonProperty("AssetType")
	private SaxoAssetType saxoAssetType;

	/**
	 * Index ratio of bonds.
	 */
	@JsonProperty("BondIndexRatio")
	private Double bondIndexRatio;

	/**
	 * The type of bond.
	 */
	@JsonProperty("BondType")
	private String bondType;

	/**
	 * End client buy sell ratio for instrument.
	 */
	//	@JsonProperty("BuySellRatio")
	//	private BuySellRatio buySellRatio;
	//	{
	//	  "DayEndClientsBuying": -2.2048855032956316E+18,
	//	  "DayEndClientsSelling": 6.6286975965944948E+26,
	//	  "MonthEndClientsBuying": 79102638044.809509,
	//	  "MonthEndClientsSelling": 94102849432042192.0,
	//	  "WeekEndClientsBuying": -2.1474073843875876E+20,
	//	  "WeekEndClientsSelling": 6.040003038820181E+24
	//	}

	/**
	 * No of units of the underlying instrument covered by one contract.
	 */
	@JsonProperty("ContractSize")
	private Double contractSize;

	/**
	 * Coupon (relevant for bonds only).
	 */
	@JsonProperty("Coupon")
	private Double cupon;

	/**
	 * Type of a coupon (relevant for bonds only).
	 * 
	 * Fixed, None, Other or Variable.
	 */
	@JsonProperty("CouponType")
	private String cuponType;

	/**
	 * The ISO currency code of the instrument/symbol.
	 */
	@JsonProperty("CurrencyCode")
	private SaxoCurrencyCode currencyCode;

	/**
	 * Cut off time for Mutual fund subscriptions, only available for Mutual Funds.
	 */
	@JsonProperty("CutOffTimeForSubscriptions")
	private Duration cutOffTimeForSubscriptions;

	/**
	 * Default amount suggested for trading this Instrument.
	 */
	@JsonProperty("DefaultAmount")
	private Double defaultAmount;

	/**
	 * The default slippage (only set for FX DMA instruments).
	 */
	@JsonProperty("DefaultSlippage")
	private Double defaultSlippage;

	/**
	 * The default slippage type - either pips or percentage (only set for FX DMA instruments).
	 * 
	 * <dl>
	 * <dt>Percentage</dt><dd>Slippage is defined as a percentage of the price.</dd>
	 * <dt>Pips</dt><dd>Slippage is defined as a number of additional pips of the price.</dd>
	 * <dt>Ticks</dt><dd>Slippage is defined as a number of additional ticks of the price.</dd>
	 * </dl>
	 */
	@JsonProperty("DefaultSlippageType")
	private String defaultSlippageType;

	/**
	 * Deflation floor protection, applicable only for Inflation Linked Bonds.
	 */
	//	@JsonProperty("DeflationFloorProtectionType")
	//	private LocalizedInfo deflationFloorProtectionType;
	//	{
	//	  "Key": "Key1147a57b-dc28-4742-a787-3046df42dbec",
	//	  "Value": "Valuee309fc1b-46cd-4b28-abe2-fbb2c8dfb1ac"
	//	}

	/**
	 * Description of Instrument (DAX Index - Nov 2013), in English.
	 */
	@JsonProperty("Description")
	private String description;

	/**
	 * Hint to the client application about how it should display the instrument.
	 */
	@JsonProperty("DisplayHint")
	private String	displayHint;

	/**
	 * Information about the exchange where this instrument or the underlying instrument is traded.
	 * <ul>
	 * <li>For most instruments this is the exchange where this instrument is traded.</li>
	 * <li>For CFDs on stocks and futures, it is the exchange where the underlying instrument is traded.</li>
	 * <li>For CFDs on stock indices, it is the exchange of the underlying ticker.</li>
	 * </ul>
	 */
	//	@JsonProperty("Exchange")
	//	private ExchangeSummary exchange;

	/**
	 * The latest time at which an option can be exercised within a given day. This cutoff is specified in Exchange local time.
	 */
	@JsonProperty("ExerciseCutOffTime")
	private LocalTime ExerciseCutOffTime;

	/**
	 * Expiry date of the instrument. Applicable for Types: Futures, CfdOnFutures, ContractOptions
	 */
	@JsonProperty("ExpiryDate")
	private LocalDate expiryDate;

	/**
	 * The moment that the instrument expires, specified in UTC time. CfdOnFutures only.
	 */
	@JsonProperty("ExpiryDateTime")
	private Instant expiryDateTime;

	/**
	 * Price formatting information
	 */
	//	@JsonProperty("Format")
	//	private PriceDisplayFormat format;

	/**
	 * The latest possible forward date for a forex instrument that can be traded as forward.
	 */
	@JsonProperty("FxForwardMaxForwardDate")
	private Instant fxForwardMaxForwardDate;

	/**
	 * The earliest possible forward date for a forex instrument that can be traded as forward.
	 */
	@JsonProperty("FxForwardMinForwardDate")
	private Instant fxForwardMinForwardDate;

	/**
	 * Spot date for a forex instrument.
	 * 
	 * Applicable for Types: FxSpot, FxForwards and FxSwap Will be removed May 2019, please use standard date endpoint to get FxSpotDate
	 */
	@JsonProperty("FxSpotDate")
	private Instant fxSpotDate;

	/**
	 * The GroupId value is used to group and structure instruments list. 0 is being used for ungrouped data.
	 */
	@JsonProperty("GroupId")
	private Long groupId;

	/**
	 * The rate at which price should be incremented when done in steps.
	 */
	@JsonProperty("IncrementSize")
	private Double incrementSize;

	/**
	 * InitialPublicOffering details.
	 */
	//	@JsonProperty("IpoDetails")
	//	private IpoDetails ipoDetails;

	/**
	 * Is the instrument (complex)?
	 */
	@JsonProperty("IsComplex")
	private Boolean complex;

	/**
	 * Indicates if the instrument has direct market access (only FxSpot).
	 */
	@JsonProperty("IsDmaEnabled")
	private Boolean dmaEnabled;

	/**
	 * Indicates if the instrument is PEA Eligible.
	 */
	@JsonProperty("IsPEAEligible")
	private Boolean	peaEligible;

	/**
	 * Indicates if the instrument is PEA-PME Eligible.
	 */
	@JsonProperty("IsPEASMEEligible")
	private Boolean	peaSmemeEligible;

	/**
	 * Is this instrument pit traded (used for informational display to user).
	 */
	@JsonProperty("IsPitTraded")
	private Boolean	pitTraded;

	/**
	 * Is the instrument (currently) tradable by the user represented in the API token?
	 */
	@JsonProperty("IsTradable")
	private Boolean	tradable;

	/**
	 * Strategy Legs (for Futures Strategies).
	 */
	//	@JsonProperty("Legs")
	//	private List<StrategyLeg> legs;

	/**
	 * The LotSize, how many contracts in a lot traded?
	 */
	@JsonProperty("LotSize")
	private Double lotSize;

	/**
	 * Lot size description for the instrument. Futures only.
	 */
	@JsonProperty("LotSizeText")
	private String lotSizeText;

	/**
	 * Lotsize Type.
	 */
	@JsonProperty("LotSizeType")
	private String lotSizeType;

	/**
	 * The maximum size of a (guaranteed) stop order for this instrument.
	 */
	@JsonProperty("MaxGuaranteedStopOrderSize")
	private Double MaxGuaranteedStopOrderSize;

	/**
	 * The minimum number of items of the instrument to buy. Stock and CFDs only.
	 */
	@JsonProperty("MinimumLotSize")
	private Double minimumLotSize;

	/**
	 * Minimum value for each order. Applicable for Types: Shares, CfdOnFutures
	 */
	@JsonProperty("MinimumOrderValue")
	private Double MinimumOrderValue;

	/**
	 * The minimum trade size of a given contract.
	 * 
	 * For CFDs and Stocks this value indicates number of contracts.<br/>
	 * For ETOs and Futures this value indicates the minimum number of lots that can be traded.<br/>
	 * In addition, the value is always 1 for ETOs and Futures. However,
	 * 1 lot can represent any number of contracts.<br/>
	 * For FX and Bonds this value indicates nominal amount.
	 */
	@JsonProperty("MinimumTradeSize")
	private Double minimumTradeSize;

	/**
	 * The minimum trade size currency of MutualFund.
	 */
	@JsonProperty("MinimumTradeSizeCurrency")
	private String minimumTradeSizeCurrency;

	/**
	 * Optional reason of why the TradingStatus is not tradable.
	 */
	@JsonProperty("NonTradableReason")
	private String nonTradableReason;

	/**
	 * Notice date of the instrument. Futures only.
	 */
	@JsonProperty("NoticeDate")
	private Instant noticeDate;

	/**
	 * Options only. Is an options chain subscription allowed for this instrument.
	 */
	@JsonProperty("OptionsChainSubscriptionAllowed")
	private Boolean optionsChainSubscriptionAllowed;

	/**
	 * Minimal order distances for each order type.
	 */
	//	@JsonProperty("OrderDistances")
	//	private OrderDistances orderDistances;

	/**
	 * Order setting of an instrument
	 */
	//	@JsonProperty("OrderSetting")
	//	private OrderSetting orderSetting;

	/**
	 * Price currency of the instrument.
	 */
	@JsonProperty("PriceCurrency")
	private String priceCurrency;

	/**
	 * Price to contract factor. Applicable for Types: Futures, CfdOnFutures, ContractOptions, CfdIndices.
	 */
	@JsonProperty("PriceToContractFactor")
	private Double priceToContractFactor;

	/**
	 * The uic of the primary listing of this instrument.
	 * 
	 * For stocks and CFDs, this is the instrument traded on the exchange of the same country as issuer country.<br/>
	 * For expiring instruments, this is the next expiring instance.
	 */
	@JsonProperty("PrimaryListing")
	private Long primaryListing;

	/**
	 * Contract options only. Indicates whether the options is a put option or a call option.
	 */
	@JsonProperty("PutCall")
	private String putCall;

	/**
	 * List of related UICs and asset types.
	 */
	@JsonProperty("RelatedInstruments")
	private List<SaxoInstrumentKey> relatedInstruments;

	/**
	 * List of related Option root id's combined with asset type.
	 */
	//	@JsonProperty("RelatedOptionRootsEnhanced")
	//	private List<RelatedOptionRoot> RelatedOptionRootsEnhanced

	/**
	 * Contract options only. The settlement style of the contract option, i.e. either cash or physical delivery.
	 */
	@JsonProperty("SettlementStyle")
	private String settlementStyle;

	/**
	 * Indicates whether short trade is disabled. True if disabled, false otherwise.
	 */
	@JsonProperty("ShortTradeDisabled")
	private Boolean shortTradeDisabled;

	/**
	 * Standard amounts. Used for drop downs in trade tickets.
	 */
	@JsonProperty("StandardAmounts")
	private List<Double> standardAmounts;

	/**
	 * Strategy Type. For calendar spreads.
	 */
	@JsonProperty("StrategyType")
	private String StrategyType;

	/**
	 * The strike price. Used on contract options.
	 */
	@JsonProperty("StrikePrice")
	private Double strikePrice;

	/**
	 * What types of orders can be placed on this instrument ( or option)
	 */
	@JsonProperty("SupportedOrderTypes")
	private List<String> placeableOrderTypes;

	/**
	 * Algo order strategies which are allowed on this instrument.
	 */
	@JsonProperty("SupportedStrategies")
	private List<String> supportedStrategies;

	/**
	 * Symbol- A combination of letters used to uniquely identify a traded instrument. e.g. ODAX/X13C8950:xeur or EURUSD.
	 */
	@JsonProperty("Symbol")
	private String symbol;

	/**
	 * Minimum price movement of an instrument.
	 */
	@JsonProperty("TickSize")
	private Double tickSize;

	/**
	 * Minimum price increment when placing a limit order.
	 */
	@JsonProperty("TickSizeLimitOrder")
	private Double TickSizeLimitOrder;

	/**
	 * The tick size scheme of the instrument. Relevant for Stock, CFDs on Stock and contract options.
	 */
	//	@JsonProperty("TickSizeScheme")
	//	private TickSizeScheme tickSizeScheme;

	/**
	 * Minimum price increment when placing a stop order.
	 */
	@JsonProperty("TickSizeStopOrder")
	private Double TickSizeStopOrder;

	/**
	 * For instruments: How can an instrument with "this" uic also be traded.
	 */
	@JsonProperty("TradableAs")
	private List<String> tradableAs;

	/**
	 * Specifies what accounts the calling client can trade this instrument or option space on.
	 */
	@JsonProperty("TradableOn")
	private List<String> tradableOn;

	/**
	 * Trading sessions of an instrument.
	 */
	//	@JsonProperty("TradingSessions")
	//	private InstrumentTradeSessions TradingSessions;
	//	{
	//	  "Sessions": [
	//	    {
	//	      "EndTime": "2020-06-09T22:29:06.380343Z",
	//	      "StartTime": "2021-10-15T18:58:36.699162Z",
	//	      "State": "Closed"
	//	    }
	//	  ],
	//	  "TimeZone": 241,
	//	  "TimeZoneAbbreviation": "TimeZoneAbbreviationfcdb715c-4771-41a1-8579-8da3badb1ee5",
	//	  "TimeZoneOffset": "00:00:00.0000169"
	//	}

	/**
	 * Indicates whether the instrument is currently tradable by the user represented in the API token.
	 * 
	 * <dl>
	 * <dt>NonTradable</dt><dd>Instrument is non tradable</dd>
	 * <dt>NotDefined</dt><dd>Not Defined</dd>
	 * <dt>ReduceOnly</dt><dd>Instrument is Reduce only, which means client can only reduce the exposure by closing existing open position(s) and cannot open new position(s).</dd>
	 * <dt>Tradable</dt><dd>Instrument is tradable</dd>
	 */
	@JsonProperty("TradingStatus")
	private String tradingStatus;

	/**
	 * Trading units plural. CFD on Futures only.
	 */
	@JsonProperty("TradingUnitsPlural")
	private String tradingUnitsPlural;

	/**
	 * Trading units singular. CFD on Futures only.
	 */
	@JsonProperty("TradingUnitsSingular")
	private String tradingUnitsSingular;

	/**
	 * Universal Instrument Code.
	 */
	@JsonProperty("Uic")
	private Long uic;
	/**
	 * The asset type of the underlying instrument.
	 */
	@JsonProperty("UnderlyingAssetType")
	private String underlyingAssetType;

}
