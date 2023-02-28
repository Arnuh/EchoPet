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

package com.dsh105.echopet.compat.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.common.collect.BiMap;

public class MiscUtil{
	
	public static <K, V> K getKeyAtValue(Map<K, V> map, V value){
		if(map instanceof BiMap){
			return ((BiMap<K, V>) map).inverse().get(value);
		}
		for(Map.Entry<K, V> entry : map.entrySet()){
			if(entry.getValue().equals(value)){
				return entry.getKey();
			}
		}
		return null;
	}
	
	
	public static int getPageCount(int size, int perPage){
		return (int) Math.ceil((double) size / perPage);
	}
	
	public static <T> List<T> getPage(List<T> data, int perPage, int pageNumber){
		if(pageNumber > getPageCount(data.size(), perPage)){
			throw new IllegalArgumentException("Page does not exist!");
		}else{
			int index = perPage * (Math.abs(pageNumber) - 1);
			List<T> list = new ArrayList<>();
			// Just sublist this? Idk, just copypasted so everything works.
			for(int i = index; i < index + perPage; ++i){
				if(data.size() > i){
					list.add(data.get(i));
				}
			}
			return list;
		}
	}
}
