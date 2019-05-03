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
package com.dsh105.echopet.compat.api.util.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.dsh105.echopet.compat.api.util.Version;

public enum MenuItem{
	/*POTION(Material.POTION, 1, (short) 0, DataMenuType.BOOLEAN, "Potion", "Witch"),
	LLAMA_VARIANT(Material.LEATHER, 1, (short) 0, DataMenuType.LLAMA_VARIANT, "Llama Variant", "Llama"),
	LLAMA_COLOR(Material.WHITE_WOOL, 1, (short) 0, DataMenuType.LLAMA_COLOR, "Color", "Llama"),
	*/
	;

	private Material material;
	private String name;
	private int amount;
	private List<String> lore;
	private short data;
	private Version version;

	MenuItem(Material material, int amount, short data, String name, String... lore){
		this(material, amount, data, name, new Version(), lore);
	}

	MenuItem(Material material, int amount, short data, String name, Version version, String... lore){
		this.material = material;
		this.name = name;
		this.amount = amount;
		this.data = data;
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList(lore));
		this.lore = list;
		this.version = version;
	}

	public ItemStack getItem(){
		ItemStack i = new ItemStack(material, this.amount, this.data);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.RED + this.name);
		meta.setLore(this.lore);
		i.setItemMeta(meta);
		return i;
	}

	public ItemStack getBoolean(boolean flag){
		ItemStack i = new ItemStack(material, this.amount, this.data);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.RED + this.name + (flag ? ChatColor.GREEN + " [TOGGLE ON]" : ChatColor.YELLOW + " [TOGGLE OFF]"));
		meta.setLore(this.lore);
		i.setItemMeta(meta);
		return i;
	}

	public boolean isSupported(){
		return version.isSupported(new Version());
	}
}