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
	BLAZE("Blaze", 61, "Blaze Pet", 20D, 6D, "blaze", "Blaze", PetData.FIRE),
	CAVESPIDER("CaveSpider", 59, "Cave Spider Pet", 12D, 5D, "cave_spider", "CaveSpider"),
	CREEPER("Creeper", 50, "Creeper Pet", 20D, 6D, "creeper", "Creeper", PetData.POWER),
	DROWNED("Drowned", 0, "Drowned Pet", 20D, 4D, "drowned", "Drowned"),
	ENDERDRAGON("EnderDragon", 63, "EnderDragon Pet", 200D, 0D, "ender_dragon", "EnderDragon"),
	ENDERMAN("Enderman", 58, "Enderman Pet", 40D, 6D, "enderman", "Enderman", PetData.SCREAMING),
	ENDERMITE("Endermite", 67, "Endermite Pet", 2D, 2D, "endermite", "Endermite"),
	GHAST("Ghast", 56, "Ghast Pet", 10D, 7D, "ghast", "Ghast"),
	GIANT("Giant", 53, "Giant Pet", 100D, 0D, "giant", "Giant"),
	GUARDIAN("Guardian", 68, "Guardian Pet", 30D, 9D, "guardian", "Guardian", PetData.ELDER),
	ELDERGUARDIAN("ElderGuardian", 4, "Elder Guardian Pet", 80D, 12D, "elder_guardian", "ElderGuardian", new Version("1.11-R1")),
	EVOKER("Evoker", 34, "Evoker Pet", 24D, 6D, "evocation_illager", "Evoker", new Version("1.11-R1")),
	MAGMACUBE("MagmaCube", 62, "Magma Cube Pet", 20D, 5D, "magma_cube", "LavaSlime", PetData.SMALL, PetData.MEDIUM, PetData.LARGE),
	PIGZOMBIE("PigZombie", 57, "Pig Zombie Pet", 20D, 6D, "zombie_pigman", "PigZombie", PetData.BABY),
	SHULKER("Shulker", 69, "Shulker Pet", 30D, 4D, "shulker", "Shulker", new Version("1.9-R1"), PetData.OPEN, PetData.BLACK, PetData.BLUE, PetData.BROWN,
	          PetData.CYAN, PetData.GRAY, PetData.GREEN,
		      PetData.LIGHT_BLUE, PetData.LIME, PetData.MAGENTA,
	          PetData.ORANGE, PetData.PINK, PetData.PURPLE, PetData.RED,
		      PetData.WHITE, PetData.YELLOW, PetData.SILVER),
	SILVERFISH("Silverfish", 60, "Silverfish Pet", 8D, 4D, "silverfish", "Silverfish"),
	SKELETON("Skeleton", 51, "Skeleton Pet", 20D, 5D, "skeleton", "Skeleton", PetData.NORMAL, PetData.WITHER, PetData.STRAY),
	WITHERSKELETON("WitherSkeleton", 5, "Wither Skeleton Pet", 20D, 5D, "wither_skeleton", "WitherSkeleton", new Version("1.11-R1")),
	STRAY("Stray", 6, "Stray Pet", 20D, 5D, "stray", "Stray", new Version("1.11-R1")),
	SLIME("Slime", 55, "Slime Pet", 20D, 4D, "slime", "Slime", PetData.SMALL, PetData.MEDIUM, PetData.LARGE),
	SPIDER("Spider", 52, "Spider Pet", 16D, 5D, "spider", "Spider"),
	WITCH("Witch", 66, "Witch Pet", 26D, 5D, "witch", "Witch"),
	WITHER("Wither", 64, "Wither Pet", 300D, 8D, "wither", "WitherBoss", PetData.SHIELD),
	ZOMBIE("Zombie", 54, "Zombie Pet", 20D, 5D, "zombie", "Zombie", PetData.BABY, PetData.BLACKSMITH, PetData.BUTCHER, PetData.FARMER, PetData.LIBRARIAN, PetData.PRIEST, PetData.HUSK),
	HUSK("Husk", 23, "Husk Pet", 20D, 5D, "husk", "Husk", new Version("1.11-R1"), PetData.BABY),
	ILLUSIONER("Illusioner", 37, "Illusioner Pet", 32D, 5D, "illusion_illager", "IllusionIllager", new Version("1.12-R1")),
	ZOMBIEVILLAGER("ZombieVillager", 27, "Zombie Villager Pet", 20D, 5D, "zombie_villager", "ZombieVillager", new Version("1.11-R1"), PetData.BABY, PetData.BLACKSMITH, PetData.BUTCHER, PetData.FARMER, PetData.LIBRARIAN, PetData.PRIEST),
	VEX("Vex", 35, "Vex Pet", 14D, 5D, "vex", "Vex", new Version("1.11-R1"), PetData.POWER),
	VINDICATOR("Vindicator", 36, "Vindicator Pet", 24D, 7D, "vindication_illager", "Vindicator", new Version("1.11-R1")),
    // Passive mobs
	BAT("Bat", 65, "Bat Pet", 6D, 3D, "bat", "Bat"),
	CHICKEN("Chicken", 93, "Chicken Pet", 4D, 3D, "chicken", "Chicken", PetData.BABY),
	COD("Cod", 00, "Cod Pet", 3D, 2D, "cod", "Cod"),
	COW("Cow", 92, "Cow Pet", 10D, 4D, "cow", "Cow", PetData.BABY),
	DOLPHIN("Dolphin", 0, "Dolphin Pet", 10D, 4D, "dolphin", "Dolphin"),
	HORSE("Horse", 100, "Horse Pet", 30D, 4D, "horse", "EntityHorse", PetData.BABY, PetData.CHESTED, PetData.SADDLE,
	        PetData.HORSE, PetData.DONKEY, PetData.MULE, PetData.SKELETON_HORSE, PetData.UNDEAD_HORSE, PetData.WHITE,
	        PetData.CREAMY, PetData.CHESTNUT, PetData.BROWN, PetData.BLACK, PetData.WHITE_SOCKS,
	        PetData.GRAY, PetData.DARK_BROWN, PetData.NONE, PetData.WHITEFIELD, PetData.WHITE_DOTS, PetData.BLACK_DOTS,
          PetData.NOARMOUR, PetData.IRON, PetData.GOLD, PetData.DIAMOND),
	SKELETONHORSE("SkeletonHorse", 28, "Skeleton Horse Pet", 30D, 4D, "skeleton_horse", "SkeletonHorse", new Version("1.11-R1"), HORSE.getAllowedDataArray()),
	ZOMBIEHORSE("ZombieHorse", 29, "Zombie Horse Pet", 30D, 4D, "zombie_horse", "ZombieHorse", new Version("1.11-R1"), HORSE.getAllowedDataArray()),
	DONKEY("Donkey", 31, "Donkey Pet", 30D, 4D, "donkey", "Donkey", new Version("1.11-R1"), HORSE.getAllowedDataArray()),
	MULE("Mule", 32, "Mule Pet", 30D, 4D, "mule", "Mule", new Version("1.11-R1"), HORSE.getAllowedDataArray()),
	IRONGOLEM("IronGolem", 99, "Iron Golem Pet", 100D, 7D, "villager_golem", "VillagerGolem"),
	LLAMA("Llama", 103, "Llama Pet", 22D, 3D, "llama", "Llama", new Version("1.11-R1"), PetData.BABY, PetData.CHESTED,
	          PetData.BLACK, PetData.BLUE, PetData.BROWN,
	          PetData.CYAN, PetData.GRAY, PetData.GREEN,
	        PetData.LIGHT_BLUE, PetData.LIME, PetData.MAGENTA,
	          PetData.ORANGE, PetData.PINK, PetData.PURPLE, PetData.RED,
	        PetData.WHITE, PetData.YELLOW, PetData.SILVER,
	        PetData.CREAMY, PetData.BROWN_LLAMA, PetData.GRAY_LLAMA, PetData.WHITE_LLAMA),
	MUSHROOMCOW("MushroomCow", 96, "Mushroom Cow Pet", 10D, 3D, "mooshroom", "MushroomCow", PetData.BABY),
	OCELOT("Ocelot", 98, "Ocelot Pet", 10D, 4D, "ocelot", "Ozelot", PetData.BABY, PetData.BLACK, PetData.RED, PetData.SIAMESE, PetData.WILD),
	PARROT("Parrot", 105, "Parrot Pet", 6D, 1D, "parrot", "Parrot", new Version("1.12-R1"), PetData.GRAY, PetData.GREEN, PetData.CYAN, PetData.BLUE, PetData.RED, PetData.LEFT_SHOULDER, PetData.RIGHT_SHOULDER),
	PIG("Pig", 90, "Pig Pet", 10D, 3D, "pig", "Pig", PetData.BABY, PetData.SADDLE),
	POLARBEAR("PolarBear", 102, "Polar Bear Pet", 30D, 9D, "polar_bear", "PolarBear", new Version("1.10-R1"), PetData.BABY, PetData.STANDING_UP),
	RABBIT("Rabbit", 101, "Rabbit Pet", 8D, 3D, "rabbit", "Rabbit", PetData.BABY, PetData.BROWN, PetData.WHITE, PetData.BLACK, PetData.BLACK_AND_WHITE, PetData.GOLD, PetData.SALT_AND_PEPPER, PetData.THE_KILLER_BUNNY),
	SHEEP("Sheep", 91, "Sheep Pet", 8D, 3D, "sheep", "Sheep", PetData.BABY, PetData.SHEARED,
          PetData.BLACK, PetData.BLUE, PetData.BROWN,
          PetData.CYAN, PetData.GRAY, PetData.GREEN,
	        PetData.LIGHT_BLUE, PetData.LIME, PetData.MAGENTA,
          PetData.ORANGE, PetData.PINK, PetData.PURPLE, PetData.RED,
	        PetData.WHITE, PetData.YELLOW, PetData.SILVER),
	SNOWMAN("Snowman", 97, "Snowman Pet", 4D, 4D, "snowman", "SnowMan", PetData.SHEARED),
	SQUID("Squid", 94, "Squid Pet", 10D, 4D, "squid", "Squid"),
	VILLAGER("Villager", 120, "Villager Pet", 20D, 4D, "villager", "Villager", PetData.BABY, PetData.BLACKSMITH, PetData.BUTCHER, PetData.FARMER, PetData.LIBRARIAN, PetData.PRIEST),
	WOLF("Wolf", 95, "Wolf Pet", 20D, 6D, "wolf", "Wolf", PetData.BABY, PetData.TAMED, PetData.ANGRY,
         PetData.BLACK, PetData.BLUE, PetData.BROWN,
         PetData.CYAN, PetData.GRAY, PetData.GREEN,
	        PetData.LIGHT_BLUE, PetData.LIME, PetData.MAGENTA,
         PetData.ORANGE, PetData.PINK, PetData.PURPLE, PetData.RED,
	        PetData.WHITE, PetData.YELLOW, PetData.SILVER),

	HUMAN("Human", 54, "Human Pet", 20D, 6D, "UNKNOWN", "UNKNOWN");

	private String classIdentifier;
    private Class<? extends IEntityPet> entityClass;
    private Class<? extends IPet> petClass;
    private String defaultName;
    private double maxHealth;
    private double attackDamage;
	private String minecraftEntityName;
	private PetData[] allowedDataArray;
    private List<PetData> allowedData;
    private int id;
	private Version version;

	PetType(String classIdentifier, int registrationId, String defaultName, double maxHealth, double attackDamage, String entityType, PetData... allowedData){
		this(classIdentifier, registrationId, defaultName, maxHealth, attackDamage, entityType, null, new Version(), allowedData);
	}

	PetType(String classIdentifier, int registrationId, String defaultName, double maxHealth, double attackDamage, String entityType, Version version, PetData... allowedData){
		this(classIdentifier, registrationId, defaultName, maxHealth, attackDamage, entityType, null, version, allowedData);
	}

	PetType(String classIdentifier, int registrationId, String defaultName, double maxHealth, double attackDamage, String entityType, String entityTypeFixedName, PetData... allowedData){
		this(classIdentifier, registrationId, defaultName, maxHealth, attackDamage, entityType, entityTypeFixedName, new Version(), allowedData);
	}

	@SuppressWarnings({"unchecked"})
	PetType(String classIdentifier, int registrationId, String defaultName, double maxHealth, double attackDamage, String minecraftEntityName, String entityTypeFixedName, Version version, PetData... allowedData){
		this.classIdentifier = classIdentifier;
		try{
		    this.entityClass = (Class<? extends IEntityPet>) Class.forName(ReflectionUtil.COMPAT_NMS_PATH + ".entity.type.Entity" + classIdentifier + "Pet");
			this.petClass = ReflectionUtil.getClass("com.dsh105.echopet.api.pet.type." + classIdentifier + "Pet");
		} catch (ClassNotFoundException e) {
		    // do nothing
		}
        this.id = registrationId;
        this.allowedDataArray = allowedData;
        this.allowedData = ImmutableList.copyOf(allowedData);
        this.maxHealth = maxHealth;
        this.attackDamage = attackDamage;
		this.minecraftEntityName = minecraftEntityName;
        this.defaultName = defaultName;
		this.version = version;
    }

	public String getClassIdentifier(){
		return classIdentifier;
	}

    public int getRegistrationId() {
        return this.id;
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }

    public String getDefaultName(String name) {
		return EchoPet.getConfig().getString("pets." + this.toString().toLowerCase().replace("_", "") + ".defaultName", this.defaultName).replace("(user)", name).replace("(userApos)", name + "'s");
    }

    public String getDefaultName() {
        return this.defaultName;
    }

    public double getAttackDamage() {
		return EchoPet.getConfig().getDouble("pets." + this.toString().toLowerCase().replace("_", "") + ".attackDamage", this.attackDamage);
    }

	public String getMinecraftName(){
		return minecraftEntityName;
	}

	public String getFixedSoundEffectEntityName(){
		switch (getClassIdentifier().toLowerCase()){
			case "polarbear":
				return "polar_bear";
			case "mushroomcow":
				return "cow";
			case "cavespider":
				return "spider";
			case "elderguardian":
				return "elder_guardian";
			case "zombievillager":
				return "villager";
			case "skeletonhorse":
				return "skeleton_horse";
			case "zombiehorse":
				return "zombie_horse";
			case "vindicator":
				return "vindication_illager";
			case "evoker":
				return "evocation_illager";
			default:
				return getClassIdentifier().toLowerCase();
		}
	}

    public List<PetData> getAllowedDataTypes() {
        return this.allowedData;
    }

	public PetData[] getAllowedDataArray(){
		return this.allowedDataArray;
	}

    public boolean isDataAllowed(PetData data) {
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
			for(PetData data : petType.getAllowedDataTypes()){
				System.out.println("            echopet.pet.type." + petTypeName + "." + data.getConfigOptionString() + ": true");
			}
		}
	}

	public static void main(String[] args){
		outputInfo();
	}
}