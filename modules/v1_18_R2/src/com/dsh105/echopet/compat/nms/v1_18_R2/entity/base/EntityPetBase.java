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

package com.dsh105.echopet.compat.nms.v1_18_R2.entity.base;

import com.dsh105.echopet.compat.api.ai.IPetGoalSelector;
import com.dsh105.echopet.compat.api.ai.PetGoalSelector;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.event.PetRideJumpEvent;
import com.dsh105.echopet.compat.api.event.PetRideMoveEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Perm;
import com.dsh105.echopet.compat.api.util.menu.PetMenu;
import com.dsh105.echopet.compat.nms.v1_18_R2.NMSEntityUtil;
import com.dsh105.echopet.compat.nms.v1_18_R2.entity.EntityPetGiveMeAccess;
import com.dsh105.echopet.compat.nms.v1_18_R2.entity.INMSEntityPetBase;
import com.dsh105.echopet.compat.nms.v1_18_R2.entity.ai.PetGoalFloat;
import com.dsh105.echopet.compat.nms.v1_18_R2.entity.ai.PetGoalFollowOwner;
import com.dsh105.echopet.compat.nms.v1_18_R2.entity.ai.PetGoalLookAtPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class EntityPetBase implements INMSEntityPetBase{
	
	private final IEntityPet entityPet;
	private PetGoalSelector petGoalSelector;
	protected double jumpHeight;
	protected float rideSpeed, flySpeed;
	public boolean shouldVanish;
	
	public EntityPetBase(IEntityPet entityPet){
		this.entityPet = entityPet;
		initiateEntityPet();
	}
	
	@Override
	public IEntityPet getEntityPet(){
		return entityPet;
	}
	
	public IPet getPet(){
		return getEntityPet().getPet();
	}
	
	public LivingEntity getEntity(){
		return (LivingEntity) entityPet;
	}
	
	protected void initiateEntityPet(){
		this.rideSpeed = getPet().getPetType().getRideSpeed();
		this.flySpeed = getPet().getPetType().getFlySpeed();
		this.jumpHeight = getPet().getPetType().getRideJumpHeight();
		AttributeInstance attributeInstance = getEntity().getAttribute(Attributes.MOVEMENT_SPEED);
		if(attributeInstance != null){
			attributeInstance.setBaseValue(getPet().getPetType().getWalkSpeed());
		}
		this.setPathfinding();
		getEntity().maxUpStep = getEntityPet().getMaxUpStep();
	}
	
	public void setPathfinding(){
		try{
			petGoalSelector = new PetGoalSelector();
			if(getEntity() instanceof Mob mob){// We support living entities but only mobs have ai.
				petGoalSelector.addGoal(new PetGoalFloat(mob), 0);
				/*if(pet.getPetType().equals(PetType.BEE)){
					petGoalSelector.addGoal(new PetGoalBeeWander(this), 1);
				}else{
					petGoalSelector.addGoal(new PetGoalFollowOwner(this), 1);
				}*/
				petGoalSelector.addGoal(new PetGoalFollowOwner(getEntityPet(), mob), 1);
				petGoalSelector.addGoal(new PetGoalLookAtPlayer(getEntityPet(), mob, ServerPlayer.class), 2);
			}
		}catch(Exception e){
			EchoPet.LOG.log(java.util.logging.Level.WARNING, "Could not add PetGoals to Pet AI.", e);
		}
	}
	
	@Override
	public IPetGoalSelector getPetGoalSelector(){
		return petGoalSelector;
	}
	
	@Override
	public boolean onInteract(Player player){
		if(player.getUniqueId().equals(getPet().getOwner().getUniqueId())){
			if(getPet().getPetType().isInteractMenuEnabled() && Perm.BASE_MENU.hasPerm(getPet().getOwner(), false, false)){
				new PetMenu(getPet()).open(false);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void remove(boolean makeSound){
		CraftEntity craftEntity = getEntity().getBukkitEntity();
		if(craftEntity != null){
			craftEntity.leaveVehicle();
			craftEntity.remove();
		}
		if(makeSound){
			if(getEntityPet() instanceof EntityPetGiveMeAccess handle){
				SoundEvent sound = handle.publicDeathSound();
				if(sound != null){
					getEntity().playSound(sound, 1.0F, 1.0F);// was makeSound in 1.8
				}
			}
		}
	}
	
	@Override
	public void tick(){// Search for "entityBaseTick". The method its in.
		IPet pet = getPet();
		if(pet == null){
			remove(false);
			return;
		}
		if(this.petGoalSelector == null){
			remove(false);
			return;
		}
		Player owner = pet.getOwner();
		if(owner == null || !owner.isOnline()){
			EchoPet.getManager().removePet(pet, true);
			return;
		}
		ServerPlayer nmsOwner = ((CraftPlayer) owner).getHandle();
		LivingEntity entity = getEntity();
		if(nmsOwner.isInvisible() != entity.isInvisible() && !this.shouldVanish){
			entity.setInvisible(!entity.isInvisible());
		}
		if(nmsOwner.isShiftKeyDown() != entity.isShiftKeyDown()){
			entity.setShiftKeyDown(!entity.isShiftKeyDown());
		}
		if(nmsOwner.isSprinting() != entity.isSprinting()){
			entity.setSprinting(!entity.isSprinting());
		}
		if(pet.isHat()){
			// this.lastYaw = this.yRot = (this.getPet().getPetType() == PetType.ENDERDRAGON ? this.getPlayerOwner().getLocation().getYaw() - 180 : this.getPlayerOwner().getLocation().getYaw());
			entity.setYRot(owner.getLocation().getYaw());
			entity.yRotO = entity.getYRot();
		}
		if(owner.isFlying() && getPet().getPetType().canFly()){
			// if(this.getEntityPetType() == PetType.VEX && !((IVexPet) this.getPet()).isPowered()) return;
			Location petLoc = entity.getBukkitEntity().getLocation();
			Location ownerLoc = owner.getLocation();
			Vector v = ownerLoc.toVector().subtract(petLoc.toVector());
			double x = v.getX();
			double y = v.getY();
			double z = v.getZ();
			Vector vo = owner.getLocation().getDirection();
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
			setVelocity(new Vector(x, y, z).normalize().multiply(0.3F));
		}
		IPet rider = pet.getRider();
		if(!entity.isPassenger() || (rider == null || !rider.isSpawned())){
			this.petGoalSelector.updateGoals();
		}
	}
	
	
	public ServerPlayer getValidRider(){
		Entity entity = getEntity();
		Entity passenger = entity.getFirstPassenger();
		if(!(passenger instanceof ServerPlayer serverPlayer)){
			return null;
		}
		if(serverPlayer.getBukkitEntity() != getPet().getOwner().getPlayer()){
			return null;
		}
		return serverPlayer;
	}
	
	@Override
	public float getSpeed(){
		LivingEntity entity = getEntity();
		float speed = rideSpeed;
		if(NMSEntityUtil.getJumpingField() != null && !entity.getPassengers().isEmpty()){
			if(getPet().getPetType().canFly()){
				if(!entity.isOnGround()){
					speed = flySpeed;
				}
			}
		}
		return speed;
	}
	
	@Override
	public Vec3 travel(Vec3 vec3d){
		ServerPlayer passenger = getValidRider();
		if(passenger == null){
			return null;
		}
		Player player = getPet().getOwner();
		LivingEntity entity = getEntity();
		entity.setYRot(passenger.getYRot());
		entity.yRotO = entity.getYRot();
		entity.setXRot(passenger.getXRot() * 0.5F);
		// entity.setRot(entity.getYRot(), entity.getXRot());
		entity.setYBodyRot(entity.getYRot());
		entity.setYHeadRot(entity.getYRot());
		
		double motX = passenger.xxa * 0.5;
		double motY = vec3d.y;
		double motZ = passenger.zza;
		if(motZ <= 0){
			motZ *= 0.25F;
		}
		entity.flyingSpeed = entity.getSpeed() * 0.1F;
		PetRideMoveEvent moveEvent = new PetRideMoveEvent(this.getPet(), (float) motX, (float) motZ);// side, forward
		EchoPet.getPlugin().getServer().getPluginManager().callEvent(moveEvent);
		if(moveEvent.isCancelled()){
			return null;
		}
		IPetType pt = this.getPet().getPetType();
		if(NMSEntityUtil.getJumpingField() != null && !entity.getPassengers().isEmpty()){
			if(pt.canFly()){
				try{
					if(player.isFlying()){
						player.setFlying(false);
					}
					if(NMSEntityUtil.getJumpingField().getBoolean(passenger)){
						PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
						EchoPet.getPlugin().getServer().getPluginManager().callEvent(rideEvent);
						if(!rideEvent.isCancelled()){
							entity.setDeltaMovement(entity.getDeltaMovement().x, 0.5F, entity.getDeltaMovement().z);
						}
					}
				}catch(IllegalArgumentException | IllegalStateException | IllegalAccessException e){
					EchoPet.LOG.log(java.util.logging.Level.WARNING, "Failed to initiate Pet Flying Motion for " + player.getName() + "'s Pet.", e);
				}
			}else if(entity.isOnGround()){
				try{
					if(NMSEntityUtil.getJumpingField().getBoolean(passenger)){
						PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
						EchoPet.getPlugin().getServer().getPluginManager().callEvent(rideEvent);
						if(!rideEvent.isCancelled()){
							entity.setDeltaMovement(entity.getDeltaMovement().x, rideEvent.getJumpHeight(), entity.getDeltaMovement().z);
						}
					}
				}catch(IllegalArgumentException | IllegalStateException | IllegalAccessException e){
					EchoPet.LOG.log(java.util.logging.Level.WARNING, "Failed to initiate Pet Jumping Motion for " + player.getName() + "'s Pet.", e);
				}
			}
		}
		return new Vec3(motX, motY, motZ);
	}
	
	public void setVelocity(Vector vel){
		LivingEntity entity = getEntity();
		entity.setDeltaMovement(vel.getX(), vel.getY(), vel.getZ());
		entity.hurtMarked = true;
	}
	
	public void setBaby(boolean flag){
		if(getEntity() instanceof Mob mob){
			mob.setBaby(flag);
		}
	}
}
