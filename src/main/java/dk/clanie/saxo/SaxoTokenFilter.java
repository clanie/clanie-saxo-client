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

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Moves SaxoSession between ServletSession and SaxoTokenHolder and vise versa, so
 * that SaxoClient can easily access the tokens without messing with the 
 * servlet http session.
 */
@Component
public class SaxoTokenFilter implements Filter {


	@Autowired
	private SaxoSessionHolder saxoSessionHolder;


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession(false);
		if (session == null) {
			chain.doFilter(httpRequest, response);
			return;
		}
		SaxoSession saxoSession = (SaxoSession)session.getAttribute("saxoSession");
		if (saxoSession != null) saxoSessionHolder.setSession(saxoSession);;
		try {
			chain.doFilter(httpRequest, response);
		} finally {
			try {
				session.setAttribute("saxoSession", saxoSessionHolder.getSession());
			} catch (IllegalStateException e) {
				// session is invalidated (user logged out), do nothing
			}
			saxoSessionHolder.removeFromThread();	
		}
	}


}
