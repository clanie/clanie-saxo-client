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

import static org.apache.commons.lang3.StringUtils.isEmpty;

public enum SaxoOpenClose {

	Open,
	Close;


	public static SaxoOpenClose parse(String string) {
		if (isEmpty(string)) {
			return null;
		}
		switch (string.toLowerCase()) {
		case "open", "åben":
			return Open;
		case "close", "lukke":
			return Close;
		default:
			throw new IllegalArgumentException("Unknown value: " + string);
		}
	}


}
