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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.entity.type.pet.IAxolotlPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IBatPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IBeePet;
import com.dsh105.echopet.compat.api.entity.type.pet.IBlazePet;
import com.dsh105.echopet.compat.api.entity.type.pet.ICatPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ICreeperPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IEndermanPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IFoxPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IGlowSquidPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IGoatPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseAbstractPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseChestedAbstractPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorsePet;
import com.dsh105.echopet.compat.api.entity.type.pet.ILlamaPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IMagmaCubePet;
import com.dsh105.echopet.compat.api.entity.type.pet.IMushroomCowPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPandaPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IParrotPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPigPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPiglinPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPolarBearPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPufferFishPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IRabbitPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISheepPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISlimePet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISnowmanPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IStriderPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ITropicalFishPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IVexPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IVillagerDataHolder;
import com.dsh105.echopet.compat.api.entity.type.pet.IWitherPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IWolfPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ItemUtil;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.Version;
import com.dsh105.echopet.compat.api.util.VersionCheckType;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PetData<T>{
	
	public static final List<PetData<?>> values = new ArrayList<>();
	
	//@formatter:off
	public static final PetData<Boolean>
		HAT = create("hat", (player, pet, category)->{
			return pet::setAsHat;
		}, Material.IRON_HELMET, "Hat Pet", "Wear your pet on your head"),
		RIDE = create("ride", (player, pet, category)->{
			return pet::ownerRidePet;
		}, Material.CARROT_ON_A_STICK, "Ride Pet", "Control your pet"),
		BABY = create("baby", (player, pet, category)->{
			if(pet instanceof IAgeablePet ageablePet){
				return ageablePet::setBaby;
			}
			return null;
		}, Material.WHEAT, "Baby"),
		SHEARED = create("sheared", (player, pet, category)->{
			if(pet instanceof ISheepPet sheepPet){
				return sheepPet::setSheared;
			}else if(pet instanceof ISnowmanPet snowmanPet){
				return snowmanPet::setSheared;
			}
			return null;
		}, Material.SHEARS, "Sheared"),
		FIRE = create("fire", (player, pet, category)->{
			if(pet instanceof IBlazePet blazePet){
				return blazePet::setOnFire;
			}
			return null;
		}, Material.FIRE_CHARGE, "Fire"),
		POWERED = create("powered", (player, pet, category)->{
			if(pet instanceof ICreeperPet creeperPet){
				return creeperPet::setPowered;
			}else if(pet instanceof IVexPet vexPet){
				return vexPet::setPowered;
			}
			return null;
		}, Material.BEACON, "Powered"),
		SCREAMING = create("screaming", (player, pet, category)->{
			if(pet instanceof IEndermanPet endermanPet){
				return endermanPet::setScreaming;
			}else if(pet instanceof IGoatPet goatPet){
				return goatPet::setScreaming;
			}
			return null;
		}, Material.ENDER_PEARL, "Screaming"),
		SHIELD = create("shield", (player, pet, category)->{
			if(pet instanceof IWitherPet witherPet){
				return witherPet::setShielded;
			}
			return null;
		}, Material.GLASS, "Shield"),
		SADDLE = create("saddle", (player, pet, category)->{
			if(pet instanceof IPigPet pig){
				return pig::setSaddle;
			}else if(pet instanceof IHorseAbstractPet horseAbstract){
				return horseAbstract::setSaddled;
			}else if(pet instanceof IStriderPet strider){
				return strider::setHasSaddle;
			}
			return null;
		}, Material.SADDLE, "Saddle"),
		STANDING_UP = create("standing_up", (player, pet, category)->{
			if(pet instanceof IPolarBearPet polarBearPet){
				return polarBearPet::setStandingUp;
			}
			return null;
		}, Material.TROPICAL_FISH, "Standing Up"),
		TAMED = create("tamed", (player, pet, category)->{
			if(pet instanceof ITameablePet tameablePet){
				return tameablePet::setTamed;
			}
			return null;
		}, Material.BONE, "Tamed"),
		ANGRY = create("angry", (player, pet, category)->{
			if(pet instanceof IWolfPet wolfPet){
				return wolfPet::setAngry;
			}else if(pet instanceof IBeePet beePet){
				return beePet::setAngry;
			}
			return null;
		}, pet->{
			if(pet.getPetType().equals(PetType.WOLF)){
				return Material.PORKCHOP;
			}
			return Material.STONE_SWORD;// Bee
		}, "Angry"),
		CHESTED = create("chest", (player, pet, category)->{
			if(pet instanceof IHorseChestedAbstractPet horseChestedAbstractPet){
				return horseChestedAbstractPet::setChested;
			}
			return null;
		}, Material.CHEST, "Chest"),
		SIT = create("sit", (player, pet, category)->{
			if(pet instanceof IFoxPet foxPet){
				return foxPet::setSitting;
			}else if(pet instanceof IPandaPet pandaPet){
				return pandaPet::setSitting;
			}else if(pet instanceof ITameablePet tameablePet){
				return tameablePet::setSitting;
			}
			return null;
		}, Material.LEAD, "Sit"),
		CROUCH = create("crouch", (player, pet, category)->{
			if(pet instanceof IFoxPet foxPet){
				return foxPet::setCrouching;
			}
			return null;
		}, Material.LEAD, "Crouch"),
		HEAD_TILT = create("head_tilt", (player, pet, category)->{
			if(pet instanceof IFoxPet foxPet){
				return foxPet::setHeadTilt;
			}
			return null;
		}, Material.PLAYER_HEAD, "Head Tilt"),
		POUNCE = create("pounce", (player, pet, category)->{
			if(pet instanceof IFoxPet foxPet){
				return foxPet::setPounce;
			}
			return null;
		}, Material.RABBIT, "Pounce"),
		SLEEP = create("sleep", (player, pet, category)->{
			if(pet instanceof IFoxPet foxPet){
				return foxPet::setSleeping;
			}
			return null;
		}, Material.COOKED_PORKCHOP, "Sleep"),
		LEG_SHAKE = create("leg_shake", (player, pet, category)->{
			if(pet instanceof IFoxPet foxPet){
				return foxPet::setLegShake;
			}
			return null;
		}, Material.FEATHER, "Leg Shake"),
		ROLL = create("roll", (player, pet, category)->{
			if(pet instanceof IPandaPet pandaPet){
				return pandaPet::setRolling;
			}
			return null;
		}, Material.COOKED_SALMON, "Roll"),
		LAY_DOWN = create("lay_down", (player, pet, category)->{
			if(pet instanceof IPandaPet pandaPet){
				return pandaPet::setLayingDown;
			}
			return null;
		}, Material.CAKE, "Lay Down"),
		// Bee
		STINGER = create("stinger", (player, pet, category)->{
			if(pet instanceof IBeePet beePet){
				//"stinger" and "has stun" are kinda flipped but it's better than an item called "Stun" or "Has Stun"
				return beePet::setHasStung;
			}
			return null;
		}, Material.POISONOUS_POTATO, "Stinger"),
		NECTAR = create("nectar", (player, pet, category)->{
			if(pet instanceof IBeePet beePet){
				return beePet::setHasNectar;
			}
			return null;
		}, Material.getMaterial("HONEY_BOTTLE"), "Nectar"),// HONEYCOMB ?
		DANCE = create("dance", (player, pet, category)->{
			if(pet instanceof IPiglinPet piglinPet){
				return piglinPet::setDancing;
			}
			return null;
		}, Material.GOLD_INGOT, "Dance"),
		PLAYING_DEAD = create("playing_dead", (player, pet, category)->{
			if(pet instanceof IAxolotlPet axolotl){
				return axolotl::setPlayingDead;
			}
			return null;
		}, Material.HEART_OF_THE_SEA, "Playing Dead"),
		DARK = create("dark", (player, pet, category)->{
			if(pet instanceof IGlowSquidPet glowSquid){
				return glowSquid::setDark;
			}
			return null;
		}, Material.INK_SAC, "Dark"),
		SIZE_SMALL = create("size_small", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.SLIME) || pet.getPetType().equals(PetType.MAGMACUBE)){
				return value->setSlimeSize(pet, 1);
			}else if(pet.getPetType().equals(PetType.PUFFERFISH)){
				return value->setPufferFishState(pet, 0);
			}
			return null;
		}, pet->{
			if(pet.getPetType().equals(PetType.PUFFERFISH)){
				return Material.PUFFERFISH;
			}
			return Material.SLIME_BALL;
		}, "Small"),
		SIZE_MEDIUM = create("size_medium", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.SLIME) || pet.getPetType().equals(PetType.MAGMACUBE)){
				return value->setSlimeSize(pet, 2);
			}else if(pet.getPetType().equals(PetType.PUFFERFISH)){
				return value->setPufferFishState(pet, 1);
			}
			return null;
		}, pet->{
			if(pet.getPetType().equals(PetType.PUFFERFISH)){
				return Material.PUFFERFISH;
			}
			return Material.SLIME_BALL;
		}, "Medium"),
		SIZE_LARGE = create("size_large", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.SLIME) || pet.getPetType().equals(PetType.MAGMACUBE)){
				return value->setSlimeSize(pet, 4);
			}else if(pet.getPetType().equals(PetType.PUFFERFISH)){
				return value->setPufferFishState(pet, 2);
			}else if(pet.getPetType().equals(PetType.TROPICALFISH)){
				return setTropicalFishLarge(pet);
			}
			return null;
		}, pet->{
			if(pet.getPetType().equals(PetType.PUFFERFISH)){
				return Material.PUFFERFISH;
			}
			return Material.SLIME_BALL;
		}, "Large"),
		// Panda Types, Brown is normal brown wool.
		NORMAL = create("normal", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.PANDA)){
				return value->setGene(pet, category, PandaGene.Normal);
			}
			return null;
		}, Material.WHITE_WOOL, "Normal"),
		LAZY = create("lazy", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.PANDA)){
				return value->setGene(pet, category, PandaGene.Lazy);
			}
			return null;
		}, Material.CAKE, "Lazy"),
		WORRIED = create("worried", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.PANDA)){
				return value->setGene(pet, category, PandaGene.Worried);
			}
			return null;
		}, Material.SLIME_BALL, "Worried"),
		PLAYFUL = create("playful", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.PANDA)){
				return value->setGene(pet, category, PandaGene.Playful);
			}
			return null;
		}, Material.COOKED_SALMON, "Playful"),
		AGGRESSIVE = create("aggressive", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.PANDA)){
				return value->setGene(pet, category, PandaGene.Aggressive);
			}
			return null;
		}, Material.RED_WOOL, "Aggressive"),
		WEAK = create("weak", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.PANDA)){
				return value->setGene(pet, category, PandaGene.Weak);
			}
			return null;
		}, Material.WOODEN_SWORD, "Weak"),
		WANDER = create("wander", (player, pet, category)->{
			if(pet instanceof IBatPet bat){
				return bat::setWandering;
			}
			return null;
		}, Material.ELYTRA, "Wander"),
		// Colors. Used for Collars(Wolf, Cat), Sheep, Llama Color, certain Rabbit Types, Axolotl.
		WHITE = create("white", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.RABBIT)){
				return value->setRabbitType(pet, Rabbit.Type.WHITE);
			}else if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseColor(pet, Horse.Color.WHITE);
			}else if(pet.getPetType().equals(PetType.LLAMA) || pet.getPetType().equals(PetType.TRADERLLAMA)){
				return value->setLlamaColor(pet, Llama.Color.WHITE);
			}
			return value->setColorByDye(pet, category, DyeColor.WHITE);
		}, Material.WHITE_WOOL, "White"),
		ORANGE = create("orange", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.ORANGE);
		}, Material.ORANGE_WOOL, "Orange"),
		MAGENTA = create("magenta", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.MAGENTA);
		}, Material.MAGENTA_WOOL, "Magenta"),
		LIGHT_BLUE = create("light_blue", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.LIGHT_BLUE);
		}, Material.LIGHT_BLUE_WOOL, "Light Blue"),
		YELLOW = create("yellow", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.YELLOW);
		}, Material.YELLOW_WOOL, "Yellow"),
		LIME = create("lime", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.LIME);
		}, Material.LIME_WOOL, "Lime"),
		PINK = create("pink", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.PINK);
		}, Material.PINK_WOOL, "Pink"),
		GRAY = create("gray", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseColor(pet, Horse.Color.GRAY);
			}else if(pet.getPetType().equals(PetType.LLAMA) || pet.getPetType().equals(PetType.TRADERLLAMA)){
				return value->setLlamaColor(pet, Llama.Color.GRAY);
			}else{
				return value->setColorByDye(pet, category, DyeColor.GRAY);
			}
		}, Material.GRAY_WOOL, "Gray"),
		LIGHT_GRAY = create("light_gray", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.LIGHT_GRAY);
		}, Material.LIGHT_GRAY_WOOL, "Light Gray"),
		CYAN = create("cyan", (player, pet, category)->{
			if(pet instanceof IAxolotlPet axolotl){
				return value->axolotl.setVariant(IAxolotlPet.Variant.Cyan);
			}
			return value->setColorByDye(pet, category, DyeColor.CYAN);
		}, Material.CYAN_WOOL, "Cyan"),
		PURPLE = create("purple", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.PURPLE);
		}, Material.PURPLE_WOOL, "Purple"),
		BLUE = create("blue", (player, pet, category)->{
			if(pet instanceof IAxolotlPet axolotl){
				return value->axolotl.setVariant(IAxolotlPet.Variant.Blue);
			}
			return value->setColorByDye(pet, category, DyeColor.BLUE);
		}, Material.BLUE_WOOL, "Blue"),
		BROWN = create("brown", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.RABBIT)){
				return value->setRabbitType(pet, Rabbit.Type.BROWN);
			}else if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseColor(pet, Horse.Color.BROWN);
			}else if(pet.getPetType().equals(PetType.LLAMA) || pet.getPetType().equals(PetType.TRADERLLAMA)){
				return value->setLlamaColor(pet, Llama.Color.BROWN);
			}else if(pet.getPetType().equals(PetType.PANDA)){
				return value->setGene(pet, category, PandaGene.Brown);
			}else if(pet.getPetType().equals(PetType.MUSHROOMCOW)){
				return value->setMushroomCowType(pet, MushroomCowType.Brown);
			}
			return value->setColorByDye(pet, category, DyeColor.BROWN);
		}, Material.BROWN_WOOL, "Brown"),
		GREEN = create("green", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.GREEN);
		}, Material.GREEN_WOOL, "Green"),
		RED = create("red", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.FOX)){
				return value->setFoxType(pet, FoxType.Red);
			}else if(pet.getPetType().equals(PetType.MUSHROOMCOW)){
				return value->setMushroomCowType(pet, MushroomCowType.Red);
			}
			return value->setColorByDye(pet, category, DyeColor.RED);
		}, Material.RED_WOOL, "Red"),
		BLACK = create("black", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.RABBIT)){
				return value->setRabbitType(pet, Rabbit.Type.BLACK);
			}else if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseColor(pet, Horse.Color.BLACK);
			}
			return value->setColorByDye(pet, category, DyeColor.BLACK);
		}, Material.BLACK_WOOL, "Black"),
		GOLD = create("gold", (player, pet, category)->{
			if(pet instanceof IAxolotlPet axolotl){
				return value->axolotl.setVariant(IAxolotlPet.Variant.Gold);
			}
			return value->setRabbitType(pet, Rabbit.Type.GOLD);
		}, Material.GOLD_BLOCK, "Gold"),
		// Copypaste of all above colors but using Carpet instead of Wool. Used for Llama
		WHITE_CARPET = create("white_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.WHITE);
		}, Material.WHITE_CARPET, "White Carpet"),
		ORANGE_CARPET = create("orange_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.ORANGE);
		}, Material.ORANGE_CARPET, "Orange Carpet"),
		MAGENTA_CARPET = create("magenta_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.MAGENTA);
		}, Material.MAGENTA_CARPET, "Magenta Carpet"),
		LIGHT_BLUE_CARPET = create("light_blue_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.LIGHT_BLUE);
		}, Material.LIGHT_BLUE_CARPET, "Light Blue Carpet"),
		YELLOW_CARPET = create("yellow_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.YELLOW);
		}, Material.YELLOW_CARPET, "Yellow Carpet"),
		LIME_CARPET = create("lime_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.LIME);
		}, Material.LIME_CARPET, "Lime Carpet"),
		PINK_CARPET = create("pink_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.PINK);
		}, Material.PINK_CARPET, "Pink Carpet"),
		GRAY_CARPET = create("gray_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.GRAY);
		}, Material.GRAY_CARPET, "Gray Carpet"),
		LIGHT_GRAY_CARPET = create("light_gray", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.LIGHT_GRAY);
		}, Material.LIGHT_GRAY_CARPET, "Light Gray Carpet"),
		CYAN_CARPET = create("cyan_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.CYAN);
		}, Material.CYAN_CARPET, "Cyan Carpet"),
		PURPLE_CARPET = create("purple_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.PURPLE);
		}, Material.PURPLE_CARPET, "Purple Carpet"),
		BLUE_CARPET = create("blue_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.BLUE);
		}, Material.BLUE_CARPET, "Blue Carpet"),
		BROWN_CARPET = create("brown_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.BROWN);
		}, Material.BROWN_CARPET, "Brown Carpet"),
		GREEN_CARPET = create("green_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.GREEN);
		}, Material.GREEN_CARPET, "Green Carpet"),
		RED_CARPET = create("red_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.RED);
		}, Material.RED_CARPET, "Red Carpet"),
		BLACK_CARPET = create("black_carpet", (player, pet, category)->{
			return value->setColorByDye(pet, category, DyeColor.BLACK);
		}, Material.BLACK_CARPET, "Black Carpet"),
		// Villager Types
		DESERT = create("desert", (player, pet, category)->{
			return value->setVillagerType(pet, VillagerType.DESERT);
		}, Material.SAND, "Desert"),
		JUNGLE = create("jungle", (player, pet, category)->{
			return value->setVillagerType(pet, VillagerType.JUNGLE);
		}, Material.VINE, "Jungle"),
		PLAINS = create("plains", (player, pet, category)->{
			return value->setVillagerType(pet, VillagerType.PLAINS);
		}, Material.GRASS_BLOCK, "Plains"),
		SAVANNA = create("savanna", (player, pet, category)->{
			return value->setVillagerType(pet, VillagerType.SAVANNA);
		}, Material.SANDSTONE, "Savanna"),
		SNOWY = create("snowy", (player, pet, category)->{
			return value->setVillagerType(pet, VillagerType.SNOWY);
		}, Material.SNOW_BLOCK, "Snowy"),
		SWAMP = create("swamp", (player, pet, category)->{
			return value->setVillagerType(pet, VillagerType.SWAMP);
		}, Material.LILY_PAD, "Swamp"),
		TAIGA = create("taiga", (player, pet, category)->{
			return value->setVillagerType(pet, VillagerType.TAIGA);
		}, Material.SPRUCE_LOG, "Taiga"),
		// Villager Profession
		NONE = create("none", (player, pet, category)->{
			return value->setProfession(pet, Profession.NONE);
		}, Material.CRAFTING_TABLE, "None"),
		ARMORER = create("armorer", (player, pet, category)->{
			return value->setProfession(pet, Profession.ARMORER);
		}, Material.getMaterial("BLAST_FURNACE"), "Armorer"),
		BUTCHER = create("butcher", (player, pet, category)->{
			return value->setProfession(pet, Profession.BUTCHER);
		}, Material.getMaterial("SMOKER"), "Butcher"),
		CARTOGRAPHER = create("cartographer", (player, pet, category)->{
			return value->setProfession(pet, Profession.CARTOGRAPHER);
		}, Material.getMaterial("CARTOGRAPHY_TABLE"), "Cartographer"),
		CLERIC = create("cleric", (player, pet, category)->{
			return value->setProfession(pet, Profession.CLERIC);
		}, Material.BREWING_STAND, "Cleric"),
		FARMER = create("farmer", (player, pet, category)->{
			return value->setProfession(pet, Profession.FARMER);
		}, Material.getMaterial("COMPOSTER"), "Farmer"),
		FISHERMAN = create("fisherman", (player, pet, category)->{
			return value->setProfession(pet, Profession.FISHERMAN);
		}, Material.getMaterial("BARREL"), "Fisherman"),
		FLETCHER = create("fletcher", (player, pet, category)->{
			return value->setProfession(pet, Profession.FLETCHER);
		}, Material.getMaterial("FLETCHING_TABLE"), "Fletcher"),
		LEATHERWORKER = create("leatherworker", (player, pet, category)->{
			return value->setProfession(pet, Profession.LEATHERWORKER);
		}, Material.CAULDRON, "Leatherworker"),
		LIBRARIAN = create("librarian", (player, pet, category)->{
			return value->setProfession(pet, Profession.LIBRARIAN);
		}, Material.getMaterial("LECTERN"), "Librarian"),
		MASON = create("mason", (player, pet, category)->{
			return value->setProfession(pet, Profession.MASON);
		}, Material.getMaterial("STONECUTTER"), "Stone Mason"),
		NITWIT = create("nitwit", (player, pet, category)->{
			return value->setProfession(pet, Profession.NITWIT);
		}, Material.STONE, "Nitwit"),
		SHEPHERD = create("sherpherd", (player, pet, category)->{
			return value->setProfession(pet, Profession.SHEPHERD);
		}, Material.getMaterial("LOOM"), "Sherpherd"),
		TOOLSMITH = create("toolsmith", (player, pet, category)->{
			return value->setProfession(pet, Profession.TOOLSMITH);
		}, Material.getMaterial("SMITHING_TABLE"), "None"),
		WEAPONSMITH = create("weaponsmith", (player, pet, category)->{
			return value->setProfession(pet, Profession.WEAPONSMITH);
		}, Material.getMaterial("GRINDSTONE"), "Weaponsmith"),
		// Villager Level
		NOVICE = create("novice", (player, pet, category)->{
			return value->setVillagerLevel(pet, VillagerLevel.NOVICE);
		}, Material.STONE, "Novice"),
		APPRENTICE = create("apprentice", (player, pet, category)->{
			return value->setVillagerLevel(pet, VillagerLevel.APPRENTICE);
		}, Material.IRON_INGOT, "Apprentice"),
		JOURNEYMEN = create("journeymen", (player, pet, category)->{
			return value->setVillagerLevel(pet, VillagerLevel.JOURNEYMEN);
		}, Material.GOLD_INGOT, "Journeymen"),
		EXPERT = create("expert", (player, pet, category)->{
			return value->setVillagerLevel(pet, VillagerLevel.EXPERT);
		}, Material.EMERALD, "Expert"),
		MASTER = create("master", (player, pet, category)->{
			return value->setVillagerLevel(pet, VillagerLevel.MASTER);
		}, Material.DIAMOND, "Master"),
		// Rabbit Type
		// Brown, White, Black are handled above.
		BLACK_AND_WHITE = create("black_and_white", (player, pet, category)->{
			return value->setRabbitType(pet, Rabbit.Type.BLACK_AND_WHITE);
		}, Material.GRAY_WOOL, "Black and White"),
		SALT_AND_PEPPER = create("salt_and_pepper", (player, pet, category)->{
			return value->setRabbitType(pet, Rabbit.Type.SALT_AND_PEPPER);
		}, Material.YELLOW_WOOL, "Salt and Pepper"),
		KILLER_BUNNY = create("killer_bunny", (player, pet, category)->{
			return value->setRabbitType(pet, Rabbit.Type.THE_KILLER_BUNNY);
		}, Material.RED_WOOL, "Killer Bunny"),
		// Horse Colors(variant). Creamy, White, Brown, Black, Gray is above
		CHESTNUT = create("chestnut", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseColor(pet, Horse.Color.CHESTNUT);
			}
			return null;
		}, Material.GRAY_TERRACOTTA, "Chestnut"),
		DARK_BROWN = create("dark_brown", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseColor(pet, Horse.Color.DARK_BROWN);
			}
			return null;
		}, Material.BROWN_TERRACOTTA, "Dark Brown"),
		// Horse Marking
		NO_MARKING = create("no_marking", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseStyle(pet, Horse.Style.NONE);
			}
			return null;
		}, Material.LEAD, "No Marking"),
		WHITE_SOCKS = create("white_socks", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseStyle(pet, Horse.Style.WHITE);
			}
			return null;
		}, Material.WHITE_CARPET, "White Socks"),
		WHITE_FIELD = create("white_field", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseStyle(pet, Horse.Style.WHITEFIELD);
			}
			return null;
		}, Material.WHITE_WOOL, "White Field"),
		WHITE_DOTS = create("white_dots", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseStyle(pet, Horse.Style.WHITE_DOTS);
			}
			return null;
		}, Material.WHITE_STAINED_GLASS, "White Dots"),
		BLACK_DOTS = create("black_dots", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseStyle(pet, Horse.Style.BLACK_DOTS);
			}
			return null;
		}, Material.BLACK_WOOL, "Black Dots"),
		// Horse Armor
		NO_ARMOR = create("no_armor", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseArmor(pet, HorseArmor.None);
			}
			return null;
		}, Material.ARMOR_STAND, "No Armor"),// none?
		IRON_ARMOR = create("iron_armor", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseArmor(pet, HorseArmor.Iron);
			}
			return null;
		}, Material.IRON_HORSE_ARMOR, "Iron Armor"),
		GOLD_ARMOR = create("gold_armor", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseArmor(pet, HorseArmor.Gold);
			}
			return null;
		}, Material.GOLDEN_HORSE_ARMOR, "Gold Armor"),
		DIAMOND_ARMOR = create("diamond_armor", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseArmor(pet, HorseArmor.Diamond);
			}
			return null;
		}, Material.DIAMOND_HORSE_ARMOR, "Diamond Armor"),
		// Tropical Fish Patterns
		KOB = create("kob", (player, pet, category)->{
			return value->setTropicalFishPattern(pet, TropicalFish.Pattern.KOB);
		}, Material.WHITE_BANNER, "Kob"),
		SUNSTREAK = create("sunstreak", (player, pet, category)->{
			return value->setTropicalFishPattern(pet, TropicalFish.Pattern.SUNSTREAK);
		}, Material.ORANGE_BANNER, "Sunstreak"),
		SNOOPER = create("snooper", (player, pet, category)->{
			return value->setTropicalFishPattern(pet, TropicalFish.Pattern.SNOOPER);
		}, Material.MAGENTA_BANNER, "Snooper"),
		DASHER = create("dasher", (player, pet, category)->{
			return value->setTropicalFishPattern(pet, TropicalFish.Pattern.DASHER);
		}, Material.LIGHT_BLUE_BANNER, "Dasher"),
		BRINELY = create("brinely", (player, pet, category)->{
			return value->setTropicalFishPattern(pet, TropicalFish.Pattern.BRINELY);
		}, Material.YELLOW_BANNER, "Brinely"),
		SPOTTY = create("spotty", (player, pet, category)->{
			return value->setTropicalFishPattern(pet, TropicalFish.Pattern.SPOTTY);
		}, Material.LIME_BANNER, "Spotty"),
		// The ones below are just the above but with large as true.
		FLOPPER = create("flopper", (player, pet, category)->{
			return value->setTropicalFishPattern(pet, TropicalFish.Pattern.FLOPPER);
		}, Material.PINK_BANNER, "Flopper"),
		STRIPEY = create("stripey", (player, pet, category)->{
			return value->setTropicalFishPattern(pet, TropicalFish.Pattern.STRIPEY);
		}, Material.GRAY_BANNER, "Stripey"),
		GLITTER = create("glitter", (player, pet, category)->{
			return value->setTropicalFishPattern(pet, TropicalFish.Pattern.GLITTER);
		}, Material.LIGHT_GRAY_BANNER, "glitter"),
		BLOCKFISH = create("blockfish", (player, pet, category)->{
			return value->setTropicalFishPattern(pet, TropicalFish.Pattern.BLOCKFISH);
		}, Material.CYAN_BANNER, "Blockfish"),
		BETTY = create("betty", (player, pet, category)->{
			return value->setTropicalFishPattern(pet, TropicalFish.Pattern.BETTY);
		}, Material.PURPLE_BANNER, "Betty"),
		CLAYFISH = create("clayfish", (player, pet, category)->{
			return value->setTropicalFishPattern(pet, TropicalFish.Pattern.CLAYFISH);
		}, Material.BLUE_BANNER, "Clayfish"),
		// Cat Types. Red, White, Black use above options
		TABBY = create("tabby", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.CAT)){
				return value->setCatType(pet, CatType.Tabby);
			}
			return null;
		}, Material.COD, "Tabby"),
		TUXEDO = create("tuxedo", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.CAT)){
				return value->setCatType(pet, CatType.Black);
			}
			return null;
		}, Material.OBSIDIAN, "Tuxedo"),
		CAT_RED = create("cat_red", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.CAT)){
				return value->setCatType(pet, CatType.Red);
			}
			return null;
		}, Material.RED_WOOL, "Red"),
		SIAMESE = create("siamese", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.CAT)){
				return value->setCatType(pet, CatType.Siamese);
			}
			return null;
		}, Material.BROWN_WOOL, "Siamese"),
		BRITISH_SHORTHAIR = create("british_shorthair", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.CAT)){
				return value->setCatType(pet, CatType.BritishShortHair);
			}
			return null;
		}, Material.LIGHT_GRAY_WOOL, "British Shorthair"),
		CALICO = create("calico", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.CAT)){
				return value->setCatType(pet, CatType.Calico);
			}
			return null;
		}, Material.ORANGE_WOOL, "Calico"),
		PERSIAN = create("persian", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.CAT)){
				return value->setCatType(pet, CatType.Persian);
			}
			return null;
		}, Material.YELLOW_WOOL, "Persian"),
		RAGDOLL = create("ragdoll", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.CAT)){
				return value->setCatType(pet, CatType.Ragdoll);
			}
			return null;
		}, Material.WHITE_CARPET, "Ragdoll"),
		CAT_WHITE = create("cat_white", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.CAT)){
				return value->setCatType(pet, CatType.White);
			}
			return null;
		}, Material.WHITE_WOOL, "White"),
		JELLIE = create("jellie", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.CAT)){
				return value->setCatType(pet, CatType.Jellie);
			}
			return null;
		}, Material.GRAY_WOOL, "Jellie"),
		CAT_BLACK = create("cat_black", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.CAT)){
				return value->setCatType(pet, CatType.Black);
			}
			return null;
		}, Material.BLACK_WOOL, "Black"),
		// Fox Type, Red is just normal red wool.
		SNOW = create("snow", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.FOX)){
				return value->setFoxType(pet, FoxType.Snow);
			}
			return null;
		}, Material.SNOW_BLOCK, "Snow"),
		CREAMY = create("creamy", (player, pet, category)->{
			if(pet.getPetType().equals(PetType.HORSE)){
				return value->setHorseColor(pet, Horse.Color.CREAMY);
			}else if(pet.getPetType().equals(PetType.LLAMA) || pet.getPetType().equals(PetType.TRADERLLAMA)){
				return value->setLlamaColor(pet, Llama.Color.CREAMY);
			}
			return null;
		}, Material.YELLOW_WOOL, "Creamy"),
		// Axolotl Variants that aren't colors. Gold, Cyan, Blue are in the color section
		LUCY = create("lucy", (player, pet, category)->{
			if(pet instanceof IAxolotlPet axolotl){
				return value->axolotl.setVariant(IAxolotlPet.Variant.Lucy);
			}
			return null;
		}, Material.PINK_WOOL, "Lucy"),
		WILD = create("wild", (player, pet, category)->{
			if(pet instanceof IAxolotlPet axolotl){
				return value->axolotl.setVariant(IAxolotlPet.Variant.Wild);
			}
			return null;
		}, Material.BROWN_WOOL, "Wild");
	
	public static final PetData<Double>
		HEALTH = PetData.create("health", (player, pet, category)->value->{
			if(value < 0.1){
				Lang.sendTo(player, Lang.INVALID_PET_DATA_VALUE.toString().replace("%data%", "Health").replace("%value%", String.valueOf(value)));
				return;
			}
			pet.getCraftPet().setHealth(value);
		}, PetDataParser.doubleParser, (PetDataMaterial) null, "Health Pet");
	//@formatter:on
	
	public static PetData<Boolean> create(String configKeyName, @Nonnull PetDataAction<Boolean> action, Material material, String name, String... loreArray){
		return new PetData<>(configKeyName, action, PetDataParser.booleanParser, (pet)->material, new Version(), VersionCheckType.COMPATIBLE, name, loreArray);
	}
	
	public static PetData<Boolean> create(String configKeyName, @Nonnull PetDataAction<Boolean> action, PetDataMaterial material, String name, String... loreArray){
		return new PetData<>(configKeyName, action, PetDataParser.booleanParser, material, new Version(), VersionCheckType.COMPATIBLE, name, loreArray);
	}
	
	public static <V> PetData<V> create(String configKeyName, @Nonnull PetDataAction<V> action, @Nonnull PetDataParser<V> parser, Material material, String name, String... loreArray){
		return new PetData<>(configKeyName, action, parser, (pet)->material, new Version(), VersionCheckType.COMPATIBLE, name, loreArray);
	}
	
	public static <V> PetData<V> create(String configKeyName, @Nonnull PetDataAction<V> action, @Nonnull PetDataParser<V> parser, PetDataMaterial material, String name, String... loreArray){
		return new PetData<>(configKeyName, action, parser, material, new Version(), VersionCheckType.COMPATIBLE, name, loreArray);
	}
	
	public static <V> PetData<V> create(String configKeyName, @Nonnull PetDataAction<V> action, @Nonnull PetDataParser<V> parser, PetDataMaterial material, Version version, VersionCheckType versionCheckType, String name, String... loreArray){
		return new PetData<>(configKeyName, action, parser, material, version, versionCheckType, name, loreArray);
	}
	
	@SuppressWarnings("unchecked")
	public static @Nullable <T> PetData<T> get(String name){
		for(PetData<?> data : values){
			if(data.getConfigKeyName().equalsIgnoreCase(name)){
				return (PetData<T>) data;
			}
		}
		return null;
	}
	
	private final String configKeyName;
	private final Version version;
	private final VersionCheckType versionCheckType;
	private final @Nonnull PetDataAction<T> action;
	private final PetDataMaterial material;
	private final String name;
	private final List<String> lore;
	private final @Nonnull PetDataParser<T> parser;
	
	PetData(String configKeyName, @Nonnull PetDataAction<T> action, @Nonnull PetDataParser<T> parser, PetDataMaterial material, Version version, VersionCheckType versionCheckType, String name, String... loreArray){
		this.configKeyName = configKeyName.toLowerCase();// just incase
		this.action = action;
		this.version = version;
		this.versionCheckType = versionCheckType;
		this.material = material;
		this.name = name;
		this.lore = new ArrayList<>();
		for(String s : loreArray){
			lore.add(ChatColor.GOLD + s);
		}
		this.parser = parser;
		values.add(this);
	}
	
	@SuppressWarnings("unchecked")
	public boolean attemptInteract(Player player, IPet pet, PetDataCategory category, ItemStack clicked){
		ItemStack item = toItem(pet);
		if(item == null) return false;
		if(ItemUtil.matches(clicked, item)){
			T current = (T) pet.getData().get(this);
			Consumer<T> setter = getAction().get(player, pet, category);
			if(setter == null) return true;
			T result = getParser().interact(current, clicked);
			setter.accept(result);
			EchoPet.getManager().setData(pet, this, result);
			// inv.setItem(slot, toItem(pet));
			return true;
		}
		return false;
	}
	
	public String getConfigKeyName(){
		return this.configKeyName;
	}
	
	@Nonnull
	public PetDataAction<T> getAction(){
		return action;
	}
	
	public String getItemName(){
		return name;
	}
	
	public boolean ignoreSaving(){
		return this == PetData.RIDE || this == PetData.HAT;
	}
	
	public @Nullable PetDataMaterial getMaterial(){
		return material;
	}
	
	public @Nullable ItemStack toItem(IPet pet){
		if(material == null) return null;
		ItemStack item = new ItemStack(material.get(pet));
		ItemMeta meta = item.getItemMeta();
		if(meta == null) return null;
		meta.setDisplayName(ChatColor.RED + name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public boolean isCompatible(){
		switch(versionCheckType){
			case IDENTICAL -> {// ==
				return version.isIdentical(new Version());
			}
			case SUPPORTED -> {// <=
				return version.isSupported(new Version());
			}
			case COMPATIBLE -> {// >=
				return version.isCompatible(new Version());
			}
		}
		return true;
	}
	
	@Nonnull
	public PetDataParser<T> getParser(){
		return parser;
	}
	
	// All the below methods are technically wrong.
	
	private static boolean setColorByDye(IPet pet, PetDataCategory category, DyeColor color){
		IPetType type = pet.getPetType();
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
		IPetType type = pet.getPetType();
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
		IPetType type = pet.getPetType();
		if(type.equals(PetType.PUFFERFISH)){
			((IPufferFishPet) pet).setPuffState(state);
			return true;
		}
		return false;
	}
	
	private static boolean setVillagerType(IPet pet, VillagerType villagerType){
		IPetType type = pet.getPetType();
		if(type.equals(PetType.VILLAGER) || type.equals(PetType.ZOMBIEVILLAGER)){
			((IVillagerDataHolder) pet).setType(villagerType);
		}
		return true;
	}
	
	private static boolean setVillagerLevel(IPet pet, VillagerLevel villagerLevel){
		IPetType type = pet.getPetType();
		if(type.equals(PetType.VILLAGER) || type.equals(PetType.ZOMBIEVILLAGER)){
			((IVillagerDataHolder) pet).setLevel(villagerLevel);
		}
		return true;
	}
	
	private static boolean setProfession(IPet pet, Profession profession){
		IPetType type = pet.getPetType();
		if(type.equals(PetType.VILLAGER) || type.equals(PetType.ZOMBIEVILLAGER)){
			((IVillagerDataHolder) pet).setProfession(profession);
		}
		return true;
	}
	
	private static boolean setRabbitType(IPet pet, Rabbit.Type rabbitType){
		IPetType type = pet.getPetType();
		if(type.equals(PetType.RABBIT)){
			((IRabbitPet) pet).setRabbitType(rabbitType);
		}
		return true;
	}
	
	private static boolean setHorseColor(IPet pet, Horse.Color color){
		IPetType type = pet.getPetType();
		if(type.equals(PetType.HORSE)){
			((IHorsePet) pet).setColor(color);
		}
		return true;
	}
	
	private static boolean setHorseStyle(IPet pet, Horse.Style style){
		IPetType type = pet.getPetType();
		if(type.equals(PetType.HORSE)){
			((IHorsePet) pet).setStyle(style);
		}
		return true;
	}
	
	private static boolean setHorseArmor(IPet pet, HorseArmor armor){
		IPetType type = pet.getPetType();
		if(type.equals(PetType.HORSE)){
			((IHorsePet) pet).setArmour(armor);
		}
		return true;
	}
	
	private static boolean setLlamaColor(IPet pet, Llama.Color color){
		IPetType type = pet.getPetType();
		if(type.equals(PetType.LLAMA) || pet.getPetType().equals(PetType.TRADERLLAMA)){
			((ILlamaPet) pet).setSkinColor(color);
		}
		return true;
	}
	
	private static boolean setTropicalFishPattern(IPet pet, TropicalFish.Pattern pattern){
		IPetType type = pet.getPetType();
		if(type.equals(PetType.TROPICALFISH)){
			((ITropicalFishPet) pet).setPattern(pattern);
			((ITropicalFishPet) pet).setLarge(pattern.ordinal() > 5);
		}
		return true;
	}
	
	private static Consumer<Boolean> setTropicalFishLarge(IPet pet){
		if(pet instanceof ITropicalFishPet tropicalFishPet){
			return tropicalFishPet::setLarge;
		}
		return null;
	}
	
	private static boolean setCatType(IPet pet, CatType catType){
		IPetType type = pet.getPetType();
		if(type.equals(PetType.CAT)){
			((ICatPet) pet).setType(catType);
		}
		return true;
	}
	
	private static boolean setFoxType(IPet pet, FoxType foxType){
		IPetType type = pet.getPetType();
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
		IPetType type = pet.getPetType();
		if(type.equals(PetType.MUSHROOMCOW)){
			((IMushroomCowPet) pet).setType(cowType);
		}
		return true;
	}
	
	@Override
	public String toString(){
		return getConfigKeyName();
	}
}