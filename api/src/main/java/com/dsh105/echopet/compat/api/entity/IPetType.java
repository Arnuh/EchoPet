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

import com.dsh105.echopet.compat.api.config.YAMLConfig;
import com.dsh105.echopet.compat.api.entity.data.CategorizedPetData;
import com.dsh105.echopet.compat.api.entity.data.PetData;
import com.dsh105.echopet.compat.api.entity.data.PetDataCategory;
import com.dsh105.echopet.compat.api.entity.nms.IEntityPet;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.util.Version;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public interface IPetType{
	
	String name();
	
	String getDefaultName();
	
	String getDefaultName(String name);
	
	String getMinecraftName();
	
	List<PetDataCategory> getAllowedCategories();
	
	List<PetData<?>> getAllowedDataTypes();
	
	/**
	 * If the specified PetData is in the pets {@link #getAllowedCategories()} or {@link #getAllowedDataTypes()}.<br>
	 * This method does not check if such PetData is enabled in the pet config. For that use {@link #isDataAllowed(PetData)}
	 *
	 * @return true if the pet data is an allowed category or data type.
	 */
	default boolean isValidData(PetData<?> data){
		for(PetDataCategory category : getAllowedCategories()){
			for(CategorizedPetData<?> d : category.getData()){
				if(d == data || d.getData().equals(data)) return true;
			}
		}
		return getAllowedDataTypes().contains(data);
	}
	
	IEntityPet getNewEntityPetInstance(Object world, IPet pet);
	
	IPet getNewPetInstance(Player owner);
	
	Class<? extends IEntityPet> getEntityClass();
	
	Class<? extends IPet> getPetClass();
	
	Version getVersion();
	
	boolean isCompatible();
	
	Material getUIMaterial();
	
	String getConfigKeyName();
	
	boolean isEnabled();
	
	boolean isTagVisible();
	
	boolean isInteractMenuEnabled();
	
	/**
	 * If other Pets are allowed to ride this Pet.<br>
	 * This method does not determine if the Pet Owner is allowed to ride.
	 *
	 * @return If other Pets are allowed to ride this Pet
	 */
	boolean allowRidersFor();
	
	boolean isDataAllowed(PetData<?> data);
	
	boolean isDataForced(PetData<?> data);
	
	YAMLConfig getConfig();
	
	Object getRawConfigValue(String variable, Object defaultValue);
	
	<T> T getConfigValue(String variable, T defaultValue);
	
	ConfigurationSection getPetDataSection(PetData<?> data);
	
	<T> T getPetDataProperty(PetData<?> data, String variable, T defaultValue);
	
	<T> T getDataDefaultValue(PetData<?> data, T defaultValue);
}
