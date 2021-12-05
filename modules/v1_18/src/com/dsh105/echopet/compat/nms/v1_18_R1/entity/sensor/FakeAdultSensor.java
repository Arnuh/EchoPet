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

package com.dsh105.echopet.compat.nms.v1_18_R1.entity.sensor;

import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.AdultSensor;

public class FakeAdultSensor extends AdultSensor{
	
	@Override
	protected void doTick(ServerLevel var0, AgeableMob var1){
		var1.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).ifPresent((var1x)->{
			this.setNearestVisibleAdult(var1, var1x);
		});
	}
	
	// Since its private we have to override the protected caller so we can call the modified method.
	private void setNearestVisibleAdult(AgeableMob var0, NearestVisibleLivingEntities var1){
		Optional<AgeableMob> var2 = var1.findClosest((var1x)->{
			return var1x.getType() == var0.getType() && !var1x.isBaby() && var1x instanceof AgeableMob;
		}).map(AgeableMob.class::cast);
		var0.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, var2);
	}
}
