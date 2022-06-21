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

import java.util.Optional;
import java.util.function.Function;
import com.dsh105.echopet.compat.api.entity.nms.IEntityPet;
import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;

public class BrainFollowOwner extends Behavior<PathfinderMob>{
	
	public static final int TEMPTATION_COOLDOWN = 100;
	public static final int CLOSE_ENOUGH_DIST = 2;
	private ServerPlayer owner;
	private final Function<LivingEntity, Float> speedModifier;
	
	public BrainFollowOwner(Function<LivingEntity, Float> var0){
		super(Util.make(()->{
			ImmutableMap.Builder<MemoryModuleType<?>, MemoryStatus> conditions = ImmutableMap.builder();
			conditions.put(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED);
			conditions.put(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED);
			conditions.put(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT);
			conditions.put(MemoryModuleType.IS_TEMPTED, MemoryStatus.REGISTERED);
			conditions.put(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_PRESENT);
			conditions.put(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT);
			conditions.put(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT);
			return conditions.build();
		}));
		this.speedModifier = var0;
	}
	
	protected float getSpeedModifier(PathfinderMob var0){
		return this.speedModifier.apply(var0);
	}
	
	private Optional<Player> getTemptingPlayer(PathfinderMob mob){
		if(owner == null && mob instanceof IEntityPet entityPet){
			owner = ((CraftPlayer) entityPet.getOwner()).getHandle();
		}
		return Optional.ofNullable(owner);
	}
	
	@Override
	protected boolean timedOut(long var0){
		return false;
	}
	
	@Override
	protected boolean canStillUse(ServerLevel var0, PathfinderMob var1, long var2){
		return this.getTemptingPlayer(var1).isPresent();// && !var1.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET) && !var1.getBrain().hasMemoryValue(MemoryModuleType.IS_PANICKING);
	}
	
	@Override
	protected void start(ServerLevel var0, PathfinderMob var1, long var2){
		var1.getBrain().setMemory(MemoryModuleType.IS_TEMPTED, true);
	}
	
	@Override
	protected void stop(ServerLevel var0, PathfinderMob var1, long var2){
		Brain<?> var4 = var1.getBrain();
		var4.setMemory(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, TEMPTATION_COOLDOWN);
		var4.setMemory(MemoryModuleType.IS_TEMPTED, false);
		var4.eraseMemory(MemoryModuleType.WALK_TARGET);
		var4.eraseMemory(MemoryModuleType.LOOK_TARGET);
	}
	
	@Override
	protected void tick(ServerLevel var0, PathfinderMob var1, long var2){
		Player var4 = this.getTemptingPlayer(var1).get();
		Brain<?> var5 = var1.getBrain();
		var5.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(var4, true));
		if(var1.distanceToSqr(var4) < 6.25){
			var5.eraseMemory(MemoryModuleType.WALK_TARGET);
		}else{
			var5.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(var4, false), this.getSpeedModifier(var1), CLOSE_ENOUGH_DIST));
		}
		
	}
}
