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

package com.dsh105.echopet.nms.entity.base;


import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.nms.IEntityLivingPet;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.nms.DamageSourceType;
import com.dsh105.echopet.nms.NMSEntityUtil;
import com.dsh105.echopet.nms.VersionBreaking;
import com.dsh105.echopet.nms.entity.INMSLivingEntityPetHandle;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_19_R3.event.CraftEventFactory;

public class LivingEntityPetHandle extends EntityPetHandle implements INMSLivingEntityPetHandle{
	
	public LivingEntityPetHandle(IEntityLivingPet entityPet){
		super(entityPet);
	}
	
	@Override
	public LivingEntity getEntity(){
		return (LivingEntity) getEntityPet();
	}
	
	@Override
	protected void initiateEntityPet(){
		super.initiateEntityPet();
		IPetType petType = getPet().getPetType();
		AttributeInstance attributeInstance = getEntity().getAttribute(Attributes.MOVEMENT_SPEED);
		if(attributeInstance != null){
			attributeInstance.setBaseValue(IPet.GOAL_WALK_SPEED.getNumber(petType).doubleValue());
		}
		attributeInstance = getEntity().getAttributes().getInstance(Attributes.FLYING_SPEED);
		if(attributeInstance != null){
			attributeInstance.setBaseValue(IPet.GOAL_FLY_SPEED.getNumber(petType).doubleValue());
		}else{
			NMSEntityUtil.addFlyingSpeedAttribute(petType, getEntity().getAttributes());
		}
	}
	
	@Override
	protected void adjustFlyingSpeed(){
		LivingEntity entity = getEntity();
		VersionBreaking.setFlyingSpeed(entity, entity.getSpeed() * 0.1F);
	}
	
	@Override
	public void originalTravel(LivingEntity entity, Vec3 vec3d){
		if(entity.isEffectiveAi() || entity.isControlledByLocalInstance()){
			double d0 = 0.08;
			boolean flag = entity.getDeltaMovement().y <= 0.0;
			if(flag && entity.hasEffect(MobEffects.SLOW_FALLING)){
				d0 = 0.01;
				entity.resetFallDistance();
			}
			
			FluidState fluid = entity.level.getFluidState(entity.blockPosition());
			double d1;
			float f;
			float f2;
			Vec3 vec3d1;
			// if(entity.isInWater() && entity.isAffectedByFluids() && !entity.canStandOnFluid(fluid)){
			if(entity.isInWater() && !entity.canStandOnFluid(fluid)){
				d1 = entity.getY();
				f = entity.isSprinting() ? 0.9F : getWaterSlowDown();
				float f1 = 0.02F;
				f2 = (float) EnchantmentHelper.getDepthStrider(entity);
				if(f2 > 3.0F){
					f2 = 3.0F;
				}
				
				if(!entity.onGround){
					f2 *= 0.5F;
				}
				
				if(f2 > 0.0F){
					f += (0.54600006F - f) * f2 / 3.0F;
					f1 += (this.getSpeed() - f1) * f2 / 3.0F;
				}
				
				if(entity.hasEffect(MobEffects.DOLPHINS_GRACE)){
					f = 0.96F;
				}
				
				entity.moveRelative(f1, vec3d);
				entity.move(MoverType.SELF, entity.getDeltaMovement());
				vec3d1 = entity.getDeltaMovement();
				if(entity.horizontalCollision && entity.onClimbable()){
					vec3d1 = new Vec3(vec3d1.x, 0.2, vec3d1.z);
				}
				
				entity.setDeltaMovement(vec3d1.multiply(f, 0.800000011920929, f));
				Vec3 vec3d2 = entity.getFluidFallingAdjustedMovement(d0, flag, entity.getDeltaMovement());
				entity.setDeltaMovement(vec3d2);
				if(entity.horizontalCollision && entity.isFree(vec3d2.x, vec3d2.y + 0.6000000238418579 - entity.getY() + d1, vec3d2.z)){
					entity.setDeltaMovement(vec3d2.x, 0.30000001192092896, vec3d2.z);
				}
			}else{
				Vec3 vec3d4;
				// if(entity.isInLava() && entity.isAffectedByFluids() && !entity.canStandOnFluid(fluid)){
				if(entity.isInLava() && !entity.canStandOnFluid(fluid)){
					d1 = entity.getY();
					entity.moveRelative(0.02F, vec3d);
					entity.move(MoverType.SELF, entity.getDeltaMovement());
					if(entity.getFluidHeight(FluidTags.LAVA) <= entity.getFluidJumpThreshold()){
						entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5, 0.800000011920929, 0.5));
						vec3d4 = entity.getFluidFallingAdjustedMovement(d0, flag, entity.getDeltaMovement());
						entity.setDeltaMovement(vec3d4);
					}else{
						entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5));
					}
					
