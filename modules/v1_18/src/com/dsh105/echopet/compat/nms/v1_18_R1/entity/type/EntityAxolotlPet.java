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

import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityAxolotlPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IAxolotlPet;
import com.dsh105.echopet.compat.nms.v1_18_R1.entity.EntityAgeablePet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 1.3F, height = 0.6F)
@EntityPetType(petType = PetType.AXOLOTL)
public class EntityAxolotlPet extends EntityAgeablePet implements IEntityAxolotlPet{
	
	private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(EntityAxolotlPet.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_PLAYING_DEAD = SynchedEntityData.defineId(EntityAxolotlPet.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityAxolotlPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityAxolotlPet(Level world){
		super(EntityType.AXOLOTL, world);
	}
	
	public EntityAxolotlPet(Level world, IPet pet){
		super(EntityType.AXOLOTL, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_VARIANT, IAxolotlPet.Variant.Lucy.ordinal());
		this.entityData.define(DATA_PLAYING_DEAD, false);
		this.entityData.define(FROM_BUCKET, false);
	}
	
	@Override
	public void setVariant(IAxolotlPet.Variant variant){
		this.entityData.set(DATA_VARIANT, variant.ordinal());
	}
	
	@Override
	public void setPlayingDead(boolean flag){
		this.entityData.set(DATA_PLAYING_DEAD, flag);
	}
	
	public boolean isPlayingDead(){
		return this.entityData.get(DATA_PLAYING_DEAD);
	}
	
	@Override
	@Nullable
	protected SoundEvent getDeathSound(){
		return SoundEvents.AXOLOTL_DEATH;
	}
	
	@Override
	@Nullable
	protected SoundEvent getAmbientSound(){
		return this.isInWater() ? SoundEvents.AXOLOTL_IDLE_WATER : SoundEvents.AXOLOTL_IDLE_AIR;
	}
	
	@Override
	protected SoundEvent getSwimSplashSound(){
		return SoundEvents.AXOLOTL_SPLASH;
	}
	
	@Override
	protected SoundEvent getSwimSound(){
		return SoundEvents.AXOLOTL_SWIM;
	}
}
