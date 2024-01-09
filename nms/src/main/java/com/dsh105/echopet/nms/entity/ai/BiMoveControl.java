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

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;

import java.util.function.Predicate;


public class BiMoveControl extends MoveControl{
	
	private final MoveControl control1, control2;
	private final Predicate<Mob> predicate;
	
	/**
	 *
	 * @param predicate Determine when to use {@code control2} over {@code control1}
	 */
	public BiMoveControl(Mob mob, MoveControl control1, MoveControl control2, Predicate<Mob> predicate){
		super(mob);
		this.control1 = control1;
		this.control2 = control2;
		this.predicate = predicate;
	}
	
	@Override
	public boolean hasWanted(){
		return get().hasWanted();
	}
	
	@Override
	public double getSpeedModifier(){
		return get().getSpeedModifier();
	}
	
	@Override
	public void setWantedPosition(double var0, double var2, double var4, double var6){
		get().setWantedPosition(var0, var2, var4, var6);
	}
	
	@Override
	public void strafe(float var0, float var1){
		get().strafe(var0, var1);
	}
	
	@Override
	public void tick(){
		get().tick();
	}
	
	@Override
	public double getWantedX(){
		return get().getWantedX();
	}
	
	@Override
	public double getWantedY(){
		return get().getWantedY();
	}
	
	@Override
	public double getWantedZ(){
		return get().getWantedZ();
	}
	
	private MoveControl get(){
		return predicate.test(mob) ? control2 : control1;
	}
}
