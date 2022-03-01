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

package com.dsh105.echopet.compat.nms.v1_18_R2.entity.ai.util;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.util.GoalUtils;

public class PetLandRandomPos{
	
	@Nullable
	public static BlockPos generateRandomPosTowardDirection(Mob var0, int var1, boolean var2, BlockPos var3){
		BlockPos var4 = PetRandomPos.generateRandomPosTowardDirection(var0, var1, var0.getRandom(), var3);
		return !PetGoalUtils.isOutsideLimits(var4, var0) && !PetGoalUtils.isRestricted(var2, var0, var4) && !GoalUtils.isNotStable(var0.getNavigation(), var4) ? var4 : null;
	}
}
