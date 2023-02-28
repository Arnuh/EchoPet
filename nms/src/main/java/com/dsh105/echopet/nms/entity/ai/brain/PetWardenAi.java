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

import com.dsh105.echopet.nms.entity.type.EntityWardenPet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.GoToTargetLocation;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.behavior.warden.Digging;
import net.minecraft.world.entity.ai.behavior.warden.Emerging;
import net.minecraft.world.entity.ai.behavior.warden.ForceUnmount;
import net.minecraft.world.entity.ai.behavior.warden.Roar;
import net.minecraft.world.entity.ai.behavior.warden.SetRoarTarget;
import net.minecraft.world.entity.ai.behavior.warden.SetWardenLookTarget;
import net.minecraft.world.entity.ai.behavior.warden.Sniffing;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.ai.behavior.warden.TryToSniff;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.schedule.Activity;

public class PetWardenAi{
	
	private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.5F;
	private static final float SPEED_MULTIPLIER_WHEN_INVESTIGATING = 0.7F;
	private static final float SPEED_MULTIPLIER_WHEN_FIGHTING = 1.2F;
	// private static final int MELEE_ATTACK_COOLDOWN = 18;
	private static final int DIGGING_DURATION = Mth.ceil(100.0F);
	public static final int EMERGE_DURATION = Mth.ceil(133.59999F);
	// public static final int ROAR_DURATION = Mth.ceil(84.0F);
	private static final int SNIFFING_DURATION = Mth.ceil(83.2F);
	public static final int DIGGING_COOLDOWN = 1200;
	private static final int DISTURBANCE_LOCATION_EXPIRY_TIME = 100;
	private static final Behavior<Warden> DIG_COOLDOWN_SETTER = new Behavior<>(ImmutableMap.of(MemoryModuleType.DIG_COOLDOWN, MemoryStatus.REGISTERED)){
		@Override
		protected void start(ServerLevel var0, Warden var1, long var2){
			PetWardenAi.setDigCooldown(var1);
		}
	};
	
	private static final ImmutableList<Activity> ACTIVITIES = ImmutableList.of(Activity.EMERGE, Activity.DIG, Activity.ROAR, Activity.FIGHT, Activity.INVESTIGATE, Activity.SNIFF, Activity.IDLE);
	
	public PetWardenAi(){
	}
	
	//@formatter:off
	public static void updateActivity(EntityWardenPet var0){
		var0.getBrain().setActiveActivityToFirstValid(ACTIVITIES);
	}
	
	public static Brain<?> makeBrain(EntityWardenPet entity, Brain<EntityWardenPet> brain){
		initCoreActivity(brain);
		initEmergeActivity(brain);
		initDiggingActivity(brain);
		initIdleActivity(brain);
		initRoarActivity(brain);
		initFightActivity(entity, brain);
		initInvestigateActivity(brain);
		initSniffingActivity(brain);
		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.useDefaultActivity();
		return brain;
	}
	
	private static void initCoreActivity(Brain<EntityWardenPet> var0){
		var0.addActivity(Activity.CORE, 0,
			ImmutableList.of(
				new Swim(0.8F),
				SetWardenLookTarget.create(),
				new LookAtTargetSink(45, 90),
				new MoveToTargetSink()
			)
		);
	}
	
	private static void initEmergeActivity(Brain<EntityWardenPet> var0){
		var0.addActivityAndRemoveMemoryWhenStopped(Activity.EMERGE, 5,
			ImmutableList.of(
				new Emerging<>(EMERGE_DURATION)
			), MemoryModuleType.IS_EMERGING);
	}
	
	private static void initDiggingActivity(Brain<EntityWardenPet> var0){
		var0.addActivityWithConditions(Activity.DIG,
			ImmutableList.of(
				Pair.of(0, new ForceUnmount()),
				Pair.of(1, new Digging<>(DIGGING_DURATION))
			),
			ImmutableSet.of(
				Pair.of(MemoryModuleType.ROAR_TARGET, MemoryStatus.VALUE_ABSENT),
				Pair.of(MemoryModuleType.DIG_COOLDOWN, MemoryStatus.VALUE_ABSENT)
			)
		);
	}
	
