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

package com.dsh105.echopet.compat.api.util;

import java.util.LinkedList;
import java.util.List;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.data.PetData;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.google.common.collect.ImmutableList;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuUtil{
	
	public static final ItemStack BACK = new ItemStack(Material.BOOK), CLOSE = new ItemStack(Material.BOOK);
	
	static{
		ItemMeta meta = BACK.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Back");
		meta.setLore(ImmutableList.of(ChatColor.GOLD + "Return to the main menu."));
		BACK.setItemMeta(meta);
		meta = CLOSE.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Close");
		meta.setLore(ImmutableList.of(ChatColor.GOLD + "Close the Pet Menu"));
		CLOSE.setItemMeta(meta);
	}
	
	public static List<Object> createOptionList(IPet pet){
		IPetType pt = pet.getPetType();
		List<Object> options = new LinkedList<>();
		for(PetData<?> data : pt.getAllowedDataTypes()){
			if(data.getMaterial() == null) continue;
			options.add(data);
		}
		options.addAll(pt.getAllowedCategories());
		return options;
	}
}
