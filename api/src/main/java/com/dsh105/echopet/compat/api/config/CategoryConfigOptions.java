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


import com.dsh105.echopet.compat.api.entity.data.PetData;
import com.dsh105.echopet.compat.api.entity.data.PetDataCategory;

import java.util.stream.Collectors;

public class CategoryConfigOptions extends Options{
	
	public CategoryConfigOptions(YAMLConfig config){
		super(config);
	}
	
	@Override
	public void setDefaults(){
		for(PetDataCategory category : PetDataCategory.values){
			if(!category.isCompatible()){
				continue;
			}
			String path = category.getConfigKey() + ".";
			if(getConfig().getConfigurationSection(path + "data") == null){
				set(path + "data", category.getDefaultData().stream()
					.map(PetData::getConfigKeyName)
					.collect(Collectors.toList()));
			}
			String itemPath = path + "item.";
			
			set(itemPath + "material", category.getDefaultMaterial().name());
			set(itemPath + "name", category.getDefaultName());
			if(!category.getDefaultLore().isEmpty()){
				set(itemPath + "lore", category.getDefaultLore());
			}
			category.load(false);
		}
	}
}
