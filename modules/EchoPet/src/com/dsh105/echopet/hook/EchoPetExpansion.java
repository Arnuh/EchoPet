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

package com.dsh105.echopet.hook;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.plugin.IEchoPetPlugin;
import com.dsh105.echopet.compat.api.util.Lang;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EchoPetExpansion extends PlaceholderExpansion{
	
	private final IEchoPetPlugin plugin;
	
	public EchoPetExpansion(IEchoPetPlugin plugin){
		this.plugin = plugin;
	}
	
	@Override
	public @NotNull String getAuthor(){
		return "Arnah";
	}
	
	@Override
	public @NotNull String getIdentifier(){
		return "echopet";
	}
	
	@Override
	public @NotNull String getVersion(){
		return "1.0.0";
	}
	
	@Override
	public String onPlaceholderRequest(Player player, @NotNull String params){
		if(player == null){
			return null;
		}
		// Individual pet grabs is kinda ugly.
		if(params.equalsIgnoreCase("type")){
			IPet pet = plugin.getPetManager().getPet(player);
			if(pet == null){
				return Lang.PLACEHOLDER_TYPE_NO_PET.toString();
			}
			return pet.getPetType().name();
		}else if(params.equalsIgnoreCase("name")){
			IPet pet = plugin.getPetManager().getPet(player);
			if(pet == null){
				return Lang.PLACEHOLDER_NAME_NO_PET.toString();
			}
			return pet.getPetName();
		}else if(params.equalsIgnoreCase("default_name")){
			IPet pet = plugin.getPetManager().getPet(player);
			if(pet == null){
				return Lang.PLACEHOLDER_DEFAULT_NAME_NO_PET.toString();
			}
			return pet.getPetType().getDefaultName(pet.getNameOfOwner());
		}
		return null;
	}
}