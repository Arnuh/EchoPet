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

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.util.menu.DataMenu.DataMenuType;

public enum DataMenuItem{
	BOOLEAN_TRUE(DataMenuType.BOOLEAN, null, Material.REDSTONE_TORCH, 1, (short) 0, "True", "Turns the feature on."),
	BOOLEAN_FALSE(DataMenuType.BOOLEAN, null, Material.REDSTONE_TORCH, 1, (short) 0, "False", "Turns the feature off."),
	BLACK_CAT(DataMenuType.CAT_TYPE, PetData.BLACK, Material.INK_SAC, 1, (short) 0, "Black", "Cat Type"),
	RED_CAT(DataMenuType.CAT_TYPE, PetData.RED, Material.ROSE_RED, 1, (short) 1, "Red", "Cat Type"),
	SIAMESE_CAT(DataMenuType.CAT_TYPE, PetData.SIAMESE, Material.LIGHT_GRAY_DYE, 1, (short) 7, "Siamese", "Cat Type"),
	WILD_CAT(DataMenuType.CAT_TYPE, PetData.WILD, Material.ORANGE_DYE, 1, (short) 14, "Wild", "Cat Type"),
	SMALL(DataMenuType.SIZE, PetData.SMALL, Material.SLIME_BALL, 1, (short) 0, "Small", "Slime Size"),
	MEDIUM(DataMenuType.SIZE, PetData.MEDIUM, Material.SLIME_BALL, 2, (short) 0, "Medium", "Slime Size"),
	LARGE(DataMenuType.SIZE, PetData.LARGE, Material.SLIME_BALL, 4, (short) 0, "Large", "Slime Size"),
	BLACKSMITH(new HashSet<>(Arrays.asList(DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION)), PetData.BLACKSMITH, Material.COAL, 1, (short) 0, "Blacksmith", "Villager Profession"),
	BUTCHER(new HashSet<>(Arrays.asList(DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION)), PetData.BUTCHER, Material.BEEF, 1, (short) 0, "Butcher", "Villager Profession"),
	FARMER(new HashSet<>(Arrays.asList(DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION)), PetData.FARMER, Material.IRON_HOE, 1, (short) 0, "Farmer", "Villager Profession"),
	LIBRARIAN(new HashSet<>(Arrays.asList(DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION)), PetData.LIBRARIAN, Material.BOOK, 1, (short) 0, "Librarian", "Villager Profession"),
	PRIEST(new HashSet<>(Arrays.asList(DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION)), PetData.PRIEST, Material.PAPER, 1, (short) 0, "Priest", "Villager Profession"),
	HUSK(DataMenuType.ZOMBIE_PROFESSION, PetData.HUSK, Material.ROTTEN_FLESH, 1, (short) 0, "Husk", "Zombie Type"),
	BLACK(DataMenuType.COLOR, PetData.BLACK, Material.BLACK_WOOL, 1, (short) 15, "Black", "Wool or Collar Color"),
	BLUE(new HashSet<>(Arrays.asList(DataMenuType.COLOR, DataMenuType.PARROT_VARIANT)), PetData.BLUE, Material.BLUE_WOOL, 1, (short) 11, "Blue", "Wool or Collar Color"),
	BROWN(DataMenuType.COLOR, PetData.BROWN, Material.BROWN_WOOL, 1, (short) 12, "Brown", "Wool or Collar Color"),
	CYAN(new HashSet<>(Arrays.asList(DataMenuType.COLOR, DataMenuType.PARROT_VARIANT)), PetData.CYAN, Material.CYAN_WOOL, 1, (short) 9, "Cyan", "Wool or Collar Color"),
	GRAY(new HashSet<>(Arrays.asList(DataMenuType.COLOR, DataMenuType.PARROT_VARIANT)), PetData.GRAY, Material.GRAY_WOOL, 1, (short) 7, "Gray", "Wool or Collar Color"),
	GREEN(new HashSet<>(Arrays.asList(DataMenuType.COLOR, DataMenuType.PARROT_VARIANT)), PetData.GREEN, Material.GREEN_WOOL, 1, (short) 13, "Green", "Wool or Collar Color"),
	LIGHT_BLUE(DataMenuType.COLOR, PetData.LIGHT_BLUE, Material.LIGHT_BLUE_WOOL, 1, (short) 3, "Light Blue", "Wool or Collar Color"),
	LIME(DataMenuType.COLOR, PetData.LIME, Material.LIME_WOOL, 1, (short) 5, "Lime", "Wool or Collar Color"),
	MAGENTA(DataMenuType.COLOR, PetData.MAGENTA, Material.MAGENTA_WOOL, 1, (short) 2, "Magenta", "Wool or Collar Color"),
	ORANGE(DataMenuType.COLOR, PetData.ORANGE, Material.ORANGE_WOOL, 1, (short) 1, "Orange", "Wool or Collar Color"),
	PINK(DataMenuType.COLOR, PetData.PINK, Material.PINK_WOOL, 1, (short) 6, "Pink", "Wool or Collar Color"),
	PURPLE(DataMenuType.COLOR, PetData.PURPLE, Material.PURPLE_WOOL, 1, (short) 10, "Purple", "Wool or Collar Color"),
	RED(new HashSet<>(Arrays.asList(DataMenuType.COLOR, DataMenuType.PARROT_VARIANT)), PetData.RED, Material.RED_WOOL, 1, (short) 14, "Red", "Wool or Collar Color"),
	SILVER(DataMenuType.COLOR, PetData.SILVER, Material.GRAY_WOOL, 1, (short) 8, "Silver", "Wool or Collar Color"),
	WHITE(DataMenuType.COLOR, PetData.WHITE, Material.WHITE_WOOL, 1, (short) 0, "White", "Wool or Collar Color"),
	YELLOW(DataMenuType.COLOR, PetData.YELLOW, Material.YELLOW_WOOL, 1, (short) 4, "Yellow", "Wool or Collar Color"),
	NORMAL(DataMenuType.HORSE_TYPE, PetData.HORSE, Material.HAY_BLOCK, 1, (short) 0, "Normal", "Type"),
	DONKEY(DataMenuType.HORSE_TYPE, PetData.DONKEY, Material.CHEST, 1, (short) 0, "Donkey", "Type"),
	MULE(DataMenuType.HORSE_TYPE, PetData.MULE, Material.CHEST, 1, (short) 0, "Mule", "Type"),
	ZOMBIE(DataMenuType.HORSE_TYPE, PetData.UNDEAD_HORSE, Material.ROTTEN_FLESH, 1, (short) 0, "Zombie", "Type"),
	SKELETON(DataMenuType.HORSE_TYPE, PetData.SKELETON_HORSE, Material.BOW, 1, (short) 0, "Skeleton", "Type"),
	WHITE_HORSE(DataMenuType.HORSE_VARIANT, PetData.WHITE, Material.WHITE_WOOL, 1, (short) 0, "White", "Variant"),
	CREAMY_HORSE(DataMenuType.HORSE_VARIANT, PetData.CREAMY, Material.YELLOW_WOOL, 1, (short) 4, "Creamy", "Variant"),
	CHESTNUT_HORSE(DataMenuType.HORSE_VARIANT, PetData.CHESTNUT, Material.LIGHT_GRAY_TERRACOTTA, 1, (short) 8, "Chestnut", "Variant"),
	BROWN_HORSE(DataMenuType.HORSE_VARIANT, PetData.BROWN, Material.BROWN_WOOL, 1, (short) 12, "Brown", "Variant"),
	BLACK_HORSE(DataMenuType.HORSE_VARIANT, PetData.BLACK, Material.BLACK_WOOL, 1, (short) 15, "Black", "Variant"),
	GRAY_HORSE(DataMenuType.HORSE_VARIANT, PetData.GRAY, Material.GRAY_WOOL, 1, (short) 7, "Gray", "Variant"),
	DARKBROWN_HORSE(DataMenuType.HORSE_VARIANT, PetData.DARK_BROWN, Material.BROWN_TERRACOTTA, 1, (short) 7, "Dark Brown", "Variant"),
	NONE(DataMenuType.HORSE_MARKING, PetData.NONE, Material.LEAD, 1, (short) 0, "None", "Marking"),
	WHITE_SOCKS(DataMenuType.HORSE_MARKING, PetData.WHITE_SOCKS, Material.LEAD, 1, (short) 0, "White Socks", "Marking"),
	WHITE_PATCH(DataMenuType.HORSE_MARKING, PetData.WHITEFIELD, Material.LEAD, 1, (short) 0, "White Patch", "Marking"),
	WHITE_SPOTS(DataMenuType.HORSE_MARKING, PetData.WHITE_DOTS, Material.LEAD, 1, (short) 0, "White Spots", "Marking"),
	BLACK_SPOTS(DataMenuType.HORSE_MARKING, PetData.BLACK_DOTS, Material.LEAD, 1, (short) 0, "Black Spots", "Marking"),
	NOARMOUR(DataMenuType.HORSE_ARMOUR, PetData.NOARMOUR, Material.LEAD, 1, (short) 0, "None", "Armour"),
	IRON(DataMenuType.HORSE_ARMOUR, PetData.IRON, Material.IRON_HORSE_ARMOR, 1, (short) 0, "Iron", "Armour"),
	GOLD(DataMenuType.HORSE_ARMOUR, PetData.GOLD, Material.GOLDEN_HORSE_ARMOR, 1, (short) 0, "Gold", "Armour"),
	DIAMOND(DataMenuType.HORSE_ARMOUR, PetData.DIAMOND, Material.DIAMOND_HORSE_ARMOR, 1, (short) 0, "Diamond", "Armour"),
	BROWN_RABBIT(DataMenuType.RABBIT_TYPE, PetData.BROWN, Material.BROWN_WOOL, 1, (short) 12, "Brown", "Bunny type"),
	WHITE_RABBIT(DataMenuType.RABBIT_TYPE, PetData.WHITE, Material.WHITE_WOOL, 1, (short) 0, "White", "Bunny type"),
	BLACK_RABBIT(DataMenuType.RABBIT_TYPE, PetData.BLACK, Material.BLACK_WOOL, 1, (short) 15, "Black", "Bunny type"),
	BLACK_AND_WHITE_RABBIT(DataMenuType.RABBIT_TYPE, PetData.BLACK_AND_WHITE, Material.GRAY_WOOL, 1, (short) 7, "Black and White", "Bunny type"),
	GOLD_RABBIT(DataMenuType.RABBIT_TYPE, PetData.GOLD, Material.YELLOW_WOOL, 1, (short) 4, "Gold", "Bunny type"),
	SALT_AND_PEPPER_RABBIT(DataMenuType.RABBIT_TYPE, PetData.SALT_AND_PEPPER, Material.YELLOW_WOOL, 1, (short) 4, "Salt and Pepper", "Bunny type"),
	KILLER_BUNNY(DataMenuType.RABBIT_TYPE, PetData.THE_KILLER_BUNNY, Material.RED_WOOL, 1, (short) 14, "Killer Bunny", "Bunny type"),
	SKELETON_NORMAL(DataMenuType.SKELETON_TYPE, PetData.NORMAL, Material.BONE, 1, (short) 0, "Normal", "Skeleton Type"),
	WITHER(DataMenuType.SKELETON_TYPE, PetData.WITHER, Material.WITHER_SKELETON_SKULL, 1, (short) 1, "Wither", "Skeleton Type"),
	STRAY(DataMenuType.SKELETON_TYPE, PetData.STRAY, Material.ARROW, 1, (short) 0, "Stray", "Skeleton Type"),
    //
	BROWN_VARIANT(DataMenuType.LLAMA_VARIANT, PetData.BROWN_LLAMA, Material.BROWN_WOOL, 1, (short) 12, "Brown", "Wool Color"),
	CREAMY_VARIANT(DataMenuType.LLAMA_VARIANT, PetData.CREAMY, Material.YELLOW_WOOL, 1, (short) 4, "Creamy", "Wool Color"),
	GRAY_VARIANT(DataMenuType.LLAMA_VARIANT, PetData.GRAY_LLAMA, Material.GRAY_WOOL, 1, (short) 7, "Gray", "Wool Color"),
	WHITE_VARIANT(DataMenuType.LLAMA_VARIANT, PetData.WHITE_LLAMA, Material.WHITE_WOOL, 1, (short) 0, "White", "Wool Color"),
	WHITE_LLAMA(DataMenuType.LLAMA_COLOR, PetData.WHITE, Material.WHITE_CARPET, 1, (short) 0, "White", "Color"),
	ORANGE_LLAMA(DataMenuType.LLAMA_COLOR, PetData.ORANGE, Material.ORANGE_CARPET, 1, (short) 1, "Orange", "Color"),
	MAGENTA_LLAMA(DataMenuType.LLAMA_COLOR, PetData.MAGENTA, Material.MAGENTA_CARPET, 1, (short) 2, "Magenta", "Color"),
	LIGHT_BLUE_LLAMA(DataMenuType.LLAMA_COLOR, PetData.LIGHT_BLUE, Material.LIGHT_BLUE_CARPET, 1, (short) 3, "Light Blue", "Color"),
	YELLOW_LLAMA(DataMenuType.LLAMA_COLOR, PetData.YELLOW, Material.YELLOW_CARPET, 1, (short) 4, "Yellow", "Color"),
	LIME_LLAMA(DataMenuType.LLAMA_COLOR, PetData.LIME, Material.LIME_CARPET, 1, (short) 5, "Lime", "Color"),
	PINK_LLAMA(DataMenuType.LLAMA_COLOR, PetData.PINK, Material.PINK_CARPET, 1, (short) 6, "Pink", "Color"),
	GRAY_LLAMA(DataMenuType.LLAMA_COLOR, PetData.GRAY, Material.GRAY_CARPET, 1, (short) 7, "Gray", "Color"),
	SILVER_LLAMA(DataMenuType.LLAMA_COLOR, PetData.SILVER, Material.GRAY_CARPET, 1, (short) 8, "Silver", "Color"),
	CYAN_LLAMA(DataMenuType.LLAMA_COLOR, PetData.CYAN, Material.CYAN_CARPET, 1, (short) 9, "Cyan", "Color"),
	PURPLE_LLAMA(DataMenuType.LLAMA_COLOR, PetData.PURPLE, Material.PURPLE_CARPET, 1, (short) 10, "Purple", "Color"),
	BLUE_LLAMA(DataMenuType.LLAMA_COLOR, PetData.BLUE, Material.BLUE_CARPET, 1, (short) 11, "Blue", "Color"),
	BROWN_LLAMA(DataMenuType.LLAMA_COLOR, PetData.BROWN, Material.BROWN_CARPET, 1, (short) 12, "Brown", "Color"),
	GREEN_LLAMA(DataMenuType.LLAMA_COLOR, PetData.GREEN, Material.GREEN_CARPET, 1, (short) 13, "Green", "Color"),
	RED_LLAMA(DataMenuType.LLAMA_COLOR, PetData.RED, Material.RED_CARPET, 1, (short) 14, "Red", "Color"),
	BLACK_LLAMA(DataMenuType.LLAMA_COLOR, PetData.BLACK, Material.BLACK_CARPET, 1, (short) 15, "Black", "Color"),
	BACK(DataMenuType.OTHER, null, Material.BOOK, 1, (short) 0, "Back", "Return to the main menu."),
	CLOSE(DataMenuType.OTHER, null, Material.BOOK, 1, (short) 0, "Close", "Close the Pet Menu");

	private Set<DataMenuType> types;
	private PetData dataLink;
	private Material mat;
	private String name;
	private List<String> lore;
	private int amount;
	private short data;

	private DataMenuItem(DataMenuType type, PetData dataLink, Material mat, int amount, short data, String name, String... lore){
		this(new HashSet<>(Arrays.asList(type)), dataLink, mat, amount, data, name, lore);
	}

	private DataMenuItem(Set<DataMenuType> types, PetData dataLink, Material mat, int amount, short data, String name, String... lore){
		this.types = types;
		this.dataLink = dataLink;
		this.mat = mat;
		this.amount = amount;
		this.data = data;
		this.name = name;
		List<String> list = new ArrayList<>();
		for(String s : lore){
			s = ChatColor.GOLD + s;
			list.add(s);
		}
		this.lore = list;
	}

	public ItemStack getItem(){
		ItemStack i = new ItemStack(this.mat, this.amount, this.data);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.RED + this.name);
		meta.setLore(this.lore);
		i.setItemMeta(meta);
		return i;
	}

	public Set<DataMenuType> getTypes(){
		return this.types;
	}

	public PetData getDataLink(){
		return this.dataLink;
	}
}