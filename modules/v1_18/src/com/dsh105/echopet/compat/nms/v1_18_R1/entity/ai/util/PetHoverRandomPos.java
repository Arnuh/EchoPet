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

package com.dsh105.echopet.compat.nms.v1_18_R1.entity.ai.util;

import javax.annotation.Nullable;
import com.dsh105.echopet.compat.nms.v1_18_R1.entity.EntityPet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;

public class PetHoverRandomPos{
	
	public PetHoverRandomPos(){
	}
	
	@Nullable
	public static Vec3 getPos(EntityPet var0, int var1, int var2, double var3, double var5, float var7, int var8, int var9){
		boolean var10 = PetGoalUtils.mobRestricted(var0, var1);
		return PetRandomPos.generateRandomPos(var0, ()->{
			BlockPos var11 = RandomPos.generateRandomDirectionWithinRadians(var0.getRandom(), var1, var2, 0, var3, var5, var7);
			if(var11 == null){
				return null;
			}else{
				BlockPos var12 = PetLandRandomPos.generateRandomPosTowardDirection(var0, var1, var10, var11);
				if(var12 == null){
					return null;
				}else{
					var12 = RandomPos.moveUpToAboveSolid(var12, var0.getRandom().nextInt(var8 - var9 + 1) + var9, var0.level.getMaxBuildHeight(), (var1x)->{
						return PetGoalUtils.isSolid(var0, var1x);
					});
					return !PetGoalUtils.isWater(var0, var12) && !PetGoalUtils.hasMalus(var0, var12) ? var12 : null;
				}
			}
		});
	}
}