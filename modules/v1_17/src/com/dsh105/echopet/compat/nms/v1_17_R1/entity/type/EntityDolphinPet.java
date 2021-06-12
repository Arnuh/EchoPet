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
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityDolphinPet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.9F, height = 0.6F)
@EntityPetType(petType = PetType.COD)
public class EntityDolphinPet extends EntityWaterAnimalPet implements IEntityDolphinPet{
	
	private static final EntityDataAccessor<BlockPos> b = SynchedEntityData.defineId(EntityDolphinPet.class, EntityDataSerializers.BLOCK_POS);// "TreasurePos" - Some target to swim to.
	private static final EntityDataAccessor<Boolean> c = SynchedEntityData.defineId(EntityDolphinPet.class, EntityDataSerializers.BOOLEAN);// "GotFish" - Used for the pathfinder goal to go to "TreasurePog"
	private static final EntityDataAccessor<Integer> bC = SynchedEntityData.defineId(EntityDolphinPet.class, EntityDataSerializers.INT);// "Moistness" - Takes damage when < 0.
	
	public EntityDolphinPet(Level world){
		super(EntityType.DOLPHIN, world);
	}
	
	public EntityDolphinPet(Level world, IPet pet){
		super(EntityType.DOLPHIN, world, pet);
	}
	
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(b, BlockPos.ZERO);
		this.entityData.define(c, Boolean.valueOf(false));
		this.entityData.define(bC, Integer.valueOf(2400));
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
