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
package com.dsh105.echopet.compat.api.entity;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum PetDataCategory{
	CAT_TYPE(Material.WHITE_WOOL, "Cat Type"),
	OCELOT_TYPE(Material.WHITE_WOOL, "Ocelot Type"),
	SHEEP_COLOR(Material.WHITE_WOOL, "Color", PetData.BLACK),
	COLLAR_COLOR(Material.WHITE_WOOL, "Collar Color", PetData.WHITE, PetData.ORANGE, PetData.MAGENTA, PetData.LIGHT_BLUE, PetData.YELLOW, PetData.LIME, PetData.PINK, PetData.GRAY, PetData.LIGHT_GRAY, PetData.CYAN, PetData.PURPLE, PetData.BLUE, PetData.BROWN, PetData.GREEN, PetData.RED, PetData.BLACK),
	SLIME_SIZE(Material.SLIME_BALL, "Slime Size", PetData.SLIME_SMALL, PetData.SLIME_MEDIUM, PetData.SLIME_LARGE),
	;

	public static final PetDataCategory[] values = values();
	private final PetData[] data;
	private ItemStack item;

	PetDataCategory(Material material, String name, PetData... data){
		this.data = data;
		item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + name);
		// meta.setLore(this.lore);
		item.setItemMeta(meta);
	}

	public PetData[] getData(){
		return data;
	}

	public ItemStack getItem(){
		return item;
	}
}
