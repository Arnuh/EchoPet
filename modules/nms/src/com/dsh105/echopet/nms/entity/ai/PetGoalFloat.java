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

package com.dsh105.echopet.nms.entity.ai;

import java.util.EnumSet;
import com.dsh105.echopet.compat.api.ai.APetGoalFloat;
import com.dsh105.echopet.compat.api.ai.PetGoal;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import net.minecraft.world.entity.Mob;

public class PetGoalFloat extends APetGoalFloat{
	
	// FloatGoal
	private final IEntityPet pet;
	private final Mob entity;
	
	public PetGoalFloat(IEntityPet pet, Mob entity){
		this.pet = pet;
		this.entity = entity;
		setFlags(EnumSet.of(PetGoal.Flag.JUMP));
		entity.getNavigation().setCanFloat(true);
	}
	
	@Override
	public boolean canUse(){
		// return this.mob.isInWater() && this.mob.getFluidHeight(FluidTags.WATER) > this.mob.getFluidJumpThreshold() || this.mob.isInLava();
		return entity.isInWater() || entity.isInLava();
	}
	
	@Override
	public void tick(){
		if(pet.random().nextFloat() < 0.8F){
			entity.getJumpControl().jump();
		}
	}
}
