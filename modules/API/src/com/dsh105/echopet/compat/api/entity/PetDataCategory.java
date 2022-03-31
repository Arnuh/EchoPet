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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import com.dsh105.echopet.compat.api.config.YAMLConfig;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum PetDataCategory{
	AXOLOTL_VARIANT(Material.AXOLOTL_BUCKET, "Axolotl Variant", PetData.LUCY, PetData.WILD, PetData.GOLD, PetData.CYAN, PetData.BLUE),
	CAT_TYPE(Material.SALMON, "Cat Type", PetData.TABBY, PetData.TUXEDO, PetData.CAT_RED, PetData.SIAMESE, PetData.BRITISH_SHORTHAIR, PetData.CALICO, PetData.PERSIAN, PetData.RAGDOLL, PetData.CAT_WHITE, PetData.JELLIE, PetData.CAT_BLACK),
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
	private final List<PetData<?>> defaultData, data;
	private final Material material;
	private final String name;
	private final List<String> lore;
	private ItemStack item;
	
	PetDataCategory(Material material, String name, PetData<?>... data){
		this.material = material;
		this.name = "&c" + name;
		this.defaultData = List.of(data);
		this.data = new LinkedList<>();
		this.lore = new LinkedList<>();
	}
	
	public String getConfigKeyName(){
		return name().toLowerCase().replace("_", "");
	}
	
	public Material getDefaultMaterial(){
		return material;
	}
	
	public String getDefaultName(){
		return name;
	}
	
	public List<String> getDefaultLore(){
		return lore;
	}
	
	public List<PetData<?>> getDefaultData(){
		return defaultData;
	}
	
	public List<PetData<?>> getData(){
		return data;
	}
	
	public boolean hasData(PetData<?> petData){
		for(PetData<?> d : getData()){
			if(d.equals(petData)) return true;
		}
		return false;
	}
	
	public ItemStack getItem(){
		if(material == null) return null;
		if(item != null) return item;
		String materialName = getConfigValue("item.material", null);
		if(materialName == null) item = new ItemStack(getDefaultMaterial());
		else item = new ItemStack(Material.getMaterial(materialName));
		ItemMeta meta = item.getItemMeta();
		if(meta == null) return null;
		String name = getConfigValue("item.name", getDefaultName());
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		List<String> lore = getConfigValue("item.lore", getDefaultLore()).stream()
			.map(s->ChatColor.translateAlternateColorCodes('&', s))
			.collect(Collectors.toList());
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static PetDataCategory getByData(IPetType type, PetData<?> data){
		for(PetDataCategory category : type.getAllowedCategories()){
			if(category.hasData(data)) return category;
		}
		return null;
	}
	
	public YAMLConfig getConfig(){
		return EchoPet.getConfig(EchoPet.ConfigType.PET_CATEGORY);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getConfigValue(String variable, T defaultValue){
		return (T) getConfig().get(getConfigKeyName() + "." + variable, defaultValue);
	}
	
	// Kinda ugly but ima do it for now.
	public void load(){
		List<String> petData = getConfig().getStringList(getConfigKeyName() + ".data");
		for(String s : petData){
			PetData<?> found = PetData.get(s);
			if(found == null) continue;
			this.data.add(found);
		}
	}
}
