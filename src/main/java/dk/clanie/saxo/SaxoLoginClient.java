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

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import dk.clanie.saxo.dto.SaxoTokens;
import dk.clanie.saxo.dto.SaxoUserDetails;
import dk.clanie.web.WebClientFactory;
import dk.clanie.web.exception.FoundException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SaxoLoginClient {

	@Value("${saxo.appKey}")
	private String appKey;

	@Value("${saxo.appSecret}")
	private String appSecret;

	@Value("${saxo.appRedirectUrl}")
	private String appRedirectUrl;

	@Value("${saxo.authzEndpoint}")
	private String authzEndpoint;

	@Value("${saxo.tokenEndpoint}")
	private String tokenEndpoint;

	@Value("#{new Boolean('${saxo.wiretap}')}")
	private boolean wiretap;


	@Autowired
	private SaxoSessionHolder saxoSessionHolder;
	
	@Autowired
	private SaxoClient saxoClient;

	@Autowired
	private WebClientFactory webClientFactory;


	private String fullAppRedirectUrl;


	private WebClient wc;
	private String authorizationHeader;



	@PostConstruct
	public void init() {
		wc = webClientFactory.newWebClient("", builder -> {
			builder.defaultHeader(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE);
		}, wiretap);
		authorizationHeader = "Basic " + Base64.getEncoder().encodeToString((appKey + ":" + appSecret).getBytes());
	}


	/**
	 * Sends an authorize request to Saxo.
	 * 
	 * Saxo will respond with a redirect, causing a FoundException.
	 * 
	 * @throws FoundException with redirect to Saxo login.
	 */
	public void authorize(UUID sessionId, String baseUrl) {
		this.fullAppRedirectUrl = baseUrl + appRedirectUrl;
		log.trace("Authorizing session {}. Saved fullAppRedirectUrl={}", sessionId, fullAppRedirectUrl);
		wc.get()
		.uri(authzEndpoint, ub -> ub
				.queryParam("response_type", "code")
				.queryParam("client_id", appKey)
				.queryParam("state", sessionId)
				.queryParam("redirect_uri", fullAppRedirectUrl)
				.build())
		.retrieve()
		.bodyToMono(String.class)
		.block();
	}


	/**
	 * Exchanges authorization code for access and refresh tokens.
	 * Stores the tokens in SaxoSession. 
	 */
	public void getTokens(String code) {
		log.trace("Fetching tokens for code {}", code);
		SaxoTokens saxoTokens = wc.post()
				.uri(tokenEndpoint, ub -> ub
						.queryParam("grant_type", "authorization_code")
						.queryParam("code", code)
						.queryParam("redirect_uri", fullAppRedirectUrl)
						.build())
				.header(AUTHORIZATION, authorizationHeader)
				.header(CONTENT_LENGTH, "0")
				.retrieve()
				.bodyToMono(SaxoTokens.class)
				.block();
		log.trace("Received tokens {}", saxoTokens);
		if (saxoTokens == null) throw new IllegalStateException("Failed to get Saxo tokens (got empty response)");
		saxoSessionHolder.registerSaxoTokens(saxoTokens);
		SaxoUserDetails userDetails = saxoClient.me();
		saxoSessionHolder.loggedIn(userDetails);
	}


	/**
	 * Refreshes the tokens in SaxoSession. 
	 */
	public void refreshTokens() {
		SaxoTokens saxoTokens = wc.post()
				.uri(tokenEndpoint, ub -> ub
						.queryParam("grant_type", "refresh_token")
						.queryParam("refresh_token", saxoSessionHolder.getRefreshToken())
						.queryParam("redirect_uri", fullAppRedirectUrl)
						.build())
				.header(AUTHORIZATION, authorizationHeader)
				.header(CONTENT_LENGTH, "0")
				.retrieve()
				.bodyToMono(SaxoTokens.class)
				.block();
		if (saxoTokens== null) throw new IllegalStateException("Failed to refresh Saxo tokens (got empty response)");
		saxoSessionHolder.registerSaxoTokens(saxoTokens);
	}


}
