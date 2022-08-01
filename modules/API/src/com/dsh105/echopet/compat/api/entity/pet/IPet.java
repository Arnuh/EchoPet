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

package com.dsh105.echopet.compat.api.entity.pet;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.dsh105.echopet.compat.api.config.PetConfigEntry;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.data.PetData;
import com.dsh105.echopet.compat.api.entity.nms.IEntityPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public interface IPet{
	
	PetConfigEntry<Boolean> RIDING_IGNORE_FALL_DAMAGE = new PetConfigEntry<>("riding.ignoreFallDamage", true);
	
	PetConfigEntry<Double> RIDING_WALK_SPEED = new PetConfigEntry<>("riding.walkSpeed", petType->{
		return 0.37D;
		/*Double speed = EchoPet.getPlugin().getSpawnUtil().getAttribute(petType, "generic.movement_speed");
		if(speed == null){
			return 0.37D;
		}
		return speed;*/
	});
	PetConfigEntry<Double> RIDING_FLY_SPEED = new PetConfigEntry<>("riding.flySpeed", petType->{
		return 0.5D;
		/*Double speed = EchoPet.getPlugin().getSpawnUtil().getAttribute(petType, "generic.flying_speed");
		if(speed == null){
			return 0.5D;
		}
		return speed;*/
	});
	PetConfigEntry<Double> RIDING_JUMP_HEIGHT = new PetConfigEntry<>("riding.jumpHeight", petType->{
		return 0.6d;
	});
	PetConfigEntry<Boolean> RIDING_FLY = new PetConfigEntry<>("riding.canFly", petType->{
		// A better way has to exist
		return petType == PetType.ALLAY || petType == PetType.BAT || petType.equals(PetType.BEE) || petType == PetType.BLAZE || petType == PetType.GHAST || petType == PetType.SQUID || petType == PetType.WITHER || petType == PetType.VEX || petType == PetType.PHANTOM;
	});
	// PetConfigEntry<Boolean> RIDING_HOVER = new PetConfigEntry<>("riding.hover", false);
	
	// Goals might get moved to a higher interface. Some pathing will require "PathfinderMob"
	PetConfigEntry<Double> GOAL_WALK_SPEED = new PetConfigEntry<>("goal.walkSpeed", petType->{
		Double speed = EchoPet.getPlugin().getSpawnUtil().getAttribute(petType, "generic.movement_speed");
		if(speed == null){
			return 0.37D;
		}
		return speed;
	});
	PetConfigEntry<Double> GOAL_FLY_SPEED = new PetConfigEntry<>("goal.flySpeed", petType->{
		Double speed = EchoPet.getPlugin().getSpawnUtil().getAttribute(petType, "generic.flying_speed");
		if(speed == null){
			return 0.5D;
		}
		return speed;
	});
	/*PetConfigEntry<Double> GOAL_JUMP_HEIGHT = new PetConfigEntry<>("goal.jumpHeight", petType->{
		return 0.6d;
	});*/ PetConfigEntry<Integer> GOAL_FOLLOW_START_DISTANCE = new PetConfigEntry<>("goal.follow.startDistance", 6);
	PetConfigEntry<Integer> GOAL_FOLLOW_STOP_DISTANCE = new PetConfigEntry<>("goal.follow.stopDistance", 2);
	PetConfigEntry<Integer> GOAL_FOLLOW_TELEPORT_DISTANCE = new PetConfigEntry<>("goal.follow.teleportDistance", 10);
	PetConfigEntry<Double> GOAL_FOLLOW_SPEED_MODIFIER = new PetConfigEntry<>("goal.follow.speedModifier", petType->{
		return 1.5d;
	});
	
	/**
	 * Spawns the respective {@link IEntityPet}, applies pet name, pet data, and spawns rider for any applicable data is available.<br>
	 * <br>
	 * @return The {@link IEntityPet} spawned, current {@link IEntityPet} if it is already spawned, or null if failed to spawn.
	 */
	IEntityPet spawnPet(Player owner, boolean ignoreHidden);
	
	/**
	 *
	 * @return If the {@link #getEntityPet()} is not null and not set as dead.
	 */
	boolean isSpawned();
	
	IEntityPet getEntityPet();
	
	Entity getCraftPet();
	
	Location getLocation();
	
	Player getOwner();
	
	String getNameOfOwner();
	
	UUID getOwnerUUID();
	
	IPetType getPetType();
	
	boolean isRider();
	
	IPet getRider();
	
	void setIsRider();
	
	boolean isOwnerInMountingProcess();
	
	String getPetName();
	
	String getPetNameWithoutColours();
	
	String serialisePetName();
	
	boolean setPetName(String name);
	
	boolean setPetName(String name, boolean sendFailMessage);
	
	Map<PetData<?>, Object> getData();
	
	IPet createRider(final IPetType pt, boolean sendFailMessage);
	
	void setRider(IPet rider);
	
	/**
	 * Usually for respawning after calling {@link #despawnRider(boolean, boolean)}
	 */
	boolean spawnRider();
	
	void despawnRider(boolean makeSound, boolean makeParticles);
	
	void removeRider(boolean makeSound, boolean makeParticles);
	
	void removePet(boolean makeSound, boolean makeParticles);
	
	boolean teleportToOwner();
	
	boolean teleport(Location to);
	
	boolean isOwnerRiding();
	
	boolean isHat();
	
	void ownerRidePet(boolean flag);
	
	void setAsHat(boolean flag);
	
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
	
	void generatePetInfo(List<String> info);
}