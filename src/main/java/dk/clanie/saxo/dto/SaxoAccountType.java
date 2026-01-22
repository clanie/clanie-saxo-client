/*
 * Copyright (C) 2026, Claus Nielsen, clausn999@gmail.com
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

public enum SaxoAccountType {

	/**
	 * Account Type for AutoTrading follower accounts.
	 */
	AutoTradingFollower,

	/**
	 * Account Type for AutoTrading leader accounts.
	 */
	AutoTradingLeader,

	/**
	 * Account used for allocating large block orders.
	 */
	BlockTrading,

	/**
	 * Account used to hold collateral assets. Not used for trading.
	 */
	Collateral,

	/**
	 * Booking account for comissions only. Not used for trading.
	 */
	Commission,

	/**
	 * Account used to transfer funds between client accounts. Not used for trading.
	 */
	Funding,

	/**
	 * Booking account for interest only. Not used for trading.
	 */
	Interest,

	/**
	 * Margin Lending.
	 */
	MarginLending,

	/**
	 * Default. Used for normal client accounts.
	 */
	Normal,

	/**
	 * Partner account used to execute and clear client trades.
	 */
	Omnibus,

	/**
	 * Account type not mapped.
	 */
	Other,

	/**
	 * Pension account.
	 */
	Pension,

	/**
	 * Saving account
	 */
	Saving,

	/**
	 * Used in connection with SettlementTrading accounts. SettlementTrading accounts is used on the actual trading account. Settlement accounts are used on the sub accounts. Not used for trading.
	 */
	Settlement,

	/**
	 * For FX Settlement trading clients, this account type is used for the main trading account.
	 */
	SettlementTrading,

	/**
	 * Retention account for accumulating or withholding taxes. Not used for trading.
	 */
	Tax,

	/**
	 * Tax favored account.
	 */
	TaxFavoredAccount;


}
