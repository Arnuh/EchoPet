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


import java.util.function.Function;
import com.dsh105.echopet.compat.api.entity.IPetType;

/**
 * A config entry for a specific pet type.
 */
public class PetConfigEntry<T>{
	
	// private final Class<T> valueType;
	private final boolean isNumber;
	public final String configKey;
	public final Function<IPetType, T> defaultValue;
	public final String[] comments;
	
	
	public PetConfigEntry(Class<T> valueType, String configKey, T defaultValue, String... comments){
		this(valueType, configKey, petType->defaultValue, comments);
	}
	
	public PetConfigEntry(Class<T> valueType, String configKey, Function<IPetType, T> defaultValue, String... comments){
		// this.valueType = valueType;
		this.isNumber = Number.class.isAssignableFrom(valueType);
		this.configKey = configKey;
		this.defaultValue = defaultValue;
		this.comments = comments;
	}
	
	public String getConfigKey(){
		return this.configKey;
	}
	
	public T getDefaultValue(IPetType petType){
		return this.defaultValue.apply(petType);
	}
	
	public String[] getComments(){
		return this.comments;
	}
	
	public T get(IPetType petType){
		return petType.getConfigValue(getConfigKey(), getDefaultValue(petType));
	}
	
	public Number getNumber(IPetType petType){
		if(!isNumber){
			throw new IllegalStateException();
		}
		return (Number) petType.getRawConfigValue(getConfigKey(), getDefaultValue(petType));
	}
}
