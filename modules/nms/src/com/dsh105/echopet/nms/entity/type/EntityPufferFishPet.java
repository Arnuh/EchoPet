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
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPufferFishPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.7F, height = 0.7F)
@EntityPetType(petType = PetType.PUFFERFISH)
public class EntityPufferFishPet extends EntityFishPet implements IEntityPufferFishPet{
	
	private static final EntityDataAccessor<Integer> PUFF_STATE = SynchedEntityData.defineId(EntityPufferFishPet.class, EntityDataSerializers.INT);
	
	//private static final int STATE_SMALL = 0, STATE_MID = 1, STATE_FULL = 2;
	
	public EntityPufferFishPet(Level world){
		super(EntityType.PUFFERFISH, world);
	}
	
	public EntityPufferFishPet(Level world, IPet pet){
		super(EntityType.PUFFERFISH, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(PUFF_STATE, 0);
	}
	
	public int getPuffState(){
		return this.entityData.get(PUFF_STATE);
	}
	
	@Override
	public void setPuffState(int state){
		int currentState = getPuffState();
		if(currentState > state){
			playSound(SoundEvents.PUFFER_FISH_BLOW_OUT, this.getSoundVolume(), this.getVoicePitch());
		}else if(state > currentState){
			playSound(SoundEvents.PUFFER_FISH_BLOW_UP, this.getSoundVolume(), this.getVoicePitch());
		}
		entityData.set(PUFF_STATE, state);
		refreshDimensions();
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
	
	@Override
	public EntityDimensions getDimensions(Pose entitypose){
		return super.getDimensions(entitypose).scale(getScale(getPuffState()));
	}
	
	private static float getScale(int i){
		return switch(i){
			case 0 -> 0.5F;
			case 1 -> 0.7F;
			default -> 1.0F;
		};
	}
}
