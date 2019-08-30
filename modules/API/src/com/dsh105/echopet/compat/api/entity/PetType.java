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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorseChestedAbstractPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.dsh105.echopet.compat.api.util.Version;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;

public enum PetType{
	
	BAT("Bat", "Bat Pet", "bat"),
	BLAZE("Blaze", "Blaze Pet", "blaze", PetData.FIRE),
	CAT("Cat", "Cat Pet", "cat", new Version("1.14-R1"), new PetDataCategory[]{PetDataCategory.COLLAR_COLOR, PetDataCategory.CAT_TYPE}, PetData.TAMED),
	CAVESPIDER("CaveSpider", "Cave Spider Pet", "cave_spider"),
	CHICKEN("Chicken", "Chicken Pet", "chicken"),
	COD("Cod", "Cod Pet", "cod"),
	COW("Cow", "Cow Pet", "cow"),
	CREEPER("Creeper", "Creeper Pet", "creeper", PetData.POWERED),
	DOLPHIN("Dolphin", "Dolphin Pet", "dolphin"),
	DONKEY("Donkey", "Donkey Pet", "donkey", PetData.SADDLE),
	DROWNED("Drowned", "Drowned Pet", "drowned"),
	ELDERGUARDIAN("ElderGuardian", "Elder Guardian Pet", "elder_guardian"),
	// ENDERDRAGON("EnderDragon", "EnderDragon Pet", "ender_dragon", "EnderDragon"),
	ENDERMAN("Enderman", "Enderman Pet", "enderman", PetData.SCREAMING),
	ENDERMITE("Endermite", "Endermite Pet", "endermite"),
	EVOKER("Evoker", "Evoker Pet", "evoker"),
	FOX("Fox", "Fox Pet", "fox", new PetDataCategory[]{PetDataCategory.FOX_TYPE}, PetData.SIT, PetData.CROUCH, PetData.HEAD_TILT, PetData.POUNCE, PetData.SLEEP, PetData.LEG_SHAKE),
	GHAST("Ghast", "Ghast Pet", "ghast"),
	GIANT("Giant", "Giant Pet", "giant"),
	GUARDIAN("Guardian", "Guardian Pet", "guardian"),
	HORSE("Horse", "Horse Pet", "horse", new PetDataCategory[]{PetDataCategory.HORSE_COLOR, PetDataCategory.HORSE_MARKING}, PetData.SADDLE),
	HUMAN("Human", "Human Pet", "UNKNOWN"),
	HUSK("Husk", "Husk Pet", "husk"),
	ILLUSIONER("Illusioner", "Illusioner Pet", "illusioner"),
	IRONGOLEM("IronGolem", "Iron Golem Pet", "iron_golem"),
	LLAMA("Llama", "Llama Pet", "llama", new PetDataCategory[]{PetDataCategory.LLAMA_COLOR, PetDataCategory.LLAMA_CARPET_COLOR}),
	MAGMACUBE("MagmaCube", "Magma Cube Pet", "magma_cube", new PetDataCategory[]{PetDataCategory.SLIME_SIZE}),
	MULE("Mule", "Mule Pet", "mule", PetData.SADDLE),
	MUSHROOMCOW("MushroomCow", "Mushroom Cow Pet", "mooshroom"),
	OCELOT("Ocelot", "Ocelot Pet", "ocelot"),
	PANDA("Panda", "Panda Pet", "panda", new PetDataCategory[]{PetDataCategory.PANDA_MAIN_GENE, PetDataCategory.PANDA_HIDDEN_GENE}, PetData.ROLL, PetData.SIT, PetData.LAY_DOWN),
	PARROT("Parrot", "Parrot Pet", "parrot", new PetDataCategory[]{PetDataCategory.PARROT_VARIANT}/*, PetData.LEFT_SHOULDER, PetData.RIGHT_SHOULDER*/),
	PHANTOM("Phantom", "Phantom Pet", "phantom"),
	PIG("Pig", "Pig Pet", "pig", PetData.SADDLE),
	PIGZOMBIE("PigZombie", "Pig Zombie Pet", "zombie_pigman"),
	PILLAGER("Pillager", "Pillager Pet", "pillager"),
	POLARBEAR("PolarBear", "Polar Bear Pet", "polar_bear", PetData.STANDING_UP),
	PUFFERFISH("PufferFish", "PufferFish Pet", "pufferfish", new PetDataCategory[]{PetDataCategory.PUFFERFISH_SIZE}),
	RABBIT("Rabbit", "Rabbit Pet", "rabbit", new PetDataCategory[]{PetDataCategory.RABBIT_TYPE}),
	RAVAGER("Ravager", "Ravager Pet", "ravager"),
	SALMON("Salmon", "Salmon Pet", "salmon"),
	SHEEP("Sheep", "Sheep Pet", "sheep", new PetDataCategory[]{PetDataCategory.WOOL_COLOR}, PetData.SHEARED),
	SHULKER("Shulker", "Shulker Pet", "shulker"),
	SILVERFISH("Silverfish", "Silverfish Pet", "silverfish"),
	SKELETON("Skeleton", "Skeleton Pet", "skeleton"),
	SKELETONHORSE("SkeletonHorse", "Skeleton Horse Pet", "skeleton_horse", PetData.SADDLE),
	SLIME("Slime", "Slime Pet", "slime", new PetDataCategory[]{PetDataCategory.SLIME_SIZE}),
	SNOWMAN("Snowman", "Snowman Pet", "snow_golem", PetData.SHEARED),
	SPIDER("Spider", "Spider Pet", "spider"),
	SQUID("Squid", "Squid Pet", "squid"),
	STRAY("Stray", "Stray Pet", "stray"),
	TRADERLLAMA("TraderLlama", "Trader Llama Pet", "trader_llama", new PetDataCategory[]{PetDataCategory.LLAMA_COLOR, PetDataCategory.LLAMA_CARPET_COLOR}),
	TROPICALFISH("TropicalFish", "Tropical Fish Pet", "tropical_fish", new PetDataCategory[]{PetDataCategory.TROPICAL_FISH_PATTERN, PetDataCategory.TROPICAL_FISH_COLOR, PetDataCategory.TROPICAL_FISH_PATTERN_COLOR}, PetData.SIZE_LARGE),
	TURTLE("Turtle", "Turtle Pet", "turtle"),
	VEX("Vex", "Vex Pet", "vex", PetData.POWERED),
	VILLAGER("Villager", "Villager Pet", "villager", new PetDataCategory[]{PetDataCategory.VILLAGER_TYPE, PetDataCategory.VILLAGER_PROFESSION, PetDataCategory.VILLAGER_LEVEL}),
	VINDICATOR("Vindicator", "Vindicator Pet", "vindicator"),
	WITCH("Witch", "Witch Pet", "witch"),
	WITHER("Wither", "Wither Pet", "wither", PetData.SHIELD),
	WITHERSKELETON("WitherSkeleton", "Wither Skeleton Pet", "wither_skeleton"),
	WOLF("Wolf", "Wolf Pet", "wolf", new PetDataCategory[]{PetDataCategory.COLLAR_COLOR}, PetData.TAMED, PetData.ANGRY),
	ZOMBIE("Zombie", "Zombie Pet", "zombie"),
	ZOMBIEHORSE("ZombieHorse", "Zombie Horse Pet", "zombie_horse", PetData.SADDLE),
	ZOMBIEVILLAGER("ZombieVillager", "Zombie Villager Pet", "zombie_villager", new PetDataCategory[]{PetDataCategory.VILLAGER_TYPE, PetDataCategory.VILLAGER_PROFESSION, PetDataCategory.VILLAGER_LEVEL}),
	;
	
