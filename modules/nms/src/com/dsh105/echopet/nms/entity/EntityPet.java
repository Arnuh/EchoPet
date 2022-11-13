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

package com.dsh105.echopet.nms.entity;

import com.dsh105.echopet.compat.api.ai.IPetGoalSelector;
import com.dsh105.echopet.compat.api.ai.PetGoal;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.nms.IEntityLivingPet;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.event.PetRideJumpEvent;
import com.dsh105.echopet.compat.api.event.PetRideMoveEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.nms.NMSEntityUtil;
import com.dsh105.echopet.nms.entity.base.EntityPetHandle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

@Deprecated
public abstract class EntityPet extends Mob implements IEntityLivingPet{
	
	protected IPet pet;
	private final INMSEntityPetHandle petHandle;
	protected boolean canFly, canControlGravity;
	protected float rideSpeed, rideFlySpeed;
	protected double rideJumpHeight;
	protected double gravityModifier;
	public boolean shouldVanish;
	
	@Deprecated
	public EntityPet(EntityType<? extends Mob> type, Level world){
		super(type, world);
		this.petHandle = createPetHandle();
	}
	
	public EntityPet(EntityType<? extends Mob> type, Level world, IPet pet){
		super(type, world);
		this.pet = pet;
		this.petHandle = createPetHandle();
		this.initiateEntityPet();
	}
	
	public INMSEntityPetHandle createPetHandle(){
		return new EntityPetHandle(this);
	}
	
	protected void initiateEntityPet(){
		IPetType petType = getPet().getPetType();
		this.canFly = IPet.RIDING_FLY.get(petType);
		this.canControlGravity = IPet.RIDING_GRAVITY_CONTROL.get(petType);
		this.rideSpeed = IPet.RIDING_WALK_SPEED.getNumber(petType).floatValue();
		this.rideFlySpeed = IPet.RIDING_FLY_SPEED.getNumber(petType).floatValue();
		this.rideJumpHeight = IPet.RIDING_JUMP_HEIGHT.getNumber(petType).doubleValue();
		this.gravityModifier = IPet.RIDING_GRAVITY_MODIFIER.getNumber(petType).doubleValue();
		AttributeInstance attributeInstance = getAttribute(Attributes.MOVEMENT_SPEED);
		if(attributeInstance != null){
			attributeInstance.setBaseValue(IPet.GOAL_WALK_SPEED.getNumber(petType).doubleValue());
		}
		attributeInstance = getAttribute(Attributes.FLYING_SPEED);
		if(attributeInstance != null){
			attributeInstance.setBaseValue(IPet.GOAL_FLY_SPEED.getNumber(petType).doubleValue());
		}else{
			NMSEntityUtil.addFlyingSpeedAttribute(petType, getAttributes());
		}
		this.maxUpStep = getMaxUpStep();
	}
	
	@Override
	public boolean isPersistenceRequired(){
		return true;
	}
	
	@Override
	public IPet getPet(){
		return this.pet;
	}
	
	@Override
	public INMSEntityPetHandle getHandle(){
		return petHandle;
	}
	
	@Override
	public Player getOwner(){
		return pet.getOwner();
	}
	
	@Override
	public boolean isDead(){
		return dead;
	}
	
	public void setShouldVanish(boolean flag){
		this.shouldVanish = flag;
	}
	
	public float getWalkTargetValue(BlockPos blockposition){
		return this.getWalkTargetValue(blockposition, this.level);
	}
	
	public float getWalkTargetValue(BlockPos blockposition, LevelReader iworldreader){
		return 0.0F;
	}
	
	@Override
	public CraftLivingEntity getEntity(){
		return (CraftLivingEntity) super.getBukkitEntity();
	}
	
