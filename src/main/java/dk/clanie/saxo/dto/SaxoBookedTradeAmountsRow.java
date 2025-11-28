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
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SaxoBookedTradeAmountsRow {

    /**
     * Id for the related trade.
     * Original: Id for relateret handel
     */
    private long relatedTradeId;

    /**
     * Id for the related position.
     * Original: Id for relateret position
     */
    private long relatedPositionId;

    /**
     * Date.
     * Original: Dato
     */
    private LocalDate date;

    /**
     * Account ID.
     * Original: Konto ID
     */
    private String accountId;

    /**
     * Value date.
     * Original: Valørdato
     */
    private LocalDate valueDate;

    /**
     * Posting ID.
     * Original: Bogførings-ID
     */
    private long postingId;

    /**
     * Instrument description.
     * Original: Instrumentbeskrivelse
     */
    private String instrumentDescription;

    /**
     * Asset type.
     * Original: Aktivtype
     */
    private SaxoAssetType assetType;

    /**
     * Posting amount type.
     * Original: Bogføringsbeløbstype
     */
    private String postingAmountType;

    /**
     * Quantity/Amount.
     * Original: Antal/Beløb
     */
    private long quantityOrAmount;

    /**
     * Amount in account currency.
     * Original: Beløb i kontovaluta
     */
    private double amountInAccountCurrency;

    /**
     * Amount in customer currency.
     * Original: Beløb i kundevaluta
     */
    private double amountInCustomerCurrency;


}
