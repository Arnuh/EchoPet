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

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public interface IPet{
	
	IEntityPet spawnPet(Player owner, boolean ignoreHidden);
	
	boolean isSpawned();
	
	IEntityPet getEntityPet();
	
	LivingEntity getCraftPet();
	
	Location getLocation();
	
	Player getOwner();
	
	String getNameOfOwner();
	
	UUID getOwnerUUID();
	
	Object getOwnerIdentification();
	
	IPetType getPetType();
	
	boolean isRider();
	
	IPet getRider();
	
	/**
	 * @return The last despawned rider(Null if no rider, or rider was removed)
	 */
	IPet getLastRider();
	
	void setLastRider(IPet lastRider);
	
	boolean isOwnerInMountingProcess();
	
	String getPetName();
	
	String getPetNameWithoutColours();
	
	String serialisePetName();
	
	boolean setPetName(String name);
	
	boolean setPetName(String name, boolean sendFailMessage);
	
	ArrayList<PetData> getPetData();
	
	void removeRider(boolean makeSound, boolean makeParticles);
	
	void removePet(boolean makeSound, boolean makeParticles);
	
	boolean teleportToOwner();
	
	boolean teleport(Location to);
	
	boolean isOwnerRiding();
	
	boolean isHat();
	
	void ownerRidePet(boolean flag);
	
	void setAsHat(boolean flag);
	
	IPet createRider(final IPetType pt, boolean sendFailMessage);
	
	void setRider(IPet rider);
	
	InventoryView getInventoryView();
	
	void setInventoryView(InventoryView dataMenu);
	
	/**
	 * Check if a pet was removed(hidden) by the player. Usually done by commands
	 * To check if the pet is spawned {@link IPet#isSpawned()}
	 *
	 * @return If the player has hidden the pet.
	 */
	boolean isHidden();
	
	/**
	 * Used to 'hide' a pet. Usually used in commands
	 * Does nothing on its own and is only checked in spawnPet. Must be used with {@link IPet#removePet(boolean, boolean)}
	 *
	 * @param isHidden Set if the pet is hidden
	 */
	void setHidden(boolean isHidden);
}