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

import com.dsh105.echopet.compat.api.ai.APetGoalFloat;
import com.dsh105.echopet.compat.api.ai.PetGoalType;
import net.minecraft.world.entity.Mob;

public class PetGoalFloat extends APetGoalFloat{
	
	//FloatGoal
	private final Mob entity;
	
	public PetGoalFloat(Mob entity){
		this.entity = entity;
		entity.getNavigation().setCanFloat(true);
	}
	
	@Override
	public PetGoalType getType(){
		return PetGoalType.FOUR;
	}
	
	@Override
	public String getDefaultKey(){
		return "Float";
	}
	
	@Override
	public boolean shouldStart(){
		return entity.isInWater() || entity.isInLava();
	}
	
	@Override
	public void tick(){
		if(entity.getRandom().nextFloat() < 0.8F){
			entity.getJumpControl().jump();
		}
	}
}
