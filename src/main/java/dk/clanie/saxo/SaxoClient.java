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
package dk.clanie.saxo;

import static dk.clanie.core.Utils.csv;
import static dk.clanie.core.Utils.opt;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import dk.clanie.saxo.dto.SaxoAccount;
import dk.clanie.saxo.dto.SaxoAccountBalanceResponse;
import dk.clanie.saxo.dto.SaxoAccountStatementResponse;
import dk.clanie.saxo.dto.SaxoAccountStatementRow;
import dk.clanie.saxo.dto.SaxoAssetType;
import dk.clanie.saxo.dto.SaxoBookedTradeAmountsRow;
import dk.clanie.saxo.dto.SaxoChartRequestMode;
import dk.clanie.saxo.dto.SaxoClientDetails;
import dk.clanie.saxo.dto.SaxoGetChartDataResponse;
import dk.clanie.saxo.dto.SaxoInfoPriceResponse;
import dk.clanie.saxo.dto.SaxoInstrument;
import dk.clanie.saxo.dto.SaxoInstrumentDetails;
import dk.clanie.saxo.dto.SaxoInstrumentSearchRequest;
import dk.clanie.saxo.dto.SaxoListResponse;
import dk.clanie.saxo.dto.SaxoTradesExecutedResponse;
import dk.clanie.saxo.dto.SaxoTradesExecutedRow;
import dk.clanie.saxo.dto.SaxoUserDetails;
import dk.clanie.web.WebClientFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SaxoClient {

	private final SaxoSessionHolder saxoSessionHolder;
	private final SaxoXlsxUtils saxoXlsxUtils;
	private final WebClientFactory webClientFactory;


	@Value("${saxo.openApiUrl}")
	private String openApiUrl;

	@Value("#{new Boolean('${saxo.wiretap}')}")
	private boolean wiretap;


	private WebClient wc;


	@PostConstruct
	public void init() {
		wc = webClientFactory.newWebClient(openApiUrl, builder -> {
			builder.filter(authorizationFilter());
		}, wiretap);
	}


	private ExchangeFilterFunction authorizationFilter() {
		return ExchangeFilterFunction.ofRequestProcessor(cr -> Mono.just(
				ClientRequest.from(cr)
				.header(AUTHORIZATION, "Bearer " + saxoSessionHolder.getAccessToken())
				.build()));
	}


	@SaxoSkipTokenRenewal
	public boolean isLoggedIn() {
		return !saxoSessionHolder.refreshTokenHasExpired();
	}


	/**
	 * Gets information about current user.
	 */
	@SaxoRetryIfThrottled
	public SaxoUserDetails me() {
		return wc.get()
				.uri("/port/v1/users/me")
				.retrieve()
				.bodyToMono(SaxoUserDetails.class)
				.block();
	}


	/**
	 * Gets information about current Client.
	 */
	@SaxoRetryIfThrottled
	public SaxoClientDetails clientMe() {
		return wc.get()
				.uri("/port/v1/clients/me")
				.retrieve()
				.bodyToMono(SaxoClientDetails.class)
				.block();
	}


	@SaxoRetryIfThrottled
	public List<SaxoAccount> accounts() {
		SaxoListResponse<SaxoAccount> resp = wc.get().uri("/port/v1/accounts", ub -> ub
				.queryParam("$skip", 0)
				.queryParam("$top", 200)
				.queryParam("IncludeSubAccounts", true)
				.build())
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<SaxoListResponse<SaxoAccount>>() {})
				.block();
		return opt(resp).map(SaxoListResponse::getData).orElse(emptyList());
		// Endpoint supports more parameters:
		// ?$inlinecount={$inlinecount}&$skip={$skip}&$top={$top}&ClientKey={ClientKey}&IncludeSubAccounts={IncludeSubAccounts}")
	}


	@SaxoRetryIfThrottled
	public SaxoAccountBalanceResponse accountBalance(String clientKey, String accountKey) {
		SaxoAccountBalanceResponse resp = wc.get().uri("/port/v1/balances", ub -> ub
				//  .queryParam("AccountGroupKey", accountGroupKey)  // The endpoint can also accept an AccountGroupKey argument 
				.queryParam("AccountKey", accountKey)
				.queryParam("ClientKey", clientKey)
				.queryParam("FieldGroups", "CalculateCashForTrading") // Also accepts "MarginOverview"
				.build())
				.retrieve()
				.bodyToMono(SaxoAccountBalanceResponse.class)
				.block();
		return resp;
	}


	@SaxoRetryIfThrottled
	public SaxoAccountStatementResponse accountStatement(String clientKey, String accountKey, @Nullable LocalDate startDate) {
		byte[] resp = wc.get()
				.uri("/cr/v1/reports/AccountStatement/{ClientKey}", ub -> ub
						.queryParam("AccountKey", accountKey)
						.queryParam("FromDate", opt(startDate).map(LocalDate::toString).orElse("2000-01-01"))
						.queryParam("ToDate", LocalDate.now())
						.build(clientKey))
				.header(HttpHeaders.ACCEPT, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
				.retrieve()
				.bodyToMono(byte[].class)
				.block();
		List<SaxoAccountStatementRow> accountStatementRows = saxoXlsxUtils.parseAccountStatement(resp);
		return new SaxoAccountStatementResponse("AccountStatement.xlsx", resp, accountStatementRows);
		// Endpoint supports more parameters:
		// ?AccountGroupKey={AccountGroupKey}&AccountKey={AccountKey}&AccountStatementSortByRule={AccountStatementSortByRule}&FromDate={FromDate}&ToDate={ToDate}
	}


	@SaxoRetryIfThrottled
	public SaxoTradesExecutedResponse tradesExecuted(String clientKey, String accountKey, @Nullable LocalDate startDate) {
		byte[] resp = wc.get()
				.uri("/cr/v1/reports/TradesExecuted/{ClientKey}", ub -> ub
						.queryParam("AccountKey", accountKey)
						.queryParam("FromDate", opt(startDate).map(LocalDate::toString).orElse("2000-01-01"))
						.queryParam("ToDate", LocalDate.now())
						.build(clientKey))
				.header(HttpHeaders.ACCEPT, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
				.retrieve()
				.bodyToMono(byte[].class)
				.block();
		List<SaxoTradesExecutedRow> tradesExecuted = saxoXlsxUtils.parseTradesExecuted(resp);
		List<SaxoBookedTradeAmountsRow> bookedTradeAmounts = saxoXlsxUtils.parseBookedTradeAmounts(resp);
		return new SaxoTradesExecutedResponse("TradesExecuted.xlsx", resp, tradesExecuted, bookedTradeAmounts);
		// Endpoint supports more paparseAccountStatement(resp)rameters:
		// /cr/v1/reports/TradesExecuted/{ClientKey}?AccountGroupKey={AccountGroupKey}&AccountKey={AccountKey}&FromDate={FromDate}&ToDate={ToDate}	
	}


	@SaxoRetryIfThrottled
	public List<SaxoInstrument> instruments(SaxoInstrumentSearchRequest req) {
		SaxoListResponse<SaxoInstrument> resp = wc.get()
				.uri("/ref/v1/instruments/", ub -> {
					ub = ub.queryParam("$top", 200).queryParam("$skip", 0).queryParam("includeNonTradable", true);
					if (req.getKeywords() != null) {
						ub = ub.queryParam("keywords", req.getKeywords());
					}
					return ub.build();
				})
				.retrieve().bodyToMono(new ParameterizedTypeReference<SaxoListResponse<SaxoInstrument>>() {})
				.block();
		return opt(resp).map(SaxoListResponse::getData).orElse(emptyList());
	}


	@SaxoRetryIfThrottled
	public SaxoInstrumentDetails instrumentDetails(SaxoAssetType assetType, long uic) {
		SaxoInstrumentDetails resp = wc.get().uri("/ref/v1/instruments/details/{uic}/{assetType}", ub -> ub
				// .queryParam("FieldGroups", "OrderSetting", "Popularity", "TradingSessions")
				// .queryParam("MarketData")
				// .queryParam("MarketDataProvider", "Factset")
				.build(uic, assetType))
				.retrieve()
				.bodyToMono(SaxoInstrumentDetails.class)
				.block();
		return resp;
	}


	@SaxoRetryIfThrottled
	public SaxoInfoPriceResponse getPrice(SaxoAssetType assetType, long uic) {
		SaxoInfoPriceResponse resp = wc.get().uri("/trade/v1/infoprices/", ub -> ub
				.queryParam("AssetType", assetType)
				.queryParam("Uic", uic)
				// .queryParam("FieldGroups", "PriceInfo", "PriceInfoDetails", "Quote")
				.build())
				.retrieve()
				.bodyToMono(SaxoInfoPriceResponse.class)
				.block();
		return resp;
	}


	@SaxoRetryIfThrottled
	public List<SaxoInfoPriceResponse> getPrices(SaxoAssetType assetType, List<Long> uics) {
		SaxoListResponse<SaxoInfoPriceResponse> resp = wc.get().uri("/trade/v1/infoprices/list/", ub -> ub
				.queryParam("AssetType", assetType)
				.queryParam("Uics", csv(uics))
				// .queryParam("FieldGroups", "PriceInfo", "PriceInfoDetails", "Quote")
				.build())
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<SaxoListResponse<SaxoInfoPriceResponse>>() {})
				.block();
		return opt(resp).map(SaxoListResponse::getData).orElse(emptyList());
	}


	/**
	 * Returns chart data as specified by the request parameters.
	 * 
	 * Which data samples are returned, is specified by a combination of time, mode
	 * and count:
	 * <ul>
	 * <li>If neither time, mode or count are specified, the endpoint returns the
	 * most recent 1200 samples.</li>
	 * <li>If count is specified, the endpoint returns max of (count, 1200)
	 * samples.</li>
	 * <li>If Mode=="Upto" the endpoint returns a number of samples upto and
	 * including the specified Time.</li>
	 * <li>If Mode=="From" the endpoint returns a number of samples starting from
	 * the specified Time</li>
	 * </ul>
	 *
	 * @param assetType Assettype of the instrument
	 * @param uic       Uic of the instrument
	 * @param horizon   The horizon (time interval in minutes) to get samples from:
	 *                  1, 5, 10, 15, 30, 60, 120, 240, 360, 480, 1440, 10080,
	 *                  43200.
	 * @param mode      If Time is supplied, mode specifies if the endpoint should
	 *                  returns samples "UpTo" (and including) or "From" (and
	 *                  including) the specified time.
	 * @param time      Specifies the time of a sample, which must either be the
	 *                  first (If Mode=="From") or the last (if Mode=="UpTo") in the
	 *                  returned data.
	 * @param count     Optionally specifies maximum number of samples to return,
	 *                  max 1200, default 1200.
	 * @return SaxoGetChartDataResponse
	 */
	@SaxoRetryIfThrottled
	public SaxoGetChartDataResponse getChartData(SaxoAssetType assetType, long uic, int horizon,
			SaxoChartRequestMode mode, Instant time, int count) {
		SaxoGetChartDataResponse resp = wc.get().uri("/chart/v3/charts/", ub -> ub
				.queryParam("AssetType", assetType)
				.queryParam("Uic", uic)
				.queryParam("Horizon", horizon)
				.queryParam("Mode", mode)
				.queryParam("Time", time)
				.queryParam("Count", count)
				.queryParam("FieldGroups", "ChartInfo,Data,DisplayAndFormat")
				.build()
				).retrieve().bodyToMono(SaxoGetChartDataResponse.class).block();
		return resp;
	}


}
