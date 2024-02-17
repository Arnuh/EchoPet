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

package com.dsh105.echopet.compat.api.plugin;

import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.data.PetData;
import com.dsh105.echopet.compat.api.entity.data.PetDataCategory;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.plugin.action.ActionChain;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map;

public interface IPetManager{
	
	ArrayList<IPet> getPets();
	
	ActionChain<IPet> loadPets(Player player, SavedType savedType, boolean sendMessage, boolean checkWorldOverride);
	
	void removeAllPets();
	
	@Nullable
	IPet createPet(Player owner, IPetType petType, boolean sendMessageOnFail);
	
	@Nullable
	IPet createPet(Player owner, IPetType petType, IPetType riderType);
	
	IPet getPet(Player player);
	
	IPet getPet(Entity pet);
	
	void forceAllValidData(IPet pet);
	
	void removePets(Player player, boolean makeDeathSound);
	
	void removePet(IPet pet, boolean makeDeathSound);
	
	void setData(IPet pet, Map<PetData<?>, Object> data);
	
	/**
	 * Modify a pets data.<br>
	 * If the {@code value} is null, the data will be removed by {@link #removeData(IPet, PetData)}
	 */
	void setData(IPet pet, PetData<?> pd, @Nullable Object value);
	
	/**
	 *
	 * @return If the {@link PetData} was removed.
	 */
	boolean removeData(IPet pet, PetData<?> data);
	
	/**
	 * Execute the {@link PetData} action for the given {@link PetData} and value.<br>
	 * The value for the provided {@link PetData} will be retrieved from the {@link IPet#getData()} map.
	 * <br>
	 * No guarantee exists if the {@link PetData} is valid for the given {@link IPet} and thus if the action execution will apply a change.
	 */
	void executePetDataAction(Player player, IPet pet, PetData<?> data);
	
	/**
	 * Execute the {@link PetData} action for the given {@link PetData} and value.<br>
	 * <br>
	 * No guarantee exists if the {@link PetData} is valid for the given {@link IPet} and thus if the action execution will apply a change.
	 */
	void executePetDataAction(Player player, IPet pet, PetData<?> data, Object value);
	
	/**
	 * Execute the {@link PetData} action for the given {@link PetData} and value.<br>
	 * <br>
	 * No guarantee exists if the {@link PetData} is valid for the given {@link IPet} and thus if the action execution will apply a change.
	 */
	void executePetDataAction(Player player, IPet pet, PetDataCategory category, PetData<?> data, Object value);
}