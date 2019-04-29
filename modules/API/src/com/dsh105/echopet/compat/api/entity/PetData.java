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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Rabbit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.dsh105.echopet.compat.api.entity.type.pet.*;
import com.dsh105.echopet.compat.api.util.Version;
import com.dsh105.echopet.compat.api.util.VersionCheckType;

public enum PetData {
	HAT("hat", (player, pet, data, flag)-> {
		pet.setAsHat(flag);
		return true;
	}, Material.IRON_HELMET, "Hat Pet", "Wear your pet on your head"),
	RIDE("ride", (player, pet, data, flag)-> {
		pet.ownerRidePet(flag);
		return true;
	}, Material.CARROT_ON_A_STICK, "Ride Pet", "Control your pet"),
	BABY("baby", (player, pet, data, flag)-> {
		if(pet instanceof IAgeablePet){
			((IAgeablePet) pet).setBaby(flag);
			return true;
		}
		return false;
	}, Material.WHEAT, "Baby"),
	SHEARED("sheared", (player, pet, data, flag)-> {
		if(pet instanceof ISheepPet){
			((ISheepPet) pet).setSheared(flag);
			return true;
		}else if(pet instanceof ISnowmanPet){
			((ISnowmanPet) pet).setSheared(flag);
			return true;
		}
		return false;
	}, Material.SHEARS, "Sheared"),
	FIRE("fire", (player, pet, data, flag)-> {
		if(pet instanceof IBlazePet){
			((IBlazePet) pet).setOnFire(flag);
			return true;
		}
		return false;
	}, Material.FIRE_CHARGE, "Fire"),
	POWERED("powered", (player, pet, data, flag)-> {
		if(pet instanceof ICreeperPet){
			((ICreeperPet) pet).setPowered(flag);
			return true;
		}else if(pet instanceof IVexPet){
			((IVexPet) pet).setPowered(flag);
			return true;
		}
		return false;
	}, Material.BEACON, "Powered"),
	SCREAMING("screaming", (player, pet, data, flag)-> {
		if(pet instanceof IEndermanPet){
			((IEndermanPet) pet).setScreaming(flag);
			return true;
		}
		return false;
	}, Material.ENDER_PEARL, "Screaming"),
	SLIME_SMALL("slime_small", (player, pet, data, flag)-> {
		return setSlimeSize(pet, 1);
	}, Material.SLIME_BALL, "Small"),
	SLIME_MEDIUM("slime_medium", (player, pet, data, flag)-> {
		return setSlimeSize(pet, 2);
	}, Material.SLIME_BALL, "Medium"),
	SLIME_LARGE("slime_large", (player, pet, data, flag)-> {
		return setSlimeSize(pet, 4);
	}, Material.SLIME_BALL, "Large"),
	SHIELD("shield", (player, pet, data, flag)-> {
		if(pet instanceof IWitherPet){
			((IWitherPet) pet).setShielded(flag);
			return true;
		}
		return false;
	}, Material.GLASS, "Shield"),
	SADDLE("saddle", (player, pet, data, flag)-> {
		if(pet instanceof IPigPet){
			((IPigPet) pet).setSaddle(flag);
			return true;
		}else if(pet instanceof IHorseAbstractPet){
			((IHorseAbstractPet) pet).setSaddled(flag);
			return true;
		}
		return false;
	}, Material.SADDLE, "Saddle"),
	STANDING_UP("standing_up", (player, pet, data, flag)-> {
		if(pet instanceof IPolarBearPet){
			((IPolarBearPet) pet).setStandingUp(flag);
			return true;
		}
		return false;
	}, Material.TROPICAL_FISH, "Standing Up"),
	TAMED("tamed", (player, pet, data, flag)-> {
		if(pet instanceof IWolfPet){
			((IWolfPet) pet).setTamed(flag);
			return true;
		}
		return false;
	}, Material.BONE, "Tamed"),
	ANGRY("angry", (player, pet, data, flag)-> {
		if(pet instanceof IWolfPet){
			((IWolfPet) pet).setAngry(flag);
			return true;
		}
		return false;
	}, Material.BONE, "Angry"),
    // Collar Colors
	WHITE("white", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.WHITE);
	}, Material.WHITE_WOOL, "White"),
	ORANGE("orange", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.ORANGE);
	}, Material.ORANGE_WOOL, "Orange"),
	MAGENTA("white", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.MAGENTA);
	}, Material.MAGENTA_WOOL, "Magenta"),
	LIGHT_BLUE("light_blue", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.LIGHT_BLUE);
	}, Material.LIGHT_BLUE_WOOL, "Light Blue"),
	YELLOW("yellow", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.YELLOW);
	}, Material.YELLOW_WOOL, "Yellow"),
	LIME("lime", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.LIME);
	}, Material.LIME_WOOL, "Lime"),
	PINK("pink", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.PINK);
	}, Material.PINK_WOOL, "Pink"),
	GRAY("gray", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.GRAY);
	}, Material.GRAY_WOOL, "Gray"),
	LIGHT_GRAY("light_gray", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.LIGHT_GRAY);
	}, Material.LIGHT_GRAY_WOOL, "Light Gray"),
	CYAN("cyan", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.CYAN);
	}, Material.CYAN_WOOL, "Cyan"),
	PURPLE("purple", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.PURPLE);
	}, Material.PURPLE_WOOL, "Purple"),
	BLUE("blue", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.BLUE);
	}, Material.BLUE_WOOL, "Blue"),
	BROWN("brown", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.BROWN);
	}, Material.BROWN_WOOL, "Brown"),
	GREEN("green", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.GREEN);
	}, Material.GREEN_WOOL, "Green"),
	RED("red", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.RED);
	}, Material.RED_WOOL, "Red"),
	BLACK("black", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.BLACK);
	}, Material.BLACK_WOOL, "Black"),
    // Villager Types
	DESERT("desert", (player, pet, data, flag)-> {
		return setVillagerType(pet, VillagerType.DESERT);
	}, Material.SAND, "Desert"),
	JUNGLE("desert", (player, pet, data, flag)-> {
		return setVillagerType(pet, VillagerType.JUNGLE);
	}, Material.VINE, "Jungle"),
	PLAINS("desert", (player, pet, data, flag)-> {
		return setVillagerType(pet, VillagerType.PLAINS);
	}, Material.GRASS_BLOCK, "Plains"),
	SAVANNA("desert", (player, pet, data, flag)-> {
		return setVillagerType(pet, VillagerType.SAVANNA);
	}, Material.SANDSTONE, "Savanna"),
	SNOWY("desert", (player, pet, data, flag)-> {
		return setVillagerType(pet, VillagerType.SNOWY);
	}, Material.SNOW_BLOCK, "Snowy"),
	SWAMP("desert", (player, pet, data, flag)-> {
		return setVillagerType(pet, VillagerType.SWAMP);
	}, Material.LILY_PAD, "Swamp"),
	TAIGA("desert", (player, pet, data, flag)-> {
		return setVillagerType(pet, VillagerType.TAIGA);
	}, Material.SPRUCE_LOG, "Taiga"),
    // Villager Profession
	NONE("none", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.NONE);
	}, Material.CRAFTING_TABLE, "None"),
	ARMORER("armorer", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.ARMORER);
	}, Material.getMaterial("BLAST_FURNACE"), "Armorer"),
	BUTCHER("butcher", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.BUTCHER);
	}, Material.getMaterial("SMOKER"), "Butcher"),
	CARTOGRAPHER("cartographer", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.CARTOGRAPHER);
	}, Material.getMaterial("CARTOGRAPHY_TABLE"), "Cartographer"),
	CLERIC("cleric", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.CLERIC);
	}, Material.BREWING_STAND, "Cleric"),
	FARMER("farmer", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.FARMER);
	}, Material.getMaterial("COMPOSTER"), "Farmer"),
	FISHERMAN("fisherman", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.FISHERMAN);
	}, Material.getMaterial("BARREL"), "Fisherman"),
	FLETCHER("fletcher", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.FLETCHER);
	}, Material.getMaterial("FLETCHING_TABLE"), "Fletcher"),
	LEATHERWORKER("leatherworker", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.LEATHERWORKER);
	}, Material.CAULDRON, "Leatherworker"),
	LIBRARIAN("librarian", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.LIBRARIAN);
	}, Material.getMaterial("LECTERN"), "Librarian"),
	MASON("mason", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.MASON);
	}, Material.getMaterial("STONECUTTER"), "Stone Mason"),
	NITWIT("nitwit", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.NITWIT);
	}, Material.STONE, "Nitwit"),
	SHEPHERD("sherpherd", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.SHEPHERD);
	}, Material.getMaterial("LOOM"), "Sherpherd"),
	TOOLSMITH("toolsmith", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.TOOLSMITH);
	}, Material.getMaterial("SMITHING_TABLE"), "None"),
	WEAPONSMITH("weaponsmith", (player, pet, data, flag)-> {
		return setProfession(pet, Profession.WEAPONSMITH);
	}, Material.getMaterial("GRINDSTONE"), "Weaponsmith"),
    // Villager Level
	NOVICE("novice", (player, pet, data, flag)-> {
		return setVillagerLevel(pet, VillagerLevel.NOVICE);
	}, Material.STONE, "Novice"),
	APPRENTICE("apprentice", (player, pet, data, flag)-> {
		return setVillagerLevel(pet, VillagerLevel.APPRENTICE);
	}, Material.IRON_INGOT, "Apprentice"),
	JOURNEYMEN("journeymen", (player, pet, data, flag)-> {
		return setVillagerLevel(pet, VillagerLevel.JOURNEYMEN);
	}, Material.GOLD_INGOT, "Journeymen"),
	EXPERT("expert", (player, pet, data, flag)-> {
		return setVillagerLevel(pet, VillagerLevel.EXPERT);
	}, Material.EMERALD, "Expert"),
	MASTER("master", (player, pet, data, flag)-> {
		return setVillagerLevel(pet, VillagerLevel.MASTER);
	}, Material.DIAMOND, "Master"),
    // Rabbit Type
	RABBIT_BROWN("rabbit_brown", (player, pet, data, flag)-> {
		return setRabbitType(pet, Rabbit.Type.BROWN);
	}, Material.BROWN_WOOL, "Brown"),
	RABBIT_WHITE("rabbit_white", (player, pet, data, flag)-> {
		return setRabbitType(pet, Rabbit.Type.WHITE);
	}, Material.WHITE_WOOL, "White"),
	RABBIT_BLACK("rabbit_black", (player, pet, data, flag)-> {
		return setRabbitType(pet, Rabbit.Type.BLACK);
	}, Material.BLACK_WOOL, "Black"),
	RABBIT_BLACK_AND_WHITE("rabbit_black_and_white", (player, pet, data, flag)-> {
		return setRabbitType(pet, Rabbit.Type.BLACK_AND_WHITE);
	}, Material.GRAY_WOOL, "Black and White"),
	RABBIT_GOLD("rabbit_gold", (player, pet, data, flag)-> {
		return setRabbitType(pet, Rabbit.Type.GOLD);
	}, Material.YELLOW_WOOL, "Gold"),
	RABBIT_SALT_AND_PEPPER("rabbit_salt_and_pepper", (player, pet, data, flag)-> {
		return setRabbitType(pet, Rabbit.Type.SALT_AND_PEPPER);
	}, Material.YELLOW_WOOL, "Salt and Pepper"),
	RABBIT_KILLER_BUNNY("rabbit_killer_bunny", (player, pet, data, flag)-> {
		return setRabbitType(pet, Rabbit.Type.THE_KILLER_BUNNY);
	}, Material.RED_WOOL, "Killer Bunny"),
	/*
	BLACKSMITH("blacksmith", DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION),
	BLACK_DOTS("blackSpot", DataMenuType.HORSE_MARKING),
	BLUE("blue", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR, DataMenuType.PARROT_VARIANT),
	BROWN_LLAMA("brown", DataMenuType.LLAMA_VARIANT),
	BUTCHER("butcher", DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION),
	CHESTED("chested", DataMenuType.BOOLEAN),
	CHESTNUT("chestnut", DataMenuType.HORSE_VARIANT),
	CREAMY("creamy", DataMenuType.HORSE_VARIANT, DataMenuType.LLAMA_VARIANT),
	CYAN("cyan", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR, DataMenuType.PARROT_VARIANT),
	DARK_BROWN("darkbrown", DataMenuType.HORSE_VARIANT),
	DIAMOND("diamond", DataMenuType.HORSE_ARMOUR),
	DONKEY("donkey", DataMenuType.HORSE_TYPE),
	ELDER("elder", DataMenuType.BOOLEAN),
	FARMER("farmer", DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION),
	FIRE("fire", DataMenuType.BOOLEAN),
	GRAY("gray", DataMenuType.COLOR, DataMenuType.HORSE_VARIANT, DataMenuType.LLAMA_COLOR, DataMenuType.PARROT_VARIANT),
	GRAY_LLAMA("gray", DataMenuType.LLAMA_VARIANT),
	SILVER("silver", DataMenuType.COLOR, DataMenuType.HORSE_VARIANT, DataMenuType.LLAMA_COLOR),
	GREEN("green", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR, DataMenuType.PARROT_VARIANT),
	HUSK("husk", DataMenuType.ZOMBIE_PROFESSION),
	IRON("iron", DataMenuType.HORSE_ARMOUR),
	LIBRARIAN("librarian", DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION),
	LIGHT_BLUE("lightBlue", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	LIME("lime", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	MAGENTA("magenta", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	MEDIUM("medium", DataMenuType.SIZE),
	MULE("mule", DataMenuType.HORSE_TYPE),
	NOARMOUR("noarmour", DataMenuType.HORSE_ARMOUR),
	NONE("noMarking", DataMenuType.HORSE_MARKING),
	HORSE("normal", DataMenuType.HORSE_TYPE),
	ORANGE("orange", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	PINK("pink", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	PRIEST("priest", DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION),
	PURPLE("purple", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	RED("red", DataMenuType.OCELOT_TYPE, DataMenuType.COLOR, DataMenuType.LLAMA_COLOR, DataMenuType.PARROT_VARIANT),
	SIAMESE("siamese", DataMenuType.OCELOT_TYPE),
	SKELETON_HORSE("skeleton", DataMenuType.HORSE_TYPE),
	VILLAGER("villager", DataMenuType.BOOLEAN),
	WHITEFIELD("whitePatch", DataMenuType.HORSE_MARKING),
	WHITE_DOTS("whiteSpot", DataMenuType.HORSE_MARKING),
	WHITE_SOCKS("whiteSocks", DataMenuType.HORSE_MARKING),
	WHITE_LLAMA("white", DataMenuType.LLAMA_VARIANT),
	WILD("wild", DataMenuType.OCELOT_TYPE),
	YELLOW("yellow", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	UNDEAD_HORSE("zombie", DataMenuType.HORSE_TYPE),
	LEFT_SHOULDER("leftshoulder", DataMenuType.BOOLEAN),
	RIGHT_SHOULDER("rightshoulder", DataMenuType.BOOLEAN),
	OPEN("open", DataMenuType.BOOLEAN)*/
	;

	public static final PetData[] values = values();

    private String configOptionString;
	private Version version;
	private VersionCheckType versionCheckType;
	private PetDataAction action;
	private Material material;
	private String name;
	private List<String> lore;

	private PetData(String configOptionString, PetDataAction action, Material material, String name, String... loreArray){
		this(configOptionString, action, material, new Version(), VersionCheckType.COMPATIBLE, name, loreArray);
	}

	private PetData(String configOptionString, PetDataAction action, Material material, Version version, VersionCheckType versionCheckType, String name, String... loreArray){
        this.configOptionString = configOptionString;
		this.action = action;
		this.version = version;
		this.versionCheckType = versionCheckType;
		this.material = material;
		this.name = name;
		lore = new ArrayList<>();
		for(String s : loreArray){
			lore.add(ChatColor.GOLD + s);
		}
    }

    public String getConfigOptionString() {
        return this.configOptionString;
    }

	public PetDataAction getAction(){
		return action;
	}

	public String getItemName(){
		return name;
	}

	public ItemStack toItem(){
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack toItem(boolean flag){
		ItemStack i = new ItemStack(material);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.RED + this.name + (flag ? ChatColor.GREEN + " [TOGGLE ON]" : ChatColor.YELLOW + " [TOGGLE OFF]"));
		meta.setLore(this.lore);
		i.setItemMeta(meta);
		return i;
	}

	public boolean isCompatible(){
		switch (versionCheckType){
			case IDENTICAL:{// ==
				return version.isIdentical(new Version());
			}
			case SUPPORTED:{// <=
				return version.isSupported(new Version());
			}
			case COMPATIBLE:{// >=
				return version.isCompatible(new Version());
			}
		}
		return true;
	}

	private static boolean setColorByDye(IPet pet, DyeColor color){
		PetType type = pet.getPetType();
		if(type.equals(PetType.SHEEP)){
			((ISheepPet) pet).setDyeColor(color);
		}else if(type.equals(PetType.WOLF)){
			((IWolfPet) pet).setCollarColor(color);
		}
		return true;
	}

	private static boolean setSlimeSize(IPet pet, int size){
		PetType type = pet.getPetType();
		if(type.equals(PetType.SLIME)){
			((ISlimePet) pet).setSize(size);
			return true;
		}else if(type.equals(PetType.MAGMACUBE)){
			((IMagmaCubePet) pet).setSize(size);
			return true;
		}
		return false;
	}

	private static boolean setVillagerType(IPet pet, VillagerType villagerType){
		PetType type = pet.getPetType();
		if(type.equals(PetType.VILLAGER)){
			((IVillagerPet) pet).setType(villagerType);
		}
		return true;
	}

	private static boolean setVillagerLevel(IPet pet, VillagerLevel villagerLevel){
		PetType type = pet.getPetType();
		if(type.equals(PetType.VILLAGER)){
			((IVillagerPet) pet).setLevel(villagerLevel);
		}
		return true;
	}

	private static boolean setProfession(IPet pet, Profession profession){
		PetType type = pet.getPetType();
		if(type.equals(PetType.VILLAGER)){
			((IVillagerPet) pet).setProfession(profession);
		}
		return true;
	}

	private static boolean setRabbitType(IPet pet, Rabbit.Type rabbitType){
		PetType type = pet.getPetType();
		if(type.equals(PetType.RABBIT)){
			((IRabbitPet) pet).setRabbitType(rabbitType);
		}
		return true;
	}
}
