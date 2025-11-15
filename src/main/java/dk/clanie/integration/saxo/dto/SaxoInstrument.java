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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SaxoInstrument extends SaxoDto {

	/**
	 * AssetType (Note: OptionRoots also have an asset type (FuturesOption, StockOption, StockIndexOption)).
	 */
	@JsonProperty("AssetType")
	private SaxoAssetType assetType;

	/**
	 * The ISO currency code of the instrument/symbol.
	 */
	@JsonProperty("CurrencyCode")
	private SaxoCurrencyCode currencyCode;

	/**
	 * Description of Instrument (DAX Index - Nov 2013), in English.
	 */
	@JsonProperty("Description")
	private String description;

	/**
	 * The Id of the exchange where this instrument or the underlying instrument is traded.
	 * 
	 * For most instruments this is the exchange where this instrument is traded.<br/>
	 * For CFDs on stocks and futures, it is the exchange where the underlying instrument is traded.<br/>
	 * For CFDs on stock indices, it is the exchange of the underlying ticker.
	 */
	@JsonProperty("ExchangeId")
	private String exchangeId;

	/**
	 * The GroupId value is used to group and structure instruments list. 0 is being used for ungrouped data.
	 */
	@JsonProperty("GroupId")
	private Long groupId;

	/**
	 * Uic if this is an instrument or ContractOptionRootId if this is an option root.
	 */
	@JsonProperty("Identifier")
	private Long identifier;

	@JsonProperty("IssuerCountry")
	private String issuerCountry;

	/**
	 * The uic of the primary listing of this instrument.
	 * 
	 * For stocks and CFDs, this is the instrument traded on the exchange of the same country as issuer country.<br/>
	 * For expiring instruments, this is the next expiring instance.
	 */
	@JsonProperty("PrimaryListing")
	private Long primaryListing;

	@JsonProperty("SummaryType")
	private String summaryType;

	/**
	 * Symbol- A combination of letters used to uniquely identify a traded instrument. e.g. ODAX/X13C8950:xeur or EURUSD.
	 */
	@JsonProperty("Symbol")
	private String symbol;

	/**
	 * For instruments: How can an instrument with "this" uic also be traded.
	 */
	@JsonProperty("TradableAs")
	private List<String> tradableAs;

	/**
	 * The asset type of the underlying instrument.
	 */
	@JsonProperty("UnderlyingAssetType")
	private String underlyingAssetType;

}
