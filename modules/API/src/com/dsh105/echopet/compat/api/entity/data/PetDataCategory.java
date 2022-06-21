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
package com.dsh105.echopet.compat.api.entity.data;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.config.YAMLConfig;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum PetDataCategory{
	AXOLOTL_VARIANT("axolotl_", Material.AXOLOTL_BUCKET, "Axolotl Variant", false, PetData.LUCY, PetData.WILD, PetData.GOLD, PetData.CYAN, PetData.BLUE),
	CAT_TYPE("cat_", Material.SALMON, "Cat Type", true, PetData.TABBY, PetData.TUXEDO, PetData.RED, PetData.SIAMESE, PetData.BRITISH_SHORTHAIR, PetData.CALICO, PetData.PERSIAN, PetData.RAGDOLL, PetData.WHITE, PetData.JELLIE, PetData.BLACK),
	WOOL_COLOR("wool_", Material.WHITE_WOOL, "Wool Color", false, PetData.WHITE, PetData.ORANGE, PetData.MAGENTA, PetData.LIGHT_BLUE, PetData.YELLOW, PetData.LIME, PetData.PINK, PetData.GRAY, PetData.LIGHT_GRAY, PetData.CYAN, PetData.PURPLE, PetData.BLUE, PetData.BROWN, PetData.GREEN, PetData.RED, PetData.BLACK),
	COLLAR_COLOR(Material.WHITE_WOOL, "Collar Color", false, PetData.WHITE, PetData.ORANGE, PetData.MAGENTA, PetData.LIGHT_BLUE, PetData.YELLOW, PetData.LIME, PetData.PINK, PetData.GRAY, PetData.LIGHT_GRAY, PetData.CYAN, PetData.PURPLE, PetData.BLUE, PetData.BROWN, PetData.GREEN, PetData.RED, PetData.BLACK),
	SLIME_SIZE("slime_", Material.SLIME_BALL, "Slime Size", false, PetData.SIZE_SMALL, PetData.SIZE_MEDIUM, PetData.SIZE_LARGE),
	PUFFERFISH_SIZE("pufferfish_", Material.PUFFERFISH, "PufferFish Size", false, PetData.SIZE_SMALL, PetData.SIZE_MEDIUM, PetData.SIZE_LARGE),
	VILLAGER_TYPE("villager_", Material.SAND, "Villager Type", false, PetData.DESERT, PetData.JUNGLE, PetData.PLAINS, PetData.SAVANNA, PetData.SNOWY, PetData.SWAMP, PetData.TAIGA),
	VILLAGER_PROFESSION("villager_", Material.IRON_AXE, "Villager Profession", false, PetData.NONE, PetData.ARMORER, PetData.BUTCHER, PetData.CARTOGRAPHER, PetData.CLERIC, PetData.FARMER, PetData.FISHERMAN, PetData.FLETCHER, PetData.LEATHERWORKER, PetData.LIBRARIAN, PetData.MASON, PetData.NITWIT, PetData.SHEPHERD, PetData.TOOLSMITH, PetData.WEAPONSMITH),
	VILLAGER_LEVEL("villager_", Material.EMERALD, "Villager Level", false, PetData.NOVICE, PetData.APPRENTICE, PetData.JOURNEYMEN, PetData.EXPERT, PetData.MASTER),
	RABBIT_TYPE("parrot_", Material.RABBIT_HIDE, "Rabbit Type", false, PetData.BROWN, PetData.WHITE, PetData.BLACK, PetData.BLACK_AND_WHITE, PetData.GOLD, PetData.SALT_AND_PEPPER, PetData.KILLER_BUNNY),
	PARROT_VARIANT("parrot_", Material.WHITE_WOOL, "Parrot Variant", false, PetData.RED, PetData.BLUE, PetData.GREEN, PetData.CYAN, PetData.GRAY),
	HORSE_COLOR("horse_", Material.LEAD, "Horse Color", false, PetData.WHITE, PetData.CREAMY, PetData.CHESTNUT, PetData.BROWN, PetData.BLACK, PetData.GRAY, PetData.DARK_BROWN),
	HORSE_MARKING("horse_", Material.INK_SAC, "Horse Marking", false, PetData.NO_MARKING, PetData.WHITE_SOCKS, PetData.WHITE_FIELD, PetData.WHITE_DOTS, PetData.BLACK_DOTS),
	HORSE_ARMOR("horse_", Material.GOLDEN_HORSE_ARMOR, "Horse Armor", false, PetData.NO_ARMOR, PetData.IRON_ARMOR, PetData.GOLD_ARMOR, PetData.DIAMOND_ARMOR),
	LLAMA_COLOR("llama_", Material.LEATHER, "Llama Color", false, PetData.CREAMY, PetData.WHITE, PetData.BROWN, PetData.GRAY),
	LLAMA_CARPET_COLOR("llama_", Material.WHITE_CARPET, "Llama Carpet Color", false, PetData.WHITE_CARPET, PetData.ORANGE_CARPET, PetData.MAGENTA_CARPET, PetData.LIGHT_BLUE_CARPET, PetData.YELLOW_CARPET, PetData.LIME_CARPET, PetData.PINK_CARPET, PetData.GRAY_CARPET, PetData.LIGHT_GRAY_CARPET, PetData.CYAN_CARPET, PetData.PURPLE_CARPET, PetData.BLUE_CARPET, PetData.BROWN_CARPET, PetData.GREEN_CARPET, PetData.RED_CARPET, PetData.BLACK_CARPET),
	TROPICAL_FISH_PATTERN("tropical_fish_", Material.TROPICAL_FISH, "Tropical Fish Pattern", true, PetData.KOB, PetData.SUNSTREAK, PetData.SNOOPER, PetData.DASHER, PetData.BRINELY, PetData.SPOTTY, PetData.FLOPPER, PetData.STRIPEY, PetData.GLITTER, PetData.BLOCKFISH, PetData.BETTY, PetData.CLAYFISH),
	TROPICAL_FISH_COLOR("tropical_fish_", Material.WHITE_WOOL, "Tropical Fish Color", true, PetData.WHITE, PetData.ORANGE, PetData.MAGENTA, PetData.LIGHT_BLUE, PetData.YELLOW, PetData.LIME, PetData.PINK, PetData.GRAY, PetData.LIGHT_GRAY, PetData.CYAN, PetData.PURPLE, PetData.BLUE, PetData.BROWN, PetData.GREEN, PetData.RED, PetData.BLACK),
	TROPICAL_FISH_PATTERN_COLOR("tropical_fish_", Material.WHITE_BANNER, "Tropical Fish Pattern Color", true, PetData.WHITE, PetData.ORANGE, PetData.MAGENTA, PetData.LIGHT_BLUE, PetData.YELLOW, PetData.LIME, PetData.PINK, PetData.GRAY, PetData.LIGHT_GRAY, PetData.CYAN, PetData.PURPLE, PetData.BLUE, PetData.BROWN, PetData.GREEN, PetData.RED, PetData.BLACK),
	FOX_TYPE("fox_", Material.SWEET_BERRIES, "Fox Type", false, PetData.RED, PetData.SNOW),
	FROG_VARIANT("frog_", Material.LILY_PAD, "Frog Variant", false, PetData.TEMPERATE, PetData.WARM, PetData.COLD),
	PANDA_MAIN_GENE("panda_", Material.BAMBOO, "Main Gene", true, PetData.NORMAL, PetData.LAZY, PetData.WORRIED, PetData.PLAYFUL, PetData.AGGRESSIVE, PetData.WEAK, PetData.BROWN),
	PANDA_HIDDEN_GENE("panda_", Material.BAMBOO, "Hidden Gene", true, PetData.NORMAL, PetData.LAZY, PetData.WORRIED, PetData.PLAYFUL, PetData.AGGRESSIVE, PetData.WEAK, PetData.BROWN),
	MUSHROOMCOW_TYPE("mushroomcow_", Material.MUSHROOM_STEW, "Mushroom Cow Type", false, PetData.RED, PetData.BROWN),
	;
	
	public static final PetDataCategory[] values = values();
	private final String configKey, permissionKey;
	private final List<PetData<?>> defaultData;
	private final List<CategorizedPetData<?>> data;
	private final Material material;
	private final String name;
	private final List<String> lore;
	private ItemStack item;
	private final boolean useCategorizedKey;
	
	PetDataCategory(Material material, String name, boolean useCategorizedKey, PetData<?>... data){
		this(null, material, name, useCategorizedKey, data);
	}
	
	PetDataCategory(String prefix, Material material, String name, boolean useCategorizedKey, PetData<?>... data){
		this.configKey = name().toLowerCase();
		if(prefix != null && !prefix.isBlank()){
			this.permissionKey = name().toLowerCase().replace(prefix, "");
		}else{
			this.permissionKey = name().toLowerCase();
		}
		this.material = material;
		this.name = "&c" + name;
		this.defaultData = List.of(data);
		this.data = new LinkedList<>();
		this.lore = new LinkedList<>();
		this.useCategorizedKey = useCategorizedKey;
	}
	
	public String getConfigKey(){
		return configKey;
	}
	
	public String getPermissionKey(){
		return permissionKey;
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
	
	public List<CategorizedPetData<?>> getData(){
		return data;
	}
	
	public boolean hasData(PetData<?> petData){
		for(CategorizedPetData<?> d : getData()){
			if(d == petData) return true;
			if(d.getData().equals(petData)) return true;
		}
		return false;
	}
	
	public ItemStack getItem(){
		if(material == null) return null;
		if(item != null) return item;
		item = ItemUtil.parseFromConfig(getConfig().getConfigurationSection(getConfigKey() + ".item"), getDefaultMaterial(), getDefaultName(), getDefaultLore());
		return item;
	}
	
	public static PetDataCategory getByData(IPetType type, PetData<?> data){
		if(data instanceof CategorizedPetData<?> categoryData){
			return categoryData.getCategory();
		}
		for(PetDataCategory category : type.getAllowedCategories()){
			if(category.hasData(data)) return category;
		}
		return null;
	}
	
	public static @Nullable PetData<?> getByKey(String key){
		for(PetDataCategory category : PetDataCategory.values){
			for(CategorizedPetData<?> check : category.getData()){
				// Could be user input so ignore case.
				if(check.getConfigKeyName().equalsIgnoreCase(key) || check.getData().getConfigKeyName().equalsIgnoreCase(key)){
					return check;
				}
			}
		}
		return null;
	}
	
	public YAMLConfig getConfig(){
		return EchoPet.getConfig(EchoPet.ConfigType.PET_CATEGORY);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getConfigValue(String variable, T defaultValue){
		return (T) getConfig().get(getConfigKey() + "." + variable, defaultValue);
	}
	
	// Kinda ugly but ima do it for now.
	public void load(boolean mock){
		data.clear();
		if(!mock){
			List<String> petData = getConfig().getStringList(getConfigKey() + ".data");
			for(String s : petData){
				PetData<?> found = PetData.getOriginal(s);
				if(found == null) continue;
				this.data.add(new CategorizedPetData<>(found, this, useCategorizedKey));
			}
		}else{
			for(PetData<?> data : defaultData){
				this.data.add(new CategorizedPetData<>(data, this, useCategorizedKey));
			}
		}
	}
}
