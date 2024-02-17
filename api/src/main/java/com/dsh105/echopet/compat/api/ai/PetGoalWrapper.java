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


import javax.annotation.Nullable;
import java.util.EnumSet;

public class PetGoalWrapper extends PetGoal{
	
	private final PetGoal goal;
	private final int priority;
	private boolean isRunning;
	
	public PetGoalWrapper(PetGoal goal, int priority){
		this.goal = goal;
		this.priority = priority;
	}
	
	public boolean canBeReplacedBy(PetGoalWrapper cmp){
		return this.isInterruptable() && cmp.getPriority() < this.getPriority();
	}
	
	@Override
	public String getIdentifier(){
		return goal.getIdentifier();
	}
	
	@Override
	public boolean canUse(){
		return this.goal.canUse();
	}
	
	@Override
	public boolean canContinueToUse(){
		return this.goal.canContinueToUse();
	}
	
	@Override
	public boolean isInterruptable(){
		return this.goal.isInterruptable();
	}
	
	@Override
	public void start(){
		if(!this.isRunning){
			this.isRunning = true;
			this.goal.start();
		}
	}
	
	@Override
	public void stop(){
		if(this.isRunning){
			this.isRunning = false;
			this.goal.stop();
		}
	}
	
	@Override
	public boolean requiresUpdateEveryTick(){
		return this.goal.requiresUpdateEveryTick();
	}
	
	@Override
	protected int adjustedTickDelay(int delay){
		return this.goal.adjustedTickDelay(delay);
	}
	
	@Override
	public void tick(){
		this.goal.tick();
	}
	
	@Override
	public void setFlags(EnumSet<Flag> var0){
		this.goal.setFlags(var0);
	}
	
	@Override
	public EnumSet<Flag> getFlags(){
		return this.goal.getFlags();
	}
	
	public boolean isRunning(){
		return this.isRunning;
	}
	
	public int getPriority(){
		return this.priority;
	}
	
	public PetGoal getGoal(){
		return this.goal;
	}
	
	@Override
	public boolean equals(@Nullable Object obj){
		if(this == obj){
			return true;
		}else{
			return obj != null && this.getClass() == obj.getClass() && this.goal.equals(((PetGoalWrapper) obj).goal);
		}
	}
	
	@Override
	public int hashCode(){
		return this.goal.hashCode();
	}
}
