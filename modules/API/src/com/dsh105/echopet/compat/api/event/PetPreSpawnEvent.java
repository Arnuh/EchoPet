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
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a {@link IPet} spawn is attempted.
 */

public class PetPreSpawnEvent extends PetEvent implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	private Location spawnLocation;
	
	public PetPreSpawnEvent(IPet pet, Location spawnLocation){
		super(pet);
		this.spawnLocation = spawnLocation;
	}
	
	/**
	 * Gets the spawn {@link org.bukkit.Location} of the {@link IPet}
	 * <p/>
	 * This {@link org.bukkit.Location} is most likely going to be the owner's {@link org.bukkit.Location}
	 *
	 * @return the Location this LivingPet spawned
	 */
	public Location getSpawnLocation(){
		return this.spawnLocation;
	}
	
	/**
	 * Sets the spawn Location of this {@link IPet}
	 *
	 * @param spawnLocation new {@link org.bukkit.Location} to spawn the {@link IPet}
	 */
	public void setSpawnLocation(Location spawnLocation){
		this.spawnLocation = spawnLocation;
	}
	
	/**
	 * Gets the cancellation state of this event. A cancelled event will not
	 * be executed in the server, but will still pass to other plugins
	 *
	 * @return true if this event is cancelled
	 */
	
	@Override
	public boolean isCancelled(){
		return this.cancelled;
	}
	
	/**
	 * Sets the cancellation state of this event. A cancelled event will not
	 * be executed in the server, but will still pass to other plugins
	 *
	 * @param cancel true if you wish to cancel this event
	 */
	
	@Override
	public void setCancelled(boolean cancel){
		this.cancelled = cancel;
	}
	
	
	@Override
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
}
