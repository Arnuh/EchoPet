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
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVexPet;
import com.dsh105.echopet.nms.entity.EntityNoClipPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntityPetType(petType = PetType.VEX)
public class EntityVexPet extends EntityNoClipPet implements IEntityVexPet{
	
	protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(EntityVexPet.class, EntityDataSerializers.BYTE);
	// Has the ability to have multiple settings.. but it seems to only use 1 for 'charged' which is 'attack mode'
	
	public EntityVexPet(Level world){
		super(EntityType.VEX, world);
	}
	
	public EntityVexPet(Level world, IPet pet){
		super(EntityType.VEX, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		entityData.define(DATA_FLAGS_ID, (byte) 0);
	}
	
	private void setVexFlag(int i, boolean flag){
		byte b0 = this.entityData.get(DATA_FLAGS_ID);
		int j;
		if(flag){
			j = b0 | i;
		}else{
			j = b0 & ~i;
		}
		this.entityData.set(DATA_FLAGS_ID, (byte) (j & 0xFF));
	}
	
	public boolean isCharging(){
		return getVexFlag(1);
	}
	
	private boolean getVexFlag(int i){// just check EntityVex
		byte b0 = this.entityData.get(DATA_FLAGS_ID);
		return (b0 & i) != 0;
	}
	
	@Override
	public void setIsCharging(boolean flag){
		setVexFlag(1, flag);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
}
