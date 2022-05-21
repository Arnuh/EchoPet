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

package com.dsh105.echopet.compat.api.config;


import com.dsh105.echopet.compat.api.entity.PetType;

/**
 * A config entry for a specific pet type.
 */
public class PetConfigEntry<T>{
	
	public final String configKey;
	public final T defaultValue;
	public final String[] comments;
	
	public PetConfigEntry(String configKey, T defaultValue, String... comments){
		this.configKey = configKey;
		this.defaultValue = defaultValue;
		this.comments = comments;
	}
	
	public String getConfigKey(){
		return this.configKey;
	}
	
	public T getDefaultValue(){
		return this.defaultValue;
	}
	
	public String[] getComments(){
		return this.comments;
	}
	
	public T getConfigValue(PetType type){
		return type.getConfigValue(getConfigKey(), getDefaultValue());
	}
}
