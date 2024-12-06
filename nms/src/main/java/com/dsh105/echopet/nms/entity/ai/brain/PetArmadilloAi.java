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

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import com.dsh105.echopet.nms.entity.type.EntityArmadilloPet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
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
import net.minecraft.world.entity.ai.behavior.OneShot;
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
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.armadillo.ArmadilloAi;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;

public class PetArmadilloAi{
	
	private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
	private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
	private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
	private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 1.25F;
	private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 1.0F;
	private static final double DEFAULT_CLOSE_ENOUGH_DIST = 2.0;
	private static final double BABY_CLOSE_ENOUGH_DIST = 1.0;
	private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
	// private static final ImmutableList<SensorType<? extends Sensor<? super Armadillo>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.ARMADILLO_TEMPTATIONS, SensorType.NEAREST_ADULT, SensorType.ARMADILLO_SCARE_DETECTED);
	private static final ImmutableList<SensorType<? extends Sensor<? super EntityArmadilloPet>>> SENSOR_TYPES = ImmutableList.of();
	private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.IS_PANICKING, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.GAZE_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.BREED_TARGET, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.DANGER_DETECTED_RECENTLY);
	private static final OneShot<Armadillo> ARMADILLO_ROLLING_OUT = BehaviorBuilder.create(context->context.group(context.absent(MemoryModuleType.DANGER_DETECTED_RECENTLY))
		.apply(context, memoryAccessor->(serverLevel, armadillo, l)->{
			if(armadillo.isScared()){
				armadillo.rollOut();
				return true;
			}else{
				return false;
			}
		}));
	
	public static Brain.Provider<EntityArmadilloPet> brainProvider(){
		return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
	}
	
	public static Brain<?> makeBrain(Brain<EntityArmadilloPet> brain){
		initCoreActivity(brain);
		initIdleActivity(brain);
		initScaredActivity(brain);
		brain.setCoreActivities(Set.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}
	
	private static void initCoreActivity(Brain<EntityArmadilloPet> brain){
		brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim<>(0.8F), new ArmadilloAi.ArmadilloPanic(SPEED_MULTIPLIER_WHEN_PANICKING), new LookAtTargetSink(45, 90), new MoveToTargetSink(){
			@Override
			protected boolean checkExtraStartConditions(ServerLevel world, Mob entity){
				if(entity instanceof EntityArmadilloPet armadillo && armadillo.isScared()){
					return false;
				}
				
				return super.checkExtraStartConditions(world, entity);
			}
		}, new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS), ARMADILLO_ROLLING_OUT));
	}
	
	private static void initIdleActivity(Brain<EntityArmadilloPet> brain){
		//@formatter:off
		brain.addActivity(Activity.IDLE,
			ImmutableList.of(
				Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
				Pair.of(1, new AnimalMakeLove(EntityType.ARMADILLO, SPEED_MULTIPLIER_WHEN_MAKING_LOVE, 1)),
				Pair.of(2, new RunOne<>(ImmutableList.of(Pair.of(new FollowTemptation(armadillo->SPEED_MULTIPLIER_WHEN_TEMPTED, armadillo->armadillo.isBaby() ? BABY_CLOSE_ENOUGH_DIST : DEFAULT_CLOSE_ENOUGH_DIST), 1),
					Pair.of(BabyFollowAdult.create(ADULT_FOLLOW_RANGE, SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT), 1)))), Pair.of(3, new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F)),
				Pair.of(4, new RunOne<>(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), ImmutableList.of(Pair.of(RandomStroll.stroll(SPEED_MULTIPLIER_WHEN_IDLING), 1),
					Pair.of(SetWalkTargetFromLookTarget.create(SPEED_MULTIPLIER_WHEN_IDLING, 3), 1),
					Pair.of(new DoNothing(30, 60), 1)))
				)
			)
		);
		//@formatter:onn
	}
	
	private static void initScaredActivity(Brain<EntityArmadilloPet> brain){
		brain.addActivityWithConditions(Activity.PANIC, ImmutableList.of(Pair.of(0, new ArmadilloAi.ArmadilloBallUp())), Set.of(Pair.of(MemoryModuleType.DANGER_DETECTED_RECENTLY, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT)));
	}
	
	public static void updateActivity(EntityArmadilloPet armadillo){
		armadillo.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.PANIC, Activity.IDLE));
	}
	
	public static Predicate<ItemStack> getTemptations(){
		return stack->stack.is(ItemTags.ARMADILLO_FOOD);
	}
	
	public static class ArmadilloBallUp extends Behavior<EntityArmadilloPet>{
		
		static final int BALL_UP_STAY_IN_STATE = 5 * TimeUtil.SECONDS_PER_MINUTE * 20;
		static final int TICKS_DELAY_TO_DETERMINE_IF_DANGER_IS_STILL_AROUND = 5;
		static final int DANGER_DETECTED_RECENTLY_DANGER_THRESHOLD = 75;
		int nextPeekTimer = 0;
		boolean dangerWasAround;
		
		public ArmadilloBallUp(){
			super(Map.of(), BALL_UP_STAY_IN_STATE);
		}
		
		@Override
		protected void tick(ServerLevel serverLevel, EntityArmadilloPet armadillo, long l){
			super.tick(serverLevel, armadillo, l);
			if(this.nextPeekTimer > 0){
				this.nextPeekTimer--;
			}
			
			if(armadillo.shouldSwitchToScaredState()){
				armadillo.switchToState(Armadillo.ArmadilloState.SCARED);
				if(armadillo.onGround()){
					armadillo.playSound(SoundEvents.ARMADILLO_LAND);
				}
			}else{
				Armadillo.ArmadilloState armadilloState = armadillo.getState();
				long m = armadillo.getBrain().getTimeUntilExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY);
				boolean bl = m > DANGER_DETECTED_RECENTLY_DANGER_THRESHOLD;
				if(bl != this.dangerWasAround){
					this.nextPeekTimer = this.pickNextPeekTimer(armadillo);
				}
				
				this.dangerWasAround = bl;
				if(armadilloState == Armadillo.ArmadilloState.SCARED){
					if(this.nextPeekTimer == 0 && armadillo.onGround() && bl){
						serverLevel.broadcastEntityEvent(armadillo, (byte) 64);
						this.nextPeekTimer = this.pickNextPeekTimer(armadillo);
					}
					
					if(m < (long) Armadillo.ArmadilloState.UNROLLING.animationDuration()){
						armadillo.playSound(SoundEvents.ARMADILLO_UNROLL_START);
						armadillo.switchToState(Armadillo.ArmadilloState.UNROLLING);
					}
				}else if(armadilloState == Armadillo.ArmadilloState.UNROLLING && m > (long) Armadillo.ArmadilloState.UNROLLING.animationDuration()){
					armadillo.switchToState(Armadillo.ArmadilloState.SCARED);
				}
			}
		}
		
		private int pickNextPeekTimer(Armadillo entity){
			return Armadillo.ArmadilloState.SCARED.animationDuration() + entity.getRandom().nextIntBetweenInclusive(100, 400);
		}
		
		@Override
		protected boolean checkExtraStartConditions(ServerLevel world, EntityArmadilloPet entity){
			return entity.onGround();
		}
		
		@Override
		protected boolean canStillUse(ServerLevel serverLevel, EntityArmadilloPet armadillo, long l){
			return armadillo.getState().isThreatened();
		}
		
		@Override
		protected void start(ServerLevel serverLevel, EntityArmadilloPet armadillo, long l){
			armadillo.rollUp();
		}
		
		@Override
		protected void stop(ServerLevel serverLevel, EntityArmadilloPet armadillo, long l){
			if(!armadillo.canStayRolledUp()){
				armadillo.rollOut();
			}
		}
	}
	
	public static class ArmadilloPanic extends AnimalPanic<EntityArmadilloPet>{
		
		public ArmadilloPanic(float speed){
			super(speed, entity->DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES);
		}
		
		@Override
		protected void start(ServerLevel serverLevel, EntityArmadilloPet armadillo, long l){
			armadillo.rollOut();
			super.start(serverLevel, armadillo, l);
		}
	}
}
