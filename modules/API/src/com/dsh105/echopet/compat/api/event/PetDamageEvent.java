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
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Called when a {@link IPet} damages another {@link org.bukkit.entity.Entity}
 */

public class PetDamageEvent extends PetEvent implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = true; // cancelled by default
	
	private double damage;
	private final DamageCause damageCause;
	
	public PetDamageEvent(IPet pet, DamageCause damageCause, final double damage){
		super(pet);
		this.damage = damage;
		this.damageCause = damageCause;
	}
	
	/**
	 * Gets the damage dealt by the {@link IPet}
	 *
	 * @return damage dealt
	 */
	public double getDamage(){
		return this.damage;
	}
	
	/**
	 * Sets the damage of the event
	 *
	 * @param damage amount of health to take off
	 */
	public void setDamage(double damage){
		this.damage = damage;
	}
	
	public DamageCause getDamageCause(){
		return this.damageCause;
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