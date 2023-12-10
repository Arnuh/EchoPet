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

package com.dsh105.echopet.nms.entity.ai;

import java.util.EnumSet;
import com.dsh105.echopet.compat.api.ai.APetGoalFollowOwner;
import com.dsh105.echopet.compat.api.ai.PetGoal;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.nms.IEntityPet;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.event.PetMoveEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.nms.VersionBreaking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;

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
		
		IPetType petType = pet.getPet().getPetType();
		double startDistance = IPet.GOAL_FOLLOW_START_DISTANCE.getNumber(petType).doubleValue();
		double stopDistance = IPet.GOAL_FOLLOW_STOP_DISTANCE.getNumber(petType).doubleValue();
		double teleportDistance = IPet.GOAL_FOLLOW_TELEPORT_DISTANCE.getNumber(petType).doubleValue();
		
		this.speedModifier = IPet.GOAL_FOLLOW_SPEED_MODIFIER.getNumber(petType).doubleValue();
		this.startDistanceSqr = startDistance * startDistance;
		this.stopDistanceSqr = (stopDistance * stopDistance);
		this.teleportDistanceSqr = teleportDistance * teleportDistance;
		this.setFlags(EnumSet.of(PetGoal.Flag.MOVE, PetGoal.Flag.LOOK));
	}
	
	@Override
	public String getIdentifier(){
		return "FollowOwner";
	}
	
	@Override
	public boolean canUse(){
		if(!this.mob.isAlive()){
			return false;
		}else if(this.pet.getPetOwner() == null){
			return false;
		}else if(this.pet.getPet().isOwnerRiding() || this.pet.getPet().isHat()){
			return false;
		}else if(mob.distanceToSqr(((CraftPlayer) this.pet.getPetOwner()).getHandle()) < this.startDistanceSqr){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canContinueToUse(){
		return !getNavigation().isDone() && this.mob.distanceToSqr(((CraftPlayer) this.pet.getPetOwner()).getHandle()) > stopDistanceSqr;
	}
	
	@Override
	public void start(){
		this.timeToRecalcPath = 0;
		// Set pathfinding radius
		mob.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(this.teleportDistanceSqr);
	}
	
	@Override
	public void stop(){
		getNavigation().stop();
	}
	
	@Override
	public void tick(){
		ServerPlayer owner = ((CraftPlayer) this.pet.getPetOwner()).getHandle();
		
		mob.getLookControl().setLookAt(owner, 10.0F, (float) mob.getMaxHeadXRot());
		if(--this.timeToRecalcPath <= 0){
			this.timeToRecalcPath = 10;
			if(mob.distanceToSqr(owner) > this.teleportDistanceSqr && VersionBreaking.onGround(((CraftPlayer) this.pet.getPetOwner()).getHandle()) || this.pet.getPetOwner()
				.isInsideVehicle()){
				this.pet.getPet().teleportToOwner();
				return;
			}
			PetMoveEvent moveEvent = new PetMoveEvent(this.pet.getPet(), this.pet.getPet().getLocation(), this.pet.getPetOwner().getLocation());
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
