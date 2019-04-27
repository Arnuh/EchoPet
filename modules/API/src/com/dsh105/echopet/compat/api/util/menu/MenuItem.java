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
package com.dsh105.echopet.compat.api.util.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.dsh105.echopet.compat.api.util.Version;
import com.dsh105.echopet.compat.api.util.menu.DataMenu.DataMenuType;

public enum MenuItem{
	HORSE_TYPE(Material.HAY_BLOCK, 1, (short) 0, DataMenuType.HORSE_TYPE, "Type", "Horse"),
	HORSE_VARIANT(Material.LEAD, 1, (short) 0, DataMenuType.HORSE_VARIANT, "Variant", "Horse"),
	HORSE_MARKING(Material.INK_SAC, 1, (short) 0, DataMenuType.HORSE_MARKING, "Marking", "Horse"),
	HORSE_ARMOUR(Material.IRON_CHESTPLATE, 1, (short) 0, DataMenuType.HORSE_ARMOUR, "Armour", "Horse"),
	RABBIT_TYPE(Material.RABBIT_HIDE, 1, (short) 0, DataMenuType.RABBIT_TYPE, "Bunny type", "Rabbit"),
	CHESTED(Material.CHEST, 1, (short) 0, DataMenuType.BOOLEAN, "Chested", "Horse"),
	FIRE(Material.FIRE, 1, (short) 0, DataMenuType.BOOLEAN, "Fire", "Blaze"),
	SADDLE(Material.SADDLE, 1, (short) 0, DataMenuType.BOOLEAN, "Saddle", "Horse", "Pig"),
	SHEARED(Material.SHEARS, 1, (short) 0, DataMenuType.BOOLEAN, "Sheared", "Sheep", "Snowman"),
	SCREAMING(Material.ENDER_PEARL, 1, (short) 0, DataMenuType.BOOLEAN, "Screaming", "Enderman"),
	POTION(Material.POTION, 1, (short) 0, DataMenuType.BOOLEAN, "Potion", "Witch"),
	SHIELD(Material.GLASS, 1, (short) 0, DataMenuType.BOOLEAN, "Shield", "Wither"),
	POWER(Material.BEACON, 1, (short) 0, DataMenuType.BOOLEAN, "Powered", "Creeper", "Vex"),
	SIZE(Material.SLIME_BALL, 1, (short) 0, DataMenuType.SIZE, "Size", "Slime", "MagmaCube"),
	BABY(Material.WHEAT, 1, (short) 0, DataMenuType.BOOLEAN, "Baby", "PigZombie", "Zombie", "Chicken", "Cow", "Horse", "MushroomCow", "Ocelot", "Pig", "PolarBear", "Sheep", "Wolf", "Villager"),
	CAT_TYPE(Material.TROPICAL_FISH, 1, (short) 0, DataMenuType.CAT_TYPE, "Cat Type", "Cat"),
	OCELOT_TYPE(Material.TROPICAL_FISH, 1, (short) 0, DataMenuType.OCELOT_TYPE, "Ocelot Type", "Ocelot"),
	ANGRY(Material.BONE, 1, (short) 0, DataMenuType.BOOLEAN, "Angry", "Wolf"),
	TAMED(Material.BONE, 1, (short) 0, DataMenuType.BOOLEAN, "Tamed", "Wolf"),
	ZOMBIE_PROFESSION(Material.EMERALD, 1, (short) 0, DataMenuType.ZOMBIE_PROFESSION, "Profession", "Zombie"),
	ELDER(Material.SEA_LANTERN, 1, (short) 0, DataMenuType.BOOLEAN, "Elder", new Version("1.10-R1"), "Guardian"),
	COLOR(Material.WHITE_WOOL, 1, (short) 0, DataMenuType.COLOR, "Color", "Sheep", "Wolf", "Cat"),
	PROFESSION(Material.IRON_AXE, 1, (short) 0, DataMenuType.PROFESSION, "Profession", "Villager", "Zombie"),
	STANDING_UP(Material.TROPICAL_FISH, 1, (short) 0, DataMenuType.BOOLEAN, "Standing Up", "PolarBear"),
	SKELETON_TYPE(Material.BONE, 1, (short) 0, DataMenuType.SKELETON_TYPE, "Skeleton Type", "Skeleton"),
	LLAMA_VARIANT(Material.LEATHER, 1, (short) 0, DataMenuType.LLAMA_VARIANT, "Llama Variant", "Llama"),
	LLAMA_COLOR(Material.WHITE_WOOL, 1, (short) 0, DataMenuType.LLAMA_COLOR, "Color", "Llama"),
	OPEN(Material.OAK_TRAPDOOR, 1, (short) 0, DataMenuType.BOOLEAN, "Open", "Shulker"),
	PARROT_VARIANT(Material.WHITE_WOOL, 1, (short) 4, DataMenuType.PARROT_VARIANT, "Variant", "Parrot"),
	LEFT_SHOULDER(Material.SADDLE, 1, (short) 0, DataMenuType.BOOLEAN, "Left Shoulder", "Put a pet on your Left Shoulder"),
	RIGHT_SHOULDER(Material.SADDLE, 1, (short) 0, DataMenuType.BOOLEAN, "Right Shoulder", "Put a pet on your Right Shoulder"),
	RIDE(Material.CARROT_ON_A_STICK, 1, (short) 0, DataMenuType.BOOLEAN, "Ride Pet", "Control your pet."),
	HAT(Material.IRON_HELMET, 1, (short) 0, DataMenuType.BOOLEAN, "Hat Pet", "Wear your pet on your head.");

	private Material material;
	private String name;
	private int amount;
	private List<String> lore;
	private short data;
	private DataMenuType menuType;
	private Version version;

	MenuItem(Material material, int amount, short data, DataMenuType menuType, String name, String... lore){
		this(material, amount, data, menuType, name, new Version(), lore);
	}

	MenuItem(Material material, int amount, short data, DataMenuType menuType, String name, Version version, String... lore){
		this.material = material;
		this.name = name;
		this.amount = amount;
		this.data = data;
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList(lore));
		this.lore = list;
		this.menuType = menuType;
		this.version = version;
	}

	public ItemStack getItem(){
		ItemStack i = new ItemStack(material, this.amount, this.data);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.RED + this.name);
		meta.setLore(this.lore);
		i.setItemMeta(meta);
		return i;
	}

	public ItemStack getBoolean(boolean flag){
		ItemStack i = new ItemStack(material, this.amount, this.data);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.RED + this.name + (flag ? ChatColor.GREEN + " [TOGGLE ON]" : ChatColor.YELLOW + " [TOGGLE OFF]"));
		meta.setLore(this.lore);
		i.setItemMeta(meta);
		return i;
	}

	public DataMenuType getMenuType(){
		return this.menuType;
	}

	public boolean isSupported(){
		return version.isSupported(new Version());
	}
}