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
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGlowSquidPet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntityPetType(petType = PetType.GLOWSQUID)
public class EntityGlowSquidPet extends EntitySquidPet implements IEntityGlowSquidPet{
	
	private static final EntityDataAccessor<Integer> DATA_DARK_TICKS_REMAINING = SynchedEntityData.defineId(EntityGlowSquidPet.class, EntityDataSerializers.INT);
	
	public EntityGlowSquidPet(Level world){
		super(EntityType.GLOW_SQUID, world);
	}
	
	public EntityGlowSquidPet(Level world, IPet pet){
		super(EntityType.GLOW_SQUID, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_DARK_TICKS_REMAINING, 0);
	}
	
	@Override
	public void aiStep(){
		super.aiStep();
		this.level.addParticle(ParticleTypes.GLOW, getRandomX(0.6D), getRandomY(), getRandomZ(0.6D), 0.0D, 0.0D, 0.0D);
	}
	
	@Override
	public void setDark(boolean flag){
		if(flag){
			// Better than having to update the amount of ticks since the client handles the rendering.
			setDarkTicks(Integer.MAX_VALUE);
		}else{
			setDarkTicks(0);
		}
	}
	
	public void setDarkTicks(int ticks){
		this.entityData.set(DATA_DARK_TICKS_REMAINING, ticks);
	}
	
	@Override
	protected SoundEvent getAmbientSound(){
		return SoundEvents.GLOW_SQUID_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource var0){
		return SoundEvents.GLOW_SQUID_HURT;
	}
	
	@Override
	public SoundEvent getDeathSound(){
		return SoundEvents.GLOW_SQUID_DEATH;
	}
}
