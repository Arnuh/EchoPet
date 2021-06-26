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

package com.dsh105.echopet.compat.nms.v1_17_R1.entity.sensor;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityPet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.phys.AABB;

public class FakeNearestLivingEntitySensor extends NearestLivingEntitySensor{
	
	public FakeNearestLivingEntitySensor(){}
	
	// Straight up copied from NearestLivingEntitySensor except with an extra instanceof check
	// I just used intellij for the code.
	
	@Override
	protected void doTick(ServerLevel var0, LivingEntity var1){
		AABB var2 = var1.getBoundingBox().inflate(16.0D, 16.0D, 16.0D);
		List<LivingEntity> var3 = var0.getEntitiesOfClass(LivingEntity.class, var2, (var1x)->{
			return var1x != var1 && var1x.isAlive() && !(var1x instanceof EntityPet);
		});
		Objects.requireNonNull(var1);
		var3.sort(Comparator.comparingDouble(var1::distanceToSqr));
		Brain<?> var4 = var1.getBrain();
		var4.setMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES, var3);
		var4.setMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, var3.stream().filter((var1x)->{
			return isEntityTargetable(var1, var1x);
		}).collect(Collectors.toList()));
	}
}