	@Override
	public void setLocation(Location l){
		this.absMoveTo(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		this.level = ((CraftWorld) l.getWorld()).getHandle();
	}
	
	@Override
	public void remove(boolean makeSound){
		if(getEntity() != null){
			getEntity().leaveVehicle();
			getEntity().remove();
		}
		if(makeSound){
			SoundEvent sound = getSoundFromString(getDeathSoundString());
			if(sound != null){
				playSound(sound, 1.0F, 1.0F);// was makeSound in 1.8
			}
		}
	}
	
	public void onLive(){
		if(this.pet == null){
			this.remove(false);
			return;
		}
		ServerPlayer player = getHandle().getNMSOwner();
		if(player == null){
			EchoPet.getManager().removePet(this.getPet(), true);
			return;
		}
		if(pet.isOwnerRiding() && this.passengers.size() == 0 && !pet.isOwnerInMountingProcess()){
			pet.ownerRidePet(false);
		}
		if(player.isInvisible() != this.isInvisible() && !this.shouldVanish){
			this.setInvisible(!this.isInvisible());
		}
		if(player.isShiftKeyDown() != this.isShiftKeyDown()){
			this.setShiftKeyDown(!this.isShiftKeyDown());
		}
		if(player.isSprinting() != this.isSprinting()){
			this.setSprinting(!this.isSprinting());
		}
		if(this.getPet().isHat()){
			// this.lastYaw = this.yRot = (this.getPet().getPetType() == PetType.ENDERDRAGON ? this.getPlayerOwner().getLocation().getYaw() - 180 : this.getPlayerOwner().getLocation().getYaw());
			setYRot(player.getYRot());
			this.yRotO = getYRot();
		}
		if(canFly && canControlGravity && !onGround && getDeltaMovement().y < 0.0){
			setDeltaMovement(getDeltaMovement().multiply(1.0, gravityModifier, 1.0));
		}
		/*if(player.isFlying() && getPet().getPetType().canFly()){
			Location petLoc = this.getLocation();
			Location ownerLoc = player.getLocation();
			Vector v = ownerLoc.toVector().subtract(petLoc.toVector());
			double x = v.getX();
			double y = v.getY();
			double z = v.getZ();
			Vector vo = player.getLocation().getDirection();
			if(vo.getX() > 0){
				x -= 1.5;
			}else if(vo.getX() < 0){
				x += 1.5;
			}
			if(vo.getZ() > 0){
				z -= 1.5;
			}else if(vo.getZ() < 0){
				z += 1.5;
			}
			this.setVelocity(new Vector(x, y, z).normalize().multiply(0.3F));
		}*/
	}
	
	public ServerPlayer getValidRider(){
		Entity passenger = getFirstPassenger();
		if(!(passenger instanceof ServerPlayer serverPlayer)){
			return null;
		}
		if(serverPlayer.getBukkitEntity() != getHandle().getCraftOwner()){
			return null;
		}
		return serverPlayer;
	}
	
	public boolean hasCustomTravel(){
		return false;
	}
	
	@Override
	public void travel(Vec3 vec3d){
		if(hasCustomTravel()){
			this.flyingSpeed = 0.02F;
			super.travel(vec3d);
			return;
		}
		ServerPlayer passenger = getValidRider();
		if(passenger == null){
			this.flyingSpeed = 0.02F;
			super.travel(vec3d);
			return;
		}
		CraftPlayer player = passenger.getBukkitEntity();
		this.setYRot(passenger.getYRot());
		this.yRotO = this.getYRot();
		this.setXRot(passenger.getXRot() * 0.5F);
		this.setRot(this.getYRot(), this.getXRot());
		this.yHeadRot = this.yBodyRot = this.getYRot();
		
		double motX = passenger.xxa * 0.5;
		double motY = vec3d.y;
		double motZ = passenger.zza;
		if(motZ <= 0){
			motZ *= 0.25F;
		}
		this.flyingSpeed = getSpeed() * 0.1F;
		PetRideMoveEvent moveEvent = new PetRideMoveEvent(this.getPet(), (float) motX, (float) motZ);// side, forward
		EchoPet.getPlugin().getServer().getPluginManager().callEvent(moveEvent);
		if(moveEvent.isCancelled()) return;
		float speed = rideSpeed;
		if(NMSEntityUtil.getJumpingField() != null && !passengers.isEmpty()){
			if(canFly){
				if(!onGround){
					speed = rideFlySpeed;
				}
				try{
					if(player.isFlying()){
						player.setFlying(false);
					}
					if(NMSEntityUtil.getJumpingField().getBoolean(passenger)){
						PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.rideJumpHeight);
						EchoPet.getPlugin().getServer().getPluginManager().callEvent(rideEvent);
						if(!rideEvent.isCancelled()){
							setDeltaMovement(getDeltaMovement().x, 0.5F, getDeltaMovement().z);
						}
					}
				}catch(IllegalArgumentException | IllegalStateException | IllegalAccessException e){
					EchoPet.LOG.log(java.util.logging.Level.WARNING, "Failed to initiate Pet Flying Motion for " + player.getName() + "'s Pet.", e);
				}
			}else if(this.onGround){
				try{
					if(NMSEntityUtil.getJumpingField().getBoolean(passenger)){
						PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.rideJumpHeight);
						EchoPet.getPlugin().getServer().getPluginManager().callEvent(rideEvent);
						if(!rideEvent.isCancelled()){
							setDeltaMovement(getDeltaMovement().x, rideEvent.getJumpHeight(), getDeltaMovement().z);
						}
					}
				}catch(IllegalArgumentException | IllegalStateException | IllegalAccessException e){
					EchoPet.LOG.log(java.util.logging.Level.WARNING, "Failed to initiate Pet Jumping Motion for " + player.getName() + "'s Pet.", e);
				}
			}
		}
		this.setSpeed(speed);
		super.travel(new Vec3(motX, motY, motZ));
	}
	
	@Override
	protected SoundEvent getAmbientSound(){
		return getSoundFromString(getAmbientSoundString());
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource){
		return getSoundFromString(getHurtSoundString());
	}
	
	@Override
	protected SoundEvent getDeathSound(){
		return getSoundFromString(getDeathSoundString());
	}
	
	// Step sounds are based on block, don't override, let the base stuff manage it.
	
	public void makeSound(String soundEffect, float f, float f1){
		SoundEvent se = getSoundFromString(soundEffect);
		if(se != null) playSound(se, f, f1);
		// Minecraft doesn't actually do a null check in the method.. we have to do one for them.
		// But minecraft does do a null check on entity SoundEffects(ambient, hurt, death)
	}
	
	public SoundEvent getSoundFromString(String soundName){
		return soundName != null ? Registry.SOUND_EVENT.get(new ResourceLocation(soundName)) : null;
		// mojang made this method private
		// return soundName != null ? SoundEffect.a.get(new MinecraftKey(soundName)) : null;
	}
	
	protected String getAmbientSoundString(){
		return "entity." + pet.getPetType().getMinecraftName() + ".ambient";
	}
	
	protected String getHurtSoundString(){
		return "entity." + pet.getPetType().getMinecraftName() + ".hurt";
	}
	
	protected String getDeathSoundString(){
		return "entity." + pet.getPetType().getMinecraftName() + ".death";
	}
	
	// Entity
	@Override
	public void tick(){// Search for "entityBaseTick". The method its in.
		super.tick();
		onLive();
		if(getHandle().getPetGoalSelector() == null){
			this.remove(false);
		}
	}
	
	@Override
	protected void updateControlFlags(){
		IPetGoalSelector goalSelector = getHandle().getPetGoalSelector();
		if(goalSelector == null) return;
		IPet rider = getPet().getRider();
		boolean canMove = !isPassenger() && !isVehicle() && (rider == null || !rider.isSpawned());
		boolean canJump = !isPassenger();
		goalSelector.setControlFlag(PetGoal.Flag.MOVE, canMove);
		goalSelector.setControlFlag(PetGoal.Flag.JUMP, canMove && canJump);
		goalSelector.setControlFlag(PetGoal.Flag.LOOK, canMove);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
	}
	
	@Override
	public boolean startRiding(Entity entity){
		return false;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound){
		// Do nothing with NBT
		// Pets should not be stored to world save files
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbttagcompound){// Loading
		//
	}
}
