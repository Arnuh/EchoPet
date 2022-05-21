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

import java.util.Set;
import java.util.stream.Stream;
import com.dsh105.echopet.compat.api.ai.PetGoal;
import com.dsh105.echopet.compat.api.ai.PetGoalSelector;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

public class GoalSelectorWrapper extends GoalSelector{
	
	private final PetGoalSelector wrapped;
	
	public GoalSelectorWrapper(PetGoalSelector wrapped){
		super(null);
		this.wrapped = wrapped;
	}
	
	@Override
	public void addGoal(int var0, Goal var1){
		//
	}
	
	@Override
	public void removeAllGoals(){
		wrapped.removeAllGoals();
	}
	
	@Override
	public void removeGoal(Goal var0){
		//
	}
	
	@Override
	public void tick(){
		wrapped.tick();
	}
	
	@Override
	public void tickRunningGoals(boolean var0){
		wrapped.tickRunningGoals(var0);
	}
	
	private static final Set<WrappedGoal> emptySet = Set.of();
	
	@Override
	public Set<WrappedGoal> getAvailableGoals(){
		return emptySet;
	}
	
	@Override
	public Stream<WrappedGoal> getRunningGoals(){
		return super.getRunningGoals();
	}
	
	@Override
	public void setNewGoalRate(int var0){
		//
	}
	
	@Override
	public void disableControlFlag(Goal.Flag var0){
		wrapped.disableControlFlag(PetGoal.Flag.values[var0.ordinal()]);
	}
	
	@Override
	public void enableControlFlag(Goal.Flag var0){
		wrapped.enableControlFlag(PetGoal.Flag.values[var0.ordinal()]);
	}
	
	@Override
	public void setControlFlag(Goal.Flag var0, boolean var1){
		// wrapped.setControlFlag(PetGoal.Flag.values[var0.ordinal()], var1);
	}
}
