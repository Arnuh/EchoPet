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

package com.dsh105.echopet.nms.entity.ai.brain;

import com.dsh105.echopet.nms.entity.type.EntitySnifferPet;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class PetSnifferAi{
	
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final int MAX_LOOK_DISTANCE = 6;
	public static final List<SensorType<? extends Sensor<? super EntitySnifferPet>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.NEAREST_PLAYERS, SensorType.SNIFFER_TEMPTATIONS);
	public static final List<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.IS_PANICKING, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryModuleType.SNIFFER_DIGGING, MemoryModuleType.SNIFFER_HAPPY, MemoryModuleType.SNIFF_COOLDOWN, MemoryModuleType.SNIFFER_EXPLORED_POSITIONS, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.BREED_TARGET, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED);
	public static final ImmutableList<SensorType<? extends Sensor<? super EntitySnifferPet>>> EMPTY_SENSOR_TYPES = ImmutableList.of();
	private static final int SNIFFING_COOLDOWN_TICKS = 9600;
	private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
	private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
	private static final float SPEED_MULTIPLIER_WHEN_SNIFFING = 1.25F;
	private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
	
	public static Ingredient getTemptations(){
		return Ingredient.of(Items.TORCHFLOWER_SEEDS);
	}
	
	protected static Brain<?> makeBrain(Brain<Sniffer> brain){
		initCoreActivity(brain);
		initIdleActivity(brain);
		initSniffingActivity(brain);
		initDigActivity(brain);
		brain.setCoreActivities(Set.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}
	
	static Sniffer resetSniffing(Sniffer sniffer){
		sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_DIGGING);
		sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
		return sniffer.transitionTo(Sniffer.State.IDLING);
	}
	
	private static void initCoreActivity(Brain<Sniffer> brain){
		brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new AnimalPanic(2.0F){
			@Override
			protected void start(ServerLevel serverLevel, PathfinderMob pathfinderMob, long l){
				PetSnifferAi.resetSniffing((Sniffer) pathfinderMob);
				super.start(serverLevel, pathfinderMob, l);
			}
		}, new MoveToTargetSink(10000, 15000), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
	}
	
	private static void initSniffingActivity(Brain<Sniffer> brain){
		brain.addActivityWithConditions(Activity.SNIFF, ImmutableList.of(Pair.of(0, new PetSnifferAi.Searching())), Set.of(Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT), Pair.of(MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT)));
	}
	
	private static void initDigActivity(Brain<Sniffer> brain){
		brain.addActivityWithConditions(Activity.DIG, ImmutableList.of(Pair.of(0, new PetSnifferAi.Digging(160, 180)), Pair.of(0, new PetSnifferAi.FinishedDigging(40))), Set.of(Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT), Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), Pair.of(MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_PRESENT)));
	}
	
	private static void initIdleActivity(Brain<Sniffer> brain){
		brain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(Pair.of(0, new AnimalMakeLove(EntityType.SNIFFER, 1.0F){
			@Override
			protected void start(ServerLevel serverLevel, Animal animal, long l){
				PetSnifferAi.resetSniffing((Sniffer) animal);
				super.start(serverLevel, animal, l);
			}
		}), Pair.of(1, new FollowTemptation((sniffer)->{
			return 1.25F;
		}, (sniffer)->{
			return sniffer.isBaby() ? 2.5D : 3.5D;
		}){
			@Override
			protected void start(ServerLevel serverLevel, PathfinderMob pathfinderMob, long l){
				PetSnifferAi.resetSniffing((Sniffer) pathfinderMob);
				super.start(serverLevel, pathfinderMob, l);
			}
		}), Pair.of(2, new LookAtTargetSink(45, 90)), Pair.of(3, new PetSnifferAi.FeelingHappy(40, 100)), Pair.of(4, new RunOne<>(ImmutableList.of(Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 2), Pair.of(new PetSnifferAi.Scenting(40, 80), 1), Pair.of(new PetSnifferAi.Sniffing(40, 80), 1), Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 6.0F), 1), Pair.of(RandomStroll.stroll(1.0F), 1), Pair.of(new DoNothing(5, 20), 2))))), Set.of(Pair.of(MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_ABSENT)));
	}
	
	public static void updateActivity(Sniffer sniffer){
		sniffer.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.DIG, Activity.SNIFF, Activity.IDLE));
	}
	
	static class Digging extends Behavior<Sniffer>{
		
		Digging(int minRunTime, int maxRunTime){
			super(Map.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_PRESENT, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.VALUE_ABSENT), minRunTime, maxRunTime);
		}
		
		@Override
		protected boolean checkExtraStartConditions(ServerLevel world, Sniffer entity){
			return entity.canSniff();
		}
		
		@Override
		protected boolean canStillUse(ServerLevel world, Sniffer entity, long time){
			return entity.getBrain().getMemory(MemoryModuleType.SNIFFER_DIGGING).isPresent() && entity.canDig() && !entity.isInLove();
		}
		
		@Override
		protected void start(ServerLevel serverLevel, Sniffer sniffer, long l){
			sniffer.transitionTo(Sniffer.State.DIGGING);
		}
		
		@Override
		protected void stop(ServerLevel serverLevel, Sniffer sniffer, long l){
			boolean bl = this.timedOut(l);
			if(bl){
				sniffer.getBrain().setMemoryWithExpiry(MemoryModuleType.SNIFF_COOLDOWN, Unit.INSTANCE, 9600L);
			}else{
				PetSnifferAi.resetSniffing(sniffer);
			}
			
		}
	}
	
	static class FeelingHappy extends Behavior<Sniffer>{
		
		FeelingHappy(int minRunTime, int maxRunTime){
			super(Map.of(MemoryModuleType.SNIFFER_HAPPY, MemoryStatus.VALUE_PRESENT), minRunTime, maxRunTime);
		}
		
		@Override
		protected boolean canStillUse(ServerLevel world, Sniffer entity, long time){
			return true;
		}
		
		@Override
		protected void start(ServerLevel serverLevel, Sniffer sniffer, long l){
			sniffer.transitionTo(Sniffer.State.FEELING_HAPPY);
		}
		
		@Override
		protected void stop(ServerLevel serverLevel, Sniffer sniffer, long l){
			sniffer.transitionTo(Sniffer.State.IDLING);
			sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_HAPPY);
		}
	}
	
	static class FinishedDigging extends Behavior<Sniffer>{
		
		FinishedDigging(int runTime){
			super(Map.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_PRESENT, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.VALUE_PRESENT), runTime, runTime);
		}
		
		@Override
		protected boolean checkExtraStartConditions(ServerLevel world, Sniffer entity){
			return true;
		}
		
		@Override
		protected boolean canStillUse(ServerLevel world, Sniffer entity, long time){
			return entity.getBrain().getMemory(MemoryModuleType.SNIFFER_DIGGING).isPresent();
		}
		
		@Override
		protected void start(ServerLevel serverLevel, Sniffer sniffer, long l){
			sniffer.transitionTo(Sniffer.State.RISING);
		}
		
		@Override
		protected void stop(ServerLevel serverLevel, Sniffer sniffer, long l){
			boolean bl = this.timedOut(l);
			sniffer.transitionTo(Sniffer.State.IDLING).onDiggingComplete(bl);
			sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_DIGGING);
			sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_HAPPY, true);
		}
	}
	
	static class Scenting extends Behavior<Sniffer>{
		
		Scenting(int minRunTime, int maxRunTime){
			super(Map.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_HAPPY, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), minRunTime, maxRunTime);
		}
		
		@Override
		protected boolean checkExtraStartConditions(ServerLevel world, Sniffer entity){
			return !entity.isTempted();
		}
		
		@Override
		protected boolean canStillUse(ServerLevel world, Sniffer entity, long time){
			return true;
		}
		
		@Override
		protected void start(ServerLevel serverLevel, Sniffer sniffer, long l){
			sniffer.transitionTo(Sniffer.State.SCENTING);
		}
		
		@Override
		protected void stop(ServerLevel serverLevel, Sniffer sniffer, long l){
			sniffer.transitionTo(Sniffer.State.IDLING);
		}
	}
	
	static class Searching extends Behavior<Sniffer>{
		
		Searching(){
			super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_PRESENT), 600);
		}
		
		@Override
		protected boolean checkExtraStartConditions(ServerLevel world, Sniffer entity){
			return entity.canSniff();
		}
		
		@Override
		protected boolean canStillUse(ServerLevel world, Sniffer entity, long time){
			if(!entity.canSniff()){
				entity.transitionTo(Sniffer.State.IDLING);
				return false;
			}else{
				Optional<BlockPos> optional = entity.getBrain()
					.getMemory(MemoryModuleType.WALK_TARGET)
					.map(WalkTarget::getTarget)
					.map(PositionTracker::currentBlockPosition);
				Optional<BlockPos> optional2 = entity.getBrain().getMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
				return !optional.isEmpty() && !optional2.isEmpty() ? optional2.get().equals(optional.get()) : false;
			}
		}
		
		@Override
		protected void start(ServerLevel serverLevel, Sniffer sniffer, long l){
			sniffer.transitionTo(Sniffer.State.SEARCHING);
		}
		
		@Override
		protected void stop(ServerLevel serverLevel, Sniffer sniffer, long l){
			if(sniffer.canDig() && sniffer.canSniff()){
				sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_DIGGING, true);
			}
			
			sniffer.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
			sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
		}
	}
	
	static class Sniffing extends Behavior<Sniffer>{
		
		Sniffing(int minRunTime, int maxRunTime){
			super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.VALUE_ABSENT), minRunTime, maxRunTime);
		}
		
		@Override
		protected boolean checkExtraStartConditions(ServerLevel world, Sniffer entity){
			return !entity.isBaby() && entity.canSniff();
		}
		
		@Override
		protected boolean canStillUse(ServerLevel world, Sniffer entity, long time){
			return entity.canSniff();
		}
		
		@Override
		protected void start(ServerLevel serverLevel, Sniffer sniffer, long l){
			sniffer.transitionTo(Sniffer.State.SNIFFING);
		}
		
		@Override
		protected void stop(ServerLevel serverLevel, Sniffer sniffer, long l){
			boolean bl = this.timedOut(l);
			sniffer.transitionTo(Sniffer.State.IDLING);
			if(bl){
				sniffer.calculateDigPosition().ifPresent((pos)->{
					sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET, pos);
					sniffer.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(pos, 1.25F, 0));
				});
			}
			
		}
	}
}
