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
import com.dsh105.echopet.compat.nms.v1_18_R2.entity.EntityPet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;

public class PetAirAndWaterRandomPos{
	
	@Nullable
	public static Vec3 getPos(EntityPet var0, int var1, int var2, int var3, double var4, double var6, double var8){
		boolean var10 = PetGoalUtils.mobRestricted(var0, var1);
		return PetRandomPos.generateRandomPos(var0, ()->{
			return generateRandomPos(var0, var1, var2, var3, var4, var6, var8, var10);
		});
	}
	
	@Nullable
	public static BlockPos generateRandomPos(Mob var0, int var1, int var2, int var3, double var4, double var6, double var8, boolean var10){
		BlockPos var11 = RandomPos.generateRandomDirectionWithinRadians(var0.getRandom(), var1, var2, var3, var4, var6, var8);
		if(var11 == null){
			return null;
		}else{
			BlockPos var12 = PetRandomPos.generateRandomPosTowardDirection(var0, var1, var0.getRandom(), var11);
			if(!PetGoalUtils.isOutsideLimits(var12, var0) && !PetGoalUtils.isRestricted(var10, var0, var12)){
				var12 = RandomPos.moveUpOutOfSolid(var12, var0.level.getMaxBuildHeight(), (var1x)->{
					return PetGoalUtils.isSolid(var0, var1x);
				});
				return PetGoalUtils.hasMalus(var0, var12) ? null : var12;
			}else{
				return null;
			}
		}
	}
}
