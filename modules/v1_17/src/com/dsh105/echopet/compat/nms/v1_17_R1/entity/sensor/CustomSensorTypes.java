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

import java.lang.reflect.Constructor;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.sensing.AdultSensor;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;

@SuppressWarnings("unchecked")
public class CustomSensorTypes{
	
	public static final SensorType<?> ORG_NEAREST_LIVING_ENTITIES = SensorType.NEAREST_LIVING_ENTITIES;
	public static final SensorType<NearestLivingEntitySensor> NEAREST_LIVING_ENTITIES = registerSensorType("nearest_living_entities", FakeNearestLivingEntitySensor::new);
	public static final SensorType<?> ORG_NEAREST_ADULT = SensorType.NEAREST_ADULT;
	public static final SensorType<AdultSensor> NEAREST_ADULT = registerSensorType("nearest_adult", FakeAdultSensor::new);
	
	private static Constructor<?> constructor;
	
	private static <U extends Sensor<?>> SensorType<U> registerSensorType(String key, Supplier<U> supplier){
		return Registry.register(Registry.SENSOR_TYPE, new ResourceLocation(key), createSensorType(supplier));
	}
	
	private static <U extends Sensor<?>> SensorType<U> createSensorType(Supplier<U> supplier){
		try{
			if(constructor == null){
				constructor = SensorType.class.getDeclaredConstructor(Supplier.class);
				constructor.setAccessible(true);
			}
			return (SensorType<U>) constructor.newInstance(supplier);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}