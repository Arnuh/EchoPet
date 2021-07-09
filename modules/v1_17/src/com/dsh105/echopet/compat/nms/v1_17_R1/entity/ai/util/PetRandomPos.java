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

package com.dsh105.echopet.compat.nms.v1_17_R1.entity.ai.util;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import javax.annotation.Nullable;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityPet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class PetRandomPos{
	
	@Nullable
	public static Vec3 generateRandomPos(EntityPet var0, Supplier<BlockPos> var1){
		Objects.requireNonNull(var0);
		return generateRandomPos(var1, var0::getWalkTargetValue);
	}
	
	@Nullable
	public static Vec3 generateRandomPos(Supplier<BlockPos> var0, ToDoubleFunction<BlockPos> var1){
		double var2 = -1.0D / 0.0;
		BlockPos var4 = null;
		
		for(int var5 = 0; var5 < 10; ++var5){
			BlockPos var6 = var0.get();
			if(var6 != null){
				double var7 = var1.applyAsDouble(var6);
				if(var7 > var2){
					var2 = var7;
					var4 = var6;
				}
			}
		}
		
		return var4 != null ? Vec3.atBottomCenterOf(var4) : null;
	}
	
	public static BlockPos generateRandomPosTowardDirection(Mob var0, int var1, Random var2, BlockPos var3){
		int var4 = var3.getX();
		int var5 = var3.getZ();
		if(var0.hasRestriction() && var1 > 1){
			BlockPos var6 = var0.getRestrictCenter();
			if(var0.getX() > (double) var6.getX()){
				var4 -= var2.nextInt(var1 / 2);
			}else{
				var4 += var2.nextInt(var1 / 2);
			}
			
			if(var0.getZ() > (double) var6.getZ()){
				var5 -= var2.nextInt(var1 / 2);
			}else{
				var5 += var2.nextInt(var1 / 2);
			}
		}
		
		return new BlockPos((double) var4 + var0.getX(), (double) var3.getY() + var0.getY(), (double) var5 + var0.getZ());
	}
}
