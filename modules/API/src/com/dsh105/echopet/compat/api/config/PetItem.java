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

package com.dsh105.echopet.compat.api.config;

import org.bukkit.Material;

import com.dsh105.echopet.compat.api.entity.PetType;

public enum PetItem {

	BAT(PetType.BAT, Material.BAT_SPAWN_EGG, "Bat Pet", "bat"),
	BLAZE(PetType.BLAZE, Material.BLAZE_SPAWN_EGG, "Blaze Pet", "blaze"),
	CAVESPIDER(PetType.CAVESPIDER, Material.CAVE_SPIDER_SPAWN_EGG, "Cave Spider Pet", "cavespider"),
	CHICKEN(PetType.CHICKEN, Material.CHICKEN_SPAWN_EGG, "Chicken Pet", "chicken"),
	COD(PetType.COD, Material.COD_SPAWN_EGG, "Cod Pet", "cod"),
	COW(PetType.COW, Material.COW_SPAWN_EGG, "Cow Pet", "cow"),
	CREEPER(PetType.CREEPER, Material.CREEPER_SPAWN_EGG, "Creeper Pet", "creeper"),
	DOLPHIN(PetType.DOLPHIN, Material.DOLPHIN_SPAWN_EGG, "Dolphin Pet", "dolphin"),
	DONKEYHORSE(PetType.DONKEY, Material.DONKEY_SPAWN_EGG, "Donkey Pet", "donkey"),
	DROWNED(PetType.DROWNED, Material.DROWNED_SPAWN_EGG, "Drowned Pet", "drowned"),
	ELDERGUARDIAN(PetType.ELDERGUARDIAN, Material.ELDER_GUARDIAN_SPAWN_EGG, "Elder Guardian Pet", "elderguardian"),
    // ENDERDRAGON(PetType.ENDERDRAGON, Material.DRAGON_EGG, "EnderDragon Pet", "enderdragon"),
	ENDERMAN(PetType.ENDERMAN, Material.ENDERMAN_SPAWN_EGG, "Enderman Pet", "enderman"),
	ENDERMITE(PetType.ENDERMITE, Material.ENDERMITE_SPAWN_EGG, "Endermite Pet", "endermite"),
	EVOKER(PetType.EVOKER, Material.EVOKER_SPAWN_EGG, "Evoker Pet", "evoker"),
	GHAST(PetType.GHAST, Material.GHAST_SPAWN_EGG, "Ghast Pet", "ghast"),
	GIANT(PetType.GIANT, Material.ZOMBIE_SPAWN_EGG, "Giant Pet", "giant"),
	GUARDIAN(PetType.GUARDIAN, Material.GUARDIAN_SPAWN_EGG, "Guardian Pet", "guardian"),
	HORSE(PetType.HORSE, Material.HORSE_SPAWN_EGG, "Horse Pet", "horse"),
	HUMAN(PetType.HUMAN, Material.SKELETON_SKULL, 3, "Human Pet", "human"),
	HUSK(PetType.HUSK, Material.HUSK_SPAWN_EGG, "Husk Pet", "husk"),
	ILLUSIONER(PetType.ILLUSIONER, Material.VINDICATOR_SPAWN_EGG, "Illusioner Pet", "illusioner"),
	IRONGOLEM(PetType.IRONGOLEM, Material.PUMPKIN, "Iron Golem Pet", "irongolem"),
	LLAMA(PetType.LLAMA, Material.LLAMA_SPAWN_EGG, "Llama Pet", "llama"),
	MAGMACUBE(PetType.MAGMACUBE, Material.MAGMA_CUBE_SPAWN_EGG, "Magma Cube Pet", "magmacube"),
	MULEHORSE(PetType.MULE, Material.MULE_SPAWN_EGG, "Mule Pet", "mule"),
	MUSHROOMCOW(PetType.MUSHROOMCOW, Material.MOOSHROOM_SPAWN_EGG, "Mushroom Cow Pet", "mushroomcow"),
	OCELOT(PetType.OCELOT, Material.OCELOT_SPAWN_EGG, "Ocelot Pet", "ocelot"),
	PARROT(PetType.PARROT, Material.PARROT_SPAWN_EGG, "Parrot Pet", "parrot"),
	PHANTOM(PetType.PHANTOM, Material.PHANTOM_SPAWN_EGG, "Phantom Pet", "phantom"),
	PIG(PetType.PIG, Material.PIG_SPAWN_EGG, "Pig Pet", "pig"),
	PIGZOMBIE(PetType.PIGZOMBIE, Material.ZOMBIE_PIGMAN_SPAWN_EGG, "PigZombie Pet", "pigzombie"),
	POLARBEAR(PetType.POLARBEAR, Material.POLAR_BEAR_SPAWN_EGG, "Polar Bear pet", "polarbear"),
	PUFFERFISH(PetType.PUFFERFISH, Material.PUFFERFISH_SPAWN_EGG, "Pufferfish Pet", "pufferfish"),
	RABBIT(PetType.RABBIT, Material.RABBIT_SPAWN_EGG, "Rabbit Pet", "rabbit"),
	SALMON(PetType.SALMON, Material.SALMON_SPAWN_EGG, "Salmon Pet", "salmon"),
	SHEEP(PetType.SHEEP, Material.SHEEP_SPAWN_EGG, "Sheep Pet", "sheep"),
	// SHULKER(PetType.SHULKER, Material._SPAWN_EGG, "Shulker Pet", "shulker"),
	SILVERFISH(PetType.SILVERFISH, Material.SILVERFISH_SPAWN_EGG, "Silverfish Pet", "silverfish"),
	SKELETONHORSE(PetType.SKELETONHORSE, Material.SKELETON_HORSE_SPAWN_EGG, "Skeleton Horse Pet", "skeletonhorse"),
	SKELETON(PetType.SKELETON, Material.SKELETON_SPAWN_EGG, "Skeleton Pet", "skeleton"),
	SLIME(PetType.SLIME, Material.SLIME_SPAWN_EGG, "Slime Pet", "slime"),
	SNOWMAN(PetType.SNOWMAN, Material.SNOWBALL, "Snowman Pet", "snowman"),
	SPIDER(PetType.SPIDER, Material.SPIDER_SPAWN_EGG, "Spider Pet", "spider"),
	SQUID(PetType.SQUID, Material.SQUID_SPAWN_EGG, "Squid Pet", "squid"),
	STRAY(PetType.STRAY, Material.STRAY_SPAWN_EGG, "Stray Pet", "stray"),
	TROPICALFISH(PetType.TROPICALFISH, Material.TROPICAL_FISH_SPAWN_EGG, "Tropical Fish Pet", "tropicalfish"),
	TURTLE(PetType.TURTLE, Material.TURTLE_SPAWN_EGG, "Turtle Pet", "turtle"),
	VEX(PetType.VEX, Material.VEX_SPAWN_EGG, "Vex Pet", "vex"),
	VILLAGER(PetType.VILLAGER, Material.VILLAGER_SPAWN_EGG, "Villager Pet", "villager"),
	VINDICATOR(PetType.VINDICATOR, Material.VINDICATOR_SPAWN_EGG, "Vindicator Pet", "vindicator"),
	WITCH(PetType.WITCH, Material.WITCH_SPAWN_EGG, "Witch Pet", "witch"),
	WITHER(PetType.WITHER, Material.NETHER_STAR, "Wither Pet", "wither"),
	WITHERSKELETON(PetType.WITHERSKELETON, Material.WITHER_SKELETON_SPAWN_EGG, "Wither Skeleton Pet", "witherskeleton"),
	WOLF(PetType.WOLF, Material.WOLF_SPAWN_EGG, "Wolf Pet", "wolf"),
	ZOMBIEHORSE(PetType.ZOMBIEHORSE, Material.ZOMBIE_HORSE_SPAWN_EGG, "Zombie Horse Pet", "zombiehorse"),
	ZOMBIE(PetType.ZOMBIE, Material.ZOMBIE_SPAWN_EGG, "Zombie Pet", "zombie"),
	ZOMBIEVILLAGER(PetType.ZOMBIEVILLAGER, Material.ZOMBIE_VILLAGER_SPAWN_EGG, "Zombie Villager Pet", "zombievillager"),
	;

    private String cmd;
    public PetType petType;
    private Material mat;
	private short materialData;
    private String name;

	PetItem(PetType petType, Material mat, String name, String cmd){
		this(petType, mat, 0, name, cmd);
	}

	PetItem(PetType petType, Material mat, int materialData, String name, String cmd){
        this.cmd = "pet " + cmd;
        this.petType = petType;
        this.mat = mat;
		this.materialData = (short) materialData;
        this.name = name;
    }

    public String getCommand() {
        return cmd;
    }

    public PetType getPetType() {
        return petType;
    }

    public Material getMat() {
        return mat;
    }

	public short getMaterialData(){
		return materialData;
	}

    public String getName() {
        return name;
    }
}
