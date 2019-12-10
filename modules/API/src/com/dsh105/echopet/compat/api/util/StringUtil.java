/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dsh105.echopet.compat.api.util;


/**
 * @since Jun 24, 2016
 */
public class StringUtil{
	
	/**
	 * Makes an enum name human readable (fixes spaces, capitalization, etc)
	 *
	 * @param enumName The name of the enum to neaten up.
	 * @return The human-readable enum name.
	 */
	public static String makeEnumHumanReadable(String enumName){
		StringBuilder builder = new StringBuilder(enumName.length() + 1);
		String[] words = enumName.split("_");
		for(String word : words){
			if(word.length() <= 2){
				builder.append(word); // assume that it's an abbrevation
			}else{
				builder.append(word.charAt(0));
				builder.append(word.substring(1).toLowerCase());
			}
			builder.append(' ');
		}
		return builder.substring(0, enumName.length());
	}
}
