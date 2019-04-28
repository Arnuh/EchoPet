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

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.dsh105.echopet.compat.api.util.Version;
import com.google.common.collect.ImmutableList;

public enum PetType {

    // Aggressive mobs
	BLAZE("Blaze", "Blaze Pet", "blaze", PetData.FIRE),
	CAVESPIDER("CaveSpider", "Cave Spider Pet", "cave_spider"),
	CREEPER("Creeper", "Creeper Pet", "creeper", PetData.POWERED),
	DROWNED("Drowned", "Drowned Pet", "drowned"),
    // ENDERDRAGON("EnderDragon", "EnderDragon Pet", "ender_dragon", "EnderDragon"),
	ENDERMAN("Enderman", "Enderman Pet", "enderman", PetData.SCREAMING),
	ENDERMITE("Endermite", "Endermite Pet", "endermite"),
	GHAST("Ghast", "Ghast Pet", "ghast"),
	GIANT("Giant", "Giant Pet", "giant"),
	GUARDIAN("Guardian", "Guardian Pet", "guardian"),
	ELDERGUARDIAN("ElderGuardian", "Elder Guardian Pet", "elder_guardian"),
	EVOKER("Evoker", "Evoker Pet", "evoker"),
	MAGMACUBE("MagmaCube", "Magma Cube Pet", "magma_cube", new PetDataCategory[]{PetDataCategory.SLIME_SIZE}),
	PHANTOM("Phantom", "Phantom Pet", "phantom"),
	PIGZOMBIE("PigZombie", "Pig Zombie Pet", "zombie_pigman"),
	SHULKER("Shulker", "Shulker Pet", "shulker"/*, PetData.OPEN, PetData.BLACK, PetData.BLUE, PetData.BROWN,
	                                           PetData.CYAN, PetData.GRAY, PetData.GREEN,
	                                           PetData.LIGHT_BLUE, PetData.LIME, PetData.MAGENTA,
	                                           PetData.ORANGE, PetData.PINK, PetData.PURPLE, PetData.RED,
	                                           PetData.WHITE, PetData.YELLOW, PetData.SILVER*/),
	SILVERFISH("Silverfish", "Silverfish Pet", "silverfish"),
	SKELETON("Skeleton", "Skeleton Pet", "skeleton"),
	WITHERSKELETON("WitherSkeleton", "Wither Skeleton Pet", "wither_skeleton"),
	STRAY("Stray", "Stray Pet", "stray"),
	SLIME("Slime", "Slime Pet", "slime", new PetDataCategory[]{PetDataCategory.SLIME_SIZE}),
	SPIDER("Spider", "Spider Pet", "spider"),
	WITCH("Witch", "Witch Pet", "witch"),
	WITHER("Wither", "Wither Pet", "wither", PetData.SHIELD),
	ZOMBIE("Zombie", "Zombie Pet", "zombie"/*, PetData.BLACKSMITH, PetData.BUTCHER, PetData.FARMER, PetData.LIBRARIAN, PetData.PRIEST, PetData.HUSK*/),
	HUSK("Husk", "Husk Pet", "husk"),
	ILLUSIONER("Illusioner", "Illusioner Pet", "illusioner"),
	ZOMBIEVILLAGER("ZombieVillager", "Zombie Villager Pet", "zombie_villager"/*, PetData.BLACKSMITH, PetData.BUTCHER, PetData.FARMER, PetData.LIBRARIAN, PetData.PRIEST*/),
	VEX("Vex", "Vex Pet", "vex", PetData.POWERED),
	VINDICATOR("Vindicator", "Vindicator Pet", "vindicator"),
    // Passive mobs
	BAT("Bat", "Bat Pet", "bat"),
	CAT("Cat", "Cat Pet", "cat", new Version("1.14-R1")),
	CHICKEN("Chicken", "Chicken Pet", "chicken"),
	COD("Cod", "Cod Pet", "cod"),
	COW("Cow", "Cow Pet", "cow"),
	DOLPHIN("Dolphin", "Dolphin Pet", "dolphin"),
	HORSE("Horse", "Horse Pet", "horse", PetData.SADDLE/*, PetData.CHESTED,
	                                                   PetData.HORSE, PetData.DONKEY, PetData.MULE, PetData.SKELETON_HORSE, PetData.UNDEAD_HORSE, PetData.WHITE,
	                                                   PetData.CREAMY, PetData.CHESTNUT, PetData.BROWN, PetData.BLACK, PetData.WHITE_SOCKS,
	                                                   PetData.GRAY, PetData.DARK_BROWN, PetData.NONE, PetData.WHITEFIELD, PetData.WHITE_DOTS, PetData.BLACK_DOTS,
	                                                   PetData.NOARMOUR, PetData.IRON, PetData.GOLD, PetData.DIAMOND*/),
	SKELETONHORSE("SkeletonHorse", "Skeleton Horse Pet", "skeleton_horse", PetData.SADDLE/*, HORSE.getAllowedDataArray()*/),
	ZOMBIEHORSE("ZombieHorse", "Zombie Horse Pet", "zombie_horse", PetData.SADDLE/*, HORSE.getAllowedDataArray()*/),
	DONKEY("Donkey", "Donkey Pet", "donkey", PetData.SADDLE/*, HORSE.getAllowedDataArray()*/),
	MULE("Mule", "Mule Pet", "mule", PetData.SADDLE/*, HORSE.getAllowedDataArray()*/),
	IRONGOLEM("IronGolem", "Iron Golem Pet", "iron_golem"),
	LLAMA("Llama", "Llama Pet", "llama"/*, PetData.CHESTED,
	                                   PetData.BLACK, PetData.BLUE, PetData.BROWN,
	                                   PetData.CYAN, PetData.GRAY, PetData.GREEN,
	                                   PetData.LIGHT_BLUE, PetData.LIME, PetData.MAGENTA,
	                                   PetData.ORANGE, PetData.PINK, PetData.PURPLE, PetData.RED,
	                                   PetData.WHITE, PetData.YELLOW, PetData.SILVER,
	                                   PetData.CREAMY, PetData.BROWN_LLAMA, PetData.GRAY_LLAMA, PetData.WHITE_LLAMA*/),
	MUSHROOMCOW("MushroomCow", "Mushroom Cow Pet", "mooshroom"),
	OCELOT("Ocelot", "Ocelot Pet", "ocelot"/*, PetData.BLACK, PetData.RED, PetData.SIAMESE, PetData.WILD*/),
	PARROT("Parrot", "Parrot Pet", "parrot"/*, PetData.GRAY, PetData.GREEN, PetData.CYAN, PetData.BLUE, PetData.RED, PetData.LEFT_SHOULDER, PetData.RIGHT_SHOULDER*/),
	PIG("Pig", "Pig Pet", "pig", PetData.SADDLE),
	POLARBEAR("PolarBear", "Polar Bear Pet", "polar_bear", PetData.STANDING_UP),
	PUFFERFISH("PufferFish", "PufferFish Pet", "pufferfish"),
	RABBIT("Rabbit", "Rabbit Pet", "rabbit"/*, PetData.BROWN, PetData.WHITE, PetData.BLACK, PetData.BLACK_AND_WHITE, PetData.GOLD, PetData.SALT_AND_PEPPER, PetData.THE_KILLER_BUNNY*/),
	SALMON("Salmon", "Salmon Pet", "salmon"),
	SHEEP("Sheep", "Sheep Pet", "sheep", new PetDataCategory[]{PetDataCategory.SHEEP_COLOR}, PetData.SHEARED/*, PetData.SHEARED,
	                                                                                                        PetData.BLACK, PetData.BLUE, PetData.BROWN,
	                                                                                                        PetData.CYAN, PetData.GRAY, PetData.GREEN,
	                                                                                                        PetData.LIGHT_BLUE, PetData.LIME, PetData.MAGENTA,
	                                                                                                        PetData.ORANGE, PetData.PINK, PetData.PURPLE, PetData.RED,
	                                                                                                        PetData.WHITE, PetData.YELLOW, PetData.SILVER*/),
	SNOWMAN("Snowman", "Snowman Pet", "snow_golem", PetData.SHEARED),
	SQUID("Squid", "Squid Pet", "squid"),
	TROPICALFISH("TropicalFish", "Tropical Fish Pet", "tropical_fish"),
	TURTLE("Turtle", "Turtle Pet", "turtle"),
	VILLAGER("Villager", "Villager Pet", "villager"/*, PetData.BLACKSMITH, PetData.BUTCHER, PetData.FARMER, PetData.LIBRARIAN, PetData.PRIEST*/),
	WOLF("Wolf", "Wolf Pet", "wolf"/*, PetData.TAMED, PetData.ANGRY,
	                               PetData.BLACK, PetData.BLUE, PetData.BROWN,
	                               PetData.CYAN, PetData.GRAY, PetData.GREEN,
	                               PetData.LIGHT_BLUE, PetData.LIME, PetData.MAGENTA,
	                               PetData.ORANGE, PetData.PINK, PetData.PURPLE, PetData.RED,
	                               PetData.WHITE, PetData.YELLOW, PetData.SILVER*/),

