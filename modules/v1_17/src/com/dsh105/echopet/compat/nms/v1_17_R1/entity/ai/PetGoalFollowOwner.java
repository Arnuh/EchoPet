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

package com.dsh105.echopet.compat.nms.v1_17_R1.entity.ai;

import com.dsh105.echopet.compat.api.ai.APetGoalFollowOwner;
import com.dsh105.echopet.compat.api.ai.PetGoalType;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.event.PetMoveEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;

public class PetGoalFollowOwner extends APetGoalFollowOwner{
	
	// FollowOwnerGoal
	private final IEntityPet pet;
	private final Mob mob;
	private int timeToRecalcPath = 0;
	private final double speedModifier;
	private final double startDistanceSqr;
	private final double stopDistanceSqr;
	private final double teleportDistanceSqr;
	
	public PetGoalFollowOwner(IEntityPet pet, Mob mob){
		this.pet = pet;
		this.mob = mob;
		
		double sizeModifier = pet.getSizeCategory().getModifier();
		IPetType petType = pet.getPet().getPetType();
		double startDistance = petType.getStartFollowDistance() * sizeModifier;
		double stopDistance = petType.getStopFollowDistance() * sizeModifier;
		double teleportDistance = petType.getTeleportDistance() * sizeModifier;
		
		this.speedModifier = petType.getFollowSpeedModifier();
		this.startDistanceSqr = startDistance * startDistance;
		this.stopDistanceSqr = stopDistance * stopDistance;
		this.teleportDistanceSqr = teleportDistance * teleportDistance;
	}
	
	@Override
	public PetGoalType getType(){
		return PetGoalType.THREE;
	}
	
	@Override
	public String getDefaultKey(){
		return "FollowOwner";
	}
	
	@Override
	public boolean shouldStart(){
		if(!this.mob.isAlive()){
			return false;
		}else if(this.pet.getOwner() == null){
			return false;
		}else if(this.pet.getPet().isOwnerRiding() || this.pet.getPet().isHat()){
			return false;
		}else if(mob.distanceToSqr(((CraftPlayer) this.pet.getOwner()).getHandle()) < this.startDistanceSqr){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean shouldContinue(){
		if(mob.getNavigation().isDone()){// Navigation - bottom of class, just returns another method.
			return false;
		}else if(this.pet.getOwner() == null){
			return false;
		}else if(this.pet.getPet().isOwnerRiding() || this.pet.getPet().isHat()){
			return false;
		}else if(mob.distanceToSqr(((CraftPlayer) this.pet.getOwner()).getHandle()) < stopDistanceSqr){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void start(){
		this.timeToRecalcPath = 0;
		// Set pathfinding radius
		mob.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(this.teleportDistanceSqr);
	}
	
	@Override
	public void finish(){
		getNavigation().stop();
	}
	
	@Override
	public void tick(){
		ServerPlayer owner = ((CraftPlayer) this.pet.getOwner()).getHandle();
		
		mob.getLookControl().setLookAt(owner, 10.0F, (float) mob.getMaxHeadXRot());
		if(--this.timeToRecalcPath <= 0){
			this.timeToRecalcPath = 10;
			/*if (this.pet.getPlayerOwner().isFlying()) {
			    //Don't move pet when owner flying
			    return;
			}*/
			if(mob.distanceToSqr(owner) > this.teleportDistanceSqr && ((CraftPlayer) this.pet.getOwner()).getHandle().isOnGround() || this.pet.getOwner().isInsideVehicle()){
				this.pet.getPet().teleportToOwner();
				return;
			}
			PetMoveEvent moveEvent = new PetMoveEvent(this.pet.getPet(), this.pet.getPet().getLocation(), this.pet.getOwner().getLocation());
			EchoPet.getPlugin().getServer().getPluginManager().callEvent(moveEvent);
			if(moveEvent.isCancelled()){
				return;
			}
			getNavigation().moveTo(owner, speedModifier);
		}
	}
	
	public PathNavigation getNavigation(){
		return mob.getNavigation();
	}
}
