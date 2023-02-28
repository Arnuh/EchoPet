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
import com.dsh105.echopet.compat.api.entity.data.type.ParrotVariant;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityParrotPet;
import com.dsh105.echopet.nms.entity.EntityTameablePet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntityPetType(petType = PetType.PARROT)
public class EntityParrotPet extends EntityTameablePet implements IEntityParrotPet{
	
	private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(EntityParrotPet.class, EntityDataSerializers.INT);
	
	public EntityParrotPet(Level world){
		super(EntityType.PARROT, world);
	}
	
	public EntityParrotPet(Level world, IPet pet){
		super(EntityType.PARROT, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_VARIANT_ID, 0);
	}
	
	public int getVariant(){
		return Mth.clamp(this.entityData.get(DATA_VARIANT_ID), 0, 4);
	}
	
	@Override
	public void setVariant(ParrotVariant variant){
		this.entityData.set(DATA_VARIANT_ID, variant.ordinal());
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound){
		super.addAdditionalSaveData(nbttagcompound);
		nbttagcompound.putInt("Variant", this.getVariant());
	}
}
