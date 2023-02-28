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
import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityAgeablePetHandle;
import net.minecraft.world.entity.Mob;

public class EntityAgeablePetHandle extends LivingEntityPetHandle implements IEntityAgeablePetHandle{
	
	public EntityAgeablePetHandle(IEntityAgeablePet entityPet){
		super(entityPet);
	}
	
	public Mob get(){ // Need all ageable pets to extend AgeableMob before we can fix this
		return (Mob) getEntity();
	}
	
	@Override
	public void setBaby(boolean flag){
		get().setBaby(flag);
	}
}
