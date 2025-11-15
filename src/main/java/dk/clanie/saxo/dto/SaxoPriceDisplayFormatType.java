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

public enum SaxoPriceDisplayFormatType {

	/**
	 * Display the last digit as a smaller than the rest of the numbers.
	 * Note that this digit is not included in the number of decimals,
	 * effectively increasing the number of decimals by one. E.g. 12.345
	 * when Decimals is 2 and DisplayFormat is AllowDecimalPips.
	 */
	AllowDecimalPips,
	
	/**
	 * Display as regular fraction i.e. 3 1/4 The Decimals field indicates
	 * the fraction demoninator as 1/(2^x). So if Decimals is 2, the value
	 * should represent the number of 1/4'ths.
	 */
	Fractions,

	/**
	 * Display as modern faction, e.g. 1’07.5.
	 * The Decimals field indicates the fraction demoninator as 1/(2^x).
	 * So if Decimals is 5, the value should represent the number of 1/32'ths
	 */
	ModernFractions,

	/**
	 * Standard decimal formatting is used with the Decimals field indicating
	 * the number of decimals.
	 */
	Normal,

	/**
	 * Display as percentage, e.g. 12.34%. The Decimals field indicates the
	 * number of decimals to show.
	 */
	Percentage


}
