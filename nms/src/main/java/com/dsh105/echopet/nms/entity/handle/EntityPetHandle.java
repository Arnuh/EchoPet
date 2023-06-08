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

package com.dsh105.echopet.nms.entity.handle;

import com.dsh105.echopet.compat.api.ai.IPetGoalSelector;
import com.dsh105.echopet.compat.api.ai.PetGoalSelector;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.nms.IEntityPet;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.event.PetRideJumpEvent;
import com.dsh105.echopet.compat.api.event.PetRideMoveEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.nms.NMSEntityUtil;
import com.dsh105.echopet.nms.VersionBreaking;
import com.dsh105.echopet.nms.entity.EntityPetGiveMeAccess;
import com.dsh105.echopet.nms.entity.INMSEntityPetHandle;
import com.dsh105.echopet.nms.entity.ai.GoalSelectorWrapper;
import com.dsh105.echopet.nms.entity.ai.PetGoalFloat;
import com.dsh105.echopet.nms.entity.ai.PetGoalFollowOwner;
import com.dsh105.echopet.nms.entity.ai.PetGoalLookAtPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.util.Vector;

public class EntityPetHandle implements INMSEntityPetHandle{
	
	protected final ServerPlayer owner;
	protected final IEntityPet entityPet;
	private PetGoalSelector petGoalSelector;
	protected boolean canFly, canControlGravity;
	protected float rideSpeed, rideFlySpeed;
	protected double jumpHeight;
	protected double gravityModifier;
	public boolean shouldVanish;
	
	public EntityPetHandle(IEntityPet entityPet){
		this.entityPet = entityPet;
		this.owner = ((CraftPlayer) entityPet.getPetOwner()).getHandle();
		initiateEntityPet();
	}
	
	@Override
	public CraftPlayer getCraftOwner(){
		return owner.getBukkitEntity();
	}
	
	@Override
	public ServerPlayer getNMSOwner(){
		return owner;
	}
	
	@Override
	public IEntityPet getEntityPet(){
		return entityPet;
	}
	
	public IPet getPet(){
		return getEntityPet().getPet();
	}
	
	public Entity getEntity(){
		return (Entity) getEntityPet();
	}
	
	protected void initiateEntityPet(){
		IPetType petType = getPet().getPetType();
		this.canFly = IPet.RIDING_FLY.get(petType);
		this.canControlGravity = IPet.RIDING_GRAVITY_CONTROL.get(petType);
		this.rideSpeed = IPet.RIDING_WALK_SPEED.getNumber(petType).floatValue();
		this.rideFlySpeed = IPet.RIDING_FLY_SPEED.getNumber(petType).floatValue();
		this.jumpHeight = IPet.RIDING_JUMP_HEIGHT.getNumber(petType).doubleValue();
		this.gravityModifier = IPet.RIDING_GRAVITY_MODIFIER.getNumber(petType).doubleValue();
		this.setPathfinding();
		VersionBreaking.setMaxUpStep(getEntity(), getEntityPet().getMaxUpStep());
	}
	
	public void setPathfinding(){
		try{
			petGoalSelector = new PetGoalSelector();
			if(getEntity() instanceof Mob mob && !getEntityPet().usesBrain()){// We support living entities but only mobs have ai.
				mob.goalSelector = new GoalSelectorWrapper(petGoalSelector);
				setDefaultGoals(mob);
			}
		}catch(Exception e){
			EchoPet.LOG.log(java.util.logging.Level.WARNING, "Could not add PetGoals to Pet AI.", e);
		}
	}
	
	@Override
	public void setDefaultGoals(Mob mob){
		petGoalSelector.removeAllGoals();
		petGoalSelector.addGoal(0, new PetGoalFloat(getEntityPet(), mob));
		petGoalSelector.addGoal(1, new PetGoalFollowOwner(getEntityPet(), mob));
		petGoalSelector.addGoal(2, new PetGoalLookAtPlayer(getEntityPet(), mob, ServerPlayer.class));
	}
	
	@Override
	public IPetGoalSelector getPetGoalSelector(){
		return petGoalSelector;
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
		ServerPlayer nmsOwner = getNMSOwner();
		Entity entity = getEntity();
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
		if(canFly && canControlGravity && !entity.onGround && entity.getDeltaMovement().y < 0.0){
			entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, gravityModifier, 1.0));
		}
		/*if(owner.isFlying() && getPet().getPetType().canFly()){
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
		}*/
	}
	
	
	public ServerPlayer getValidRider(){
		Entity entity = getEntity();
		Entity passenger = entity.getFirstPassenger();
		if(!(passenger instanceof ServerPlayer serverPlayer)){
			return null;
		}
		if(serverPlayer.getBukkitEntity() != getCraftOwner()){
			return null;
		}
		return serverPlayer;
	}
	
	@Override
	public float getSpeed(){
		Entity entity = getEntity();
		float speed = rideSpeed;
		if(NMSEntityUtil.getJumpingField() != null && !entity.getPassengers().isEmpty()){
			if(canFly){
				if(!VersionBreaking.onGround(entity)){
					speed = rideFlySpeed;
				}
			}
		}
		return speed;
	}
	
	@Override
	public void setPose(Pose pose){
		getEntity().setPose(net.minecraft.world.entity.Pose.values()[pose.ordinal()]);
	}
	
	protected void adjustFlyingSpeed(){
		// Stub for LivingEntity
	}
	
	@Override
	public Vec3 travel(Vec3 vec3d){
		ServerPlayer passenger = getValidRider();
		if(passenger == null){
			return null;
		}
		CraftPlayer player = passenger.getBukkitEntity();
		Entity entity = getEntity();
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
		adjustFlyingSpeed();
		PetRideMoveEvent moveEvent = new PetRideMoveEvent(this.getPet(), (float) motX, (float) motZ);// side, forward
		EchoPet.getPlugin().getServer().getPluginManager().callEvent(moveEvent);
		if(moveEvent.isCancelled()){
			return null;
		}
		if(NMSEntityUtil.getJumpingField() != null && !entity.getPassengers().isEmpty()){
			if(canFly){
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
			}else if(VersionBreaking.onGround(entity)){
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
		Entity entity = getEntity();
		entity.setDeltaMovement(vel.getX(), vel.getY(), vel.getZ());
		entity.hurtMarked = true;
	}
	
	public void setBaby(boolean flag){
		if(getEntity() instanceof Mob mob){
			mob.setBaby(flag);
		}
	}
}
