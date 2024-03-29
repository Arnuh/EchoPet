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

import java.util.Map;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.data.PetData;

public class PetStorage{
	
	public IPetType petType;
	public String petName;
	public Map<PetData<?>, Object> petDataList;
	public PetStorage rider;
	
	public PetStorage(IPetType petType, String petName, Map<PetData<?>, Object> petDataList){
		this.petDataList = petDataList;
		this.petType = petType;
		this.petName = petName;
	}
}
