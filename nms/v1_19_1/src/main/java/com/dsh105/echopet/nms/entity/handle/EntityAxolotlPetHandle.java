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


import com.dsh105.echopet.compat.api.entity.nms.IEntityAgeablePet;
import com.dsh105.echopet.compat.api.entity.type.nms.handle.IEntityAxolotlPetHandle;
import com.dsh105.echopet.compat.api.entity.type.pet.IAxolotlPet;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

public class EntityAxolotlPetHandle extends EntityAgeablePetHandle implements IEntityAxolotlPetHandle{
	
	public EntityAxolotlPetHandle(IEntityAgeablePet entityPet){
		super(entityPet);
	}
	
	@Override
	public Axolotl get(){
		return (Axolotl) getEntity();
	}
	
	@Override
	public void setVariant(IAxolotlPet.Variant variant){
		get().setVariant(Axolotl.Variant.BY_ID[variant.ordinal()]);
	}
	
	@Override
	public void setPlayingDead(boolean flag){
		get().setPlayingDead(flag);
	}
}