	private static void initIdleActivity(Brain<EntityWardenPet> var0){
		var0.addActivity(Activity.IDLE, 10,
			ImmutableList.of(
				SetRoarTarget.create(Warden::getEntityAngryAt),
				TryToSniff.create(),
				new RunOne<>(
					ImmutableMap.of(MemoryModuleType.IS_SNIFFING, MemoryStatus.VALUE_ABSENT),
					ImmutableList.of(
						Pair.of(RandomStroll.stroll(SPEED_MULTIPLIER_WHEN_IDLING), 2),
						Pair.of(new DoNothing(30, 60), 1)
					)
				)
			)
		);
	}
	
	private static void initInvestigateActivity(Brain<EntityWardenPet> var0){
		var0.addActivityAndRemoveMemoryWhenStopped(Activity.INVESTIGATE, 5,
			ImmutableList.of(
				SetRoarTarget.create(Warden::getEntityAngryAt),
				GoToTargetLocation.create(MemoryModuleType.DISTURBANCE_LOCATION, 2, SPEED_MULTIPLIER_WHEN_INVESTIGATING)
			), MemoryModuleType.DISTURBANCE_LOCATION);
	}
	
	private static void initSniffingActivity(Brain<EntityWardenPet> var0){
		var0.addActivityAndRemoveMemoryWhenStopped(Activity.SNIFF, 5,
			ImmutableList.of(
				SetRoarTarget.create(Warden::getEntityAngryAt),
				new Sniffing<>(SNIFFING_DURATION)
			), MemoryModuleType.IS_SNIFFING);
	}
	
	private static void initRoarActivity(Brain<EntityWardenPet> var0){
		var0.addActivityAndRemoveMemoryWhenStopped(Activity.ROAR, 10,
			ImmutableList.of(
				new Roar()
			), MemoryModuleType.ROAR_TARGET);
	}
	
	private static void initFightActivity(EntityWardenPet entity, Brain<EntityWardenPet> brain){
		brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10,
			ImmutableList.of(
				DIG_COOLDOWN_SETTER,
				StopAttackingIfTargetInvalid.create(target->!entity.getAngerLevel().isAngry() || !entity.canTargetEntity(target), PetWardenAi::onTargetInvalid, false),
				SetEntityLookTarget.create(target->isTarget(entity, target), (float) entity.getAttributeValue(Attributes.FOLLOW_RANGE)),
				SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(SPEED_MULTIPLIER_WHEN_FIGHTING),
				new SonicBoom(),
				MeleeAttack.create(18)
			), MemoryModuleType.ATTACK_TARGET);
	}
	
	private static boolean isTarget(EntityWardenPet warden, LivingEntity target){
		return warden.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET)
			.filter(entity->{
				return entity == target;
			}).isPresent();
	}
	
	private static void onTargetInvalid(EntityWardenPet warden, LivingEntity entity){
		if(!warden.canTargetEntity(entity)){
			warden.clearAnger(entity);
		}
		
		setDigCooldown(warden);
	}
	
	public static void setDigCooldown(LivingEntity warden){
		if(warden.getBrain().hasMemoryValue(MemoryModuleType.DIG_COOLDOWN)){
			warden.getBrain().setMemoryWithExpiry(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, DIGGING_COOLDOWN);
		}
		
	}
	
	public static void setDisturbanceLocation(EntityWardenPet warden, BlockPos pos){
		if(warden.level.getWorldBorder().isWithinBounds(pos) && warden.getEntityAngryAt().isEmpty() && warden.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty()){
			setDigCooldown(warden);
			warden.getBrain().setMemoryWithExpiry(MemoryModuleType.SNIFF_COOLDOWN, Unit.INSTANCE, DISTURBANCE_LOCATION_EXPIRY_TIME);
			warden.getBrain().setMemoryWithExpiry(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(pos), DISTURBANCE_LOCATION_EXPIRY_TIME);
			warden.getBrain().setMemoryWithExpiry(MemoryModuleType.DISTURBANCE_LOCATION, pos, DISTURBANCE_LOCATION_EXPIRY_TIME);
			warden.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
		}
	}
}
