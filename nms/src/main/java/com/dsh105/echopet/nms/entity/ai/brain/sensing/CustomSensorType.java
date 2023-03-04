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

package com.dsh105.echopet.nms.entity.ai.brain.sensing;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.PlayerSensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;

public class CustomSensorType{
	
	public static final SensorType<NearestLivingEntitySensor<LivingEntity>> NEAREST_LIVING_ENTITIES = register(PetNearestLivingEntitySensor::new);
	public static final SensorType<PlayerSensor> NEAREST_PLAYERS = register(PetPlayerSensor::new);
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private static <U extends Sensor<?>> SensorType<U> register(Supplier<U> supplier){
		try{
			Constructor<SensorType> constructor = SensorType.class.getDeclaredConstructor(Supplier.class);
			constructor.setAccessible(true);
			return constructor.newInstance(supplier);
		}catch(Exception ex){
			ex.printStackTrace();
			return (SensorType<U>) SensorType.NEAREST_LIVING_ENTITIES;
		}
	}
}
