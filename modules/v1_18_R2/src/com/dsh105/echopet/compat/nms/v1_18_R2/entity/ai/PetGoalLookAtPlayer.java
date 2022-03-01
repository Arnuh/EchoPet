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

package com.dsh105.echopet.compat.nms.v1_18_R2.entity.ai;

import com.dsh105.echopet.compat.api.ai.APetGoalLookAtPlayer;
import com.dsh105.echopet.compat.api.ai.PetGoalType;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;

@SuppressWarnings("rawtypes")
public class PetGoalLookAtPlayer extends APetGoalLookAtPlayer{
	
	//LookAtPlayerGoal
	private final IEntityPet pet;
	private final Mob mob;
	protected Entity player;
	private final float lookDistance, lookDistanceSqr;
	private int lookTime;
	private final float probability;
	private final Class clazz;
	
	public PetGoalLookAtPlayer(IEntityPet pet, Mob mob, Class c){
		this(pet, mob, c, 8.0F, 0.02F);
	}
	
	public PetGoalLookAtPlayer(IEntityPet pet, Mob mob, Class c, float f, float f1){
		this.pet = pet;
		this.mob = mob;
		this.lookDistance = f;
		this.lookDistanceSqr = lookDistance * lookDistance;
		this.probability = f1;
		this.clazz = c;
	}
	
	@Override
	public PetGoalType getType(){
		return PetGoalType.TWO;
	}
	
	@Override
	public String getDefaultKey(){
		return "LookAtPlayer";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean shouldStart(){
		if(mob.getRandom().nextFloat() >= this.probability){
			return false;
		}else if(mob.passengers.size() > 0){
			return false;
		}else{
			if(this.clazz == ServerPlayer.class){
				this.player = mob.level.getNearestPlayer(mob, lookDistance);
			}else{
				this.player = (Entity) mob.level.getEntitiesOfClass(this.clazz, mob.getBoundingBox().inflate(this.lookDistance, 3.0D, this.lookDistance), EntitySelector.notRiding(mob)).stream().findAny().orElse(null);
			}
			return this.player != null;
		}
	}
	
	@Override
	public boolean shouldContinue(){
		return this.player.isAlive() && (mob.distanceToSqr(this.player) <= lookDistanceSqr && this.lookTime > 0);
	}
	
	@Override
	public void start(){
		this.lookTime = 40 + mob.getRandom().nextInt(40);
	}
	
	@Override
	public void finish(){
		this.player = null;
	}
	
	@Override
	public void tick(){
		mob.getLookControl().setLookAt(this.player.getX(), this.player.getEyeY(), this.player.getZ());
		--this.lookTime;
	}
}
