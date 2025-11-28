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

public enum SaxoAssetType {

	/**
	 * Bond.
	 */
	Bond,
	/**
	 * Cash. Not tradeable!
	 */
	Cash,
	/**
	 * Mirrors the price movement of the underlying only if and when the underlying price exceeds the defined barrier.
	 * If the certificate expires below the barrier, it offers partial protection/return of investment.
	 */
	CertificateBonus,
	/**
	 * Certificate Capped Bonus.
	 */
	CertificateCappedBonus,
	/**
	 * Guarantees a capped percentage increase of the underlying asset's value above the issue price at expiry/maturity.
	 * Max loss is the amount invested multiplied by the CapitalProtection percentage.
	 */
	CertificateCappedCapitalProtected,
	/**
	 * Capped Outperformance Certificate.
	 */
	CertificateCappedOutperformance,
	/**
	 * Certificate Constant Leverage.
	 */
	CertificateConstantLeverage,
	/**
	 * Yields a capped return if the underlying asset's value is above the specified cap level at expiry.
	 * If the underlying's value is below the strike at expiry, the investor received the underlying or equivalent value.
	 * Offers direct exposure in underlying at a lower price (discount) with a capped potential profit and limited loss.
	 */
	CertificateDiscount,
	/**
	 * Certificate Express kick out.
	 */
	CertificateExpress,
	/**
	 * A certificate that mirrors the price movement of the underlying instrument. Often used to trade movements in indicies.
	 * Movements can be a fixed ratio of the underlying and can be inverted for bearish/short speculation.
	 * Risk is equivalent to owning the underlying.
	 */
	CertificateTracker,
	/**
	 * Guarantees a percentage increase of the underlying asset's value above the issue price at expiry/maturity.
	 * Max loss is the amount invested multiplied by the CapitalProtection percentage.
	 */
	CertificateUncappedCapitalProtection,
	/**
	 * Provides leveraged returns when the underlying price exceeds the threshold strike price.
	 * The amount leverage is defined by the Participation %.
	 * When the underlying is below the strike price, the certificate mirrors the underlying price 1:1.
	 */
	CertificateUncappedOutperformance,
	/**
	 * Cfd Index Option.
	 */
	CfdIndexOption,
	/**
	 * Cfd on unlisted warrant issued by a corporation.
	 */
	CfdOnCompanyWarrant,

	CfdOnEtc,

	CfdOnEtn,

	CfdOnEtf,

	CfdOnFund,

	/**
	 * Cfd on Futures.
	 */
	CfdOnFutures,
	/**
	 * Cfd on Stock Index.
	 */
	CfdOnIndex,

	CfdOnRights,

	/**
	 * Cfd on Stock.
	 */
	CfdOnStock,
	/**
	 * Unlisted warrant issued by a corporation, often physically settled.
	 */
	CompanyWarrant,
	/**
	 * Contract Futures.
	 */
	ContractFutures,

	Etc,

	Etf,

	Etn,

	Fund,

	/**
	 * Futures Option.
	 */
	FuturesOption,
	/**
	 * Futures Strategy.
	 */
	FuturesStrategy,
	/**
	 * Forex Binary Option.
	 */
	FxBinaryOption,
	/**
	 * Forex Forward.
	 */
	FxForwards,
	/**
	 * Forex Knock In Option.
	 */
	FxKnockInOption,
	/**
	 * Forex Knock Out Option.
	 */
	FxKnockOutOption,
	/**
	 * Forex No Touch Option.
	 */
	FxNoTouchOption,
	/**
	 * Forex One Touch Option.
	 */
	FxOneTouchOption,
	/**
	 * Forex Spot.
	 */
	FxSpot,

	FxSwap,

	/**
	 * Forex Vanilla Option.
	 */
	FxVanillaOption,
	/**
	 * Danish investment scheme (“Grantbevis”). Not online tradeable.
	 */
	GuaranteeNote,
	/**
	 * IPO on Stock
	 */
	IpoOnStock,
	/**
	 * Obsolete: Managed Fund.
	 */
	ManagedFund,
	/**
	 * MiniFuture.
	 */
	MiniFuture,
	/**
	 * Mutual Fund.
	 */
	MutualFund,
	/**
	 * Danish pooled investment scheme (“Pulje”). Not online tradeable.
	 */
	PortfolioNote,
	/**
	 * 
	 */
	Rights,
	/**
	 * SRD. (Service de Règlement Différé) on Stock.
	 */
	SrdOnStock,
	/**
	 * 
	 */
	Stock,
	/**
	 * Stock Index.
	 */
	StockIndex,
	/**
	 * Stock Index Option.
	 */
	StockIndexOption,
	/**
	 * Stock Option.
	 */
	StockOption,
	/**
	 * Warrant
	 */
	Warrant,
	/**
	 * Warrant with two knock-out barriers.
	 */
	WarrantDoubleKnockOut,
	/**
	 * Warrant with a knock-out barrier.
	 */
	WarrantKnockOut,
	/**
	 * Knock-out Warrant with no expiry.
	 */
	WarrantOpenEndKnockOut,
	/**
	 * Warrant with built-in spread.
	 */
	WarrantSpread;


}
