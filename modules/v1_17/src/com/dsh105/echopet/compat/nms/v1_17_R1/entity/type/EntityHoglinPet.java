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
package com.dsh105.echopet.compat.nms.v1_17_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHoglinPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityAgeablePet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 1.3965F, height = 1.4F)
@EntityPetType(petType = PetType.HOGLIN)
public class EntityHoglinPet extends EntityAgeablePet implements IEntityHoglinPet{
	
	private static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(EntityHoglinPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityHoglinPet(Level world){
		super(EntityType.HOGLIN, world);
	}
	
	public EntityHoglinPet(Level world, IPet pet){
		super(EntityType.HOGLIN, world, pet);
	}
	
	@Override
	public double getMovementSpeed(){
		return 0.30000001192092896D;
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_IMMUNE_TO_ZOMBIFICATION, true);//Default to true to fix shaking when in overworld.
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.LARGE;//large kinda too big but idk
	}
}