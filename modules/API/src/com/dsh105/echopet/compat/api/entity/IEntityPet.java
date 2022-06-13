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

package com.dsh105.echopet.compat.api.entity;

import java.util.Random;
import com.dsh105.echopet.compat.api.ai.IPetGoalSelector;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface IEntityPet{
	
	Random RANDOM = new Random();
	
	IPet getPet();
	
	IEntityPetBase getHandle();
	
	IPetGoalSelector getPetGoalSelector();
	
	SizeCategory getSizeCategory();
	
	Entity getEntity();
	
	Player getOwner();
	
	boolean onInteract(Player p);
	
	void remove(boolean makeSound);
	
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