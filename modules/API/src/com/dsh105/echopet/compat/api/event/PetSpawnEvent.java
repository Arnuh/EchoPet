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

import com.dsh105.echopet.compat.api.entity.IPet;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when a {@link IPet} successfully spawns in the world and has all of its data applied.<br>
 * The Event is called from {@link IPet#spawnPet(Player, boolean)}
 */
public class PetSpawnEvent extends PetEvent{
	
	private static final HandlerList handlers = new HandlerList();
	
	public PetSpawnEvent(IPet pet){
		super(pet);
	}
	
	@Override
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
}
