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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SaxoDisplayAndFormat extends SaxoDto {

	/**
	 * 	The currency in which the data is displayed.
	 */
	@JsonProperty("Currency")
	private String currency;

	/**
	 * The number of decimals in the chart data.
	 */
	@JsonProperty("Decimals")
	private Integer decimals;

	/**
	 * Description of the data.
	 */
	@JsonProperty("Description")
	String description;

	/**
	 * The format in which data is delivered.
	 */
	@JsonProperty("Format")
	private SaxoPriceDisplayFormatType format;

	/**
	 * Provided when format is either fractions or ModernFractions. Indicates how many decimals are shown in the numerator value in a fraction.
	 */
	@JsonProperty("NumeratorDecimals")
	Integer numeratorDecimals;

	/**
	 * The symbol of the instrument.
	 */
	@JsonProperty("Symbol")
	private String symbol;

}
