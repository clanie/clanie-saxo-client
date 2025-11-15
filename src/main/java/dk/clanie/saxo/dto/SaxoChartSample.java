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

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SaxoChartSample extends SaxoDto {

	@JsonProperty("Close")
	double close;
	
//	@JsonProperty("CloseAsk")
//	double closeAsk;

//	@JsonProperty("CloseBid")
//	double closeBid;

//	@JsonProperty("Growth")
//	double growth;

	@JsonProperty("High")
	double high;

//	@JsonProperty("HighAsk")
//	double highAsk;

//	@JsonProperty("HighBid")
//	double highBid;

	@JsonProperty("Interest")
	double interest;

	@JsonProperty("Low")
	double low;

//	@JsonProperty("LowAsk")
//	double lowAsk;

//	@JsonProperty("LowBid")
//	double lowBid;

	@JsonProperty("Open")
	double open;

//	@JsonProperty("OpenAsk")
//	double openAsk;

//	@JsonProperty("OpenBid")
//	double openBid;

	@JsonProperty("Time")
	Instant time;

	@JsonProperty("Volume")
	double volume;

	public double midPrice() {
		return (low + high) / 2d;
	}

	
}
