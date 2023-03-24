/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  EchoPet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.nms.entity.ai.brain;


import java.util.function.Predicate;
import com.dsh105.echopet.nms.entity.type.EntityCamelPet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.ai.behavior.AnimalPanic;
import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.FollowTemptation;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomLookAround;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.crafting.Ingredient;

public class PetCamelAi{
	
	private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 4.0F;
	private static final float SPEED_MULTIPLIER_WHEN_IDLING = 2.0F;
	private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 2.5F;
	private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 2.5F;
	private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 1.0F;
	private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
	private static final ImmutableList<SensorType<? extends Sensor<? super EntityCamelPet>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.CAMEL_TEMPTATIONS, SensorType.NEAREST_ADULT);
	private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.IS_PANICKING, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.GAZE_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.BREED_TARGET, MemoryModuleType.NEAREST_VISIBLE_ADULT);
	protected static final ImmutableList<SensorType<? extends Sensor<? super EntityCamelPet>>> EMPTY_SENSOR_TYPES = ImmutableList.of();
	
	protected static void initMemories(EntityCamelPet camel, RandomSource randomSource){
	}
	
	public static Brain.Provider<EntityCamelPet> brainProvider(boolean usesBrain){
		return Brain.provider(MEMORY_TYPES, usesBrain ? SENSOR_TYPES : EMPTY_SENSOR_TYPES);
	}
	
	public static Brain<?> makeBrain(Brain<EntityCamelPet> brain){
		initCoreActivity(brain);
		initIdleActivity(brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}
	
	private static void initCoreActivity(Brain<EntityCamelPet> brain){
		//@formatter:off
		brain.addActivity(Activity.CORE, 0,
			ImmutableList.of(
				new Swim(0.8F),
				new CamelPanic(SPEED_MULTIPLIER_WHEN_PANICKING),
				new LookAtTargetSink(45, 90),
				new MoveToTargetSink(),
				new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
				new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS)
			)
		);
		//@formatter:on
	}
	
	private static void initIdleActivity(Brain<EntityCamelPet> brain){
		//@formatter:off
		brain.addActivity(Activity.IDLE,
			ImmutableList.of(
				Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
				Pair.of(1, new AnimalMakeLove(EntityType.CAMEL, SPEED_MULTIPLIER_WHEN_MAKING_LOVE)),
				Pair.of(2, new FollowTemptation((entity)->{
					return SPEED_MULTIPLIER_WHEN_TEMPTED;
				})),
				Pair.of(3, BehaviorBuilder.triggerIf(Predicate.not(Camel::refuseToMove), BabyFollowAdult.create(ADULT_FOLLOW_RANGE, SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT))),
				Pair.of(4, new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F)),
				Pair.of(5, new RunOne<>(
					ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
					ImmutableList.of(
						Pair.of(BehaviorBuilder.triggerIf(Predicate.not(Camel::refuseToMove), RandomStroll.stroll(SPEED_MULTIPLIER_WHEN_IDLING)), 1),
						Pair.of(BehaviorBuilder.triggerIf(Predicate.not(Camel::refuseToMove), SetWalkTargetFromLookTarget.create(SPEED_MULTIPLIER_WHEN_IDLING, 3)), 1),
						Pair.of(new RandomSitting(20), 1),
						Pair.of(new DoNothing(30, 60), 1))
					)
				)
			)
		);
		//@formatter:on
	}
	
	public static void updateActivity(EntityCamelPet camel){
		camel.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
	}
	
	public static Ingredient getTemptations(){
		return Camel.TEMPTATION_ITEM;
	}
	
	public static class CamelPanic extends AnimalPanic{
		
		public CamelPanic(float speed){
			super(speed);
		}
		
		@Override
		protected void start(ServerLevel serverLevel, PathfinderMob pathfinderMob, long l){
			if(pathfinderMob instanceof EntityCamelPet camel){
				camel.standUpPanic();
			}
			
			super.start(serverLevel, pathfinderMob, l);
		}
	}
	
	public static class RandomSitting extends Behavior<EntityCamelPet>{
		
		private final int minimalPoseTicks;
		
		public RandomSitting(int lastPoseSecondsDelta){
			super(ImmutableMap.of());
			this.minimalPoseTicks = lastPoseSecondsDelta * 20;
		}
		
		@Override
		protected boolean checkExtraStartConditions(ServerLevel world, EntityCamelPet entity){
			return !entity.isInWater() && entity.getPoseTime() >= (long) this.minimalPoseTicks && !entity.isLeashed() && entity.isOnGround() && !entity.hasControllingPassenger();
		}
		
		@Override
		protected void start(ServerLevel serverLevel, EntityCamelPet camel, long l){
			if(camel.isCamelSitting()){
				camel.standUp();
			}else if(!camel.isPanicking()){
				camel.sitDown();
			}
			
		}
	}
}
