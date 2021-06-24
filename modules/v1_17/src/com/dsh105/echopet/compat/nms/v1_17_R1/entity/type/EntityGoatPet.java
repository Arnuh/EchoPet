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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGoatPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityAgeablePet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@EntitySize(width = 1.3F, height = 0.9F)
@EntityPetType(petType = PetType.GOAT)
public class EntityGoatPet extends EntityAgeablePet implements IEntityGoatPet{
	
	private static final EntityDataAccessor<Boolean> DATA_IS_SCREAMING_GOAT = SynchedEntityData.defineId(EntityGoatPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityGoatPet(Level world){
		super(EntityType.GOAT, world);
	}
	
	public EntityGoatPet(Level world, IPet pet){
		super(EntityType.GOAT, world, pet);
	}
	
	@Override
	public double getMovementSpeed(){
		return 0.20000000298023224D;
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_IS_SCREAMING_GOAT, false);
	}
	
	public boolean isScreamingGoat(){
		return this.entityData.get(DATA_IS_SCREAMING_GOAT);
	}
	
	@Override
	public void setScreaming(boolean flag){
		this.entityData.set(DATA_IS_SCREAMING_GOAT, flag);
	}
	
	@Override
	protected SoundEvent getAmbientSound(){
		return this.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_AMBIENT : SoundEvents.GOAT_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damagesource){
		return this.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_HURT : SoundEvents.GOAT_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound(){
		return this.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_DEATH : SoundEvents.GOAT_DEATH;
	}
	
	@Override
	protected void playStepSound(BlockPos blockposition, BlockState iblockdata){
		this.playSound(SoundEvents.GOAT_STEP, 0.15F, 1.0F);
	}
	
	@Override
	public SoundEvent getEatingSound(ItemStack itemstack){
		return this.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_EAT : SoundEvents.GOAT_EAT;
	}
}
