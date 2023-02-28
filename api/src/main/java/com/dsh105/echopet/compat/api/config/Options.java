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

import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.lang.Validate;

public abstract class Options{
	
	private HashMap<Option, Object> LOCKED = new HashMap<>();
	private HashMap<Option, Object[]> LOCKED_CONDITION = new HashMap<>();
	
	protected YAMLConfig config;
	
	public Options(YAMLConfig config){
		this.config = config;
		this.setDefaults();
		config.saveConfig();
	}
	
	public abstract void setDefaults();
	
	public YAMLConfig getConfig(){
		return this.config;
	}
	
	protected void set(String path, Object defObject, String... comments){
		this.config.set(path, this.config.get(path, defObject), comments);
	}
	
	public <T> void set(Option<T> option, String... pathReplacements){
		set(option.getPath(pathReplacements), get(option, pathReplacements), option.getComments());
	}
	
	public boolean isLocked(Option option){
		return LOCKED.containsKey(option);
	}
	
	public boolean isLocked(Option option, Object[] condition){
		return LOCKED.containsKey(option) && Arrays.equals(LOCKED_CONDITION.get(option), condition);
	}
	
	public <T> T getLockedValue(Option<T> option){
		return (T) LOCKED.get(option);
	}
	
	public <T> void lockValue(Option<T> option, T value){
		LOCKED.put(option, value);
	}
	
	public <T> void lockValue(Option<T> option, T value, Object... condition){
		Validate.notEmpty(condition, "Condition cannot be empty!");
		LOCKED.put(option, value);
		LOCKED_CONDITION.put(option, condition);
	}
	
	public <T> void unlockValue(Option<T> option, T value){
		LOCKED.put(option, value);
		if(LOCKED_CONDITION.containsKey(option)){
			LOCKED_CONDITION.remove(option);
		}
	}
	
	public <T> T get(Option<T> option, String... pathReplacements){
		return option.getValue(this.config.config(), pathReplacements);
	}
	
	public <T> T get(Option<T> option, T defaultValue, String... pathReplacements){
		return option.getValue(this.config.config(), defaultValue, pathReplacements);
	}
}