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
import com.dsh105.echopet.compat.api.entity.type.pet.IBlazePet;
import com.dsh105.echopet.compat.api.entity.type.pet.ICatPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ICreeperPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IEndermanPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IFoxPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseAbstractPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseChestedAbstractPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorsePet;
import com.dsh105.echopet.compat.api.entity.type.pet.ILlamaPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IMagmaCubePet;
import com.dsh105.echopet.compat.api.entity.type.pet.IMushroomCowPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPandaPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IParrotPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPigPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPolarBearPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPufferFishPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IRabbitPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISheepPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISlimePet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISnowmanPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ITropicalFishPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IVexPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IVillagerDataHolder;
import com.dsh105.echopet.compat.api.entity.type.pet.IWitherPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IWolfPet;
import com.dsh105.echopet.compat.api.util.Version;
import com.dsh105.echopet.compat.api.util.VersionCheckType;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum PetData{
	HAT("hat", (player, pet, category, flag)->{
		pet.setAsHat(flag);
		return true;
	}, Material.IRON_HELMET, "Hat Pet", "Wear your pet on your head"),
	RIDE("ride", (player, pet, category, flag)->{
		pet.ownerRidePet(flag);
		return true;
	}, Material.CARROT_ON_A_STICK, "Ride Pet", "Control your pet"),
	BABY("baby", (player, pet, category, flag)->{
		if(pet instanceof IAgeablePet){
			((IAgeablePet) pet).setBaby(flag);
			return true;
		}
		return false;
	}, Material.WHEAT, "Baby"),
	SHEARED("sheared", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.SHEEP)){
			((ISheepPet) pet).setSheared(flag);
			return true;
		}else if(pet.getPetType().equals(PetType.SNOWMAN)){
			((ISnowmanPet) pet).setSheared(flag);
			return true;
		}
		return false;
	}, Material.SHEARS, "Sheared"),
	FIRE("fire", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.BLAZE)){
			((IBlazePet) pet).setOnFire(flag);
			return true;
		}
		return false;
	}, Material.FIRE_CHARGE, "Fire"),
	POWERED("powered", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.CREEPER)){
			((ICreeperPet) pet).setPowered(flag);
			return true;
		}else if(pet.getPetType().equals(PetType.VEX)){
			((IVexPet) pet).setPowered(flag);
			return true;
		}
		return false;
	}, Material.BEACON, "Powered"),
	SCREAMING("screaming", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.ENDERMAN)){
			((IEndermanPet) pet).setScreaming(flag);
			return true;
		}
		return false;
	}, Material.ENDER_PEARL, "Screaming"),
	SHIELD("shield", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.WITHER)){
			((IWitherPet) pet).setShielded(flag);
			return true;
		}
		return false;
	}, Material.GLASS, "Shield"),
	SADDLE("saddle", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.PIG)){
			((IPigPet) pet).setSaddle(flag);
			return true;
		}else if(pet instanceof IHorseAbstractPet){
			((IHorseAbstractPet) pet).setSaddled(flag);
			return true;
		}
		return false;
	}, Material.SADDLE, "Saddle"),
	STANDING_UP("standing_up", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.POLARBEAR)){
			((IPolarBearPet) pet).setStandingUp(flag);
			return true;
		}
		return false;
	}, Material.TROPICAL_FISH, "Standing Up"),
	TAMED("tamed", (player, pet, category, flag)->{
		if(pet instanceof ITameablePet){
			((ITameablePet) pet).setTamed(flag);
			return true;
		}
		return false;
	}, Material.BONE, "Tamed"),
	ANGRY("angry", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.WOLF)){
			((IWolfPet) pet).setAngry(flag);
			return true;
		}
		return false;
	}, Material.BONE, "Angry"),
	CHESTED("chest", (player, pet, category, flag)->{
		if(pet instanceof IHorseChestedAbstractPet){
			((IHorseChestedAbstractPet) pet).setChested(flag);
			return true;
		}
		return false;
	}, Material.CHEST, "Chest"),
	SIT("sit", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.FOX)){
			((IFoxPet) pet).setSitting(flag);
			return true;
		}else if(pet.getPetType().equals(PetType.PANDA)){
			((IPandaPet) pet).setSitting(flag);
			return true;
		}
		return false;
	}, Material.LEAD, "Sit"),
	CROUCH("crouch", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.FOX)){
			((IFoxPet) pet).setCrouching(flag);
			return true;
		}
		return false;
	}, Material.LEAD, "Crouch"),
	HEAD_TILT("head_tilt", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.FOX)){
			((IFoxPet) pet).setHeadTilt(flag);
			return true;
		}
		return false;
	}, Material.PLAYER_HEAD, "Head Tilt"),
	POUNCE("pounce", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.FOX)){
			((IFoxPet) pet).setPounce(flag);
			return true;
		}
		return false;
	}, Material.RABBIT, "Head Tilt"),
	SLEEP("sleep", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.FOX)){
			((IFoxPet) pet).setSleeping(flag);
			return true;
		}
		return false;
	}, Material.COOKED_PORKCHOP, "Sleep"),
	LEG_SHAKE("leg_shake", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.FOX)){
			((IFoxPet) pet).setLegShake(flag);
			return true;
		}
		return false;
	}, Material.FEATHER, "Leg Shake"),
	ROLL("roll", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.PANDA)){
			((IPandaPet) pet).setRolling(flag);
			return true;
		}
		return false;
	}, Material.COOKED_SALMON, "Roll"),
	LAY_DOWN("lay_down", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.PANDA)){
			((IPandaPet) pet).setLayingDown(flag);
			return true;
		}
		return false;
	}, Material.CAKE, "Lay Down"),
	
	//
	SIZE_SMALL("size_small", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.SLIME)){
			return setSlimeSize(pet, 1);
		}else if(pet.getPetType().equals(PetType.PUFFERFISH)){
			return setPufferFishState(pet, 0);
		}
		return false;
	}, (pet)->{
		if(pet.getPetType().equals(PetType.PUFFERFISH)){
			return Material.PUFFERFISH;
		}
		return Material.SLIME_BALL;
	}, "Small"),
	SIZE_MEDIUM("size_medium", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.SLIME)){
			return setSlimeSize(pet, 2);
		}else if(pet.getPetType().equals(PetType.PUFFERFISH)){
			return setPufferFishState(pet, 1);
		}
		return false;
	}, (pet)->{
		if(pet.getPetType().equals(PetType.PUFFERFISH)){
			return Material.PUFFERFISH;
		}
		return Material.SLIME_BALL;
	}, "Medium"),
	SIZE_LARGE("size_large", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.SLIME)){
			return setSlimeSize(pet, 4);
		}else if(pet.getPetType().equals(PetType.PUFFERFISH)){
			return setPufferFishState(pet, 2);
		}else if(pet.getPetType().equals(PetType.TROPICALFISH)){
			return setTropicalFishLarge(pet, flag);
		}
		return false;
	}, (pet)->{
		if(pet.getPetType().equals(PetType.PUFFERFISH)){
			return Material.PUFFERFISH;
		}
		return Material.SLIME_BALL;
	}, "Large"),
	CREAMY("creamy", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseColor(pet, Horse.Color.CREAMY);
		}else if(pet.getPetType().equals(PetType.LLAMA) || pet.getPetType().equals(PetType.TRADERLLAMA)){
			return setLlamaColor(pet, Llama.Color.CREAMY);
		}
		return false;
	}, Material.YELLOW_WOOL, "Creamy"),
	// Colors. Used for Collars(wolf), Sheep, Llama Color, and certain Rabbit Types.
	WHITE("white", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.RABBIT)){
			return setRabbitType(pet, Rabbit.Type.WHITE);
		}else if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseColor(pet, Horse.Color.WHITE);
		}else if(pet.getPetType().equals(PetType.LLAMA) || pet.getPetType().equals(PetType.TRADERLLAMA)){
			return setLlamaColor(pet, Llama.Color.WHITE);
		}else if(pet.getPetType().equals(PetType.CAT)){
			return setCatType(pet, CatType.White);
		}else{
			return setColorByDye(pet, category, DyeColor.WHITE);
		}
	}, Material.WHITE_WOOL, "White"),
	ORANGE("orange", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.ORANGE);
	}, Material.ORANGE_WOOL, "Orange"),
	MAGENTA("magenta", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.MAGENTA);
	}, Material.MAGENTA_WOOL, "Magenta"),
	LIGHT_BLUE("light_blue", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.LIGHT_BLUE);
	}, Material.LIGHT_BLUE_WOOL, "Light Blue"),
	YELLOW("yellow", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.YELLOW);
	}, Material.YELLOW_WOOL, "Yellow"),
	LIME("lime", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.LIME);
	}, Material.LIME_WOOL, "Lime"),
	PINK("pink", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.PINK);
	}, Material.PINK_WOOL, "Pink"),
	GRAY("gray", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseColor(pet, Horse.Color.GRAY);
		}else if(pet.getPetType().equals(PetType.LLAMA) || pet.getPetType().equals(PetType.TRADERLLAMA)){
			return setLlamaColor(pet, Llama.Color.GRAY);
		}else{
			return setColorByDye(pet, category, DyeColor.GRAY);
		}
	}, Material.GRAY_WOOL, "Gray"),
	LIGHT_GRAY("light_gray", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.LIGHT_GRAY);
	}, Material.LIGHT_GRAY_WOOL, "Light Gray"),
	CYAN("cyan", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.CYAN);
	}, Material.CYAN_WOOL, "Cyan"),
	PURPLE("purple", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.PURPLE);
	}, Material.PURPLE_WOOL, "Purple"),
	BLUE("blue", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.BLUE);
	}, Material.BLUE_WOOL, "Blue"),
	BROWN("brown", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.RABBIT)){
			return setRabbitType(pet, Rabbit.Type.BROWN);
		}else if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseColor(pet, Horse.Color.BROWN);
		}else if(pet.getPetType().equals(PetType.LLAMA) || pet.getPetType().equals(PetType.TRADERLLAMA)){
			return setLlamaColor(pet, Llama.Color.BROWN);
		}else if(pet.getPetType().equals(PetType.PANDA)){
			return setGene(pet, category, PandaGene.Brown);
		}else if(pet.getPetType().equals(PetType.MUSHROOMCOW)){
			return setMushroomCowType(pet, MushroomCowType.Brown);
		}else{
			return setColorByDye(pet, category, DyeColor.BROWN);
		}
	}, Material.BROWN_WOOL, "Brown"),
	GREEN("green", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.GREEN);
	}, Material.GREEN_WOOL, "Green"),
	RED("red", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.CAT)){
			return setCatType(pet, CatType.Red);
		}else if(pet.getPetType().equals(PetType.FOX)){
			return setFoxType(pet, FoxType.Red);
		}else if(pet.getPetType().equals(PetType.MUSHROOMCOW)){
			return setMushroomCowType(pet, MushroomCowType.Red);
		}
		return setColorByDye(pet, category, DyeColor.RED);
	}, Material.RED_WOOL, "Red"),
	BLACK("black", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.RABBIT)){
			return setRabbitType(pet, Rabbit.Type.BLACK);
		}else if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseColor(pet, Horse.Color.BLACK);
		}else if(pet.getPetType().equals(PetType.CAT)){
			return setCatType(pet, CatType.AllBlack);
		}else{
			return setColorByDye(pet, category, DyeColor.BLACK);
		}
	}, Material.BLACK_WOOL, "Black"),
	// Copypaste of all above colors but using Carpet instead of Wool. Used for Llama
	WHITE_CARPET("white_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.WHITE);
	}, Material.WHITE_CARPET, "White Carpet"),
	ORANGE_CARPET("orange_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.ORANGE);
	}, Material.ORANGE_CARPET, "Orange Carpet"),
	MAGENTA_CARPET("magenta_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.MAGENTA);
	}, Material.MAGENTA_CARPET, "Magenta Carpet"),
	LIGHT_BLUE_CARPET("light_blue_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.LIGHT_BLUE);
	}, Material.LIGHT_BLUE_CARPET, "Light Blue Carpet"),
	YELLOW_CARPET("yellow_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.YELLOW);
	}, Material.YELLOW_CARPET, "Yellow Carpet"),
	LIME_CARPET("lime_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.LIME);
	}, Material.LIME_CARPET, "Lime Carpet"),
	PINK_CARPET("pink_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.PINK);
	}, Material.PINK_CARPET, "Pink Carpet"),
	GRAY_CARPET("gray_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.GRAY);
	}, Material.GRAY_CARPET, "Gray Carpet"),
	LIGHT_GRAY_CARPET("light_gray", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.LIGHT_GRAY);
	}, Material.LIGHT_GRAY_CARPET, "Light Gray Carpet"),
	CYAN_CARPET("cyan_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.CYAN);
	}, Material.CYAN_CARPET, "Cyan Carpet"),
	PURPLE_CARPET("purple_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.PURPLE);
	}, Material.PURPLE_CARPET, "Purple Carpet"),
	BLUE_CARPET("blue_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.BLUE);
	}, Material.BLUE_CARPET, "Blue Carpet"),
	BROWN_CARPET("brown_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.BROWN);
	}, Material.BROWN_CARPET, "Brown Carpet"),
	GREEN_CARPET("green_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.GREEN);
	}, Material.GREEN_CARPET, "Green Carpet"),
	RED_CARPET("red_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.RED);
	}, Material.RED_CARPET, "Red Carpet"),
	BLACK_CARPET("black_carpet", (player, pet, category, flag)->{
		return setColorByDye(pet, category, DyeColor.BLACK);
	}, Material.BLACK_CARPET, "Black Carpet"),
	// Villager Types
	DESERT("desert", (player, pet, category, flag)->{
		return setVillagerType(pet, VillagerType.DESERT);
	}, Material.SAND, "Desert"),
	JUNGLE("jungle", (player, pet, category, flag)->{
		return setVillagerType(pet, VillagerType.JUNGLE);
	}, Material.VINE, "Jungle"),
	PLAINS("plains", (player, pet, category, flag)->{
		return setVillagerType(pet, VillagerType.PLAINS);
	}, Material.GRASS_BLOCK, "Plains"),
	SAVANNA("savanna", (player, pet, category, flag)->{
		return setVillagerType(pet, VillagerType.SAVANNA);
	}, Material.SANDSTONE, "Savanna"),
	SNOWY("snowy", (player, pet, category, flag)->{
		return setVillagerType(pet, VillagerType.SNOWY);
	}, Material.SNOW_BLOCK, "Snowy"),
	SWAMP("swamp", (player, pet, category, flag)->{
		return setVillagerType(pet, VillagerType.SWAMP);
	}, Material.LILY_PAD, "Swamp"),
	TAIGA("taiga", (player, pet, category, flag)->{
		return setVillagerType(pet, VillagerType.TAIGA);
	}, Material.SPRUCE_LOG, "Taiga"),
	// Villager Profession
	NONE("none", (player, pet, category, flag)->{
		return setProfession(pet, Profession.NONE);
	}, Material.CRAFTING_TABLE, "None"),
	ARMORER("armorer", (player, pet, category, flag)->{
		return setProfession(pet, Profession.ARMORER);
	}, Material.getMaterial("BLAST_FURNACE"), "Armorer"),
	BUTCHER("butcher", (player, pet, category, flag)->{
		return setProfession(pet, Profession.BUTCHER);
	}, Material.getMaterial("SMOKER"), "Butcher"),
	CARTOGRAPHER("cartographer", (player, pet, category, flag)->{
		return setProfession(pet, Profession.CARTOGRAPHER);
	}, Material.getMaterial("CARTOGRAPHY_TABLE"), "Cartographer"),
	CLERIC("cleric", (player, pet, category, flag)->{
		return setProfession(pet, Profession.CLERIC);
	}, Material.BREWING_STAND, "Cleric"),
	FARMER("farmer", (player, pet, category, flag)->{
		return setProfession(pet, Profession.FARMER);
	}, Material.getMaterial("COMPOSTER"), "Farmer"),
	FISHERMAN("fisherman", (player, pet, category, flag)->{
		return setProfession(pet, Profession.FISHERMAN);
	}, Material.getMaterial("BARREL"), "Fisherman"),
	FLETCHER("fletcher", (player, pet, category, flag)->{
		return setProfession(pet, Profession.FLETCHER);
	}, Material.getMaterial("FLETCHING_TABLE"), "Fletcher"),
	LEATHERWORKER("leatherworker", (player, pet, category, flag)->{
		return setProfession(pet, Profession.LEATHERWORKER);
	}, Material.CAULDRON, "Leatherworker"),
	LIBRARIAN("librarian", (player, pet, category, flag)->{
		return setProfession(pet, Profession.LIBRARIAN);
	}, Material.getMaterial("LECTERN"), "Librarian"),
	MASON("mason", (player, pet, category, flag)->{
		return setProfession(pet, Profession.MASON);
	}, Material.getMaterial("STONECUTTER"), "Stone Mason"),
	NITWIT("nitwit", (player, pet, category, flag)->{
		return setProfession(pet, Profession.NITWIT);
	}, Material.STONE, "Nitwit"),
	SHEPHERD("sherpherd", (player, pet, category, flag)->{
		return setProfession(pet, Profession.SHEPHERD);
	}, Material.getMaterial("LOOM"), "Sherpherd"),
	TOOLSMITH("toolsmith", (player, pet, category, flag)->{
		return setProfession(pet, Profession.TOOLSMITH);
	}, Material.getMaterial("SMITHING_TABLE"), "None"),
	WEAPONSMITH("weaponsmith", (player, pet, category, flag)->{
		return setProfession(pet, Profession.WEAPONSMITH);
	}, Material.getMaterial("GRINDSTONE"), "Weaponsmith"),
	// Villager Level
	NOVICE("novice", (player, pet, category, flag)->{
		return setVillagerLevel(pet, VillagerLevel.NOVICE);
	}, Material.STONE, "Novice"),
	APPRENTICE("apprentice", (player, pet, category, flag)->{
		return setVillagerLevel(pet, VillagerLevel.APPRENTICE);
	}, Material.IRON_INGOT, "Apprentice"),
	JOURNEYMEN("journeymen", (player, pet, category, flag)->{
		return setVillagerLevel(pet, VillagerLevel.JOURNEYMEN);
	}, Material.GOLD_INGOT, "Journeymen"),
	EXPERT("expert", (player, pet, category, flag)->{
		return setVillagerLevel(pet, VillagerLevel.EXPERT);
	}, Material.EMERALD, "Expert"),
	MASTER("master", (player, pet, category, flag)->{
		return setVillagerLevel(pet, VillagerLevel.MASTER);
	}, Material.DIAMOND, "Master"),
	// Rabbit Type
	// Brown, White, Black are handled above.
	BLACK_AND_WHITE("black_and_white", (player, pet, category, flag)->{
		return setRabbitType(pet, Rabbit.Type.BLACK_AND_WHITE);
	}, Material.GRAY_WOOL, "Black and White"),
	GOLD("gold", (player, pet, category, flag)->{
		return setRabbitType(pet, Rabbit.Type.GOLD);
	}, Material.GOLD_BLOCK, "Gold"),
	SALT_AND_PEPPER("salt_and_pepper", (player, pet, category, flag)->{
		return setRabbitType(pet, Rabbit.Type.SALT_AND_PEPPER);
	}, Material.YELLOW_WOOL, "Salt and Pepper"),
	KILLER_BUNNY("killer_bunny", (player, pet, category, flag)->{
		return setRabbitType(pet, Rabbit.Type.THE_KILLER_BUNNY);
	}, Material.RED_WOOL, "Killer Bunny"),
	// Horse Colors(variant). Creamy, White, Brown, Black, Gray is above
	CHESTNUT("chestnut", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseColor(pet, Horse.Color.CHESTNUT);
		}
		return false;
	}, Material.GRAY_TERRACOTTA, "Chestnut"),
	DARK_BROWN("dark_brown", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseColor(pet, Horse.Color.DARK_BROWN);
		}
		return false;
	}, Material.BROWN_TERRACOTTA, "Dark Brown"),
	// Horse Marking
	NO_MARKING("no_marking", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseStyle(pet, Horse.Style.NONE);
		}
		return false;
	}, Material.LEAD, "No Marking"),
	WHITE_SOCKS("white_socks", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseStyle(pet, Horse.Style.WHITE);
		}
		return false;
	}, Material.WHITE_CARPET, "White Socks"),
	WHITE_FIELD("white_field", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseStyle(pet, Horse.Style.WHITEFIELD);
		}
		return false;
	}, Material.WHITE_WOOL, "White Field"),
	WHITE_DOTS("white_dots", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseStyle(pet, Horse.Style.WHITE_DOTS);
		}
		return false;
	}, Material.WHITE_STAINED_GLASS, "White Dots"),
	BLACK_DOTS("black_dots", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseStyle(pet, Horse.Style.BLACK_DOTS);
		}
		return false;
	}, Material.BLACK_WOOL, "Black Dots"),
	//Horse Armor
	NO_ARMOR("no_armor", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseArmor(pet, HorseArmor.None);
		}
		return false;
	}, Material.ARMOR_STAND, "No Armor"),//none?
	IRON_ARMOR("iron_armor", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseArmor(pet, HorseArmor.Iron);
		}
		return false;
	}, Material.IRON_HORSE_ARMOR, "Iron Armor"),
	GOLD_ARMOR("gold_armor", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseArmor(pet, HorseArmor.Gold);
		}
		return false;
	}, Material.GOLDEN_HORSE_ARMOR, "Gold Armor"),
	DIAMOND_ARMOR("diamond_armor", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.HORSE)){
			return setHorseArmor(pet, HorseArmor.Diamond);
		}
		return false;
	}, Material.DIAMOND_HORSE_ARMOR, "Diamond Armor"),
	// Tropical Fish Patterns
	KOB("kob", (player, pet, category, flag)->{
		return setTropicalFishPattern(pet, TropicalFish.Pattern.KOB);
	}, Material.WHITE_BANNER, "Kob"),
	SUNSTREAK("sunstreak", (player, pet, category, flag)->{
		return setTropicalFishPattern(pet, TropicalFish.Pattern.SUNSTREAK);
	}, Material.ORANGE_BANNER, "Sunstreak"),
	SNOOPER("snooper", (player, pet, category, flag)->{
		return setTropicalFishPattern(pet, TropicalFish.Pattern.SNOOPER);
	}, Material.MAGENTA_BANNER, "Snooper"),
	DASHER("dasher", (player, pet, category, flag)->{
		return setTropicalFishPattern(pet, TropicalFish.Pattern.DASHER);
	}, Material.LIGHT_BLUE_BANNER, "Dasher"),
	BRINELY("brinely", (player, pet, category, flag)->{
		return setTropicalFishPattern(pet, TropicalFish.Pattern.BRINELY);
	}, Material.YELLOW_BANNER, "Brinely"),
	SPOTTY("spotty", (player, pet, category, flag)->{
		return setTropicalFishPattern(pet, TropicalFish.Pattern.SPOTTY);
	}, Material.LIME_BANNER, "Spotty"),
	// The ones below are just the above but with large as true.
	FLOPPER("flopper", (player, pet, category, flag)->{
		return setTropicalFishPattern(pet, TropicalFish.Pattern.FLOPPER);
	}, Material.PINK_BANNER, "Flopper"),
	STRIPEY("stripey", (player, pet, category, flag)->{
		return setTropicalFishPattern(pet, TropicalFish.Pattern.STRIPEY);
	}, Material.GRAY_BANNER, "Stripey"),
	GLITTER("glitter", (player, pet, category, flag)->{
		return setTropicalFishPattern(pet, TropicalFish.Pattern.GLITTER);
	}, Material.LIGHT_GRAY_BANNER, "glitter"),
	BLOCKFISH("blockfish", (player, pet, category, flag)->{
		return setTropicalFishPattern(pet, TropicalFish.Pattern.BLOCKFISH);
	}, Material.CYAN_BANNER, "Blockfish"),
	BETTY("betty", (player, pet, category, flag)->{
		return setTropicalFishPattern(pet, TropicalFish.Pattern.BETTY);
	}, Material.PURPLE_BANNER, "Betty"),
	CLAYFISH("clayfish", (player, pet, category, flag)->{
		return setTropicalFishPattern(pet, TropicalFish.Pattern.CLAYFISH);
	}, Material.BLUE_BANNER, "Clayfish"),
	// Cat Types. Red, White, Black use above options
	TABBY("tabby", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.CAT)){
			return setCatType(pet, CatType.Tabby);
		}else return false;
	}, Material.COD, "Tabby"),
	TUXEDO("tuxedo", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.CAT)){
			return setCatType(pet, CatType.Black);
		}else return false;
	}, Material.WHITE_CARPET, "Tuxedo"),
	SIAMESE("siamese", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.CAT)){
			return setCatType(pet, CatType.Siamese);
		}else return false;
	}, Material.BROWN_CARPET, "Siamese"),
	BRITISH_SHORTHAIR("british_shorthair", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.CAT)){
			return setCatType(pet, CatType.BritishShortHair);
		}else return false;
	}, Material.LIGHT_GRAY_WOOL, "British Shorthair"),
	CALICO("calico", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.CAT)){
			return setCatType(pet, CatType.Calico);
		}else return false;
	}, Material.ORANGE_WOOL, "Calico"),
	PERSIAN("persian", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.CAT)){
			return setCatType(pet, CatType.Persian);
		}else return false;
	}, Material.YELLOW_WOOL, "Persian"),
	RAGDOLL("ragdoll", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.CAT)){
			return setCatType(pet, CatType.Ragdoll);
		}else return false;
	}, Material.WHITE_CARPET, "Ragdoll"),
	JELLIE("jellie", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.CAT)){
			return setCatType(pet, CatType.Jellie);
		}else return false;
	}, Material.GRAY_WOOL, "Jellie"),
	// Fox Type, Red is just normal red wool.
	SNOW("snow", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.FOX)){
			return setFoxType(pet, FoxType.Snow);
		}else return false;
	}, Material.SNOW_BLOCK, "Snow"),
	//Panda Types, Brown is normal brown wool.
	NORMAL("normal", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.PANDA)){
			return setGene(pet, category, PandaGene.Normal);
		}else return false;
	}, Material.WHITE_WOOL, "Normal"),
	LAZY("lazy", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.PANDA)){
			return setGene(pet, category, PandaGene.Lazy);
		}else return false;
	}, Material.CAKE, "Lazy"),
	WORRIED("worried", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.PANDA)){
			return setGene(pet, category, PandaGene.Worried);
		}else return false;
	}, Material.SLIME_BALL, "Worried"),
	PLAYFUL("playful", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.PANDA)){
			return setGene(pet, category, PandaGene.Playful);
		}else return false;
	}, Material.COOKED_SALMON, "Playful"),
	AGGRESSIVE("aggressive", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.PANDA)){
			return setGene(pet, category, PandaGene.Aggressive);
		}else return false;
	}, Material.RED_WOOL, "Aggressive"),
	WEAK("weak", (player, pet, category, flag)->{
		if(pet.getPetType().equals(PetType.PANDA)){
			return setGene(pet, category, PandaGene.Weak);
		}else return false;
	}, Material.WOODEN_SWORD, "Weak"),
	;
	
	public static final PetData[] values = values();
	
	private String configOptionString;
	private Version version;
	private VersionCheckType versionCheckType;
	private PetDataAction action;
	private PetDataMaterial material;
	private String name;
	private List<String> lore;
	
	PetData(String configOptionString, PetDataAction action, Material material, String name, String... loreArray){
		this(configOptionString, action, (pet)->material, new Version(), VersionCheckType.COMPATIBLE, name, loreArray);
	}
	
	PetData(String configOptionString, PetDataAction action, PetDataMaterial material, String name, String... loreArray){
		this(configOptionString, action, material, new Version(), VersionCheckType.COMPATIBLE, name, loreArray);
	}
	
	PetData(String configOptionString, PetDataAction action, PetDataMaterial material, Version version, VersionCheckType versionCheckType, String name, String... loreArray){
		this.configOptionString = configOptionString;
		this.action = action;
		this.version = version;
		this.versionCheckType = versionCheckType;
		this.material = material;
		this.name = name;
		this.lore = new ArrayList<>();
		for(String s : loreArray){
			lore.add(ChatColor.GOLD + s);
		}
	}
	
	public String getConfigOptionString(){
		return this.configOptionString;
	}
	
	public PetDataAction getAction(){
		return action;
	}
	
	public String getItemName(){
		return name;
	}
	
	public boolean ignoreSaving(){
		return this == PetData.RIDE || this == PetData.HAT;
	}
	
	public ItemStack toItem(IPet pet){
		ItemStack item = new ItemStack(material.get(pet));
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack toItem(IPet pet, boolean flag){
		ItemStack i = new ItemStack(material.get(pet));
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.RED + this.name + (flag ? ChatColor.GREEN + " [TOGGLE ON]" : ChatColor.YELLOW + " [TOGGLE OFF]"));
		meta.setLore(this.lore);
		i.setItemMeta(meta);
		return i;
	}
	
	public boolean isCompatible(){
		switch(versionCheckType){
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
	
	private static boolean setColorByDye(IPet pet, PetDataCategory category, DyeColor color){
		PetType type = pet.getPetType();
		if(type.equals(PetType.SHEEP)){
			((ISheepPet) pet).setDyeColor(color);
		}else if(type.equals(PetType.WOLF)){
			((IWolfPet) pet).setCollarColor(color);
		}else if(type.equals(PetType.PARROT)){
			((IParrotPet) pet).setVariant(ParrotVariant.valueOf(color.name()));
		}else if(type.equals(PetType.LLAMA) || pet.getPetType().equals(PetType.TRADERLLAMA)){
			((ILlamaPet) pet).setCarpetColor(color);
		}else if(type.equals(PetType.TROPICALFISH)){
			ITropicalFishPet fish = ((ITropicalFishPet) pet);
			if(category.equals(PetDataCategory.TROPICAL_FISH_COLOR)){
				fish.setColor(color);
			}else if(category.equals(PetDataCategory.TROPICAL_FISH_PATTERN_COLOR)){
				fish.setPatternColor(color);
			}
		}else if(type.equals(PetType.CAT)){
			((ICatPet) pet).setCollarColor(color);
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
	
	private static boolean setPufferFishState(IPet pet, int state){
		PetType type = pet.getPetType();
		if(type.equals(PetType.PUFFERFISH)){
			((IPufferFishPet) pet).setPuffState(state);
			return true;
		}
		return false;
	}
	
	private static boolean setVillagerType(IPet pet, VillagerType villagerType){
		PetType type = pet.getPetType();
		if(type.equals(PetType.VILLAGER) || type.equals(PetType.ZOMBIEVILLAGER)){
			((IVillagerDataHolder) pet).setType(villagerType);
		}
		return true;
	}
	
	private static boolean setVillagerLevel(IPet pet, VillagerLevel villagerLevel){
		PetType type = pet.getPetType();
		if(type.equals(PetType.VILLAGER) || type.equals(PetType.ZOMBIEVILLAGER)){
			((IVillagerDataHolder) pet).setLevel(villagerLevel);
		}
		return true;
	}
	
	private static boolean setProfession(IPet pet, Profession profession){
		PetType type = pet.getPetType();
		if(type.equals(PetType.VILLAGER) || type.equals(PetType.ZOMBIEVILLAGER)){
			((IVillagerDataHolder) pet).setProfession(profession);
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
	
	private static boolean setHorseColor(IPet pet, Horse.Color color){
		PetType type = pet.getPetType();
		if(type.equals(PetType.HORSE)){
			((IHorsePet) pet).setColor(color);
		}
		return true;
	}
	
	private static boolean setHorseStyle(IPet pet, Horse.Style style){
		PetType type = pet.getPetType();
		if(type.equals(PetType.HORSE)){
			((IHorsePet) pet).setStyle(style);
		}
		return true;
	}
	
	private static boolean setHorseArmor(IPet pet, HorseArmor armor){
		PetType type = pet.getPetType();
		if(type.equals(PetType.HORSE)){
			((IHorsePet) pet).setArmour(armor);
		}
		return true;
	}
	
	private static boolean setLlamaColor(IPet pet, Llama.Color color){
		PetType type = pet.getPetType();
		if(type.equals(PetType.LLAMA) || pet.getPetType().equals(PetType.TRADERLLAMA)){
			((ILlamaPet) pet).setSkinColor(color);
		}
		return true;
	}
	
	private static boolean setTropicalFishPattern(IPet pet, TropicalFish.Pattern pattern){
		PetType type = pet.getPetType();
		if(type.equals(PetType.TROPICALFISH)){
			((ITropicalFishPet) pet).setPattern(pattern);
			((ITropicalFishPet) pet).setLarge(pattern.ordinal() > 5);
		}
		return true;
	}
	
	private static boolean setTropicalFishLarge(IPet pet, boolean large){
		PetType type = pet.getPetType();
		if(type.equals(PetType.TROPICALFISH)){
			((ITropicalFishPet) pet).setLarge(large);
		}
		return true;
	}
	
	private static boolean setCatType(IPet pet, CatType catType){
		PetType type = pet.getPetType();
		if(type.equals(PetType.CAT)){
			((ICatPet) pet).setType(catType);
		}
		return true;
	}
	
	private static boolean setFoxType(IPet pet, FoxType foxType){
		PetType type = pet.getPetType();
		if(type.equals(PetType.FOX)){
			((IFoxPet) pet).setType(foxType);
		}
		return true;
	}
	
	private static boolean setGene(IPet pet, PetDataCategory category, PandaGene gene){
		if(!pet.getPetType().equals(PetType.PANDA)) return false;
		IPandaPet panda = ((IPandaPet) pet);
		if(category.equals(PetDataCategory.PANDA_MAIN_GENE)){
			panda.setMainGene(gene);
		}else if(category.equals(PetDataCategory.PANDA_HIDDEN_GENE)){
			panda.setHiddenGene(gene);
		}
		return true;
	}
	
	private static boolean setMushroomCowType(IPet pet, MushroomCowType cowType){
		PetType type = pet.getPetType();
		if(type.equals(PetType.MUSHROOMCOW)){
			((IMushroomCowPet) pet).setType(cowType);
		}
		return true;
	}
}