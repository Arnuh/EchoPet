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

package com.dsh105.echopet.compat.api.util.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public enum SelectorItem{
	
	SELECTOR(Material.BONE, 1, ChatColor.GREEN + "Pets", ""),
	TOGGLE(Material.BONE, 1, ChatColor.YELLOW + "Toggle Pet", "toggle"),
	CALL(Material.ENDER_PEARL, 1, ChatColor.YELLOW + "Call Pet", "call"),
	RIDE(Material.CARROT_ON_A_STICK, 1, ChatColor.YELLOW + "Ride Pet", "ride"),
	HAT(Material.IRON_HELMET, 1, ChatColor.YELLOW + "Hat Pet", "hat"),
	NAME(Material.NAME_TAG, 1, ChatColor.YELLOW + "Name Your Pet", "name"),
	MENU(Material.CRAFTING_TABLE, 1, ChatColor.YELLOW + "Open PetMenu", "menu"),
	CLOSE(Material.BOOK, 1, ChatColor.YELLOW + "Close", "select"),
	BACK(Material.SLIME_BALL, 1, ChatColor.YELLOW + "< Page", ""),
	NEXT(Material.SLIME_BALL, 1, ChatColor.YELLOW + "Page > ", "");
	
	private String command;
	private Material mat;
	private int amount;
	private String name;
	
	SelectorItem(Material mat, int amount, String name, String command){
		this.command = "pet " + command;
		this.mat = mat;
		this.amount = amount;
		this.name = name;
	}
	
	public String getCommand(){
		return command;
	}
	
	public Material getMat(){
		return mat;
	}
	
	public int getAmount(){
		return amount;
	}
	
	public String getName(){
		return name;
	}
	
	public ItemStack getItem(){
		ItemStack i = new ItemStack(this.mat, this.amount);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(this.name);
		//meta.setLore(this.lore);
		i.setItemMeta(meta);
		return i;
	}
	
	public ItemStack getItem(int amount){
		ItemStack i = new ItemStack(this.mat, amount);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(this.name);
		//meta.setLore(this.lore);
		i.setItemMeta(meta);
		return i;
	}
}