	private String classIdentifier;
	private Class<? extends IEntityPet> entityClass;
	private Class<? extends IPet> petClass;
	private String defaultName;
	private String minecraftEntityName;
	private List<PetDataCategory> allowedCategories = new ArrayList<>();
	private List<PetData> allowedData = new ArrayList<>();
	private Version version;
	
	PetType(String classIdentifier, String defaultName, String minecraftEntityName, PetData... allowedData){
		this(classIdentifier, defaultName, minecraftEntityName, new Version(), null, allowedData);
	}
	
	PetType(String classIdentifier, String defaultName, String minecraftEntityName, PetDataCategory[] categories, PetData... allowedData){
		this(classIdentifier, defaultName, minecraftEntityName, new Version(), categories, allowedData);
	}
	
	PetType(String classIdentifier, String defaultName, String minecraftEntityName, Version version, PetData... allowedData){
		this(classIdentifier, defaultName, minecraftEntityName, version, null, allowedData);
	}
	
	@SuppressWarnings({"unchecked"})
	PetType(String classIdentifier, String defaultName, String minecraftEntityName, Version version, PetDataCategory[] categories, PetData... allowedData){
		this.classIdentifier = classIdentifier;
		try{
			this.entityClass = (Class<? extends IEntityPet>) Class.forName(ReflectionUtil.COMPAT_NMS_PATH + ".entity.type.Entity" + classIdentifier + "Pet");
			this.petClass = ReflectionUtil.getClass("com.dsh105.echopet.api.pet.type." + classIdentifier + "Pet");
		}catch(ClassNotFoundException ignored){
		}
		this.minecraftEntityName = minecraftEntityName;
		this.defaultName = defaultName;
		this.version = version;
		//
		this.allowedData.add(PetData.HAT);
		this.allowedData.add(PetData.RIDE);
		Class<?> clazz = ReflectionUtil.getClass("com.dsh105.echopet.compat.api.entity.type.pet.I" + classIdentifier + "Pet");
		if(clazz != null){
			if(IAgeablePet.class.isAssignableFrom(clazz)){
				this.allowedData.add(PetData.BABY);
			}else if(IEntityHorseChestedAbstractPet.class.isAssignableFrom(clazz)){
				this.allowedData.add(PetData.CHESTED);
			}
		}
		if(allowedData != null){
			this.allowedData.addAll(ImmutableList.copyOf(allowedData));
		}
		if(categories != null){
			this.allowedCategories.addAll(ImmutableList.copyOf(categories));
		}
	}
	
