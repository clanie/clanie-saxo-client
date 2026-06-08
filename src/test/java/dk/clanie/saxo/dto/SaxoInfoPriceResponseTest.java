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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.json.JsonMapper;

class SaxoInfoPriceResponseTest {


	@Test
	void deserializesDetailsAndPrefersQuoteMid() throws Exception {
		JsonMapper objectMapper = JsonMapper.builder().build();

		String json = """
				{
				  "AssetType": "Stock",
				  "LastUpdated": "2026-06-02T04:18:56.873Z",
				  "PriceInfo": {
				    "High": 57.98,
				    "Low": 54.12
				  },
				  "PriceInfoDetails": {
				    "LastClose": 55.32,
				    "LastTraded": 56.95
				  },
				  "Quote": {
				    "Amount": 100,
				    "Ask": null,
				    "Bid": null,
				    "DelayedByMinutes": 0,
				    "ErrorCode": "None",
				    "MarketState": "Closed",
				    "Mid": 56.95,
				    "PriceTypeAsk": "OldIndicative",
				    "PriceTypeBid": "OldIndicative"
				  },
				  "Uic": 7391391
				}
				""";

		SaxoInfoPriceResponse response = objectMapper.readValue(json, SaxoInfoPriceResponse.class);

		assertThat(response.getPriceInfoDetails().getLastTraded()).isEqualTo(56.95d);
		assertThat(response.getPriceInfoDetails().getLastClose()).isEqualTo(55.32d);
		assertThat(response.getBestPrice()).isEqualTo(new SaxoBestPrice(56.95d, SaxoPriceType.MID));
	}


	@Test
	void fallsBackToLastCloseWhenMidAndLastTradedAreMissing() throws Exception {
		JsonMapper objectMapper = JsonMapper.builder().build();

		String json = """
				{
				  "LastUpdated": "2026-06-02T04:18:56.873Z",
				  "PriceInfoDetails": {
				    "LastClose": 55.32,
				    "LastTraded": 0
				  },
				  "Quote": {
				    "Ask": null,
				    "Bid": null,
				    "Mid": null
				  },
				  "Uic": 7391391
				}
				""";

		SaxoInfoPriceResponse response = objectMapper.readValue(json, SaxoInfoPriceResponse.class);

		assertThat(response.getBestPrice()).isEqualTo(new SaxoBestPrice(55.32d, SaxoPriceType.LAST_CLOSE));
	}

}
