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

package com.dsh105.echopet.compat.api.entity.nms;

import com.dsh105.echopet.compat.api.ai.IPetGoalSelector;
import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityPetHandle;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * Base interface for all NMS entity pet objects.<br>
 * <br>
 * To prevent NMS remapping conflicts, all NMS pets now implement a {@link IEntityPetHandle} to properly control the NMS entity and its EntityData.<br>
 * <br>
 * Pets are no longer guaranteed to implement an entity type specific version of {@link IEntityPet}, but instead provide an {@link IEntityPetHandle} when required.<br>
 * @see IEntityPetHandle
 */
public interface IEntityPet{
	
	Random RANDOM = new Random();
	
	IPet getPet();
	
	IEntityPetHandle getHandle();
	
	@Deprecated
	default IPetGoalSelector getPetGoalSelector(){
		return getHandle().getPetGoalSelector();
	}
	
	default boolean usesBrain(){
		return false;
	}
	
	Entity getEntity();
	
	default Player getPetOwner(){
		return getPet().getOwner();
	}
	
	@Deprecated
	default void remove(boolean makeSound){
		getHandle().remove(makeSound);
	}
	
	boolean isDead();
	
	default float getMaxUpStep(){
		return 0.5F;
	}
	
	void setLocation(Location location);
	
	/**
	 * 1.19 changed pet random to a custom "RandomSource" class where before it was javas random.
	 * Re-add compatibility for single version NMS despite the change as no obvious reason exists for the change.
	 */
	default Random random(){
		return RANDOM;
	}
}