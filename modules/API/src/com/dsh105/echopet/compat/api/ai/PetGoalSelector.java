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

package com.dsh105.echopet.compat.api.ai;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import com.google.common.collect.Sets;

/*
 * From EntityAPI :)
 * Also means I coded it <3
 */

public class PetGoalSelector implements IPetGoalSelector{
	
	private static final PetGoalWrapper NO_GOAL = new PetGoalWrapper(new PetGoal(){
		@Override
		public boolean canUse(){
			return false;
		}
	}, Integer.MAX_VALUE){
		@Override
		public boolean isRunning(){
			return false;
		}
	};
	
	private final Set<PetGoalWrapper> availableGoals = Sets.newLinkedHashSet();
	private final Map<PetGoal.Flag, PetGoalWrapper> lockedFlags = new EnumMap(PetGoal.Flag.class);
	private final EnumSet<PetGoal.Flag> disabledFlags = EnumSet.noneOf(PetGoal.Flag.class);
	
	public PetGoalSelector(){
	}
	
	@Override
	public void addGoal(int priority, PetGoal petGoal){
		availableGoals.add(new PetGoalWrapper(petGoal, priority));
	}
	
	@Override
	public void removeAllGoals(){
		this.availableGoals.clear();
	}
	
	@Override
	public void removeGoal(PetGoal goal){
		this.availableGoals.stream()
			.filter(var1x->var1x.getGoal() == goal)
			.filter(PetGoalWrapper::isRunning).forEach(PetGoalWrapper::stop);
		this.availableGoals.removeIf(var1x->var1x.getGoal() == goal);
	}
	
	private static boolean goalContainsAnyFlags(PetGoalWrapper goal, EnumSet<PetGoal.Flag> flags){
		for(PetGoal.Flag flag : goal.getFlags()){
			if(flags.contains(flag)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean goalCanBeReplacedForAllFlags(PetGoalWrapper goal, Map<PetGoal.Flag, PetGoalWrapper> flags){
		for(PetGoal.Flag flag : goal.getFlags()){
			if(!flags.getOrDefault(flag, NO_GOAL).canBeReplacedBy(goal)){
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void tick(){
		for(PetGoalWrapper goalWrapper : this.availableGoals){
			if(goalWrapper.isRunning() && (goalContainsAnyFlags(goalWrapper, this.disabledFlags) || !goalWrapper.canContinueToUse())){
				goalWrapper.stop();
			}
		}
		
		this.lockedFlags.entrySet().removeIf(entry->!entry.getValue().isRunning());
		
		for(PetGoalWrapper goalWrapper : this.availableGoals){
			if(!goalWrapper.isRunning() && !goalContainsAnyFlags(goalWrapper, this.disabledFlags) && goalCanBeReplacedForAllFlags(goalWrapper, this.lockedFlags) && goalWrapper.canUse()){
				for(PetGoal.Flag flag : goalWrapper.getFlags()){
					PetGoalWrapper goal = this.lockedFlags.getOrDefault(flag, NO_GOAL);
					goal.stop();
					this.lockedFlags.put(flag, goal); // NO_GOAL gets put in here and then instantly removed next tick.
				}
				goalWrapper.start();
			}
		}
		
		tickRunningGoals(true);
	}
	
	public void tickRunningGoals(boolean forceUpdate){
		for(PetGoalWrapper goal : this.availableGoals){
			if(goal.isRunning() && (forceUpdate || goal.requiresUpdateEveryTick())){
				goal.tick();
			}
		}
	}
	
	@Override
	public Set<PetGoalWrapper> getAvailableGoals(){
		return this.availableGoals;
	}
	
	public void disableControlFlag(PetGoal.Flag flag){
		this.disabledFlags.add(flag);
	}
	
	public void enableControlFlag(PetGoal.Flag flag){
		this.disabledFlags.remove(flag);
	}
	
	@Override
	public void setControlFlag(PetGoal.Flag flag, boolean enable){
		if(enable){
			this.enableControlFlag(flag);
		}else{
			this.disableControlFlag(flag);
		}
		
	}
}