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

import java.util.ArrayList;

import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseAbstractPet;
import com.dsh105.echopet.compat.api.util.menu.MenuItem;
import com.dsh105.echopet.compat.api.util.menu.MenuOption;

public class MenuUtil {

	public static ArrayList<MenuOption> createOptionList(IPet pet){
		PetType pt = pet.getPetType();
        ArrayList<MenuOption> options = new ArrayList<MenuOption>();
        int i = 0;
        options.add(new MenuOption(i++, MenuItem.HAT));
        options.add(new MenuOption(i++, MenuItem.RIDE));
        if (pt == PetType.BLAZE) {
            options.add(new MenuOption(i++, MenuItem.FIRE));
        }
		if(pt == PetType.CAT){
			options.add(new MenuOption(i++, MenuItem.BABY));
			options.add(new MenuOption(i++, MenuItem.CAT_TYPE));
			options.add(new MenuOption(i++, MenuItem.COLOR));
		}
        if (pt == PetType.CREEPER) {
            options.add(new MenuOption(i++, MenuItem.POWER));
        }
        if (pt == PetType.ENDERMAN) {
            options.add(new MenuOption(i++, MenuItem.SCREAMING));
        }
        if (pt == PetType.MAGMACUBE) {
            options.add(new MenuOption(i++, MenuItem.SIZE));
        }
        if (pt == PetType.PIGZOMBIE) {
            options.add(new MenuOption(i++, MenuItem.BABY));
        }
        if (pt == PetType.SKELETON) {
			if(new Version("1.10-R1").isSupported(new Version())){
				options.add(new MenuOption(i++, MenuItem.SKELETON_TYPE));
			}
        }
        if (pt == PetType.SLIME) {
            options.add(new MenuOption(i++, MenuItem.SIZE));
        }
        if (pt == PetType.WITCH) {
            options.add(new MenuOption(i++, MenuItem.POTION));
        }
        if (pt == PetType.WITHER) {
            options.add(new MenuOption(i++, MenuItem.SHIELD));
        }
		if(pt == PetType.ZOMBIE || pt == PetType.ZOMBIEVILLAGER){
            options.add(new MenuOption(i++, MenuItem.BABY));
			if(pt == PetType.ZOMBIEVILLAGER){
				options.add(new MenuOption(i++, MenuItem.ZOMBIE_PROFESSION));
			}else if(new Version("1.10-R1").isSupported(new Version())){
				options.add(new MenuOption(i++, MenuItem.ZOMBIE_PROFESSION));
			}
        }
		if(pt == PetType.HUSK){
			options.add(new MenuOption(i++, MenuItem.BABY));
		}
        if (pt == PetType.CHICKEN) {
            options.add(new MenuOption(i++, MenuItem.BABY));
        }
        if (pt == PetType.COW) {
            options.add(new MenuOption(i++, MenuItem.BABY));
        }
        if (pt == PetType.MUSHROOMCOW) {
            options.add(new MenuOption(i++, MenuItem.BABY));
        }
        if (pt == PetType.OCELOT) {
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.OCELOT_TYPE));
        }
        if (pt == PetType.PIG) {
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.SADDLE));
        }
		if(pt == PetType.POLARBEAR){
			options.add(new MenuOption(i++, MenuItem.BABY));
			options.add(new MenuOption(i++, MenuItem.STANDING_UP));
		}
		if(pt == PetType.SHEEP || pt == PetType.SNOWMAN){
			if(pt == PetType.SHEEP){
				options.add(new MenuOption(i++, MenuItem.BABY));
				options.add(new MenuOption(i++, MenuItem.COLOR));
			}
            options.add(new MenuOption(i++, MenuItem.SHEARED));
        }
        if (pt == PetType.WOLF) {
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.TAMED));
            options.add(new MenuOption(i++, MenuItem.ANGRY));
            options.add(new MenuOption(i++, MenuItem.COLOR));
        }
        if (pt == PetType.VILLAGER) {
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.PROFESSION));
        }
		if(pt == PetType.HORSE || pt == PetType.DONKEY || pt == PetType.MULE || pt == PetType.SKELETONHORSE || pt == PetType.ZOMBIEHORSE){
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.SADDLE));
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
        if (pt == PetType.GUARDIAN) {
            options.add(new MenuOption(i++, MenuItem.ELDER));
        }
        if (pt == PetType.RABBIT) {
            options.add(new MenuOption(i++, MenuItem.BABY));
            options.add(new MenuOption(i++, MenuItem.RABBIT_TYPE));
        }
		if(pt == PetType.LLAMA){
			options.add(new MenuOption(i++, MenuItem.BABY));
			options.add(new MenuOption(i++, MenuItem.CHESTED));
			options.add(new MenuOption(i++, MenuItem.LLAMA_COLOR));
			options.add(new MenuOption(i++, MenuItem.LLAMA_VARIANT));
		}
		if(pt == PetType.VEX){
			options.add(new MenuOption(i++, MenuItem.POWER));
		}
		if(pt == PetType.SHULKER){
			options.add(new MenuOption(i++, MenuItem.OPEN));
			// doesn't appear to work
			/*if(new Version("1.11-R1").isCompatible(new Version())){
				options.add(new MenuOption(i++, MenuItem.COLOR));
			}*/
		}
		if(pt == PetType.PARROT){
			options.add(new MenuOption(i++, MenuItem.PARROT_VARIANT));
			options.add(new MenuOption(i++, MenuItem.LEFT_SHOULDER));
			options.add(new MenuOption(i++, MenuItem.RIGHT_SHOULDER));
		}
        return options;
    }
}
