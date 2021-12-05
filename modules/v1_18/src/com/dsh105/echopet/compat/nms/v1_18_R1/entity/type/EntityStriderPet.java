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

package com.dsh105.echopet.compat.nms.v1_18_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityStriderPet;
import com.dsh105.echopet.compat.nms.v1_18_R1.entity.EntityAgeablePet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.9F, height = 1.7F)
@EntityPetType(petType = PetType.STRIDER)
public class EntityStriderPet extends EntityAgeablePet implements IEntityStriderPet{
	
	private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(EntityStriderPet.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_SUFFOCATING = SynchedEntityData.defineId(EntityStriderPet.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID = SynchedEntityData.defineId(EntityStriderPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityStriderPet(Level world){
		super(EntityType.STRIDER, world);
	}
	
	public EntityStriderPet(Level world, IPet pet){
		super(EntityType.STRIDER, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_BOOST_TIME, 0);
		this.entityData.define(DATA_SUFFOCATING, false);
		this.entityData.define(DATA_SADDLE_ID, false);
	}
	
	@Override
	public void setHasSaddle(boolean flag){
		entityData.set(DATA_SADDLE_ID, flag);
	}
}