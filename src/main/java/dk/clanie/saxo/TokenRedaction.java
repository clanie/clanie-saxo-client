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

import org.jspecify.annotations.Nullable;

/**
 * Renders OAuth secrets in a form that is safe to log.
 *
 * A Saxo access or refresh token is a live credential, so no part of one belongs in a
 * log line - not even a prefix, which is enough to correlate one token with another.
 * What a reader of the log actually needs is whether a token was there at all, and that
 * is what {@link #redact(String)} leaves behind.
 */
public final class TokenRedaction {


	private TokenRedaction() {}


	/**
	 * The given secret's presence and length, never its value.
	 *
	 * @param secret the secret to redact, may be {@code null}.
	 * @return {@code "null"} for a missing secret, otherwise a placeholder naming its length.
	 */
	public static String redact(@Nullable String secret) {
		if (secret == null) return "null";
		return "<redacted, " + secret.length() + " chars>";
	}


}
