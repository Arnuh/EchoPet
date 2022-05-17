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

package com.dsh105.echopet.nms.entity.ai.util;

import com.dsh105.echopet.nms.VersionBreaking;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class PetGoalUtils{
	
	public PetGoalUtils(){
	}
	
	public static boolean mobRestricted(Mob var0, int var1){
		return var0.hasRestriction() && VersionBreaking.closerToCenterThan(var0.getRestrictCenter(), var0.position(), (double) (var0.getRestrictRadius() + (float) var1) + 1.0D);
	}
	
	public static boolean isOutsideLimits(BlockPos var0, Mob var1){
		return var0.getY() < var1.level.getMinBuildHeight() || var0.getY() > var1.level.getMaxBuildHeight();
	}
	
	public static boolean isRestricted(boolean var0, Mob var1, BlockPos var2){
		return var0 && !var1.isWithinRestriction(var2);
	}
	
	public static boolean isWater(Mob var0, BlockPos var1){
		return var0.level.getFluidState(var1).is(FluidTags.WATER);
	}
	
	public static boolean hasMalus(Mob var0, BlockPos var1){
		return var0.getPathfindingMalus(WalkNodeEvaluator.getBlockPathTypeStatic(var0.level, var1.mutable())) != 0.0F;
	}
	
	public static boolean isSolid(Mob var0, BlockPos var1){
		return var0.level.getBlockState(var1).getMaterial().isSolid();
	}
}