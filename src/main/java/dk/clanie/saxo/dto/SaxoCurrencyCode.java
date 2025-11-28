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

import static dk.clanie.saxo.dto.SaxoCurrencyType.COMMODITY;
import static dk.clanie.saxo.dto.SaxoCurrencyType.FIAT;
import static dk.clanie.saxo.dto.SaxoCurrencyType.SYNTHETIC;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SaxoCurrencyCode {

	ATS("Austrian Schilling", "Austrian Schilling (historical, replaced by EUR)", FIAT),
	AUD("Australian Dollar", "Australian Dollar", FIAT),
	BEF("Belgian Franc", "Belgian Franc (historical, replaced by EUR)", FIAT),
	BGN("Bulgarian Lev", "Bulgarian Lev", FIAT),
	BRL("Brazilian Real", "Brazilian Real", FIAT),
	CAD("Canadian Dollar", "Canadian Dollar", FIAT),
	CHF("Swiss Franc", "Swiss Franc", FIAT),
	CNH("Chinese Yuan Offshore", "Chinese Yuan (offshore, Hong Kong)", FIAT),
	CNY("Chinese Yuan", "Chinese Yuan Renminbi", FIAT),
	CYP("Cypriot Pound", "Cypriot Pound (historical, replaced by EUR)", FIAT),
	CZK("Czech Koruna", "Czech Koruna", FIAT),
	DEM("German Mark", "German Mark (historical, replaced by EUR)", FIAT),
	DKK("Danish Krone", "Danish Krone", FIAT),
	EEK("Estonian Kroon", "Estonian Kroon (historical, replaced by EUR)", FIAT),
	ESP("Spanish Peseta", "Spanish Peseta (historical, replaced by EUR)", FIAT),
	EUR("Euro", "Euro", FIAT),
	FIM("Finnish Markka", "Finnish Markka (historical, replaced by EUR)", FIAT),
	FRF("French Franc", "French Franc (historical, replaced by EUR)", FIAT),
	GBP("British Pound Sterling", "British Pound Sterling", FIAT),
	GRD("Greek Drachma", "Greek Drachma (historical, replaced by EUR)", FIAT),
	HKD("Hong Kong Dollar", "Hong Kong Dollar", FIAT),
	HRK("Croatian Kuna", "Croatian Kuna (replaced by EUR in 2023)", FIAT),
	HUF("Hungarian Forint", "Hungarian Forint", FIAT),
	IDR("Indonesian Rupiah", "Indonesian Rupiah", FIAT),
	IEP("Irish Pound", "Irish Pound (historical, replaced by EUR)", FIAT),
	ILS("Israeli Shekel", "Israeli New Shekel", FIAT),
	INR("Indian Rupee", "Indian Rupee", FIAT),
	ISK("Icelandic Króna", "Icelandic Króna", FIAT),
	ITL("Italian Lira", "Italian Lira (historical, replaced by EUR)", FIAT),
	JPY("Japanese Yen", "Japanese Yen", FIAT),
	KRW("South Korean Won", "South Korean Won", FIAT),
	LTL("Lithuanian Litas", "Lithuanian Litas (historical, replaced by EUR)", FIAT),
	LVL("Latvian Lats", "Latvian Lats (historical, replaced by EUR)", FIAT),
	MTL("Maltese Lira", "Maltese Lira (historical, replaced by EUR)", FIAT),
	MXN("Mexican Peso", "Mexican Peso", FIAT),
	MYR("Malaysian Ringgit", "Malaysian Ringgit", FIAT),
	NLG("Dutch Guilder", "Dutch Guilder (historical, replaced by EUR)", FIAT),
	NOK("Norwegian Krone", "Norwegian Krone", FIAT),
	NZD("New Zealand Dollar", "New Zealand Dollar", FIAT),
	PHP("Philippine Peso", "Philippine Peso", FIAT),
	PLN("Polish Zloty", "Polish Zloty", FIAT),
	PTE("Portuguese Escudo", "Portuguese Escudo (historical, replaced by EUR)", FIAT),
	ROL("Romanian Leu (old)", "Romanian Leu (old, replaced by RON)", FIAT),
	RON("Romanian Leu", "Romanian Leu", FIAT),
	RUB("Russian Ruble", "Russian Ruble", FIAT),
	SEK("Swedish Krona", "Swedish Krona", FIAT),
	SGD("Singapore Dollar", "Singapore Dollar", FIAT),
	SIT("Slovenian Tolar", "Slovenian Tolar (historical, replaced by EUR)", FIAT),
	SKK("Slovak Koruna", "Slovak Koruna (historical, replaced by EUR)", FIAT),
	THB("Thai Baht", "Thai Baht", FIAT),
	TRL("Turkish Lira (old)", "Turkish Lira (old, replaced by TRY)", FIAT),
	TRY("Turkish Lira", "Turkish Lira", FIAT),
	USD("US Dollar", "United States Dollar", FIAT),
	X00("No Currency", "No Currency", SYNTHETIC),
	XDR("Special Drawing Rights", "IMF Special Drawing Rights", SYNTHETIC),
	XEU("European Currency Unit", "European Currency Unit (historical, replaced by EUR)", SYNTHETIC),
	Z60("Gold", "Gold (commodity/precious metal)", COMMODITY),
	ZAR("South African Rand", "South African Rand", FIAT);

	private final String name;
	private final String description;
	private final SaxoCurrencyType currencyType;

}
