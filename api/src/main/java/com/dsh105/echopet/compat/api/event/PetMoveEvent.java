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
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a {@link IPet} moves
 */

public class PetMoveEvent extends PetEvent implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	private final Location from;
	private Location to;
	
	public PetMoveEvent(IPet pet, Location from, Location to){
		super(pet);
		this.from = from;
		this.to = to;
	}
	
	/**
	 * Gets the {@link org.bukkit.Location} this {@link IPet} moved to
	 *
	 * @return {@link org.bukkit.Location} moved to
	 */
	public Location getTo(){
		return this.to;
	}
	
	/**
	 * Gets the {@link org.bukkit.Location} this {@link IPet} moved from
	 *
	 * @return {@link org.bukkit.Location} moved from
	 */
	public Location getFrom(){
		return this.from;
	}
	
	/**
	 * Sets the {@link org.bukkit.Location} that this {@link IPet} will move to
	 *
	 * @param to new {@link org.bukkit.Location} this {@link IPet} will move to
	 */
	public void setTo(Location to){
		this.to = to;
	}
	
	/**
	 * Gets the cancellation state of this event. A cancelled event will not
	 * be executed in the server, but will still pass to other plugins
	 *
	 * @return true if this event is cancelled
	 */
	@Override
	public boolean isCancelled(){
		return cancelled;
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
