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

import org.bukkit.inventory.ItemStack;

public interface PetDataParser<T>{
	
	T parse(String input);
	
	T defaultValue();
	
	T interact(T current, ItemStack item);
	
	PetDataParser<Boolean> booleanParser = new PetDataParser<>(){
		@Override
		public Boolean parse(String input){
			return input.equalsIgnoreCase("true") || input.equalsIgnoreCase("1");
		}
		
		@Override
		public Boolean defaultValue(){
			return true;
		}
		
		@Override
		public Boolean interact(Boolean current, ItemStack item){
			return current == null || !current;
		}
	};
	
	/*PetDataParser<Integer> integerParser = new PetDataParser<>(){
		@Override
		public Integer parse(String input){
			return Integer.parseInt(input);
		}
		
		@Override
		public Integer defaultValue(){
			return 0;
		}
		
		@Override
		public Integer interact(Integer current, ItemStack item){
			return null;
		}
	};*/
}
