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

package com.dsh105.echopet.compat.nms.v1_17_R1.entity.ai.bee;

import com.dsh105.echopet.compat.api.ai.APetGoalFloat;
import com.dsh105.echopet.compat.api.ai.PetGoalType;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.ai.util.PetAirAndWaterRandomPos;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.ai.util.PetHoverRandomPos;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;

public class PetGoalBeeWander extends APetGoalFloat{
	
	private final EntityPet pet;
	private final double teleportDistanceSqr;
	
	public PetGoalBeeWander(EntityPet pet){
		this.pet = pet;
		double sizeModifier = pet.getSizeCategory().getModifier();
		double teleportDistance = pet.getPet().getPetType().getTeleportDistance() * sizeModifier;
		this.teleportDistanceSqr = teleportDistance * teleportDistance;
	}
	
	@Override
	public PetGoalType getType(){
		return PetGoalType.FOUR;
	}
	
	@Override
	public String getDefaultKey(){
		return "BeeWander";
	}
	
	@Override
	public boolean shouldStart(){
		return getNavigation().isDone() && pet.getRandom().nextInt(10) == 0;
	}
	
	@Override
	public boolean shouldContinue(){
		return getNavigation().isInProgress();
	}
	
	@Override
	public void start(){
		// Its used in createPath
		pet.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(this.teleportDistanceSqr);
		
		Vec3 vec3d = this.findPos();
		if(vec3d != null){
			getNavigation().moveTo(getNavigation().createPath(new BlockPos(vec3d), 1), 1.0D);// reachRange, speedModifier
		}
	}
	
	@Override
	public void tick(){
	}
	
	private Vec3 findPos(){
		Vec3 vec3d;
		ServerPlayer owner = ((CraftPlayer) pet.getOwner()).getHandle();
		if(!pet.closerThan(owner, 22)){
			Vec3 vec3d1 = atCenterOf(owner.position());
			vec3d = vec3d1.subtract(pet.position()).normalize();
		}else{
			vec3d = pet.getViewVector(0.0F);
		}
		Vec3 vec3d2 = PetHoverRandomPos.getPos(pet, 8, 7, vec3d.x, vec3d.z, 1.5707964F, 3, 1);
		return vec3d2 != null ? vec3d2 : PetAirAndWaterRandomPos.getPos(pet, 8, 4, -2, vec3d.x, vec3d.z, 1.5707963705062866D);
	}
	
	public PathNavigation getNavigation(){
		return pet.getNavigation();
	}
	
	public static Vec3 atCenterOf(Vec3 var0){
		return new Vec3((int) var0.x() + 0.5D, (int) var0.y() + 0.5D, (int) var0.z() + 0.5D);
	}
}