	public String getClassIdentifier(){
		return classIdentifier;
	}
	
	public String getDefaultName(String name){
		return EchoPet.getConfig().getString("pets." + this.toString().toLowerCase().replace("_", "") + ".defaultName", this.defaultName).replace("(user)", name).replace("(userApos)", name + "'s");
	}
	
	public String getDefaultName(){
		return this.defaultName;
	}
	
	public String getMinecraftName(){
		return minecraftEntityName;
	}
	
	public List<PetDataCategory> getAllowedCategories(){
		return allowedCategories;
	}
	
	public List<PetData> getAllowedDataTypes(){
		return this.allowedData;
	}
	
	public boolean isDataAllowed(PetData data){
		for(PetDataCategory category : allowedCategories){
			for(PetData d : category.getData()){
				if(d.equals(data)) return true;
			}
		}
		return getAllowedDataTypes().contains(data);
	}
	
	public IEntityPet getNewEntityPetInstance(Object world, IPet pet){
		return EchoPet.getPetRegistry().getRegistrationEntry(pet.getPetType()).createEntityPet(world, pet);
	}
	
	public IPet getNewPetInstance(Player owner){
		if(owner != null){
			return EchoPet.getPetRegistry().spawn(this, owner);
		}
		return null;
	}
	
	public Class<? extends IEntityPet> getEntityClass(){
		return this.entityClass;
	}
	
	public Class<? extends IPet> getPetClass(){
		return this.petClass;
	}
	
	public Version getVersion(){
		return version;
	}
	
	public boolean isCompatible(){
		return version.isCompatible(new Version());
	}
	
