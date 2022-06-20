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

package com.dsh105.echopet.nms.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityTurtlePet;
import com.dsh105.echopet.nms.entity.EntityAgeablePet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 1.2F, height = 0.4F)
@EntityPetType(petType = PetType.TURTLE)
public class EntityTurtlePet extends EntityAgeablePet implements IEntityTurtlePet{
	
	//beach they spawned at
	private static final EntityDataAccessor<BlockPos> HOME_POS = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);
	// Set to false when egg is placed("HasEgg" to false right after)
	private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<BlockPos> TRAVEL_POS = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<Boolean> GOING_HOME = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> TRAVELLING = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityTurtlePet(Level world){
		super(EntityType.TURTLE, world);
	}
	
	public EntityTurtlePet(Level world, IPet pet){
		super(EntityType.TURTLE, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(HOME_POS, BlockPos.ZERO);
		this.entityData.define(HAS_EGG, false);
		this.entityData.define(TRAVEL_POS, BlockPos.ZERO);
		this.entityData.define(GOING_HOME, false);
		this.entityData.define(TRAVELLING, false);
		this.entityData.define(LAYING_EGG, false);
	}
}
