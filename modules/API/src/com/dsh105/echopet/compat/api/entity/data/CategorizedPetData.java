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

import java.util.List;
import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.entity.IPet;
import org.bukkit.inventory.ItemStack;

public class CategorizedPetData<T> extends PetData<T>{
	
	private final PetData<T> data;
	private final PetDataCategory category;
	private final boolean useCategorizedKey;
	
	CategorizedPetData(PetData<T> copy, PetDataCategory category, boolean useCategorizedKey){
		super(copy.getAction(), copy.parserFunction);
		this.data = copy;
		this.category = category;
		this.useCategorizedKey = useCategorizedKey;
	}
	
	public PetData<T> getData(){
		return data;
	}
	
	public PetDataCategory getCategory(){
		return category;
	}
	
	@Override
	public String getDefaultName(){
		return data.getDefaultName();
	}
	
	@Nullable
	@Override
	public PetDataMaterial getMaterial(){
		return data.getMaterial();
	}
	
	@Override
	public List<String> getDefaultLore(){
		return data.getDefaultLore();
	}
	
	@Nullable
	@Override
	public ItemStack toItem(IPet pet){
		return super.toItem(pet);
	}
	
	@Override
	public boolean isCompatible(){
		return data.isCompatible();
	}
	
	@Override
	public String getConfigKeyName(){
		if(!useCategorizedKey) return data.getConfigKeyName();
		return category.getPermissionKey() + "_" + data.getConfigKeyName();
	}
	
	@Override
	public String toString(){
		if(!useCategorizedKey) return data.toString();
		return category.toString() + "_" + data.toString();
	}
}