	private static void outputInfo(){
		String[] petTypes = new String[PetType.values().length];
		int pos = 0;
		for(PetType type : PetType.values()){
			petTypes[pos++] = type.name().toLowerCase();
		}
		Arrays.sort(petTypes);
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File("perms.yml")))){
			for(String petTypeName : petTypes){
				bw.write("    echopet.pet.type." + petTypeName + ":\n");
				bw.write("        default: op\n");
			}
			for(String petTypeName : petTypes){
				bw.write("    echopet.pet.default.set.type." + petTypeName + ":\n");
				bw.write("        default: op\n");
			}
			for(String petTypeName : petTypes){
				PetType petType = PetType.valueOf(petTypeName.toUpperCase());
				for(PetData data : petType.getAllowedDataTypes()){
					bw.write("    echopet.pet.type." + petTypeName + "." + data.getConfigOptionString() + ":\n");
					bw.write("        default: op\n");
				}
				Set<String> alreadyHad = new HashSet<>();
				for(PetDataCategory category : petType.getAllowedCategories()){
					for(PetData data : category.getData()){
						if(alreadyHad.contains(data.getConfigOptionString())){
							System.out.println(petType + " has dupe perm string " + data.getConfigOptionString() + " one from data " + data + " and category " + category);
							continue;
						}
						alreadyHad.add(data.getConfigOptionString());
						bw.write("    echopet.pet.type." + petTypeName + "." + data.getConfigOptionString() + ":\n");
						bw.write("        default: op\n");
					}
				}
			}
			for(String petTypeName : petTypes){
				bw.write("    echopet.petadmin.type." + petTypeName + ":\n");
				bw.write("        default: op\n");
			}
			for(String petTypeName : petTypes){
				bw.write("    echopet.petadmin.hat." + petTypeName + ":\n");
				bw.write("        default: op\n");
			}
			for(String petTypeName : petTypes){
				bw.write("    echopet.petadmin.ride." + petTypeName + ":\n");
				bw.write("        default: op\n");
			}
			for(String petTypeName : petTypes){
				bw.write("    echopet.petadmin.default.set.type." + petTypeName + ":\n");
				bw.write("        default: op\n");
			}
			bw.write("    echopet.*:\n");
			bw.write("        default: op\n");
			bw.write("        description: 'All EchoPet commands'\n");
			bw.write("        children:\n");
			bw.write("            echopet.pet.*: true\n");
			bw.write("            echopet.petadmin.*: true\n");
			//
			bw.write("    echopet.pet.*:\n");
			bw.write("        default: op\n");
			bw.write("        description: 'All permissions under the /pet command'\n");
			bw.write("        children:\n");
			bw.write("            echopet.pet: true\n            echopet.pet.remove: true\n            echopet.pet.list: true\n            echopet.pet.info: true\n            echopet.pet.menu: true\n            echopet.pet.show: true\n            echopet.pet.hide: true\n            echopet.pet.toggle: true\n            echopet.pet.call: true\n            echopet.pet.name: true\n            echopet.pet.name.override: true\n            echopet.pet.select: true\n            echopet.pet.selector: true\n            echopet.pet.type.*: true\n            echopet.pet.data.*: true\n            echopet.pet.default.*: true\n");
			//
			bw.write("    echopet.pet.type.*:\n");
			bw.write("        default: op\n");
			bw.write("        description: 'All pet type permissions'\n");
			bw.write("        children:\n");
			for(String petTypeName : petTypes){
				bw.write("            echopet.pet.type." + petTypeName + ": true\n");
			}
			//
			bw.write("    echopet.pet.hat.*:\n");
			bw.write("        default: op\n");
			bw.write("        description: 'All hat permissions'\n");
			bw.write("        children:\n");
			for(String petTypeName : petTypes){
				bw.write("            echopet.pet.type." + petTypeName + ".hat: true\n");
			}
			//
			bw.write("    echopet.pet.ride.*:\n");
			bw.write("        default: op\n");
			bw.write("        description: 'All ride permissions'\n");
			bw.write("        children:\n");
			for(String petTypeName : petTypes){
				bw.write("            echopet.pet.type." + petTypeName + ".ride: true\n");
			}
			//
			bw.write("    echopet.pet.default.*:\n        default: op\n        description: 'All permissions under /pet default <...>'\n        children:\n            echopet.pet.default.set.current: true\n            echopet.pet.default.remove: true\n            echopet.pet.default.set.type.*: true\n");
			//
			bw.write("    echopet.pet.default.set.type.*:\n");
			bw.write("        default: op\n");
			bw.write("        description: 'All pet types for /pet default set <...>'\n");
			bw.write("        children:\n");
			for(String petTypeName : petTypes){
				bw.write("            echopet.pet.default.set.type." + petTypeName + ": true\n");
			}
			//
			bw.write("    echopet.petadmin.*:\n        default: op\n        description: 'All permissions under the /petadmin command'\n        children:\n            echopet.petadmin: true\n            echopet.petadmin.reload: true\n            echopet.petadmin.remove: true\n            echopet.petadmin.list: true\n            echopet.petadmin.info: true\n            echopet.petadmin.menu: true\n            echopet.petadmin.show: true\n            echopet.petadmin.hide: true\n            echopet.petadmin.call: true\n            echopet.petadmin.name: true\n            echopet.petadmin.select: true\n            echopet.petadmin.selector: true\n            echopet.petadmin.type.*: true\n            echopet.pet.data.*: true\n            echopet.petadmin.ride.*: true\n            echopet.petadmin.hat.*: true\n            echopet.petadmin.default.*: true\n");
			//
			bw.write("    echopet.petadmin.type.*:\n");
			bw.write("        default: op\n");
			bw.write("        description: 'All pet type permissions for PetAdmin commands'\n");
			bw.write("        children:\n");
			for(String petTypeName : petTypes){
				bw.write("            echopet.petadmin.type." + petTypeName + ": true\n");
			}
			//
			bw.write("    echopet.petadmin.hat.*:\n");
			bw.write("        default: op\n");
			bw.write("        description: 'All pet hat permissions for PetAdmin commands'\n");
			bw.write("        children:\n");
			for(String petTypeName : petTypes){
				bw.write("            echopet.petadmin.hat." + petTypeName + ": true\n");
			}
			//
			bw.write("    echopet.petadmin.ride.*:\n");
			bw.write("        default: op\n");
			bw.write("        description: 'All pet ride permissions for PetAdmin commands'\n");
			bw.write("        children:\n");
			for(String petTypeName : petTypes){
				bw.write("            echopet.petadmin.ride." + petTypeName + ": true\n");
			}
			//
			bw.write("    echopet.petadmin.default.*:\n        default: op\n        description: 'All permissions under /petadmin default <...>'\n        children:\n            echopet.petadmin.default.set.current: true\n            echopet.petadmin.default.remove: true\n            echopet.petadmin.default.set.type.*: true\n");
			//
			bw.write("    echopet.petadmin.default.set.type.*:\n");
			bw.write("        default: op\n");
			bw.write("        description: 'All pet types for /petadmin default set <...>'\n");
			bw.write("        children:\n");
			for(String petTypeName : petTypes){
				bw.write("            echopet.petadmin.default.set.type." + petTypeName + ": true\n");
			}
			//
			for(String petTypeName : petTypes){
				bw.write("    echopet.pet.type." + petTypeName + ".*:\n");
				bw.write("        default: op\n");
				bw.write("        description: 'All " + petTypeName + " pet data permissions'\n");
				bw.write("        children:\n");
				PetType petType = PetType.valueOf(petTypeName.toUpperCase());
				for(PetData data : petType.getAllowedDataTypes()){
					bw.write("            echopet.pet.type." + petTypeName + "." + data.getConfigOptionString() + ": true\n");
				}
				Set<String> alreadyHad = new HashSet<>();
				for(PetDataCategory category : petType.getAllowedCategories()){
					for(PetData data : category.getData()){
						if(alreadyHad.contains(data.getConfigOptionString())){
							//System.out.println(petType + " has dupe perm string " + data.getConfigOptionString() + " one from data " + data + " and category " + category);
							continue;
						}
						alreadyHad.add(data.getConfigOptionString());
						bw.write("            echopet.pet.type." + petTypeName + "." + data.getConfigOptionString() + ": true\n");
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		outputInfo();
	}
}