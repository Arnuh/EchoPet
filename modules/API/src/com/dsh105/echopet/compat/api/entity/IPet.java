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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.api.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import com.dsh105.echopet.compat.api.particle.Trail;

public interface IPet{

	public IEntityPet spawnPet(Player owner, boolean ignoreHidden);

	public boolean isSpawned();

    public IEntityPet getEntityPet();

	public LivingEntity getCraftPet();

    public Location getLocation();

    public Player getOwner();

    public String getNameOfOwner();

    public UUID getOwnerUUID();

    public Object getOwnerIdentification();

    public PetType getPetType();

    public boolean isRider();

    public IPet getRider();

	/**
	 * @return The last despawned rider(Null if no rider, or rider was removed)
	 */
	public IPet getLastRider();

	public void setLastRider(IPet lastRider);

    public boolean isOwnerInMountingProcess();

    public String getPetName();

    public String getPetNameWithoutColours();
    
    public String serialisePetName();

    public boolean setPetName(String name);

    public boolean setPetName(String name, boolean sendFailMessage);

	public ArrayList<PetData> getPetData();

	public void removeRider(boolean makeSound, boolean makeParticles);

	public void removePet(boolean makeSound, boolean makeParticles);

    public boolean teleportToOwner();

    public boolean teleport(Location to);

    public boolean isOwnerRiding();

    public boolean isHat();

    public void ownerRidePet(boolean flag);

	public void setAsHat(boolean flag);

    public IPet createRider(final PetType pt, boolean sendFailMessage);

	public void setRider(IPet rider);

	public InventoryView getInventoryView();

	public void setInventoryView(InventoryView dataMenu);

	public List<Trail> getTrails();

	public void addTrail(Trail trail);

	public void removeTrail(Trail trail);

	/**
	 * Check if a pet was removed(hidden) by the player. Usually done by commands
	 * To check if the pet is spawned {@link IPet#isSpawned()}
	 * 
	 * @return If the player has hidden the pet.
	 */
	public boolean isHidden();

	/**
	 * Used to 'hide' a pet. Usually used in commands
	 * Does nothing on its own and is only checked in spawnPet. Must be used with {@link IPet#removePet(boolean, boolean)}
	 * 
	 * @param isHidden Set if the pet is hidden
	 */
	public void setHidden(boolean isHidden);
}