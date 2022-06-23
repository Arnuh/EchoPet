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
import com.dsh105.echopet.nms.entity.ai.brain.behavior.SetOwnerLookTarget;
import com.dsh105.echopet.nms.entity.type.EntityTadpolePet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.GateBehavior;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomSwim;
import net.minecraft.world.entity.ai.behavior.RunIf;
import net.minecraft.world.entity.ai.behavior.RunSometimes;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

public class PetTadpoleAi{
	
	// private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
	private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 0.5F;
	private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
	
	private static final ImmutableList<Activity> ACTIVITIES = ImmutableList.of(Activity.IDLE);
	
	public PetTadpoleAi(){
	}
	
	public static Brain<?> makeBrain(Brain<EntityTadpolePet> brain){
		initCoreActivity(brain);
		initIdleActivity(brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}
	
	//@formatter:off
	private static void initCoreActivity(Brain<EntityTadpolePet> brain){
		brain.addActivity(Activity.CORE, 0,
			ImmutableList.of(
				//new AnimalPanic(SPEED_MULTIPLIER_WHEN_PANICKING),
				new LookAtTargetSink(45, 90),
				new MoveToTargetSink()
				//new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)
			)
		);
	}
	
	private static void initIdleActivity(Brain<EntityTadpolePet> brain){
		brain.addActivity(Activity.IDLE,
			ImmutableList.of(
				//Pair.of(0, new RunSometimes<>(new SetEntityLookTarget(EntityType.PLAYER, 6.0F), UniformInt.of(30, 60))),
				Pair.of(0, new RunSometimes<>(new SetOwnerLookTarget(), UniformInt.of(30, 60))),
				/*Pair.of(1, new FollowTemptation(entity->{
					return SPEED_MULTIPLIER_WHEN_TEMPTED;
				})),*/
				Pair.of(1, new BrainFollowOwner(entity->{
					return 1F;
				})),
				Pair.of(2, new GateBehavior<>(
					ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
					ImmutableSet.of(),
					GateBehavior.OrderPolicy.ORDERED,
					GateBehavior.RunningPolicy.TRY_ALL,
					ImmutableList.of(
						//TODO: RandomSwim
						Pair.of(new RandomSwim(SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER), 2),
						Pair.of(new SetWalkTargetFromLookTarget(SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER, 3), 3),
						Pair.of(new RunIf<>(Entity::isInWaterOrBubble, new DoNothing(30, 60)), 5)
					)
				))
			)
		);
	}
	
	public static void updateActivity(EntityTadpolePet entity){
		entity.getBrain().setActiveActivityToFirstValid(ACTIVITIES);
	}
}
