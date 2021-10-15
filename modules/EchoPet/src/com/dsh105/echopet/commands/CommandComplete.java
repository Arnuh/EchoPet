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

package com.dsh105.echopet.commands;

import java.util.ArrayList;
import java.util.List;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;


public class CommandComplete implements TabCompleter{
	
	private static final String[] basic = {"name", "rider", "list", "info", "default", "ride", "hat", "call", "show", "hide", "menu", "select", "remove"};
	
	private static final String[] commands;
	private static String[] firstArgs;
	private static final String[] blankArray = new String[0];
	private static int knownPetSize = 0;
	
	static{
		//handles /pet and /petadmin autocomplete
		commands = new String[]{EchoPet.getPlugin().getCommandString(), EchoPet.getPlugin().getCommandString() + "admin"};
		loadArgs();
	}
	
	private static void loadArgs(){
		if(knownPetSize == PetType.pets.size()){
			return;// This is kinda a hacky setup, but I don't know how I would best expose this to EchoPet API.
		}
		//handles /pet <pettype, all values in the basic array>
		firstArgs = new String[basic.length + PetType.pets.size()];
		System.arraycopy(basic, 0, firstArgs, 0, basic.length);
		int i = basic.length;
		for(IPetType petType : PetType.pets){
			firstArgs[i++] = petType.name().toLowerCase();
		}
		knownPetSize = PetType.pets.size();
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
		loadArgs();
		List<String> list = new ArrayList<>();
		String cmdString = EchoPet.getPlugin().getCommandString();
		if(cmd.getName().equalsIgnoreCase(cmdString)){
			String[] completions;
			if(args.length >= 2){
				completions = getCompletions(args.length, args[args.length - 2]);
			}else{
				completions = getCompletions(args.length);
			}
			for(String s : completions){
				if(s.startsWith(args[args.length - 1])){
					list.add(s);
				}
			}
		}
		return list;
	}
	
	private String[] getCompletions(int i){
		return switch(i){
			case 0 -> commands;
			case 1 -> firstArgs;
			default -> blankArray;
		};
	}
	
	private String[] getCompletions(int i, String argBefore){
		switch(i){
			case 0:
			case 1:
				return getCompletions(i);
			case 2:
				ArrayList<String> list = new ArrayList<>();
				for(IPetType pt : PetType.pets){
					if(argBefore.equalsIgnoreCase(pt.toString().toLowerCase())){
						list.add(pt.toString().toLowerCase());
					}
				}
				if(argBefore.equalsIgnoreCase("name")){
					list.add("Pet");
					list.add("rider");
				}else if(argBefore.equalsIgnoreCase("rider")){
					list.add("remove");
					for(IPetType pt : PetType.pets){
						list.add(pt.toString().toLowerCase());
					}
				}else if(argBefore.equalsIgnoreCase("default")){
					list.add("set");
					list.add("remove");
				}
				return list.toArray(new String[0]);
		}
		return blankArray;
	}
}