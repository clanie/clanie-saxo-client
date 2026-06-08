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

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaxoInfoPriceResponse extends SaxoDto {

	@JsonProperty("AssetType")
	private SaxoAssetType assetType;

	@JsonProperty("LastUpdated")
	private Instant lastUpdated;

	@JsonProperty("PriceSource")
	private String priceSource;

	@JsonProperty("Quote")
	private SaxoQuote quote;

	@JsonProperty("PriceInfoDetails")
	private SaxoPriceInfoDetails priceInfoDetails;

	@JsonProperty("Uic")
	private Long uic;


	/**
	 * Returns the best available price from this response, in priority order:
	 * Quote.Mid → PriceInfoDetails.LastTraded → PriceInfoDetails.LastClose.
	 * Returns {@code null} when no usable price is available.
	 */
	public SaxoBestPrice getBestPrice() {
		if (quote != null && isUsablePrice(quote.getMid())) return new SaxoBestPrice(quote.getMid(), SaxoPriceType.MID);
		if (priceInfoDetails != null && isUsablePrice(priceInfoDetails.getLastTraded())) return new SaxoBestPrice(priceInfoDetails.getLastTraded(), SaxoPriceType.LAST_TRADED);
		if (priceInfoDetails != null && isUsablePrice(priceInfoDetails.getLastClose())) return new SaxoBestPrice(priceInfoDetails.getLastClose(), SaxoPriceType.LAST_CLOSE);
		return null;
	}


	private boolean isUsablePrice(Double price) {
		return price != null && price > 0d;
	}

}
