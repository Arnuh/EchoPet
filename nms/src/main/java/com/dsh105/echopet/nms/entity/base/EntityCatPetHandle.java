/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  EchoPet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.nms.entity.base;

import com.dsh105.echopet.compat.api.entity.data.type.CatType;
import com.dsh105.echopet.compat.api.entity.nms.IEntityTameablePet;
import com.dsh105.echopet.compat.api.entity.type.nms.handle.IEntityCatPetHandle;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.animal.Cat;
import org.bukkit.DyeColor;

public class EntityCatPetHandle extends EntityTameablePetHandle implements IEntityCatPetHandle{
	
	public EntityCatPetHandle(IEntityTameablePet entityPet){
		super(entityPet);
	}
	
	@Override
	public Cat get(){
		return (Cat) getEntity();
	}
	
	@Override
	public void setType(CatType type){
		get().setVariant(BuiltInRegistries.CAT_VARIANT.byId(type.ordinal()));
	}
	
	@Override
	public void setCollarColor(DyeColor color){
		get().setCollarColor(net.minecraft.world.item.DyeColor.byId(color.ordinal()));
	}
}
