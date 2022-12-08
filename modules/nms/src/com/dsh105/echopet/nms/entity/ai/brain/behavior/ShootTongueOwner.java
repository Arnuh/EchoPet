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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.pathfinder.Path;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;

public class ShootTongueOwner extends Behavior<Frog>{
	
	public static final int TIME_OUT_DURATION = 100;
	public static final int CATCH_ANIMATION_DURATION = 6;
	public static final int TONGUE_ANIMATION_DURATION = 10;
	private static final float EATING_DISTANCE = 1.75F;
	private static final float EATING_MOVEMENT_FACTOR = 0.75F;
	private ServerPlayer owner;
	private int eatAnimationTimer;
	private int calculatePathCounter;
	private final SoundEvent tongueSound;
	private final SoundEvent eatSound;
	private State state;
	
	public ShootTongueOwner(SoundEvent tongueSound, SoundEvent eatSound){
		super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), TIME_OUT_DURATION);
		this.state = State.DONE;
		this.tongueSound = tongueSound;
		this.eatSound = eatSound;
	}
	
	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, Frog frog){
		if(owner == null && frog instanceof IEntityPet entityPet){
			owner = ((CraftPlayer) entityPet.getOwner()).getHandle();
		}
		return canPathfindToTarget(frog, owner) && frog.getPose() != Pose.CROAKING;
	}
	
	@Override
	protected boolean canStillUse(ServerLevel level, Frog frog, long time){
		return state != State.DONE;
	}
	
	@Override
	protected void start(ServerLevel level, Frog frog, long time){
		BehaviorUtils.lookAtEntity(frog, owner);
		frog.setTongueTarget(owner);
		frog.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(owner.position(), 2.0F, 0));
		calculatePathCounter = TONGUE_ANIMATION_DURATION;
		state = State.MOVE_TO_TARGET;
	}
	
	@Override
	protected void stop(ServerLevel level, Frog frog, long time){
		frog.eraseTongueTarget();
		frog.setPose(Pose.STANDING);
	}
	
	@Override
	protected void tick(ServerLevel level, Frog frog, long time){
		frog.setTongueTarget(owner);
		switch(state){
			case MOVE_TO_TARGET:
				if(owner.distanceTo(frog) < EATING_DISTANCE){
					level.playSound(null, frog, tongueSound, SoundSource.NEUTRAL, 2.0F, 1.0F);
					frog.setPose(Pose.USING_TONGUE);
					if(EATING_MOVEMENT_FACTOR != 0){
						owner.setDeltaMovement(owner.position().vectorTo(frog.position()).normalize().scale(EATING_MOVEMENT_FACTOR));
					}
					eatAnimationTimer = 0;
					state = State.CATCH_ANIMATION;
				}else if(calculatePathCounter <= 0){
					frog.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(owner.position(), 2.0F, 0));
					calculatePathCounter = TONGUE_ANIMATION_DURATION;
				}else{
					--calculatePathCounter;
				}
				break;
			case CATCH_ANIMATION:
				if(eatAnimationTimer++ >= CATCH_ANIMATION_DURATION){
					state = State.EAT_ANIMATION;
					level.playSound(null, frog, eatSound, SoundSource.NEUTRAL, 2.0F, 1.0F);
				}
				break;
			case EAT_ANIMATION:
				if(eatAnimationTimer >= TONGUE_ANIMATION_DURATION){
					state = State.DONE;
				}else{
					++eatAnimationTimer;
				}
			case DONE:
		}
		
	}
	
	private boolean canPathfindToTarget(Frog frog, LivingEntity target){
		Path path = frog.getNavigation().createPath(target, 0);
		return path != null && path.getDistToTarget() < EATING_DISTANCE;
	}
	
	private enum State{
		MOVE_TO_TARGET,
		CATCH_ANIMATION,
		EAT_ANIMATION,
		DONE;
		
		State(){
		}
	}
}
