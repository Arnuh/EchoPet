/*
 * This file is part of EchoPet.
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.nms.v1_17_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityTurtlePet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityAgeablePet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 1.2F, height = 0.4F)
@EntityPetType(petType = PetType.TURTLE)
public class EntityTurtlePet extends EntityAgeablePet implements IEntityTurtlePet{
	
	private static final EntityDataAccessor<BlockPos> bD = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BLOCK_POS);// "HomePos" - beach they spawned at
	private static final EntityDataAccessor<Boolean> bE = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);// "HasEgg" If the turtle is carrying an egg.
	private static final EntityDataAccessor<Boolean> bG = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);// Set to false when egg is placed("HasEgg" to false right after)
	private static final EntityDataAccessor<BlockPos> bH = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BLOCK_POS);// "TravelPos"
	private static final EntityDataAccessor<Boolean> bI = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);// set in c() and d() of PathfinderGoal
	private static final EntityDataAccessor<Boolean> bJ = SynchedEntityData.defineId(EntityTurtlePet.class, EntityDataSerializers.BOOLEAN);// set to true when "TravelPos" is set to a position.
	
	public EntityTurtlePet(Level world){
		super(EntityType.TURTLE, world);
	}
	
	public EntityTurtlePet(Level world, IPet pet){
		super(EntityType.TURTLE, world, pet);
	}
	
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(bD, BlockPos.ZERO);
		this.entityData.define(bE, Boolean.valueOf(false));
		this.entityData.define(bH, BlockPos.ZERO);
		this.entityData.define(bI, Boolean.valueOf(false));
		this.entityData.define(bJ, Boolean.valueOf(false));
		this.entityData.define(bG, Boolean.valueOf(false));
	}
}
