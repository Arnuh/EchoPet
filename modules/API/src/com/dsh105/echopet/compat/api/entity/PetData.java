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
	BLACK("black", (player, pet, data, flag)-> {
		return setColorByDye(pet, DyeColor.BLACK);
	}, Material.BLACK_WOOL, "Black"),
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
    /*ANGRY("angry", DataMenuType.BOOLEAN),
	BABY("baby", DataMenuType.BOOLEAN),
	BLACK("black", DataMenuType.COLOR, DataMenuType.OCELOT_TYPE, DataMenuType.HORSE_VARIANT, DataMenuType.RABBIT_TYPE, DataMenuType.LLAMA_COLOR),
	BLACK_AND_WHITE("blackandwhite", DataMenuType.RABBIT_TYPE),
	BLACKSMITH("blacksmith", DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION),
	BLACK_DOTS("blackSpot", DataMenuType.HORSE_MARKING),
	BLUE("blue", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR, DataMenuType.PARROT_VARIANT),
	BROWN("brown", DataMenuType.COLOR, DataMenuType.RABBIT_TYPE, DataMenuType.LLAMA_COLOR),
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
	GOLD("gold", DataMenuType.HORSE_ARMOUR, DataMenuType.RABBIT_TYPE),
	HUSK("husk", DataMenuType.ZOMBIE_PROFESSION),
	IRON("iron", DataMenuType.HORSE_ARMOUR),
	THE_KILLER_BUNNY("killerbunny", DataMenuType.RABBIT_TYPE),
	LARGE("large", DataMenuType.SIZE),
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
	POWER("powered", DataMenuType.BOOLEAN),
	PRIEST("priest", DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION),
	PURPLE("purple", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	RED("red", DataMenuType.OCELOT_TYPE, DataMenuType.COLOR, DataMenuType.LLAMA_COLOR, DataMenuType.PARROT_VARIANT),
	SADDLE("saddle", DataMenuType.BOOLEAN),
	SALT_AND_PEPPER("saltandpepper", DataMenuType.RABBIT_TYPE),
	SCREAMING("screaming", DataMenuType.BOOLEAN),
	SHEARED("sheared", DataMenuType.BOOLEAN),
	SHIELD("shield", DataMenuType.BOOLEAN),
	SIAMESE("siamese", DataMenuType.OCELOT_TYPE),
	SKELETON_HORSE("skeleton", DataMenuType.HORSE_TYPE),
	SMALL("small", DataMenuType.SIZE),
	TAMED("tamed", DataMenuType.BOOLEAN),
	VILLAGER("villager", DataMenuType.BOOLEAN),
	WHITEFIELD("whitePatch", DataMenuType.HORSE_MARKING),
	WHITE_DOTS("whiteSpot", DataMenuType.HORSE_MARKING),
	WHITE_SOCKS("whiteSocks", DataMenuType.HORSE_MARKING),
	WHITE("white", DataMenuType.COLOR, DataMenuType.HORSE_VARIANT, DataMenuType.RABBIT_TYPE, DataMenuType.LLAMA_COLOR),
	WHITE_LLAMA("white", DataMenuType.LLAMA_VARIANT),
	WILD("wild", DataMenuType.OCELOT_TYPE),
	YELLOW("yellow", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	UNDEAD_HORSE("zombie", DataMenuType.HORSE_TYPE),
	STANDING_UP("standingup", DataMenuType.BOOLEAN),
	NORMAL("normal", DataMenuType.SKELETON_TYPE),
	WITHER("wither", DataMenuType.SKELETON_TYPE),
	STRAY("stray", DataMenuType.SKELETON_TYPE),
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
}
