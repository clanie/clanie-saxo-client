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

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaxoTradesExecutedRow {

	private long tradeId; // Handels-ID
	private String accountId; // Konto ID
	private String instrument; // Instrument
	private LocalDate tradeDate; // Handelstidspunkt
	private SaxoBoughtOrSold boughtOrSold; // K/S
	private SaxoOpenClose openClose; // Åben/luk
	private double quantity; // Antal/Beløb
	private double price; // Pris
	private double tradeValue; // Handelsværdi
	private double spreadCosts; // Spread-omkostninger
	private double bookedAmount; // Bogført beløb
	private String symbol; // Symbol
	private String exchange; // Børs
	private String localExchange; // Lokal børs
	private LocalDate valueDate; // Valørdato
	private long orderId; // Ordre-ID
	private SaxoAssetType assetType; // Aktivtype
	private int accountCurrencyDecimals; // Kontovalutadecimaler
	private SaxoCurrencyCode accountCurrency; // Kontovaluta
	private double bookedAmountInCustomerCurrency; // Bogført beløb i kundevaluta
	private double bookedAmountInUsd; // Bogført beløb i USD
	private SaxoCurrencyCode customerCurrency; // Kundevaluta
	private LocalDate adjustedTradeDate; // Justeret handelsdato
	private LocalDate expiryDate; // Udløbsdato
	private Double initialMargin; // Startmargin
	private Double interestMargin; // Rentemargin
	private int instrumentCurrencyDecimals; // Instrumentvalutadecimaler
	private String instrumentSectorName; // Navn på instrumentsektor
	private String instrumentSectorTypeId; // Id for instrumentsektortype
	private String optionEventType; // Optionseventtype
	private String underlyingInstrumentSectorName; // Navn på rodinstrumentsektor
	private String underlyingInstrumentSectorTypeId; // Type-id for rodinstrumentsektor
	private double spreadCostInCustomerCurrency; // Spread-omkostning i kundevaluta
	private double spreadCostInUsd; // Spread-omkostning i USD
	private String direction; // Retning
	private String strike; // Strike
	private String barrierStopLoss; // Barrier/Stop Loss
	private String financingLevel; // Financing Level
	private String issuer; // Issuer
	private LocalDate tradeExecutionDate; // Gennemførselstidspunkt for handel
	private long uic; // UIC (Unified Instrument Code)
	private String underlyingInstrumentDescription; // Beskrivelse af underliggende instrument
	private String underlyingInstrumentSymbol; // Symbol for underliggende instrument

}
