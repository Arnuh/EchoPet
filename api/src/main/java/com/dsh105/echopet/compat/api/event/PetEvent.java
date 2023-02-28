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

package com.dsh105.echopet.compat.api.event;


import com.dsh105.echopet.compat.api.entity.pet.IPet;
import org.bukkit.event.Event;

public abstract class PetEvent extends Event{
	
	private final IPet pet;
	
	public PetEvent(IPet pet){
		this.pet = pet;
	}
	
	/**
	 * Gets the {@link IPet} involved in this event
	 *
	 * @return the {@link IPet} involved
	 */
	public IPet getPet(){
		return pet;
	}
}
