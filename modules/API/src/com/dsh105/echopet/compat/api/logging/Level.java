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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.api.logging;

import org.bukkit.ChatColor;

/**
 * Represents the logging level of a certain record
 */
public enum Level{
	
	/**
	 * Basic information logging
	 */
	INFO(" [INFO] "),
	
	/**
	 * Warning messages - coloured red
	 */
	WARNING(ChatColor.RED + " [WARNING] "),
	
	/**
	 * Severe messages - coloured red
	 */
	SEVERE(ChatColor.RED + " [SEVERE] ");
	
	private final String prefix;
	
	Level(String prefix){
		this.prefix = prefix;
	}
	
	/**
	 * Gets the logging prefix for this level
	 *
	 * @return logging prefix
	 */
	public String getPrefix(){
		return prefix;
	}
}