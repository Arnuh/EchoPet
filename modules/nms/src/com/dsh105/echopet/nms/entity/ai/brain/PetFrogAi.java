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


import com.dsh105.echopet.nms.entity.ai.brain.behavior.BrainFollowOwner;
import com.dsh105.echopet.nms.entity.ai.brain.behavior.RandomStrollAroundOwner;
import com.dsh105.echopet.nms.entity.ai.brain.behavior.ShootTongueOwner;
import com.dsh105.echopet.nms.entity.type.EntityFrogPet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
import net.minecraft.world.entity.ai.behavior.Croak;
import net.minecraft.world.entity.ai.behavior.GateBehavior;
import net.minecraft.world.entity.ai.behavior.LongJumpMidJump;
import net.minecraft.world.entity.ai.behavior.LongJumpToPreferredBlock;
import net.minecraft.world.entity.ai.behavior.LongJumpToRandomPos;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.behavior.TryFindLand;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class PetFrogAi{
	
	// private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
	// private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 1.0F;
	// private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
	private static final float SPEED_MULTIPLIER_ON_LAND = 1.0F;
	private static final float SPEED_MULTIPLIER_IN_WATER = 0.75F;
	private static final UniformInt TIME_BETWEEN_LONG_JUMPS = UniformInt.of(100, 140);
	private static final int MAX_LONG_JUMP_HEIGHT = 2;
	private static final int MAX_LONG_JUMP_WIDTH = 4;
	private static final float MAX_JUMP_VELOCITY = 1.5F;
	// private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
	
	private static final ImmutableList<Activity> ACTIVITIES = ImmutableList.of(/*Activity.TONGUE, */Activity.LONG_JUMP/*, Activity.SWIM*/, Activity.IDLE);
	
	//@formatter:off
	public PetFrogAi(){
	}
	
	public static Brain<?> makeBrain(Brain<EntityFrogPet> frogBrain){
		initCoreActivity(frogBrain);
		initIdleActivity(frogBrain);
		initSwimActivity(frogBrain);
		// initLaySpawnActivity(var0);
		initTongueActivity(frogBrain);
		initJumpActivity(frogBrain);
		frogBrain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		frogBrain.setDefaultActivity(Activity.IDLE);
		frogBrain.useDefaultActivity();
		return frogBrain;
	}
	
	private static void initCoreActivity(Brain<EntityFrogPet> frogBrain){
		frogBrain.addActivity(Activity.CORE, 0,
			ImmutableList.of(
				//new AnimalPanic(SPEED_MULTIPLIER_WHEN_PANICKING),
				new LookAtTargetSink(45, 90),
				new MoveToTargetSink(150, 250),
				new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
				new CountDownCooldownTicks(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS)
			)
		);
	}
	
	private static void initIdleActivity(Brain<EntityFrogPet> frogBrain){
		frogBrain.addActivityWithConditions(Activity.IDLE,
			ImmutableList.of(
				//Pair.of(0, new RunSometimes<>(new SetEntityLookTarget(EntityType.PLAYER, 6.0F), UniformInt.of(30, 60))),
				Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
				//Pair.of(0, new AnimalMakeLove(EntityType.FROG, SPEED_MULTIPLIER_WHEN_MAKING_LOVE)),
				/*Pair.of(1, new FollowTemptation(entity->{
					return SPEED_MULTIPLIER_WHEN_TEMPTED;
				})),*/
				Pair.of(1, new BrainFollowOwner(entity->{
					return SPEED_MULTIPLIER_ON_LAND;
				})),
				/*Pair.of(2, new StartAttacking<>(PetFrogAi::canAttack, frog -> {
					return frog.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
				})),*/
				Pair.of(3, TryFindLand.create(6, SPEED_MULTIPLIER_ON_LAND)),
				//Pair.of(1, new TryFindOwner(6, SPEED_MULTIPLIER_WHEN_IDLING)),
				Pair.of(4, new RunOne<>(
					ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
					ImmutableList.of(
						//Pair.of(new RandomStroll(SPEED_MULTIPLIER_WHEN_IDLING), 1),
						Pair.of(new RandomStrollAroundOwner(SPEED_MULTIPLIER_ON_LAND), 1),
						Pair.of(SetWalkTargetFromLookTarget.create(SPEED_MULTIPLIER_ON_LAND, 3), 1),
						Pair.of(new Croak(), 3),
						Pair.of(BehaviorBuilder.triggerIf(Entity::isOnGround), 2)
					)
				))
			), ImmutableSet.of(Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT),
			Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT))
		);
	}
	
	private static void initSwimActivity(Brain<EntityFrogPet> frogBrain){
		//TODO: RandomSwim
		frogBrain.addActivityWithConditions(Activity.SWIM,
			ImmutableList.of(
				//Pair.of(0, new RunSometimes<>(new SetEntityLookTarget(EntityType.PLAYER, 6.0F), UniformInt.of(30, 60))),
				Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
				/*Pair.of(1, new FollowTemptation(target->{
					return SPEED_MULTIPLIER_WHEN_TEMPTED;
				})),*/
				Pair.of(1, new BrainFollowOwner(entity->{
					return SPEED_MULTIPLIER_IN_WATER;
				})),
				/*Pair.of(2, new StartAttacking<>(PetFrogAi::canAttack, frog -> {
					return frog.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
				})),*/
				Pair.of(3, TryFindLand.create(8, MAX_JUMP_VELOCITY)),
				Pair.of(5, new GateBehavior<>(
					ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
					ImmutableSet.of(),
					GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL,
					ImmutableList.of(
						Pair.of(RandomStroll.swim(SPEED_MULTIPLIER_IN_WATER), 1),
						Pair.of(SetWalkTargetFromLookTarget.create(SPEED_MULTIPLIER_IN_WATER, 3), 1),
						Pair.of(BehaviorBuilder.triggerIf(Entity::isInWaterOrBubble), 5)
					)
				))
			),
			ImmutableSet.of(
				Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT),
				Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_PRESENT)
			)
		);
	}
	
	private static void initJumpActivity(Brain<EntityFrogPet> frogBrain){
		frogBrain.addActivityWithConditions(Activity.LONG_JUMP,
			ImmutableList.of(
				Pair.of(0, new LongJumpMidJump(TIME_BETWEEN_LONG_JUMPS, SoundEvents.FROG_STEP)),
				Pair.of(1, new LongJumpToPreferredBlock<>(TIME_BETWEEN_LONG_JUMPS, MAX_LONG_JUMP_HEIGHT, MAX_LONG_JUMP_WIDTH, MAX_JUMP_VELOCITY, frog->{
					return SoundEvents.FROG_LONG_JUMP;
				}, BlockTags.FROG_PREFER_JUMP_TO, 0.5F, PetFrogAi::isAcceptableLandingSpot))
			),
			ImmutableSet.of(
				Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT),
				Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT),
				Pair.of(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT),
				Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT)
			)
		);
	}
	
	private static void initTongueActivity(Brain<EntityFrogPet> var0){
		//TODO: Test this
		var0.addActivity(Activity.TONGUE, 0,
			ImmutableList.of(
				//new StopAttackingIfTargetInvalid<>(),
				//new ShootTongue(SoundEvents.FROG_TONGUE, SoundEvents.FROG_EAT)
				new ShootTongueOwner(SoundEvents.FROG_TONGUE, SoundEvents.FROG_EAT)
			)
			// Use to check for attack target but we don't have one.
		);
	}
	
	public static void updateActivity(EntityFrogPet frog){
		frog.getBrain().setActiveActivityToFirstValid(ACTIVITIES);
	}
	
	private static <E extends Mob> boolean isAcceptableLandingSpot(E var0, BlockPos var1) {
		Level var2 = var0.level;
		BlockPos var3 = var1.below();
		if (var2.getFluidState(var1).isEmpty() && var2.getFluidState(var3).isEmpty() && var2.getFluidState(var1.above()).isEmpty()) {
			BlockState var4 = var2.getBlockState(var1);
			BlockState var5 = var2.getBlockState(var3);
			if (!var4.is(BlockTags.FROG_PREFER_JUMP_TO) && !var5.is(BlockTags.FROG_PREFER_JUMP_TO)) {
				BlockPathTypes var6 = WalkNodeEvaluator.getBlockPathTypeStatic(var2, var1.mutable());
				BlockPathTypes var7 = WalkNodeEvaluator.getBlockPathTypeStatic(var2, var3.mutable());
				return var6==BlockPathTypes.TRAPDOOR || (var4.isAir() && var7==BlockPathTypes.TRAPDOOR) || LongJumpToRandomPos.defaultAcceptableLandingSpot(var0, var1);
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
}
