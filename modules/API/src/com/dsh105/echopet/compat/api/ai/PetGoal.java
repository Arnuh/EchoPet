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

import java.util.EnumSet;

public abstract class PetGoal{
	
	private final EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);
	
	public abstract boolean canUse();
	
	public boolean canContinueToUse(){
		return this.canUse();
	}
	
	public boolean isInterruptable(){
		return true;
	}
	
	public void start(){
	}
	
	public void stop(){
	}
	
	public boolean requiresUpdateEveryTick(){
		return false;
	}
	
	public void tick(){
	}
	
	public void setFlags(EnumSet<Flag> var0){
		this.flags.clear();
		this.flags.addAll(var0);
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}
	
	public EnumSet<Flag> getFlags(){
		return this.flags;
	}
	
	protected int adjustedTickDelay(int delay){
		return this.requiresUpdateEveryTick() ? delay : reducedTickDelay(delay);
	}
	
	protected static int reducedTickDelay(int delay){
		return positiveCeilDiv(delay, 2);
	}
	
	public static int positiveCeilDiv(int x, int y){
		return -Math.floorDiv(-x, y);
	}
	
	public enum Flag{
		MOVE,
		LOOK,
		JUMP,
		TARGET;
		
		public static final Flag[] values = values();
	}
}