					if(!entity.isNoGravity()){
						entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, -d0 / 4.0, 0.0));
					}
					
					vec3d4 = entity.getDeltaMovement();
					if(entity.horizontalCollision && entity.isFree(vec3d4.x, vec3d4.y + 0.6000000238418579 - entity.getY() + d1, vec3d4.z)){
						entity.setDeltaMovement(vec3d4.x, 0.30000001192092896, vec3d4.z);
					}
				}else if(entity.isFallFlying()){
					vec3d4 = entity.getDeltaMovement();
					if(vec3d4.y > -0.5){
						entity.fallDistance = 1.0F;
					}
					
					Vec3 vec3d5 = entity.getLookAngle();
					f = entity.getXRot() * 0.017453292F;
					double d2 = Math.sqrt(vec3d5.x * vec3d5.x + vec3d5.z * vec3d5.z);
					double d3 = vec3d4.horizontalDistance();
					double d4 = vec3d5.length();
					double d5 = Math.cos(f);
					d5 = d5 * d5 * Math.min(1.0, d4 / 0.4);
					vec3d4 = entity.getDeltaMovement().add(0.0, d0 * (-1.0 + d5 * 0.75), 0.0);
					double d6;
					if(vec3d4.y < 0.0 && d2 > 0.0){
						d6 = vec3d4.y * -0.1 * d5;
						vec3d4 = vec3d4.add(vec3d5.x * d6 / d2, d6, vec3d5.z * d6 / d2);
					}
					
					if(f < 0.0F && d2 > 0.0){
						d6 = d3 * (double) (-Mth.sin(f)) * 0.04;
						vec3d4 = vec3d4.add(-vec3d5.x * d6 / d2, d6 * 3.2, -vec3d5.z * d6 / d2);
					}
					
					if(d2 > 0.0){
						vec3d4 = vec3d4.add((vec3d5.x / d2 * d3 - vec3d4.x) * 0.1, 0.0, (vec3d5.z / d2 * d3 - vec3d4.z) * 0.1);
					}
					
					entity.setDeltaMovement(vec3d4.multiply(0.9900000095367432, 0.9800000190734863, 0.9900000095367432));
					entity.move(MoverType.SELF, entity.getDeltaMovement());
					if(entity.horizontalCollision && !entity.level.isClientSide){
						d6 = entity.getDeltaMovement().horizontalDistance();
						double d7 = d3 - d6;
						float f3 = (float) (d7 * 10.0 - 3.0);
						if(f3 > 0.0F){
							entity.playSound(getFallDamageSound(entity, (int) f3), 1.0F, 1.0F);
							entity.hurt(VersionBreaking.getDamageSource(entity, DamageSourceType.FLY_INTO_WALL), f3);
						}
					}
					
					if(entity.onGround && !entity.level.isClientSide && entity.getSharedFlag(7) && !CraftEventFactory.callToggleGlideEvent(entity, false)
						.isCancelled()){
						entity.setSharedFlag(7, false);
					}
				}else{
					BlockPos blockposition = getBlockPosBelowThatAffectsMyMovement(entity);
					f2 = entity.level.getBlockState(blockposition).getBlock().getFriction();
					f = entity.onGround ? f2 * 0.91F : 0.91F;
					vec3d1 = entity.handleRelativeFrictionAndCalculateMovement(vec3d, f2);
					double d8 = vec3d1.y;
					if(entity.hasEffect(MobEffects.LEVITATION)){
						d8 += (0.05 * (double) (entity.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec3d1.y) * 0.2;
						entity.resetFallDistance();
					}else if(entity.level.isClientSide && !entity.level.hasChunkAt(blockposition)){
						if(entity.getY() > (double) entity.level.getMinBuildHeight()){
							d8 = -0.1;
						}else{
							d8 = 0.0;
						}
					}else if(!entity.isNoGravity()){
						d8 -= d0;
					}
					
					if(entity.shouldDiscardFriction()){
						entity.setDeltaMovement(vec3d1.x, d8, vec3d1.z);
					}else{
						entity.setDeltaMovement(vec3d1.x * (double) f, d8 * 0.9800000190734863, vec3d1.z * (double) f);
					}
				}
			}
		}
		
		VersionBreaking.calculateEntityAnimation(entity, entity instanceof FlyingAnimal);
	}
	
	protected BlockPos getBlockPosBelowThatAffectsMyMovement(LivingEntity entity){
		return VersionBreaking.blockPos(entity.position().x, entity.getBoundingBox().minY - 0.5000001, entity.position().z);
	}
	
	private SoundEvent getFallDamageSound(LivingEntity entity, int i){
		return i > 4 ? entity.getFallSounds().big() : entity.getFallSounds().small();
	}
	
	protected float getWaterSlowDown(){ // Not proper
		return 0.8F;
	}
}
