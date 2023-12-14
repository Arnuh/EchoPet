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

package com.dsh105.echopet.nms.entity.ai.brain.behavior;

import com.dsh105.echopet.compat.api.entity.nms.IEntityPet;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;

public class SetOwnerLookTarget extends Behavior<LivingEntity>{
	
	private ServerPlayer owner;
	
	public SetOwnerLookTarget(){
		super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT));
	}
	
	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, LivingEntity mob){
		if(owner == null && mob instanceof IEntityPet entityPet){
			owner = ((CraftPlayer) entityPet.getPetOwner()).getHandle();
		}
		return owner != null;
	}
	
	@Override
	protected void start(ServerLevel level, LivingEntity mob, long time){
		mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new PetEntityTracker(owner, true));
	}
}
