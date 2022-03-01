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

package com.dsh105.echopet.compat.nms.v1_18_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPigPet;
import com.dsh105.echopet.compat.nms.v1_18_R2.entity.EntityAgeablePet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.9F, height = 0.9F)
@EntityPetType(petType = PetType.PIG)
public class EntityPigPet extends EntityAgeablePet implements IEntityPigPet{
	
	private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID = SynchedEntityData.defineId(EntityPigPet.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(EntityPigPet.class, EntityDataSerializers.INT);
	
	public EntityPigPet(Level world){
		super(EntityType.PIG, world);
	}
	
	public EntityPigPet(Level world, IPet pet){
		super(EntityType.PIG, world, pet);
	}
	
	public boolean hasSaddle(){
		return this.entityData.get(DATA_SADDLE_ID);
	}
	
	@Override
	public void setSaddled(boolean flag){
		this.entityData.set(DATA_SADDLE_ID, flag);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_SADDLE_ID, Boolean.FALSE);
		this.entityData.define(DATA_BOOST_TIME, 0);
	}
}
