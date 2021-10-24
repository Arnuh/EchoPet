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

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Basic logging to a server console
 * <p>
 * Supports chat colours :)
 */
public class Log{
	
	private final String prefix;
	private ConsoleCommandSender console;
	
	/**
	 * Constructs a new console logger with the given prefix
	 *
	 * @param prefix prefix appended to all messages, excluding any opening/closing brackets e.g. [{@code prefix}] Hello
	 *               world.
	 */
	public Log(String prefix){
		this.prefix = prefix;
	}
	
	/**
	 * Logs a coloured message to the console
	 *
	 * @param message message to log
	 */
	public void log(String message){
		info(message);
	}
	
	/**
	 * Logs a coloured message to the console
	 *
	 * @param message message to log
	 */
	public void info(String message){
		console(message);
	}
	
	/**
	 * Logs a coloured message to the console, with the {@link Level#WARNING} colour appended
	 *
	 * @param message message to log
	 */
	public void warning(String message){
		console(Level.WARNING, message);
	}
	
	/**
	 * Logs a coloured message to the console, with the {@link Level#SEVERE} colour appended
	 *
	 * @param message message to log
	 */
	public void severe(String message){
		console(Level.SEVERE, message);
	}
	
	/**
	 * Logs a coloured message to the console
	 *
	 * @param message message to log
	 */
	public void console(String message){
		console(Level.INFO, message);
	}
	
	/**
	 * Logs a coloured message to the console with the given level
	 *
	 * @param level level to log the message at
	 * @param message message to log
	 */
	public void console(Level level, String message){
		if(console == null){
			console = Bukkit.getConsoleSender();
		}
		console.sendMessage("[" + prefix + "]" + level.getPrefix() + message);
	}
}