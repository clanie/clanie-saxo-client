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
package dk.clanie.saxo;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dk.clanie.web.exception.InternalServerErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SaxoLoginController {

	public static final String SAXO_LOGIN_SESSION_ID_ATTRIBUTE = "saxoLoginSessionId";
	public static final String SAXO_LOGIN_REDIRECT_AFTER_ATTRIBUTE = "saxoLoginRedirectAfter";

	private static final String SAXO_LOGIN = "/saxo/login";


	@Autowired
	private SaxoLoginClient saxoLoginClient;


	/**
	 * Starts the login process.
	 * 
	 * Sends an authorize-request which will result in a redirect (a NotFoundException
	 * which GlobalExceptionHandler takes care of forwarding as a 302 Found response
	 * to the client, redirecting to Saxo login.
	 * 
	 * That in turn results in a redirect our /saxo/code endpoint.
	 */
	@GetMapping(SAXO_LOGIN)
	public void start(
			@RequestParam String redirectAfter,
			HttpSession session,
			HttpServletRequest request) {
		UUID sessionId = UUID.randomUUID();
		log.trace("Assigned sessionId={}. Received redirectAfter={}.", sessionId, redirectAfter);
		session.setAttribute(SAXO_LOGIN_SESSION_ID_ATTRIBUTE, sessionId);
		session.setAttribute(SAXO_LOGIN_REDIRECT_AFTER_ATTRIBUTE, redirectAfter);

		String fullUrl = request.getRequestURL().toString();  // e.g., https://portfolio.clanie.dk/saxo/login
		String baseUrl = fullUrl.substring(0, fullUrl.indexOf(SAXO_LOGIN));  // https://portfolio.clanie.dk

		saxoLoginClient.authorize(sessionId, baseUrl);
	}


	/**
	 * Receives authorization code after successful login with Saxo.
	 */
	@GetMapping("/saxo/login/code")
	public String code(
			@RequestParam String code,
			@RequestParam("state") UUID sessionId,
			HttpSession session,
			Model model) {
		log.trace("Recived code {} for session {}.", code, sessionId);
		if (!session.getAttribute(SAXO_LOGIN_SESSION_ID_ATTRIBUTE).equals(sessionId)) throw new InternalServerErrorException("Wrong " + SAXO_LOGIN_SESSION_ID_ATTRIBUTE);
		saxoLoginClient.getTokens(code); // Initializes SaxoSession
		session.removeAttribute(SAXO_LOGIN_SESSION_ID_ATTRIBUTE);
		String redirect = "redirect:" + session.getAttribute(SAXO_LOGIN_REDIRECT_AFTER_ATTRIBUTE);
		session.removeAttribute(SAXO_LOGIN_REDIRECT_AFTER_ATTRIBUTE);
		return redirect;
	}


}
