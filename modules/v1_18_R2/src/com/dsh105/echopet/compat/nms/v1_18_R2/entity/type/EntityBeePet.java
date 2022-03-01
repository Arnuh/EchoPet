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
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityBeePet;
import com.dsh105.echopet.compat.nms.v1_18_R2.entity.EntityAgeablePet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.7F, height = 0.6F)
@EntityPetType(petType = PetType.BEE)
public class EntityBeePet extends EntityAgeablePet implements IEntityBeePet{
	
	private static final int Rolling = 0x2, Stung = 0x4, Nectar = 0x8;
	private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(EntityBeePet.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(EntityBeePet.class, EntityDataSerializers.INT);
	
	public EntityBeePet(Level world){
		super(EntityType.BEE, world);
	}
	
	public EntityBeePet(Level world, IPet pet){
		super(EntityType.BEE, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_FLAGS_ID, (byte) 0);
		this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
	}
	
	@Override
	public void setHasStung(boolean hasStung){
		if(hasStung) addFlag(Stung);
		else removeFlag(Stung);
	}
	
	@Override
	public void setHasNectar(boolean hasNectar){
		if(hasNectar) addFlag(Nectar);
		else removeFlag(Nectar);
	}
	
	@Override
	public void setAngry(boolean angry){
		entityData.set(DATA_REMAINING_ANGER_TIME, angry ? 1 : 0);
	}
	
	private void addFlag(int flag){
		entityData.set(DATA_FLAGS_ID, (byte) (getFlag() | flag));
	}
	
	private void removeFlag(int flag){
		entityData.set(DATA_FLAGS_ID, (byte) (getFlag() & ~flag));
	}
	
	public int getFlag(){
		return entityData.get(DATA_FLAGS_ID);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
}