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

package com.dsh105.echopet.compat.nms.v1_17_R1.entity;

import com.dsh105.echopet.compat.api.ai.PetGoalSelector;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IEntityPetBase;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.event.PetRideJumpEvent;
import com.dsh105.echopet.compat.api.event.PetRideMoveEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Logger;
import com.dsh105.echopet.compat.api.util.Perm;
import com.dsh105.echopet.compat.api.util.menu.PetMenu;
import com.dsh105.echopet.compat.nms.v1_17_R1.NMSEntityUtil;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.ai.PetGoalFloat;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.ai.PetGoalFollowOwner;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.ai.PetGoalLookAtPlayer;
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
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class EntityPet extends Mob implements IEntityPet{
	
	protected IPet pet;
	private final INMSEntityPetBase petBase;
	public PetGoalSelector petGoalSelector;
	protected double jumpHeight;
	protected float rideSpeed, flySpeed;
	public boolean shouldVanish;
	
	@Deprecated
	public EntityPet(EntityType<? extends Mob> type, Level world){
		super(type, world);
		this.petBase = createPetBase();
	}
	
	public EntityPet(EntityType<? extends Mob> type, Level world, IPet pet){
		super(type, world);
		this.pet = pet;
		this.petBase = createPetBase();
		this.initiateEntityPet();
	}
	
	public INMSEntityPetBase createPetBase(){
		return new EntityPetBase(this);
	}
	
	protected void initiateEntityPet(){
		this.rideSpeed = getPet().getPetType().getRideSpeed();
		this.flySpeed = getPet().getPetType().getFlySpeed();
		this.jumpHeight = getPet().getPetType().getRideJumpHeight();
		AttributeInstance attributeInstance = getAttribute(Attributes.MOVEMENT_SPEED);
		if(attributeInstance != null){
			attributeInstance.setBaseValue(getPet().getPetType().getWalkSpeed());
		}
		this.setPathfinding();
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
	public IEntityPetBase getHandle(){
		return petBase;
	}
	
	@Override
	public Player getOwner(){
		return pet.getOwner();
	}
	
	public Location getLocation(){
		return this.pet.getLocation();
	}
	
	public void setVelocity(Vector vel){
		this.setDeltaMovement(vel.getX(), vel.getY(), vel.getZ());
		this.hurtMarked = true;
	}
	
	@Override
	public PetGoalSelector getPetGoalSelector(){
		return petGoalSelector;
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
	
	public void setPathfinding(){
		try{
			petGoalSelector = new PetGoalSelector();
			petGoalSelector.addGoal(new PetGoalFloat(this), 0);
			/*if(pet.getPetType().equals(PetType.BEE)){
				petGoalSelector.addGoal(new PetGoalBeeWander(this), 1);
			}else{
				petGoalSelector.addGoal(new PetGoalFollowOwner(this), 1);
			}*/
			petGoalSelector.addGoal(new PetGoalFollowOwner(this, this), 1);
			petGoalSelector.addGoal(new PetGoalLookAtPlayer(this, this, ServerPlayer.class), 2);
		}catch(Exception e){
			e.printStackTrace();
			Logger.log(Logger.LogLevel.WARNING, "Could not add PetGoals to Pet AI.", e, true);
		}
	}
	
	@Override
	public CraftLivingEntity getEntity(){
		return (CraftLivingEntity) super.getBukkitEntity();
	}
	
	@Override
	public boolean onInteract(Player p){
		if(p.getUniqueId().equals(getOwner().getUniqueId())){
			if(getPet().getPetType().isInteractMenuEnabled() && Perm.BASE_MENU.hasPerm(this.getOwner(), false, false)){
				new PetMenu(getPet()).open(false);
			}
			return true;
		}
		return false;
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
		if(this.getOwner() == null || !this.getOwner().isOnline()){
			EchoPet.getManager().removePet(this.getPet(), true);
			return;
		}
		if(pet.isOwnerRiding() && this.passengers.size() == 0 && !pet.isOwnerInMountingProcess()){
			pet.ownerRidePet(false);
		}
		if(((CraftPlayer) this.getOwner()).getHandle().isInvisible() != this.isInvisible() && !this.shouldVanish){
			this.setInvisible(!this.isInvisible());
		}
		if(((CraftPlayer) this.getOwner()).getHandle().isShiftKeyDown() != this.isShiftKeyDown()){
			this.setShiftKeyDown(!this.isShiftKeyDown());
		}
		if(((CraftPlayer) this.getOwner()).getHandle().isSprinting() != this.isSprinting()){
			this.setSprinting(!this.isSprinting());
		}
		if(this.getPet().isHat()){
			// this.lastYaw = this.yRot = (this.getPet().getPetType() == PetType.ENDERDRAGON ? this.getPlayerOwner().getLocation().getYaw() - 180 : this.getPlayerOwner().getLocation().getYaw());
			setYRot(this.getOwner().getLocation().getYaw());
			this.yRotO = getYRot();
		}
		if(this.getOwner().isFlying() && getPet().getPetType().canFly()){
			// if(this.getEntityPetType() == PetType.VEX && !((IVexPet) this.getPet()).isPowered()) return;
			Location petLoc = this.getLocation();
			Location ownerLoc = this.getOwner().getLocation();
			Vector v = ownerLoc.toVector().subtract(petLoc.toVector());
			double x = v.getX();
			double y = v.getY();
			double z = v.getZ();
			Vector vo = this.getOwner().getLocation().getDirection();
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
		}
	}
	
	public ServerPlayer getValidRider(){
		Entity passenger = getFirstPassenger();
		if(!(passenger instanceof ServerPlayer serverPlayer)){
			return null;
		}
		if(serverPlayer.getBukkitEntity() != this.getOwner().getPlayer()){
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
		IPetType pt = this.getPet().getPetType();
		float speed = rideSpeed;
		if(NMSEntityUtil.getJumpingField() != null && !passengers.isEmpty()){
			if(pt.canFly()){
				if(!onGround){
					speed = flySpeed;
				}
				try{
					if(getOwner().isFlying()){
						getOwner().setFlying(false);
					}
					if(NMSEntityUtil.getJumpingField().getBoolean(passenger)){
						PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
						EchoPet.getPlugin().getServer().getPluginManager().callEvent(rideEvent);
						if(!rideEvent.isCancelled()){
							setDeltaMovement(getDeltaMovement().x, 0.5F, getDeltaMovement().z);
						}
					}
				}catch(IllegalArgumentException | IllegalStateException | IllegalAccessException e){
					Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Flying Motion for " + this.getOwner().getName() + "'s Pet.", e, true);
				}
			}else if(this.onGround){
				try{
					if(NMSEntityUtil.getJumpingField().getBoolean(passenger)){
						PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
						EchoPet.getPlugin().getServer().getPluginManager().callEvent(rideEvent);
						if(!rideEvent.isCancelled()){
							setDeltaMovement(getDeltaMovement().x, rideEvent.getJumpHeight(), getDeltaMovement().z);
						}
					}
				}catch(IllegalArgumentException | IllegalStateException | IllegalAccessException e){
					Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Jumping Motion for " + this.getOwner().getName() + "'s Pet.", e, true);
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
	
	@Override
	public abstract SizeCategory getSizeCategory();
	
	// Entity
	@Override
	public void tick(){// Search for "entityBaseTick". The method its in.
		super.tick();
		onLive();
		if(this.petGoalSelector == null){
			this.remove(false);
			return;
		}
		if(!isPassenger() || getPet().getRider() == null){
			this.petGoalSelector.updateGoals();
		}
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
