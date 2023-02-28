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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

public class CommandManager{
	
	private final Plugin plugin;
	private CommandMap commandMap;
	private Map<String, Command> knownCommands;
	
	public CommandManager(Plugin plugin){
		this.plugin = plugin;
	}
	
	@SuppressWarnings("unchecked")
	public void initialize() throws NoSuchFieldException, IllegalAccessException{
		if(!(Bukkit.getPluginManager() instanceof SimplePluginManager)){
			this.plugin.getLogger().warning("Seems like your server is using a custom PluginManager? Well let's try injecting our custom commands anyways...");
		}
		var field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
		field.setAccessible(true);
		commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
		if(commandMap == null){
			plugin.getLogger().warning("Failed to get the PluginManager CommandMap! Let's give it a last shot...");
			commandMap = new SimpleCommandMap(EchoPet.getPlugin().getServer());
			Bukkit.getPluginManager().registerEvents(new FallbackCommandRegistrationListener(commandMap), this.plugin);
		}
		field = SimpleCommandMap.class.getDeclaredField("knownCommands");
		field.setAccessible(true);
		knownCommands = (Map<String, Command>) field.get(commandMap);
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
