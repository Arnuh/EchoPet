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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySnowmanPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.4F, height = 1.8F)
@EntityPetType(petType = PetType.SNOWMAN)
public class EntitySnowGolemPet extends EntityPet implements IEntitySnowmanPet{
	
	private static final EntityDataAccessor<Byte> DATA_PUMPKIN_ID = SynchedEntityData.defineId(EntitySnowGolemPet.class, EntityDataSerializers.BYTE);
	
	public EntitySnowGolemPet(Level world){
		super(EntityType.SNOW_GOLEM, world);
	}
	
	public EntitySnowGolemPet(Level world, IPet pet){
		super(EntityType.SNOW_GOLEM, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_PUMPKIN_ID, (byte) 16);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
	
	@Override
	public void setSheared(boolean flag){
		byte b0 = this.entityData.get(DATA_PUMPKIN_ID);
		if(!flag){
			this.entityData.set(DATA_PUMPKIN_ID, (byte) (b0 | 0x10));
		}else{
			this.entityData.set(DATA_PUMPKIN_ID, (byte) (b0 & -0x11));
		}
	}
}
