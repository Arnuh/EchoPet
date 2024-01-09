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

package com.dsh105.echopet.commands.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommandManager{
	
	private final Plugin plugin;
	private final CommandMap commandMap;
	private final Map<String, Command> knownCommands;
	
	public CommandManager(Plugin plugin){
		this.plugin = plugin;
		commandMap = plugin.getServer().getCommandMap();
		knownCommands = commandMap.getKnownCommands();
	}

	public void register(DynamicPluginCommand command){
		commandMap.register(this.plugin.getName(), command);
	}
	
	public void unregister(){
		List<String> toRemove = new ArrayList<>();
		if(knownCommands == null){
			return;
		}
		for(Iterator<Command> i = knownCommands.values().iterator(); i.hasNext(); ){
			Command cmd = i.next();
			if(cmd instanceof DynamicPluginCommand){
				i.remove();
				for(String alias : cmd.getAliases()){
					Command aliasCmd = knownCommands.get(alias);
					if(cmd.equals(aliasCmd)){
						toRemove.add(alias);
					}
				}
			}
		}
		for(String string : toRemove){
			knownCommands.remove(string);
		}
	}
}
