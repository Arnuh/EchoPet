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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.config.YAMLConfig;
import com.dsh105.echopet.compat.api.entity.data.PetData;
import com.dsh105.echopet.compat.api.entity.data.PetDataCategory;
import com.dsh105.echopet.compat.api.entity.nms.IEntityPet;
import com.dsh105.echopet.compat.api.entity.pet.IAgeablePet;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.pet.ITameablePet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseChestedAbstractPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.dsh105.echopet.compat.api.util.Version;
import com.google.common.collect.ImmutableList;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public enum PetType implements IPetType{
	ALLAY("Allay", "Allay Pet", "allay", new Version("1.19-R1")),
	AXOLOTL("Axolotl", "Axolotl Pet", "axolotl", new Version("1.17-R1"), new PetDataCategory[]{PetDataCategory.AXOLOTL_VARIANT}, PetData.PLAYING_DEAD),
	BAT("Bat", "Bat Pet", "bat", PetData.WANDER),
	BEE("Bee", "Bee Pet", "bee", PetData.STINGER, PetData.NECTAR, PetData.ANGRY),
	BLAZE("Blaze", "Blaze Pet", "blaze", PetData.FIRE),
	CAT("Cat", "Cat Pet", "cat", new PetDataCategory[]{PetDataCategory.COLLAR_COLOR, PetDataCategory.CAT_TYPE}),
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
	FROG("Frog", "Frog Pet", "frog", new Version("1.19-R1"), new PetDataCategory[]{PetDataCategory.FROG_VARIANT}),
	GHAST("Ghast", "Ghast Pet", "ghast"),
	GIANT("Giant", "Giant Pet", "giant", Material.ZOMBIE_SPAWN_EGG),
	GLOWSQUID("GlowSquid", "Glow Squid Pet", "glow_squid", new Version("1.17-R1"), PetData.DARK),
	GOAT("Goat", "Goat Pet", "goat", new Version("1.17-R1"), PetData.SCREAMING, PetData.LEFT_HORN, PetData.RIGHT_HORN),
	GUARDIAN("Guardian", "Guardian Pet", "guardian"),
	HOGLIN("Hoglin", "Hoglin Pet", "hoglin"),
	HORSE("Horse", "Horse Pet", "horse", new PetDataCategory[]{PetDataCategory.HORSE_ARMOR, PetDataCategory.HORSE_COLOR, PetDataCategory.HORSE_MARKING}, PetData.SADDLE),
	HUMAN("Human", "Human Pet", "UNKNOWN"),
	HUSK("Husk", "Husk Pet", "husk"),
	ILLUSIONER("Illusioner", "Illusioner Pet", "illusioner", Material.VILLAGER_SPAWN_EGG),
	IRONGOLEM("IronGolem", "Iron Golem Pet", "iron_golem", Material.IRON_BLOCK, PetData.HEALTH),
	LLAMA("Llama", "Llama Pet", "llama", new PetDataCategory[]{PetDataCategory.LLAMA_COLOR, PetDataCategory.LLAMA_CARPET_COLOR}),
	MAGMACUBE("MagmaCube", "Magma Cube Pet", "magma_cube", new PetDataCategory[]{PetDataCategory.SLIME_SIZE}),
	MULE("Mule", "Mule Pet", "mule", PetData.SADDLE),
	MUSHROOMCOW("MushroomCow", "Mushroom Cow Pet", "mooshroom", new PetDataCategory[]{PetDataCategory.MUSHROOMCOW_TYPE}),
	OCELOT("Ocelot", "Ocelot Pet", "ocelot"),
	PANDA("Panda", "Panda Pet", "panda", new PetDataCategory[]{PetDataCategory.PANDA_MAIN_GENE, PetDataCategory.PANDA_HIDDEN_GENE}, PetData.ROLL, PetData.SIT, PetData.LAY_DOWN),
	PARROT("Parrot", "Parrot Pet", "parrot", new PetDataCategory[]{PetDataCategory.PARROT_VARIANT}/*, PetData.LEFT_SHOULDER, PetData.RIGHT_SHOULDER*/),
	PHANTOM("Phantom", "Phantom Pet", "phantom", PetData.SIZE, PetData.WANDER),
	PIG("Pig", "Pig Pet", "pig", PetData.SADDLE),
	PIGLIN("Piglin", "Piglin Pet", "piglin", PetData.DANCE),
	PIGZOMBIE("PigZombie", "Pig Zombie Pet", "zombie_pigman"),
	PILLAGER("Pillager", "Pillager Pet", "pillager"),
	POLARBEAR("PolarBear", "Polar Bear Pet", "polar_bear", PetData.STANDING_UP),
	PUFFERFISH("PufferFish", "PufferFish Pet", "pufferfish", new PetDataCategory[]{PetDataCategory.PUFFERFISH_SIZE}),
	RABBIT("Rabbit", "Rabbit Pet", "rabbit", new PetDataCategory[]{PetDataCategory.RABBIT_TYPE}),
	RAVAGER("Ravager", "Ravager Pet", "ravager"),
	SALMON("Salmon", "Salmon Pet", "salmon"),
	SHEEP("Sheep", "Sheep Pet", "sheep", new PetDataCategory[]{PetDataCategory.WOOL_COLOR}, PetData.SHEARED),
	SHULKER("Shulker", "Shulker Pet", "shulker", new PetDataCategory[]{PetDataCategory.SHULKER_COLOR}, PetData.PEEK),
	SILVERFISH("Silverfish", "Silverfish Pet", "silverfish"),
	SKELETON("Skeleton", "Skeleton Pet", "skeleton"),
	SKELETONHORSE("SkeletonHorse", "Skeleton Horse Pet", "skeleton_horse", PetData.SADDLE),
	SLIME("Slime", "Slime Pet", "slime", new PetDataCategory[]{PetDataCategory.SLIME_SIZE}),
	SNOWMAN("Snowman", "Snowman Pet", "snow_golem", Material.PUMPKIN, PetData.SHEARED),
	SPIDER("Spider", "Spider Pet", "spider"),
	SQUID("Squid", "Squid Pet", "squid"),
	STRAY("Stray", "Stray Pet", "stray"),
	STRIDER("Strider", "Strider Pet", "strider", PetData.SADDLE),
	TADPOLE("Tadpole", "Tadpole Pet", "tadpole", new Version("1.19-R1")),
	TRADERLLAMA("TraderLlama", "Trader Llama Pet", "trader_llama", new PetDataCategory[]{PetDataCategory.LLAMA_COLOR, PetDataCategory.LLAMA_CARPET_COLOR}),
	TROPICALFISH("TropicalFish", "Tropical Fish Pet", "tropical_fish", new PetDataCategory[]{PetDataCategory.TROPICAL_FISH_PATTERN, PetDataCategory.TROPICAL_FISH_COLOR, PetDataCategory.TROPICAL_FISH_PATTERN_COLOR}, PetData.SIZE_LARGE),
	TURTLE("Turtle", "Turtle Pet", "turtle"),
	VEX("Vex", "Vex Pet", "vex", PetData.POWERED),
	VILLAGER("Villager", "Villager Pet", "villager", new PetDataCategory[]{PetDataCategory.VILLAGER_TYPE, PetDataCategory.VILLAGER_PROFESSION, PetDataCategory.VILLAGER_LEVEL}),
	VINDICATOR("Vindicator", "Vindicator Pet", "vindicator"),
	WANDERINGTRADER("WanderingTrader", "Wandering Trader Pet", "wandering_trader"),
	WARDEN("Warden", "Warden Pet", "warden", new Version("1.19-R1"), new PetDataCategory[]{PetDataCategory.WARDEN_ANGER_LEVEL}),
	WITCH("Witch", "Witch Pet", "witch"),
	WITHER("Wither", "Wither Pet", "wither", PetData.SHIELD),
	WITHERSKELETON("WitherSkeleton", "Wither Skeleton Pet", "wither_skeleton"),
	WOLF("Wolf", "Wolf Pet", "wolf", new PetDataCategory[]{PetDataCategory.COLLAR_COLOR}, PetData.ANGRY, PetData.HEALTH),
	ZOGLIN("Zoglin", "Zoglin Pet", "zoglin"),
	ZOMBIE("Zombie", "Zombie Pet", "zombie"),
	ZOMBIEHORSE("ZombieHorse", "Zombie Horse Pet", "zombie_horse", PetData.SADDLE),
	ZOMBIEVILLAGER("ZombieVillager", "Zombie Villager Pet", "zombie_villager", new PetDataCategory[]{PetDataCategory.VILLAGER_TYPE, PetDataCategory.VILLAGER_PROFESSION, PetDataCategory.VILLAGER_LEVEL}),
	;
	
	public static final PetType[] values = values();
	
	public static final List<IPetType> pets = new ArrayList<>();
	
	static{
		pets.addAll(List.of(values));
	}
	
	private Class<? extends IEntityPet> entityClass;
	private Class<? extends IPet> petClass;
	private final String defaultName;
	private final String minecraftEntityName;
	private final List<PetDataCategory> allowedCategories = new ArrayList<>();
	private final List<PetData<?>> allowedData = new ArrayList<>();
	private final Version version;
	private Material uiMaterial;
	
	// This is a fucking mess
	PetType(String classIdentifier, String defaultName, String minecraftEntityName, PetData<?>... allowedData){
		this(classIdentifier, defaultName, minecraftEntityName, new Version(), null, allowedData);
	}
	
	PetType(String classIdentifier, String defaultName, String minecraftEntityName, Material uiMaterial, PetData<?>... allowedData){
		this(classIdentifier, defaultName, minecraftEntityName, new Version(), uiMaterial, null, allowedData);
	}
	
	PetType(String classIdentifier, String defaultName, String minecraftEntityName, PetDataCategory[] categories, PetData<?>... allowedData){
		this(classIdentifier, defaultName, minecraftEntityName, new Version(), null, categories, allowedData);
	}
	
	PetType(String classIdentifier, String defaultName, String minecraftEntityName, Version version, PetData<?>... allowedData){
		this(classIdentifier, defaultName, minecraftEntityName, version, null, null, allowedData);
	}
	
	PetType(String classIdentifier, String defaultName, String minecraftEntityName, Version version, PetDataCategory[] categories, PetData<?>... allowedData){
		this(classIdentifier, defaultName, minecraftEntityName, version, null, categories, allowedData);
	}
	
	@SuppressWarnings("unchecked")
	PetType(String classIdentifier, String defaultName, String minecraftEntityName, Version version, Material uiMaterial, PetDataCategory[] categories, PetData<?>... allowedData){
		try{
			this.entityClass = ReflectionUtil.getVersionedClass(IEntityPet.class, "entity.type.Entity" + classIdentifier + "Pet");
		}catch(ClassNotFoundException | NoClassDefFoundError ignored){
		}
		try{
			this.petClass = ReflectionUtil.getClass(IPet.class, "com.dsh105.echopet.api.pet.type." + classIdentifier + "Pet");
		}catch(Exception ex){
			EchoPet.LOG.log(Level.SEVERE, "", ex);
		}
		if(name().equals("PIGZOMBIE")){// hacky fix for now
			if(new Version("1.16-R1").isCompatible(new Version())){
				defaultName = "Zombified Piglin Pet";
				minecraftEntityName = "zombified_piglin";
			}
		}
		this.minecraftEntityName = minecraftEntityName;
		this.defaultName = defaultName;
		this.version = version;
		this.uiMaterial = uiMaterial;
		if(this.uiMaterial == null){
			for(Material material : Material.values()){
				String name = material.name().toLowerCase();
				if(name.endsWith("spawn_egg") && name.startsWith(minecraftEntityName)){
					this.uiMaterial = material;
					break;
				}
			}
		}
		if(isCompatible() && !name().equalsIgnoreCase("HUMAN")){// Humans ignore the config.
			if(this.uiMaterial == null){// Plugin probably wont work
				EchoPet.getPlugin().getLogger().warning("Found no Material for pet type " + name());
			}
		}
		//
		this.allowedData.add(PetData.HAT);
		this.allowedData.add(PetData.RIDE);
		
		if(IAgeablePet.class.isAssignableFrom(petClass)){
			this.allowedData.add(PetData.BABY);
		}
		if(IHorseChestedAbstractPet.class.isAssignableFrom(petClass)){
			this.allowedData.add(PetData.CHESTED);
		}
		if(ITameablePet.class.isAssignableFrom(petClass)){
			this.allowedData.add(PetData.SIT);
			this.allowedData.add(PetData.TAMED);
		}
		if(allowedData != null){
			this.allowedData.addAll(ImmutableList.copyOf(allowedData));
		}
		if(categories != null){
			this.allowedCategories.addAll(ImmutableList.copyOf(categories));
		}
	}
	
	@Override
	public String getDefaultName(String name){
		return EchoPet.getConfig().getString("pets." + getConfigKeyName() + ".defaultName", this.defaultName).replace("(user)", name).replace("(userApos)", name + "'s");
	}
	
	@Override
	public String getDefaultName(){
		return this.defaultName;
	}
	
	@Override
	public String getMinecraftName(){
		return minecraftEntityName;
	}
	
	@Override
	public List<PetDataCategory> getAllowedCategories(){
		return allowedCategories;
	}
	
	@Override
	public List<PetData<?>> getAllowedDataTypes(){
		return this.allowedData;
	}
	
	@Override
	public IEntityPet getNewEntityPetInstance(Object world, IPet pet){
		return EchoPet.getPetRegistry().getRegistrationEntry(pet.getPetType()).createEntityPet(world, pet);
	}
	
	@Override
	public IPet getNewPetInstance(Player owner){
		if(owner != null){
			return EchoPet.getPetRegistry().spawn(this, owner);
		}
		return null;
	}
	
	@Override
	public Class<? extends IEntityPet> getEntityClass(){
		return this.entityClass;
	}
	
	@Override
	public Class<? extends IPet> getPetClass(){
		return this.petClass;
	}
	
	@Override
	public Version getVersion(){
		return version;
	}
	
	@Override
	public boolean isCompatible(){
		return version.isCompatible(new Version());
	}
	
	@Override
	public Material getUIMaterial(){
		return uiMaterial;
	}
	
	/**
	 * @return String used for both configs and permissions to identify this pet.
	 */
	@Override
	public String getConfigKeyName(){
		return name().toLowerCase().replace("_", "");
	}
	
	@Override
	public YAMLConfig getConfig(){
		return EchoPet.getConfig();
	}
	
	@Override
	public Object getRawConfigValue(String variable, Object defaultValue){
		return getConfig().get("pets." + getConfigKeyName() + "." + variable, defaultValue);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getConfigValue(String variable, T defaultValue){
		return (T) getRawConfigValue(variable, defaultValue);
	}
	
	@Override
	public ConfigurationSection getPetDataSection(PetData<?> data){
		return getConfig().getConfigurationSection("pets." + getConfigKeyName() + ".data." + data.getConfigKeyName());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getPetDataProperty(PetData<?> data, String variable, T defaultValue){
		ConfigurationSection section = getPetDataSection(data);
		if(section == null) return defaultValue;
		return (T) section.get("." + variable, defaultValue);
	}
	
	@Override
	public boolean isEnabled(){
		return getConfigValue("enable", true);
	}
	
	@Override
	public boolean isTagVisible(){
		return getConfigValue("tagVisible", true);
	}
	
	@Override
	public boolean isInteractMenuEnabled(){
		return getConfigValue("interactMenu", true);
	}
	
	@Override
	public boolean allowRidersFor(){
		return getConfigValue("riders", true);
	}
	
	@Override
	public boolean isDataAllowed(PetData<?> data){
		return getPetDataProperty(data, "allow", true);
	}
	
	@Override
	public boolean isDataForced(PetData<?> data){
		return getPetDataProperty(data, "force", false);
	}
	
	@Override
	public <T> T getDataDefaultValue(PetData<?> data, T defaultValue){
		return getPetDataProperty(data, "default", defaultValue);
	}
	
	public static @Nullable IPetType get(String name){
		for(IPetType pet : pets){
			if(pet.name().equalsIgnoreCase(name)){
				return pet;
			}
		}
		return null;
	}
	
	private static void outputInfo(){
		String[] petTypes = new String[PetType.values.length];
		int pos = 0;
		for(PetType type : PetType.values){
			petTypes[pos++] = type.getConfigKeyName();
		}
		Arrays.sort(petTypes);
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("perms.yml"))){
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
				for(PetData<?> data : petType.getAllowedDataTypes()){
					bw.write("    echopet.pet.type." + petTypeName + "." + data.getConfigKeyName() + ":\n");
					bw.write("        default: op\n");
				}
				Set<String> alreadyHad = new HashSet<>();
				for(PetDataCategory category : petType.getAllowedCategories()){
					category.load(true);
					for(PetData<?> data : category.getData()){
						if(alreadyHad.contains(data.getConfigKeyName())){
							System.out.println(petType + " has dupe perm string " + data.getConfigKeyName() + " one from category " + category);
							continue;
						}
						alreadyHad.add(data.getConfigKeyName());
						bw.write("    echopet.pet.type." + petTypeName + "." + data.getConfigKeyName() + ":\n");
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
			
			// Grab from HelpEntry?
			String[] options = new String[]{"echopet.pet", "echopet.pet.remove", "echopet.pet.list", "echopet.pet.info", "echopet.pet.menu", "echopet.pet.show", "echopet.pet.hide", "echopet.pet.toggle", "echopet.pet.call", "echopet.pet.name", "echopet.pet.name.override", "echopet.pet.select", "echopet.pet.selector", "echopet.pet.modify", "echopet.pet.type.*", "echopet.pet.data.*", "echopet.pet.default.*"};
			for(String s : options){
				bw.write("            " + s + ": true\n");
			}
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
				bw.write("            echopet.pet.type." + petTypeName + "." + PetData.HAT.getConfigKeyName() + ": true\n");
			}
			//
			bw.write("    echopet.pet.ride.*:\n");
			bw.write("        default: op\n");
			bw.write("        description: 'All ride permissions'\n");
			bw.write("        children:\n");
			for(String petTypeName : petTypes){
				bw.write("            echopet.pet.type." + petTypeName + "." + PetData.RIDE.getConfigKeyName() + ": true\n");
			}
			//
			bw.write("    echopet.pet.baby.*:\n");
			bw.write("        default: op\n");
			bw.write("        description: 'All baby permissions'\n");
			bw.write("        children:\n");
			for(String petTypeName : petTypes){
				PetType petType = PetType.valueOf(petTypeName.toUpperCase());
				if(petType.getAllowedDataTypes().contains(PetData.BABY)){
					bw.write("            echopet.pet.type." + petTypeName + "." + PetData.BABY.getConfigKeyName() + ": true\n");
				}
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
				for(PetData<?> data : petType.getAllowedDataTypes()){
					bw.write("            echopet.pet.type." + petTypeName + "." + data.getConfigKeyName() + ": true\n");
				}
				Set<String> alreadyHad = new HashSet<>();
				for(PetDataCategory category : petType.getAllowedCategories()){
					for(PetData<?> data : category.getData()){
						if(alreadyHad.contains(data.getConfigKeyName())){
							// System.out.println(petType + " has dupe perm string " + data.getConfigOptionString() + " one from data " + data + " and category " + category);
							continue;
						}
						alreadyHad.add(data.getConfigKeyName());
						bw.write("            echopet.pet.type." + petTypeName + "." + data.getConfigKeyName() + ": true\n");
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		outputInfo();
		
		/*String[] petTypes = new String[PetType.values.length];
		int pos = 0;
		for(PetType type : PetType.values){
			petTypes[pos++] = type.getConfigKeyName();
		}
		Arrays.sort(petTypes);
		for(String petTypeName : petTypes){
			PetType petType = PetType.valueOf(petTypeName.toUpperCase());
			System.out.println(petType.name());
			System.out.print("\t");
			int i = 0;
			for(PetData<?> data : petType.getAllowedDataTypes()){
				if(data.equals(PetData.RIDE) || data.equals(PetData.HAT)) continue;
				if(i++ != 0){
					System.out.print(", ");
				}
				System.out.print(data.getDefaultName());
			}
			for(PetDataCategory category : petType.getAllowedCategories()){
				for(PetData<?> data : category.getData()){
					if(i++ != 0){
						System.out.print(", ");
					}
					System.out.print(data.getDefaultName());
				}
			}
			System.out.print("\n");
		}*/
	}
}