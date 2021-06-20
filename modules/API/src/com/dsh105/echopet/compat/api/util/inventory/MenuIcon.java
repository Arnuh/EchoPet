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

package com.dsh105.echopet.compat.api.util.inventory;

import java.util.ArrayList;
import java.util.List;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuIcon{
	
	private int slot;
	private Material material;
	private String name, entityTag;
	private List<String> lore;
	
	public MenuIcon(int slot, Material material, String name, List<String> lore){
		this.slot = slot;
		this.material = material;
		this.name = name;
		this.lore = lore;
	}
	
	public MenuIcon(int slot, Material material, String entityTag, String name, List<String> lore){
		this.slot = slot;
		this.material = material;
		this.entityTag = entityTag;
		this.name = name;
		this.lore = lore;
	}
	
	public int getSlot(){
		return slot;
	}
	
	public Material getMaterial(){
		return material;
	}
	
	public String getName(){
		return name;
	}
	
	public List<String> getLore(){
		return lore;
	}
	
	public ItemStack getIcon(Player viewer){
		ItemStack i = new ItemStack(material, 1);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(name);
		if(this.lore != null && !lore.isEmpty()){
			List<String> currentLore = meta.getLore();
			if(currentLore == null) currentLore = new ArrayList<>();
			currentLore.addAll(lore);
			meta.setLore(currentLore);
		}
		i.setItemMeta(meta);
		if(entityTag != null){
			i = EchoPet.getPlugin().getSpawnUtil().getSpawnEgg(i, entityTag);
		}
		return i;
	}
	
	public void onClick(Player viewer){
	
	}
}