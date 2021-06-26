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
package com.dsh105.echopet.compat.api.entity;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum PetDataCategory{
	AXOLOTL_VARIANT(Material.getMaterial("AXOLOTL_BUCKET"), "Axolotl Variant", PetData.LUCY, PetData.WILD, PetData.GOLD, PetData.CYAN, PetData.BLUE),
	CAT_TYPE(Material.SALMON, "Cat Type", PetData.TABBY, PetData.TUXEDO, PetData.CAT_RED, PetData.SIAMESE, PetData.BRITISH_SHORTHAIR, PetData.CALICO, PetData.PERSIAN, PetData.RAGDOLL, PetData.CAT_WHITE, PetData.JELLIE, PetData.CAT_BLACK),
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
	HORSE_ARMOR(Material.GOLDEN_HORSE_ARMOR, "Horse Armor", PetData.NO_ARMOR, PetData.IRON_ARMOR, PetData.GOLD_ARMOR, PetData.DIAMOND_ARMOR),
	LLAMA_COLOR(Material.LEATHER, "Llama Color", PetData.CREAMY, PetData.WHITE, PetData.BROWN, PetData.GRAY),
	LLAMA_CARPET_COLOR(Material.WHITE_CARPET, "Llama Carpet Color", PetData.WHITE_CARPET, PetData.ORANGE_CARPET, PetData.MAGENTA_CARPET, PetData.LIGHT_BLUE_CARPET, PetData.YELLOW_CARPET, PetData.LIME_CARPET, PetData.PINK_CARPET, PetData.GRAY_CARPET, PetData.LIGHT_GRAY_CARPET, PetData.CYAN_CARPET, PetData.PURPLE_CARPET, PetData.BLUE_CARPET, PetData.BROWN_CARPET, PetData.GREEN_CARPET, PetData.RED_CARPET, PetData.BLACK_CARPET),
	TROPICAL_FISH_PATTERN(Material.TROPICAL_FISH, "Tropical Fish Pattern", PetData.KOB, PetData.SUNSTREAK, PetData.SNOOPER, PetData.DASHER, PetData.BRINELY, PetData.SPOTTY, PetData.FLOPPER, PetData.STRIPEY, PetData.GLITTER, PetData.BLOCKFISH, PetData.BETTY, PetData.CLAYFISH),
	TROPICAL_FISH_COLOR(Material.WHITE_WOOL, "Tropical Fish Color", PetData.WHITE, PetData.ORANGE, PetData.MAGENTA, PetData.LIGHT_BLUE, PetData.YELLOW, PetData.LIME, PetData.PINK, PetData.GRAY, PetData.LIGHT_GRAY, PetData.CYAN, PetData.PURPLE, PetData.BLUE, PetData.BROWN, PetData.GREEN, PetData.RED, PetData.BLACK),
	TROPICAL_FISH_PATTERN_COLOR(Material.WHITE_BANNER, "Tropical Fish Pattern Color", PetData.WHITE_CARPET, PetData.ORANGE_CARPET, PetData.MAGENTA_CARPET, PetData.LIGHT_BLUE_CARPET, PetData.YELLOW_CARPET, PetData.LIME_CARPET, PetData.PINK_CARPET, PetData.GRAY_CARPET, PetData.LIGHT_GRAY_CARPET, PetData.CYAN_CARPET, PetData.PURPLE_CARPET, PetData.BLUE_CARPET, PetData.BROWN_CARPET, PetData.GREEN_CARPET, PetData.RED_CARPET, PetData.BLACK_CARPET),
	FOX_TYPE(Material.SWEET_BERRIES, "Fox Type", PetData.RED, PetData.SNOW),
	PANDA_MAIN_GENE(Material.BAMBOO, "Main Gene", PetData.NORMAL, PetData.LAZY, PetData.WORRIED, PetData.PLAYFUL, PetData.AGGRESSIVE, PetData.WEAK, PetData.BROWN),
	PANDA_HIDDEN_GENE(Material.BAMBOO, "Hidden Gene", PetData.NORMAL, PetData.LAZY, PetData.WORRIED, PetData.PLAYFUL, PetData.AGGRESSIVE, PetData.WEAK, PetData.BROWN),
	MUSHROOMCOW_TYPE(Material.MUSHROOM_STEW, "Mushroom Cow Type", PetData.RED, PetData.BROWN),
	;
	
	public static final PetDataCategory[] values = values();
	private final PetData[] data;
	private final Material material;
	private final String name;
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
		if(item == null && material != null){
			item = new ItemStack(material);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.RED + name);
			// meta.setLore(this.lore);
			item.setItemMeta(meta);
		}
		return item;
	}
	
	public static PetDataCategory getByData(PetType type, PetData data){
		for(PetDataCategory category : type.getAllowedCategories()){
			if(category.hasData(data)) return category;
		}
		return null;
	}
}
