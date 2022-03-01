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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySkeletonHorsePet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 1.4F, height = 1.6F)
@EntityPetType(petType = PetType.SKELETONHORSE)
public class EntitySkeletonHorsePet extends EntityHorseAbstractPet implements IEntitySkeletonHorsePet{
	
	public EntitySkeletonHorsePet(Level world){
		super(EntityType.SKELETON_HORSE, world);
	}
	
	public EntitySkeletonHorsePet(Level world, IPet pet){
		super(EntityType.SKELETON_HORSE, world, pet);
	}
	
	@Override
	public boolean rideableUnderWater(){
		return true;
	}
	
	@Override
	protected float getWaterSlowDown(){
		return 0.96F;
	}
	
	@Override
	protected SoundEvent getAmbientSound(){
		super.getAmbientSound();
		return isEyeInFluid(FluidTags.WATER) ? SoundEvents.SKELETON_HORSE_AMBIENT_WATER : SoundEvents.SKELETON_HORSE_AMBIENT;
	}
	
	@Override
	protected SoundEvent getDeathSound(){
		super.getDeathSound();
		return SoundEvents.SKELETON_HORSE_DEATH;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource var0){
		super.getHurtSound(var0);
		return SoundEvents.SKELETON_HORSE_HURT;
	}
	
	@Override
	protected SoundEvent getSwimSound(){
		if(onGround){
			if(!isVehicle()){
				return SoundEvents.SKELETON_HORSE_STEP_WATER;
			}
			
			++gallopSoundCounter;
			if(gallopSoundCounter > 5 && gallopSoundCounter % 3 == 0){
				return SoundEvents.SKELETON_HORSE_GALLOP_WATER;
			}
			
			if(gallopSoundCounter <= 5){
				return SoundEvents.SKELETON_HORSE_STEP_WATER;
			}
		}
		
		return SoundEvents.SKELETON_HORSE_SWIM;
	}
	
	@Override
	protected void playSwimSound(float var0){
		if(onGround){
			super.playSwimSound(0.3F);
		}else{
			super.playSwimSound(Math.min(0.1F, var0 * 25.0F));
		}
	}
	
	@Override
	protected void playJumpSound(){
		if(isInWater()){
			playSound(SoundEvents.SKELETON_HORSE_JUMP_WATER, 0.4F, 1.0F);
		}else{
			super.playJumpSound();
		}
	}
}
