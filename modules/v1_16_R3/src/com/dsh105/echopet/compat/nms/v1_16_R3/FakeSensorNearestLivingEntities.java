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

package com.dsh105.echopet.compat.nms.v1_16_R3;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.EntityPet;
import net.minecraft.server.v1_16_R3.AxisAlignedBB;
import net.minecraft.server.v1_16_R3.BehaviorController;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.MemoryModuleType;
import net.minecraft.server.v1_16_R3.SensorNearestLivingEntities;
import net.minecraft.server.v1_16_R3.WorldServer;

public class FakeSensorNearestLivingEntities extends SensorNearestLivingEntities{
	
	public FakeSensorNearestLivingEntities(){}
	
	// Straight up copied from SensorNearestLivingEntities except with an extra instanceof check
	// I just used intellij for the code.
	
	@Override
	protected void a(WorldServer var0, EntityLiving var1){
		AxisAlignedBB var2 = var1.getBoundingBox().grow(16.0D, 16.0D, 16.0D);
		List<EntityLiving> var3 = var0.a(EntityLiving.class, var2, (var1x)->{
			return var1x != var1 && var1x.isAlive() && !(var1x instanceof EntityPet);
		});
		var3.sort(Comparator.comparingDouble(var1::h));
		BehaviorController<?> var4 = var1.getBehaviorController();
		var4.setMemory(MemoryModuleType.MOBS, var3);
		var4.setMemory(MemoryModuleType.VISIBLE_MOBS, var3.stream().filter((var1x)->{
			return a(var1, var1x);
		}).collect(Collectors.toList()));
	}
}