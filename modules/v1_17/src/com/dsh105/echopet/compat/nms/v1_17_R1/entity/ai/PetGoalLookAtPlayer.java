/*
 * This file is part of EchoPet.
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dsh105.echopet.compat.nms.v1_17_R1.entity.ai;

import com.dsh105.echopet.compat.api.ai.APetGoalLookAtPlayer;
import com.dsh105.echopet.compat.api.ai.PetGoalType;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityPet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;

@SuppressWarnings("rawtypes")
public class PetGoalLookAtPlayer extends APetGoalLookAtPlayer{
	
	//LookAtPlayerGoal
	private final EntityPet pet;
	protected Entity player;
	private final float lookDistance, lookDistanceSqr;
	private int lookTime;
	private final float probability;
	private final Class clazz;
	
	public PetGoalLookAtPlayer(EntityPet pet, Class c){
		this.pet = pet;
		this.lookDistance = 8.0F;
		this.lookDistanceSqr = lookDistance * lookDistance;
		this.probability = 0.02F;
		this.clazz = c;
	}
	
	public PetGoalLookAtPlayer(EntityPet pet, Class c, float f, float f1){
		this.pet = pet;
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
		if(this.pet.random().nextFloat() >= this.probability){
			return false;
		}else if(this.pet.passengers.size() > 0){
			return false;
		}else{
			if(this.clazz == ServerPlayer.class){
				this.player = this.pet.level.getNearestPlayer(this.pet, lookDistance);
			}else{
				this.player = (Entity) this.pet.level.getEntitiesOfClass(this.clazz, this.pet.getBoundingBox().inflate(this.lookDistance, 3.0D, this.lookDistance), EntitySelector.notRiding(pet)).stream().findAny().orElse(null);
			}
			return this.player != null;
		}
	}
	
	@Override
	public boolean shouldContinue(){
		return this.player.isAlive() && (this.pet.distanceToSqr(this.player) <= lookDistanceSqr && this.lookTime > 0);
	}
	
	@Override
	public void start(){
		this.lookTime = 40 + this.pet.random().nextInt(40);
	}
	
	@Override
	public void finish(){
		this.player = null;
	}
	
	@Override
	public void tick(){
		this.pet.getLookControl().setLookAt(this.player.getX(), this.player.getEyeY(), this.player.getZ());
		--this.lookTime;
	}
}
