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
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWolfPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IWolfPet;
import com.dsh105.echopet.nms.VersionBreaking;
import com.dsh105.echopet.nms.entity.EntityTameablePet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.DyeColor;

@EntitySize(width = 0.6F, height = 0.8F)
@EntityPetType(petType = PetType.WOLF)
public class EntityWolfPet extends EntityTameablePet implements IEntityWolfPet{
	
	private static final EntityDataAccessor<Boolean> DATA_INTERESTED_ID = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.INT);
	private boolean isWet;
	private boolean isShaking;
	private float shakeAnim;
	
	public EntityWolfPet(Level world){
		super(EntityType.WOLF, world);
	}
	
	public EntityWolfPet(Level world, IPet pet){
		super(EntityType.WOLF, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_INTERESTED_ID, false);
		this.entityData.define(DATA_COLLAR_COLOR, net.minecraft.world.item.DyeColor.RED.getId());
		this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
	}
	
	@Override
	public void setAngry(boolean angry){
		if(isTamed() && angry){
			setTamed(false);
		}
		setRemainingPersistentAngerTime(angry ? 1 : 0);
	}
	
	@Override
	public void setTamed(boolean tamed){
		super.setTamed(tamed);
		if(isAngry() && tamed){
			setAngry(false);
		}
		if(tamed){
			getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0);
		}else{
			getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0);
		}
	}
	
	public boolean isAngry(){
		return getRemainingPersistentAngerTime() > 0;
	}
	
	public int getRemainingPersistentAngerTime(){
		return entityData.get(DATA_REMAINING_ANGER_TIME);
	}
	
	public void setRemainingPersistentAngerTime(int i){
		entityData.set(DATA_REMAINING_ANGER_TIME, i);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setCollarColor(DyeColor dc){
		if(((IWolfPet) pet).isTamed()){
			this.entityData.set(DATA_COLLAR_COLOR, net.minecraft.world.item.DyeColor.byId(dc.getWoolData()).getId());
		}
	}
	
	public boolean isPathFinding(){
		return !getNavigation().isDone();
	}
	
	@Override
	public void aiStep(){
		super.aiStep();
		if(!this.level.isClientSide && this.isWet && !this.isShaking && !this.isPathFinding() && this.onGround){
			this.isShaking = true;
			this.shakeAnim = 0.0F;
			this.level.broadcastEntityEvent(this, (byte) 8);
		}
	}
	
	@Override
	public void onLive(){
		super.onLive();
		if(isInWaterRainOrBubble()){
			isWet = true;
			if(isShaking && !level.isClientSide){
				this.level.broadcastEntityEvent(this, (byte) 56);
				cancelShake();
			}
		}else if((isWet || isShaking) && isShaking){
			if(this.shakeAnim == 0.0F){
				playSound(SoundEvents.WOLF_SHAKE, getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
				VersionBreaking.entityShake(this);
			}
			this.shakeAnim += 0.05F;
			if(this.shakeAnim - 0.05F >= 2.0F){
				this.isWet = false;
				this.isShaking = false;
				this.shakeAnim = 0.0F;
			}
			if(this.shakeAnim > 0.4F){
				float f = (float) this.getY();
				int i = (int) (Mth.sin((shakeAnim - 0.4F) * 3.1415927F) * 7.0F);
				Vec3 vec3d = this.getDeltaMovement();
				
				for(int j = 0; j < i; ++j){
					float f1 = (random.nextFloat() * 2.0F - 1.0F) * getBbWidth() * 0.5F;
					float f2 = (random.nextFloat() * 2.0F - 1.0F) * getBbWidth() * 0.5F;
					this.level.addParticle(ParticleTypes.SPLASH, getX() + (double) f1, f + 0.8F, getZ() + (double) f2, vec3d.x, vec3d.y, vec3d.z);
				}
			}
		}
	}
	
	private void cancelShake(){
		this.isShaking = false;
		this.shakeAnim = 0.0F;
	}
	
	@Override
	protected float getSoundVolume(){
		return 0.4F;
	}
	
	@Override
	protected SoundEvent getAmbientSound(){
		return this.isAngry() ? SoundEvents.WOLF_GROWL : (random.nextInt(3) == 0 ? (isTamed() && getHealth() < 10.0F ? SoundEvents.WOLF_WHINE : SoundEvents.WOLF_PANT) : SoundEvents.WOLF_AMBIENT);
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damagesource){
		return SoundEvents.WOLF_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound(){
		return SoundEvents.WOLF_DEATH;
	}
}