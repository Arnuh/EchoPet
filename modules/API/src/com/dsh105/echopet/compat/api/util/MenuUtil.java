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

package com.dsh105.echopet.compat.api.util;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.dsh105.echopet.compat.api.entity.IEntityAgeablePet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetDataCategory;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.google.common.collect.ImmutableList;

import net.md_5.bungee.api.ChatColor;

public class MenuUtil {

	public static final ItemStack BACK = new ItemStack(Material.BOOK), CLOSE = new ItemStack(Material.BOOK);
	public static final ItemStack BOOLEAN_TRUE = new ItemStack(Material.REDSTONE_TORCH), BOOLEAN_FALSE = new ItemStack(Material.REDSTONE_TORCH);
	static{
		ItemMeta meta = BACK.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Back");
		meta.setLore(ImmutableList.of(ChatColor.GOLD + "Return to the main menu."));
		BACK.setItemMeta(meta);
		meta = CLOSE.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Close");
		meta.setLore(ImmutableList.of(ChatColor.GOLD + "Close the Pet Menu"));
		CLOSE.setItemMeta(meta);
		//
		meta = BOOLEAN_TRUE.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "True");
		meta.setLore(ImmutableList.of(ChatColor.GOLD + "Turn the feature on."));
		BOOLEAN_TRUE.setItemMeta(meta);
		meta = BOOLEAN_FALSE.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "False");
		meta.setLore(ImmutableList.of(ChatColor.GOLD + "Turn the feature off."));
		BOOLEAN_FALSE.setItemMeta(meta);
	}

	public static List<Object> createOptionList(IPet pet){
		PetType pt = pet.getPetType();
		List<Object> options = new LinkedList<>();
		options.add(PetData.HAT);
		options.add(PetData.RIDE);
		if(pet.getEntityPet() instanceof IEntityAgeablePet){
			options.add(PetData.BABY);
		}
		for(PetData data : pt.getAllowedDataTypes()){
			options.add(data);
		}
		for(PetDataCategory data : pt.getAllowedCategories()){
			options.add(data);
		}
		/*
		if(pt == PetType.CAT){
			options.add(new MenuOption(i++, MenuItem.CAT_TYPE));
			options.add(new MenuOption(i++, MenuItem.COLOR));
		}
		if (pt == PetType.WITCH) {
		    options.add(new MenuOption(i++, MenuItem.POTION));
		}
		if(pt == PetType.HORSE || pt == PetType.DONKEY || pt == PetType.MULE || pt == PetType.SKELETONHORSE || pt == PetType.ZOMBIEHORSE){
			if(((IHorseAbstractPet) pet).getVariant() == HorseVariant.HORSE || new Version("1.10-R1").isSupported(new Version())){
				options.add(new MenuOption(i++, MenuItem.HORSE_TYPE));
				options.add(new MenuOption(i++, MenuItem.HORSE_ARMOUR));
				options.add(new MenuOption(i++, MenuItem.HORSE_VARIANT));
				options.add(new MenuOption(i++, MenuItem.HORSE_MARKING));
			}
			if(((IHorseAbstractPet) pet).getVariant() == HorseVariant.DONKEY || ((IHorseAbstractPet) pet).getVariant() == HorseVariant.MULE){
				options.add(new MenuOption(i++, MenuItem.CHESTED));
			}
		}
		if(pt == PetType.LLAMA){
			options.add(new MenuOption(i++, MenuItem.CHESTED));
			options.add(new MenuOption(i++, MenuItem.LLAMA_COLOR));
			options.add(new MenuOption(i++, MenuItem.LLAMA_VARIANT));
		}
		if(pt == PetType.PARROT){
			options.add(new MenuOption(i++, MenuItem.LEFT_SHOULDER));
			options.add(new MenuOption(i++, MenuItem.RIGHT_SHOULDER));
		}*/
        return options;
    }
}