	HUMAN("Human", "Human Pet", "UNKNOWN");

	private String classIdentifier;
    private Class<? extends IEntityPet> entityClass;
    private Class<? extends IPet> petClass;
    private String defaultName;
	private String minecraftEntityName;
	private List<PetDataCategory> allowedCategories;
    private List<PetData> allowedData;
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
		    // do nothing
		}
		this.allowedCategories = categories == null ? ImmutableList.of() : ImmutableList.copyOf(categories);
		this.allowedData = allowedData == null ? ImmutableList.of() : ImmutableList.copyOf(allowedData);
		this.minecraftEntityName = minecraftEntityName;
        this.defaultName = defaultName;
		this.version = version;
    }

	public String getClassIdentifier(){
		return classIdentifier;
	}

    public String getDefaultName(String name) {
		return EchoPet.getConfig().getString("pets." + this.toString().toLowerCase().replace("_", "") + ".defaultName", this.defaultName).replace("(user)", name).replace("(userApos)", name + "'s");
    }

    public String getDefaultName() {
        return this.defaultName;
    }

	public String getMinecraftName(){
		return minecraftEntityName;
	}

	public List<PetDataCategory> getAllowedCategories(){
		return allowedCategories;
	}

    public List<PetData> getAllowedDataTypes() {
        return this.allowedData;
    }


    public boolean isDataAllowed(PetData data) {
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
        if (owner != null) {
            return EchoPet.getPetRegistry().spawn(this, owner);
        }
        return null;
    }

    public Class<? extends IEntityPet> getEntityClass() {
        return this.entityClass;
    }

    public Class<? extends IPet> getPetClass() {
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
		for(String petTypeName : petTypes){
			System.out.println("    echopet.pet.type." + petTypeName + ":");
			System.out.println("        default: op");
		}
		for(String petTypeName : petTypes){
			System.out.println("    echopet.pet.hat." + petTypeName + ":");
			System.out.println("        default: op");
		}
		for(String petTypeName : petTypes){
			System.out.println("    echopet.pet.ride." + petTypeName + ":");
			System.out.println("        default: op");
		}
		for(String petTypeName : petTypes){
			System.out.println("    echopet.pet.default.set.type." + petTypeName + ":");
			System.out.println("        default: op");
		}
		for(String petTypeName : petTypes){
			PetType petType = PetType.valueOf(petTypeName.toUpperCase());
			for(PetData data : petType.getAllowedDataTypes()){
				System.out.println("    echopet.pet.type." + petTypeName + "." + data.getConfigOptionString() + ":");
				System.out.println("        default: op");
			}
		}
		for(String petTypeName : petTypes){
			System.out.println("    echopet.petadmin.type." + petTypeName + ":");
			System.out.println("        default: op");
		}
		for(String petTypeName : petTypes){
			System.out.println("    echopet.petadmin.hat." + petTypeName + ":");
			System.out.println("        default: op");
		}
		for(String petTypeName : petTypes){
			System.out.println("    echopet.petadmin.ride." + petTypeName + ":");
			System.out.println("        default: op");
		}
		for(String petTypeName : petTypes){
			System.out.println("    echopet.petadmin.default.set.type." + petTypeName + ":");
			System.out.println("        default: op");
		}
		System.out.println("    echopet.*:");
		System.out.println("        default: op");
		System.out.println("        description: 'All EchoPet commands'");
		System.out.println("        children:");
		System.out.println("            echopet.pet.*: true");
		System.out.println("            echopet.petadmin.*: true");
		//
		System.out.println("    echopet.pet.*:");
		System.out.println("        default: op");
		System.out.println("        description: 'All permissions under the /pet command'");
		System.out.println("        children:");
		System.out.println("            echopet.pet: true\n            echopet.pet.remove: true\n            echopet.pet.list: true\n            echopet.pet.info: true\n            echopet.pet.menu: true\n            echopet.pet.show: true\n            echopet.pet.hide: true\n            echopet.pet.toggle: true\n            echopet.pet.call: true\n            echopet.pet.name: true\n            echopet.pet.name.override: true\n            echopet.pet.select: true\n            echopet.pet.selector: true\n            echopet.pet.type.*: true\n            echopet.pet.data.*: true\n            echopet.pet.ride.*: true\n            echopet.pet.hat.*: true\n            echopet.pet.default.*: true");
		//
		System.out.println("    echopet.pet.type.*:");
		System.out.println("        default: op");
		System.out.println("        description: 'All pet type permissions'");
		System.out.println("        children:");
		for(String petTypeName : petTypes){
			System.out.println("            echopet.pet.type." + petTypeName + ": true");
		}
		//
		System.out.println("    echopet.pet.hat.*:");
		System.out.println("        default: op");
		System.out.println("        description: 'All hat permissions'");
		System.out.println("        children:");
		for(String petTypeName : petTypes){
			System.out.println("            echopet.pet.hat." + petTypeName + ": true");
		}
		//
		System.out.println("    echopet.pet.ride.*:");
		System.out.println("        default: op");
		System.out.println("        description: 'All ride permissions'");
		System.out.println("        children:");
		for(String petTypeName : petTypes){
			System.out.println("            echopet.pet.ride." + petTypeName + ": true");
		}
		//
		System.out.println("    echopet.pet.default.*:\n        default: op\n        description: 'All permissions under /pet default <...>'\n        children:\n            echopet.pet.default.set.current: true\n            echopet.pet.default.remove: true\n            echopet.pet.default.set.type.*: true");
		//
		System.out.println("    echopet.pet.default.set.type.*:");
		System.out.println("        default: op");
		System.out.println("        description: 'All pet types for /pet default set <...>'");
		System.out.println("        children:");
		for(String petTypeName : petTypes){
			System.out.println("            echopet.pet.default.set.type." + petTypeName + ": true");
		}
		//
		System.out.println("    echopet.petadmin.*:\n        default: op\n        description: 'All permissions under the /petadmin command'\n        children:\n            echopet.petadmin: true\n            echopet.petadmin.reload: true\n            echopet.petadmin.remove: true\n            echopet.petadmin.list: true\n            echopet.petadmin.info: true\n            echopet.petadmin.menu: true\n            echopet.petadmin.show: true\n            echopet.petadmin.hide: true\n            echopet.petadmin.call: true\n            echopet.petadmin.name: true\n            echopet.petadmin.select: true\n            echopet.petadmin.selector: true\n            echopet.petadmin.type.*: true\n            echopet.pet.data.*: true\n            echopet.petadmin.ride.*: true\n            echopet.petadmin.hat.*: true\n            echopet.petadmin.default.*: true");
		//
		System.out.println("    echopet.petadmin.type.*:");
		System.out.println("        default: op");
		System.out.println("        description: 'All pet type permissions for PetAdmin commands'");
		System.out.println("        children:");
		for(String petTypeName : petTypes){
			System.out.println("            echopet.petadmin.type." + petTypeName + ": true");
		}
		//
		System.out.println("    echopet.petadmin.hat.*:");
		System.out.println("        default: op");
		System.out.println("        description: 'All pet hat permissions for PetAdmin commands'");
		System.out.println("        children:");
		for(String petTypeName : petTypes){
			System.out.println("            echopet.petadmin.hat." + petTypeName + ": true");
		}
		//
		System.out.println("    echopet.petadmin.ride.*:");
		System.out.println("        default: op");
		System.out.println("        description: 'All pet ride permissions for PetAdmin commands'");
		System.out.println("        children:");
		for(String petTypeName : petTypes){
			System.out.println("            echopet.petadmin.ride." + petTypeName + ": true");
		}
		//
		System.out.println("    echopet.petadmin.default.*:\n        default: op\n        description: 'All permissions under /petadmin default <...>'\n        children:\n            echopet.petadmin.default.set.current: true\n            echopet.petadmin.default.remove: true\n            echopet.petadmin.default.set.type.*: true");
		//
		System.out.println("    echopet.petadmin.default.set.type.*:");
		System.out.println("        default: op");
		System.out.println("        description: 'All pet types for /petadmin default set <...>'");
		System.out.println("        children:");
		for(String petTypeName : petTypes){
			System.out.println("            echopet.petadmin.default.set.type." + petTypeName + ": true");
		}
		//
		for(String petTypeName : petTypes){
			System.out.println("    echopet.pet.type." + petTypeName + ".*:");
			System.out.println("        default: op");
			System.out.println("        description: 'All " + petTypeName + " pet data permissions'");
			System.out.println("        children:");
			PetType petType = PetType.valueOf(petTypeName.toUpperCase());
			for(PetData data : PetData.values){
				if(petType.isDataAllowed(data)){
					System.out.println("            echopet.pet.type." + petTypeName + "." + data.getConfigOptionString() + ": true");
				}
			}
		}
	}

	public static void main(String[] args){
		outputInfo();
	}
}