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
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;

public class RandomStrollAroundOwner extends Behavior<PathfinderMob>{
	
	private static final int MAX_XZ_DIST = 10;
	private static final int MAX_Y_DIST = 7;
	private final float speedModifier;
	protected final int maxHorizontalDistance;
	protected final int maxVerticalDistance;
	private final boolean mayStrollFromWater;
	
	private ServerPlayer owner;
	
	public RandomStrollAroundOwner(float speedModifier){
		this(speedModifier, true);
	}
	
	public RandomStrollAroundOwner(float speedModifier, boolean mayStrollFromWater){
		this(speedModifier, MAX_XZ_DIST, MAX_Y_DIST, mayStrollFromWater);
	}
	
	public RandomStrollAroundOwner(float speedModifier, int maxHorzDistance, int maxVertDistance){
		this(speedModifier, maxHorzDistance, maxVertDistance, true);
	}
	
	public RandomStrollAroundOwner(float speedModifier, int maxHorzDistance, int maxVertDistance, boolean mayStrollFromWater){
		super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
		this.speedModifier = speedModifier;
		this.maxHorizontalDistance = maxHorzDistance;
		this.maxVerticalDistance = maxVertDistance;
		this.mayStrollFromWater = mayStrollFromWater;
	}
	
	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob mob){
		return this.mayStrollFromWater || !mob.isInWaterOrBubble();
	}
	
	@Override
	protected void start(ServerLevel level, PathfinderMob mob, long time){
		if(owner == null && mob instanceof IEntityPet entityPet){
			owner = ((CraftPlayer) entityPet.getPetOwner()).getHandle();
		}else return;
		mob.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new PetEntityTracker(owner, false), this.speedModifier, 0));
	}
}
