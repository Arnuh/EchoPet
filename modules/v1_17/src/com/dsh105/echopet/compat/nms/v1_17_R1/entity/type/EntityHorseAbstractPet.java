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

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorseAbstractPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityAgeablePet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class EntityHorseAbstractPet extends EntityAgeablePet implements IEntityHorseAbstractPet, PlayerRideableJumping{
	
	// EntityHorseAbstract: Zombie, Skeleton
	private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(EntityHorseAbstractPet.class, EntityDataSerializers.BYTE);// feet kicking, whatev
	private static final EntityDataAccessor<Optional<UUID>> DATA_ID_OWNER_UUID = SynchedEntityData.defineId(EntityHorseAbstractPet.class, EntityDataSerializers.OPTIONAL_UUID);
	
	private int eatingCounter;
	private int mouthCounter;
	private int standCounter;
	protected int tailCounter;
	protected int sprintCounter;
	protected boolean isJumping;
	protected float playerJumpPendingScale;
	private boolean allowStandSliding;
	protected int gallopSoundCounter = 0;
	protected boolean canGallop = true;
	
	private static final int FLAG_TAME = 0x2, FLAG_SADDLE = 0x4, FLAG_BRED = 0x8, FLAG_EATING = 0x10, FLAG_STANDING = 0x20, FLAG_OPEN_MOUTH = 0x40;
	
	public EntityHorseAbstractPet(EntityType<? extends Mob> type, Level world){
		super(type, world);
	}
	
	public EntityHorseAbstractPet(EntityType<? extends Mob> type, Level world, IPet pet){
		super(type, world, pet);
	}
	
	@Override
	protected void initiateEntityPet(){
		super.initiateEntityPet();
		AttributeInstance attributeInstance = getAttribute(Attributes.JUMP_STRENGTH);
		if(attributeInstance != null){
			attributeInstance.setBaseValue(jumpHeight);
		}
	}
	
	@Override
	public float getMaxUpStep(){
		return 1F;
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		entityData.define(DATA_ID_FLAGS, (byte) 0);
		entityData.define(DATA_ID_OWNER_UUID, Optional.empty());
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		if(isBaby()){
			return SizeCategory.TINY;
		}else{
			return SizeCategory.LARGE;
		}
	}
	
	@Override
	public void onLive(){
		super.onLive();
		if(mouthCounter > 0 && ++mouthCounter > 30){
			mouthCounter = 0;
			setVisualFlag(FLAG_OPEN_MOUTH, false);
		}
		if((isControlledByLocalInstance() || isEffectiveAi()) && standCounter > 0 && ++standCounter > 20){
			standCounter = 0;
			setStanding(false);
		}
		if(tailCounter > 0 && ++tailCounter > 8){
			tailCounter = 0;
		}
		
		if(sprintCounter > 0){
			++sprintCounter;
			if(sprintCounter > 300){
				sprintCounter = 0;
			}
		}
		// Bunch of animation stuff
		if(!isStanding()){
			allowStandSliding = false;
		}
	}
	
	@Override
	public void aiStep(){
		if(random.nextInt(200) == 0){
			moveTail();
		}
		
		super.aiStep();
		if(!level.isClientSide && isAlive()){
			
			if(canEatGrass()){
				if(!isEating() && !isVehicle() && random.nextInt(300) == 0 && level.getBlockState(blockPosition().down()).is(Blocks.GRASS_BLOCK)){
					setEating(true);
				}
				
				if(isEating() && ++eatingCounter > 50){
					eatingCounter = 0;
					setEating(false);
				}
			}
		}
	}
	
	public boolean isSaddled(){
		return getVisualFlag(FLAG_SADDLE);
	}
	
	@Override
	public void setSaddled(boolean flag){
		setVisualFlag(FLAG_SADDLE, flag);
		//playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
	}
	
	protected boolean getVisualFlag(int i){
		return (entityData.get(DATA_ID_FLAGS) & i) != 0;
	}
	
	protected void setVisualFlag(int i, boolean flag){
		byte b0 = entityData.get(DATA_ID_FLAGS);
		if(flag){
			entityData.set(DATA_ID_FLAGS, (byte) (b0 | i));
		}else{
			entityData.set(DATA_ID_FLAGS, (byte) (b0 & ~i));
		}
	}
	
	@Override
	public void setVariant(HorseVariant variant){}
	
	public boolean isEating(){
		return getVisualFlag(FLAG_EATING);
	}
	
	public void setEating(boolean flag){
		setVisualFlag(FLAG_EATING, flag);
	}
	
	public boolean isStanding(){
		return getVisualFlag(FLAG_STANDING);
	}
	
	public void setStanding(boolean flag){
		if(flag){
			setEating(false);
		}
		setVisualFlag(FLAG_STANDING, flag);
	}
	
	public boolean canEatGrass(){
		return true;
	}
	
	public boolean isJumping(){
		return isJumping;
	}
	
	public void setIsJumping(boolean flag){
		isJumping = flag;
	}
	
	public double getCustomJump(){
		return getAttributeValue(Attributes.JUMP_STRENGTH);
	}
	
	@Override
	protected boolean isImmobile(){
		return super.isImmobile() && isVehicle() && isSaddled() || isEating() || isStanding();
	}
	
	@Override
	public boolean canBeControlledByRider(){
		return getValidRider() != null;
	}
	
	@Override
	@Nullable
	public Entity getControllingPassenger(){
		return this.getFirstPassenger();
	}
	
	@Override
	public boolean hasCustomTravel(){
		return true;
	}
	
	@Override
	public void travel(Vec3 vec3d){
		if(!hasCustomTravel()){
			this.flyingSpeed = 0.02F;
			super.travel(vec3d);
			return;
		}
		ServerPlayer passenger = getValidRider();
		if(passenger == null){
			flyingSpeed = 0.02F;
			super.travel(vec3d);
			return;
		}
		setYRot(passenger.getYRot());
		yRotO = getYRot();
		setXRot(passenger.getXRot() * 0.5F);
		setRot(getYRot(), getXRot());
		yBodyRot = getYRot();
		yHeadRot = yBodyRot;
		float f = passenger.xxa * 0.5F;
		float f1 = passenger.zza;
		if(f1 <= 0.0F){
			f1 *= 0.25F;
			gallopSoundCounter = 0;
		}
		
		if(onGround && playerJumpPendingScale == 0.0F && isStanding() && !allowStandSliding){
			f = 0.0F;
			f1 = 0.0F;
		}
		
		if(playerJumpPendingScale > 0.0F && !isJumping() && onGround){
			double d0 = getCustomJump() * (double) playerJumpPendingScale * (double) getBlockJumpFactor();
			double d1 = d0 + getJumpBoostPower();
			Vec3 vec3d1 = getDeltaMovement();
			setDeltaMovement(vec3d1.x, d1, vec3d1.z);
			setIsJumping(true);
			hasImpulse = true;
			if(f1 > 0.0F){
				float f2 = Mth.sin(getYRot() * 0.017453292F);
				float f3 = Mth.cos(getYRot() * 0.017453292F);
				setDeltaMovement(getDeltaMovement().add((-0.4F * f2 * playerJumpPendingScale), 0.0D, (0.4F * f3 * playerJumpPendingScale)));
			}
			
			playerJumpPendingScale = 0.0F;
		}
		
		flyingSpeed = getSpeed() * 0.1F;
		if(isControlledByLocalInstance()){
			setSpeed((float) getAttributeValue(Attributes.MOVEMENT_SPEED));
			super.travel(new Vec3(f, vec3d.y, f1));
		}else{
			setDeltaMovement(Vec3.ZERO);
		}
		
		if(onGround){
			playerJumpPendingScale = 0.0F;
			setIsJumping(false);
		}
		
		calculateEntityAnimation(this, false);
		tryCheckInsideBlocks();
	}
	
	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity entityliving){
		Vec3 vec3d = getCollisionHorizontalEscapeVector(this.getBbWidth(), entityliving.getBbWidth(), this.getYRot() + (entityliving.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F));
		Vec3 vec3d1 = this.getDismountLocationInDirection(vec3d, entityliving);
		if(vec3d1 != null){
			return vec3d1;
		}else{
			Vec3 vec3d2 = getCollisionHorizontalEscapeVector(this.getBbWidth(), entityliving.getBbWidth(), this.getYRot() + (entityliving.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F));
			Vec3 vec3d3 = this.getDismountLocationInDirection(vec3d2, entityliving);
			return vec3d3 != null ? vec3d3 : this.position();
		}
	}
	
	@Nullable
	private Vec3 getDismountLocationInDirection(Vec3 vec3d, LivingEntity entityliving){
		double d0 = this.getX() + vec3d.x;
		double d1 = this.getBoundingBox().minY;
		double d2 = this.getZ() + vec3d.z;
		BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();
		
		for(Pose entitypose : entityliving.getDismountPoses()){
			blockposition_mutableblockposition.set(d0, d1, d2);
			double d3 = this.getBoundingBox().maxY + 0.75D;
			
			while(true){
				double d4 = this.level.getBlockFloorHeight(blockposition_mutableblockposition);
				if((double) blockposition_mutableblockposition.getY() + d4 > d3){
					break;
				}
				
				if(DismountHelper.isBlockFloorValid(d4)){
					AABB axisalignedbb = entityliving.getLocalBoundsForPose(entitypose);
					Vec3 vec3d1 = new Vec3(d0, (double) blockposition_mutableblockposition.getY() + d4, d2);
					if(DismountHelper.canDismountTo(this.level, entityliving, axisalignedbb.move(vec3d1))){
						entityliving.setPose(entitypose);
						return vec3d1;
					}
				}
				
				blockposition_mutableblockposition.move(Direction.UP);
				if((double) blockposition_mutableblockposition.getY() >= d3){
					break;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public boolean canJump(){
		return isSaddled();
	}
	
	@Override
	public void handleStartJump(int i){
		allowStandSliding = true;
		stand();
		playJumpSound();
	}
	
	@Override
	public void handleStopJump(){}
	
	@Override
	public void onPlayerJump(int i){
		if(isSaddled()){
			if(i < 0){
				i = 0;
			}else{
				allowStandSliding = true;
				stand();
			}
			
			if(i >= 90){
				playerJumpPendingScale = 1.0F;
			}else{
				playerJumpPendingScale = 0.4F + 0.4F * (float) i / 90.0F;
			}
		}
	}
	
	private void stand(){
		if(isControlledByLocalInstance() || isEffectiveAi()){
			standCounter = 1;
			setStanding(true);
		}
	}
	
	private void moveTail(){
		tailCounter = 1;
	}
	
	@Override
	protected float getSoundVolume(){
		return 0.8F;
	}
	
	@Override
	public int getAmbientSoundInterval(){
		return 400;
	}
	
	protected void playJumpSound(){
		playSound(SoundEvents.HORSE_JUMP, 0.4F, 1.0F);
	}
	
	@Nullable
	protected SoundEvent getEatingSound(){
		return null;
	}
	
	@Override
	@Nullable
	protected SoundEvent getDeathSound(){
		return null;
	}
	
	@Override
	@Nullable
	protected SoundEvent getHurtSound(DamageSource damagesource){
		if(random.nextInt(3) == 0){
			stand();
		}
		
		return null;
	}
	
	@Override
	@Nullable
	protected SoundEvent getAmbientSound(){
		if(random.nextInt(10) == 0 && !isImmobile()){
			stand();
		}
		
		return null;
	}
	
	@Nullable
	protected SoundEvent getAngrySound(){
		stand();
		return null;
	}
	
	protected void playGallopSound(SoundType soundeffecttype){
		playSound(SoundEvents.HORSE_GALLOP, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
	}
	
	@Override
	protected void playStepSound(BlockPos blockposition, BlockState iblockdata){
		if(!iblockdata.getMaterial().isLiquid()){
			BlockState iblockdata1 = level.getBlockState(blockposition.up());
			SoundType soundeffecttype = iblockdata.getSoundType();
			if(iblockdata1.is(Blocks.SNOW)){
				soundeffecttype = iblockdata1.getSoundType();
			}
			
			if(isVehicle() && canGallop){
				++gallopSoundCounter;
				if(gallopSoundCounter > 5 && gallopSoundCounter % 3 == 0){
					playGallopSound(soundeffecttype);
				}else if(gallopSoundCounter <= 5){
					playSound(SoundEvents.HORSE_STEP_WOOD, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
				}
			}else if(soundeffecttype == SoundType.WOOD){
				playSound(SoundEvents.HORSE_STEP_WOOD, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
			}else{
				playSound(SoundEvents.HORSE_STEP, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
			}
		}
	}
}
