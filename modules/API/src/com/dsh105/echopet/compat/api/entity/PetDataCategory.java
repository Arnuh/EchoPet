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
	WOOL_COLOR(Material.WHITE_WOOL, "Wool Color", PetData.WHITE, PetData.ORANGE, PetData.MAGENTA, PetData.LIGHT_BLUE, PetData.YELLOW, PetData.LIME, PetData.PINK, PetData.GRAY, PetData.LIGHT_GRAY, PetData.CYAN, PetData.PURPLE, PetData.BLUE, PetData.BROWN, PetData.GREEN, PetData.RED, PetData.BLACK),
	COLLAR_COLOR(Material.WHITE_WOOL, "Collar Color", PetData.WHITE, PetData.ORANGE, PetData.MAGENTA, PetData.LIGHT_BLUE, PetData.YELLOW, PetData.LIME, PetData.PINK, PetData.GRAY, PetData.LIGHT_GRAY, PetData.CYAN, PetData.PURPLE, PetData.BLUE, PetData.BROWN, PetData.GREEN, PetData.RED, PetData.BLACK),
	SLIME_SIZE(Material.SLIME_BALL, "Slime Size", PetData.SIZE_SMALL, PetData.SIZE_MEDIUM, PetData.SIZE_LARGE),
	PUFFERFISH_SIZE(Material.PUFFERFISH, "PufferFish Size", PetData.SIZE_SMALL, PetData.SIZE_MEDIUM, PetData.SIZE_LARGE),
	VILLAGER_TYPE(Material.SAND, "Villager Type", PetData.DESERT, PetData.JUNGLE, PetData.PLAINS, PetData.SAVANNA, PetData.SNOWY, PetData.SWAMP, PetData.TAIGA),
	VILLAGER_PROFESSION(Material.IRON_AXE, "Villager Profession", PetData.NONE, PetData.ARMORER, PetData.BUTCHER, PetData.CARTOGRAPHER, PetData.CLERIC, PetData.FARMER, PetData.FISHERMAN, PetData.FLETCHER, PetData.LEATHERWORKER, PetData.LIBRARIAN, PetData.MASON, PetData.NITWIT, PetData.SHEPHERD, PetData.TOOLSMITH, PetData.WEAPONSMITH),
	VILLAGER_LEVEL(Material.EMERALD, "Villager Level", PetData.NOVICE, PetData.APPRENTICE, PetData.JOURNEYMEN, PetData.EXPERT, PetData.MASTER),
	RABBIT_TYPE(Material.RABBIT_HIDE, "Rabbit Type", PetData.BROWN, PetData.WHITE, PetData.BLACK, PetData.BLACK_AND_WHITE, PetData.GOLD, PetData.SALT_AND_PEPPER, PetData.KILLER_BUNNY),
	PARROT_VARIANT(Material.WHITE_WOOL, "Parrot Variant", PetData.RED, PetData.BLUE, PetData.GREEN, PetData.CYAN, PetData.GRAY),
	HORSE_COLOR(Material.LEAD, "Horse Color", PetData.WHITE, PetData.CREAMY, PetData.CHESTNUT, PetData.BROWN, PetData.BLACK, PetData.GRAY, PetData.DARK_BROWN),
	HORSE_MARKING(Material.INK_SAC, "Horse Marking", PetData.NO_MARKING, PetData.WHITE_SOCKS, PetData.WHITE_FIELD, PetData.WHITE_DOTS, PetData.BLACK_DOTS),
	LLAMA_COLOR(Material.LEATHER, "Llama Color", PetData.CREAMY, PetData.WHITE, PetData.BROWN, PetData.GRAY),
	LLAMA_CARPET_COLOR(Material.WHITE_CARPET, "Llama Carpet Color", PetData.WHITE_CARPET, PetData.ORANGE_CARPET, PetData.MAGENTA_CARPET, PetData.LIGHT_BLUE_CARPET, PetData.YELLOW_CARPET, PetData.LIME_CARPET, PetData.PINK_CARPET, PetData.GRAY_CARPET, PetData.LIGHT_GRAY_CARPET, PetData.CYAN_CARPET, PetData.PURPLE_CARPET, PetData.BLUE_CARPET, PetData.BROWN_CARPET, PetData.GREEN_CARPET, PetData.RED_CARPET, PetData.BLACK_CARPET),
	;

	public static final PetDataCategory[] values = values();
	private final PetData[] data;
	private Material material;
	private String name;
	private ItemStack item;

	PetDataCategory(Material material, String name, PetData... data){
		this.material = material;
		this.name = name;
		this.data = data;
	}

	public PetData[] getData(){
		return data;
	}

	public boolean hasData(PetData petData){
		for(PetData d : data){
			if(d.equals(petData)) return true;
		}
		return false;
	}

	public ItemStack getItem(){
		if(item == null){
			item = new ItemStack(material);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.RED + name);
			// meta.setLore(this.lore);
			item.setItemMeta(meta);
		}
		return item;
	}
}
