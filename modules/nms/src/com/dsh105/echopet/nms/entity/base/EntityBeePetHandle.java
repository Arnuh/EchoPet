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

package com.dsh105.echopet.nms.entity.base;

import com.dsh105.echopet.compat.api.entity.nms.IEntityAgeablePet;
import com.dsh105.echopet.compat.api.entity.type.nms.handle.IEntityBeePetHandle;
import net.minecraft.world.entity.animal.Bee;

public class EntityBeePetHandle extends EntityAgeablePetHandle implements IEntityBeePetHandle{
	
	public EntityBeePetHandle(IEntityAgeablePet entityPet){
		super(entityPet);
	}
	
	@Override
	public Bee get(){
		return (Bee) getEntity();
	}
	
	@Override
	public void setHasStung(boolean hasStung){
		get().setHasStung(hasStung);
	}
	
	@Override
	public void setHasNectar(boolean hasNectar){
		get().setHasNectar(hasNectar);
	}
	
	@Override
	public void setAngry(boolean angry){
		get().setRemainingPersistentAngerTime(angry ? 1 : 0);
	}
}
