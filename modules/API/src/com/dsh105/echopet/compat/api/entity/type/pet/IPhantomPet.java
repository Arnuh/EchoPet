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

package com.dsh105.echopet.compat.api.entity.type.pet;

import com.dsh105.echopet.compat.api.config.PetConfigEntry;

public interface IPhantomPet extends IFlyingPet{
	
	PetConfigEntry<Integer> SWEEP_NEXT_START_DELAY = new PetConfigEntry<>("ai.sweep.nextDelayStart", 10, "In server ticks");
	PetConfigEntry<Integer> SWEEP_NEXT_MIN_DELAY = new PetConfigEntry<>("ai.sweep.nextMinDelay", 4, "In seconds");
	PetConfigEntry<Integer> SWEEP_NEXT_RAND_DELAY = new PetConfigEntry<>("ai.sweep.nextRandDelay", 2, "In seconds, added to min");
	PetConfigEntry<Integer> SWEEP_MIN_HEIGHT_OFFSET = new PetConfigEntry<>("ai.sweep.minHeightOffset", 10);
	PetConfigEntry<Integer> SWEEP_RAND_HEIGHT_OFFSET = new PetConfigEntry<>("ai.sweep.randHeightOffset", 2);
	
	PetConfigEntry<Double> CIRCLE_MIN_DISTANCE = new PetConfigEntry<>("ai.circle.minDistance", 5.0);
	PetConfigEntry<Double> CIRCLE_MAX_RAND_DISTANCE = new PetConfigEntry<>("ai.circle.maxRandDistance", 10.0);
	PetConfigEntry<Double> CIRCLE_MAX_DISTANCE = new PetConfigEntry<>("ai.circle.maxDistance", 15.0);
	PetConfigEntry<Double> CIRCLE_MAX_DISTANCE_RESET = new PetConfigEntry<>("ai.circle.maxDistanceReset", 5.0);
	PetConfigEntry<Double> CIRCLE_MIN_HEIGHT = new PetConfigEntry<>("ai.circle.minHeight", 0.0);
	PetConfigEntry<Double> CIRCLE_MAX_RAND_HEIGHT = new PetConfigEntry<>("ai.circle.maxRandHeight", 9.0);
	
	void setSize(int size);
	
	int getSize();
	
	void setWandering(boolean flag);
	
	boolean isWandering();
}
