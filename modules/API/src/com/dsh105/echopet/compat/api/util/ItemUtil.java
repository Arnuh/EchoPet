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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtil{
	
	public static boolean matches(ItemStack itemStack1, ItemStack itemStack2){
		if(itemStack1 == null || itemStack2 == null){
			return false;
		}
		
		if(!itemStack1.getType().equals(itemStack2.getType())){
			return false;
		}
		
		if(itemStack1.getAmount() != itemStack2.getAmount()){
			return false;
		}
		
		if(itemStack1.getDurability() != itemStack2.getDurability()){
			return false;
		}
		
		if(itemStack1.getMaxStackSize() != itemStack2.getMaxStackSize()){
			return false;
		}
		
		if(itemStack1.hasItemMeta() != itemStack2.hasItemMeta()){
			return false;
		}
		ItemMeta meta1 = itemStack1.getItemMeta();
		ItemMeta meta2 = itemStack2.getItemMeta();
		if(meta1 == null || meta2 == null){
			return false;
		}
		
		if(meta1.hasDisplayName() != meta2.hasDisplayName()){
			return false;
		}
		return Objects.equals(meta1.getDisplayName(), meta2.getDisplayName());
	}
	
	@SuppressWarnings("unchecked")
	public static ItemStack parseFromConfig(ConfigurationSection section, Material defaultMaterial, String defaultName, List<String> defaultLore){
		ItemStack item;
		String materialName = section.getString("material", null);
		if(materialName == null) item = new ItemStack(defaultMaterial);
		else item = new ItemStack(Material.getMaterial(materialName));
		
		ItemMeta meta = item.getItemMeta();
		if(meta == null) return null;
		String name = section.getString("name", defaultName);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		List<String> lore = ((List<String>) section.get("item.lore", defaultLore)).stream()
			.map(s->ChatColor.translateAlternateColorCodes('&', s))
			.collect(Collectors.toList());
		meta.setLore(lore);
		int customModelData = section.getInt("customModelData", -1);
		meta.setCustomModelData(customModelData == -1 ? null : customModelData);
		if(meta instanceof SkullMeta skullMeta){
			String texture = section.getString("texture", null);
			if(texture != null){
				EchoPet.getPlugin().getSpawnUtil().setSkullTexture(skullMeta, texture);
			}
		}
		item.setItemMeta(meta);
		return item;
	}
}
