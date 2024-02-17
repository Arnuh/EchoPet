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

package com.dsh105.echopet.compat.api.entity.data;

import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Function;

public interface PetDataParser<T>{
	
	T parse(String input);
	
	T defaultValue(IPetType petType);
	
	/**
	 * For setting an initial default value in configs.
	 */
	default T configDefaultValue(IPetType petType){
		return defaultValue(petType);
	}
	
	T interact(@Nullable T current, ItemStack item);
	
	PetDataParser<T> createNew(PetData<T> data);
	
	class BooleanParser implements PetDataParser<Boolean>{
		
		private final PetData<Boolean> data;
		
		public BooleanParser(PetData<Boolean> data){this.data = data;}
		
		@Override
		public Boolean parse(String input){
			return input != null && input.equalsIgnoreCase("true") || input.equalsIgnoreCase("1");
		}
		
		@Override
		public Boolean defaultValue(IPetType petType){
			return petType.getDataDefaultValue(data, true);
		}
		
		@Override
		public Boolean interact(Boolean current, ItemStack item){
			return current == null || !current;
		}
		
		@Override
		public PetDataParser<Boolean> createNew(PetData<Boolean> data){
			return new BooleanParser(data);
		}
	}
	
	static Function<PetData<Boolean>, PetDataParser<Boolean>> booleanParser(){
		return BooleanParser::new;
	}
	
	class ByteParser implements PetDataParser<Byte>{
		
		private final PetData<Byte> data;
		
		public ByteParser(PetData<Byte> data){this.data = data;}
		
		@Override
		public Byte parse(String input){
			return Byte.parseByte(input);
		}
		
		@Override
		public Byte defaultValue(IPetType petType){
			return ((Number) petType.getDataDefaultValue(data, (byte) 0)).byteValue();
		}
		
		@Override
		public Byte interact(Byte current, ItemStack item){
			return null;
		}
		
		@Override
		public PetDataParser<Byte> createNew(PetData<Byte> data){
			return new ByteParser(data);
		}
	}
	
	static Function<PetData<Byte>, PetDataParser<Byte>> byteParser(){
		return ByteParser::new;
	}
	
	class IntegerParser implements PetDataParser<Integer>{
		
		private final PetData<Integer> data;
		
		public IntegerParser(PetData<Integer> data){this.data = data;}
		
		@Override
		public Integer parse(String input){
			return Integer.parseInt(input);
		}
		
		@Override
		public Integer defaultValue(IPetType petType){
			return petType.getDataDefaultValue(data, 0);
		}
		
		@Override
		public Integer interact(Integer current, ItemStack item){
			return null;
		}
		
		@Override
		public PetDataParser<Integer> createNew(PetData<Integer> data){
			return new IntegerParser(data);
		}
	}
	
	static Function<PetData<Integer>, PetDataParser<Integer>> integerParser(){
		return IntegerParser::new;
	}
	
	class DoubleParser implements PetDataParser<Double>{
		
		private final PetData<Double> data;
		private final Double defaultValue;
		
		public DoubleParser(PetData<Double> data, Double defaultValue){
			this.data = data;
			this.defaultValue = defaultValue;
		}
		
		@Override
		public Double parse(String input){
			return Double.parseDouble(input);
		}
		
		@Override
		public Double defaultValue(IPetType petType){
			return petType.getDataDefaultValue(data, defaultValue);
		}
		
		@Override
		public Double interact(Double current, ItemStack item){
			return null;
		}
		
		@Override
		public PetDataParser<Double> createNew(PetData<Double> data){
			return new DoubleParser(data, defaultValue);
		}
	}
	
	static Function<PetData<Double>, PetDataParser<Double>> doubleParser(double defaultValue){
		return data->new DoubleParser(data, defaultValue);
	}
	
	static Function<PetData<Double>, PetDataParser<Double>> doubleParser(double defaultValue, String attributeKey){
		return data->new DoubleParser(data, defaultValue){
			@Override
			public Double configDefaultValue(IPetType petType){
				return EchoPet.getPlugin().getSpawnUtil().getAttribute(petType, attributeKey);
			}
		};
	}
}
