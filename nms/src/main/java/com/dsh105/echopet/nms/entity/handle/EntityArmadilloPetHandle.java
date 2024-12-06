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

package com.dsh105.echopet.nms.entity.handle;

import com.dsh105.echopet.compat.api.entity.nms.IEntityAnimalPet;
import com.dsh105.echopet.compat.api.entity.type.nms.handle.IEntityArmadilloPetHandle;
import com.dsh105.echopet.compat.api.entity.type.pet.IArmadilloPet;
import net.minecraft.world.entity.animal.armadillo.Armadillo;

public class EntityArmadilloPetHandle extends EntityAnimalPetHandle implements IEntityArmadilloPetHandle{
	
	public EntityArmadilloPetHandle(IEntityAnimalPet entityPet){
		super(entityPet);
	}
	
	@Override
	public Armadillo get(){
		return (Armadillo) getEntity();
	}
	
	@Override
	public void switchToState(IArmadilloPet.ArmadilloState state){
		get().switchToState(Armadillo.ArmadilloState.fromName(state.name().toLowerCase()));
	}
	
	@Override
	public IArmadilloPet.ArmadilloState getState(){
		return IArmadilloPet.ArmadilloState.valueOf(get().getState().name());
	}
}
