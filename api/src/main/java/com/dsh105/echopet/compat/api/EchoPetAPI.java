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

package com.dsh105.echopet.compat.api;

import com.dsh105.echopet.compat.api.ai.PetGoal;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.data.PetData;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.SavedType;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.StringUtil;
import com.dsh105.echopet.compat.api.util.menu.PetMenu;
import com.dsh105.echopet.compat.api.util.menu.SelectorMenu;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public final class EchoPetAPI{
	
	EchoPetAPI(){}
	
	private static EchoPetAPI instance;
	
	public static EchoPetAPI getAPI(){
		if(instance == null){
			instance = new EchoPetAPI();
		}
		return instance;
	}
	
	/**
	 * Gives a {@link IPet} to the specified {@link Player}
	 * <p>
	 * Pets will be spawned immediately next to the target player, linked until it is removed.
	 *
	 * @param player the {@link Player} that will be provided with a {@link IPet}
	 * @param petType the {@link IPetType} (type of {@link IPet}) that will be given to the player
	 * @param sendMessage defines if the plugin sends a message to the target {@link Player}
	 * @return the {@link IPet} created
	 */
	public IPet givePet(Player player, IPetType petType, boolean sendMessage){
		if(player != null && petType != null){
			IPet pet = EchoPet.getManager().createPet(player, petType, sendMessage);
			if(pet == null){
				EchoPet.LOG.severe("Failed to give " + petType.toString() + " to " + player.getName() + " through the EchoPetAPI. Maybe this PetType is disabled in the Config.yml?");
				return null;
			}
			if(sendMessage){
				Lang.sendTo(player, Lang.CREATE_PET.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
			}
			return pet;
		}
		return null;
	}
	
	/**
	 * Removes a {@link IPet} if the {@link Player} has one active
	 *
	 * @param player the {@link Player} to remove their {@link IPet} from
	 * @param sendMessage defines if the plugin sends a message to the target {@link Player}
	 */
	public void removePet(Player player, boolean sendMessage, boolean save){
		EchoPet.getManager().removePets(player, true);
		if(save){
			IPet pet = getPet(player);
			if(pet != null){
				EchoPet.getDataManager().save(player, pet, SavedType.Auto);
			}
		}
		if(sendMessage){
			Lang.sendTo(player, Lang.REMOVE_PET.toString());
		}
	}
	
	/**
	 * Checks if a {@link Player} has a {@link IPet}
	 *
	 * @param player the {@link Player} used to check for {@link IPet}
	 * @return true if {@link Player} has a {@link IPet}, false if not
	 */
	public boolean hasPet(Player player){
		return EchoPet.getManager().getPet(player) != null;
	}
	
	/**
	 * Gets a {@link Player}'s {@link IPet}
	 *
	 * @param player the {@link Player} to get the {@link IPet} of
	 * @return the {@link IPet} instance linked to the {@link Player}
	 */
	public IPet getPet(Player player){
		return EchoPet.getManager().getPet(player);
	}
	
	/**
	 * Gets all active {@link IPet}
	 *
	 * @return an array of all active {@link IPet}s
	 */
	
	public IPet[] getAllPets(){
		ArrayList<IPet> pets = EchoPet.getManager().getPets();
		return pets.toArray(new IPet[0]);
	}
	
	/**
	 * Teleports a {@link IPet} to a {@link Location}
	 *
	 * @param pet the {@link IPet} to be teleported
	 * @param location the {@link Location} to teleport the {@link IPet} to
	 * @return success of teleportation
	 */
	public boolean teleportPet(IPet pet, Location location){
		if(pet == null){
			EchoPet.LOG.severe("Failed to teleport Pet to Location through the EchoPetAPI. {@link IPet} cannot be null.");
			return false;
		}
		if(pet.isHat() || pet.isOwnerRiding()){
			return false;
		}
		return pet.teleport(location);
	}
	
	/**
	 * Adds {@link PetData} to a {@link IPet}
	 *
	 * @param pet the {@link IPet} to add the data to
	 * @param petData {@link PetData} to add to the {@link IPet}
	 * @deprecated Use {@link #setData(IPet, PetData, Object)}
	 */
	@Deprecated
	public void addData(IPet pet, PetData<?> petData){
		if(pet == null){
			EchoPet.LOG.severe("Failed to add PetData [" + petData.toString() + "] to Pet through the EchoPetAPI. Pet cannot be null.");
			return;
		}
		EchoPet.getManager().setData(pet, petData, true);
	}
	
	/**
	 * Adds {@link PetData} to a {@link IPet}
	 *
	 * @param pet The {@link IPet} to add the data to
	 * @param petData {@link PetData} to add to the {@link IPet}
	 * @param value The value of the {@link PetData}
	 * @deprecated Use {@link #setData(IPet, PetData, Object)}
	 */
	@Deprecated
	public void addData(IPet pet, PetData<?> petData, Object value){
		if(pet == null){
			EchoPet.LOG.severe("Failed to add PetData [" + petData.toString() + "] to Pet through the EchoPetAPI. Pet cannot be null.");
			return;
		}
		EchoPet.getManager().setData(pet, petData, value);
	}
	
	/**
	 * Adds {@link PetData} to a {@link IPet}
	 *
	 * @param pet The {@link IPet} to add the data to
	 * @param petData {@link PetData} to add to the {@link IPet}
	 * @param value The value of the {@link PetData}
	 */
	public void setData(IPet pet, PetData<?> petData, Object value){
		if(pet == null){
			EchoPet.LOG.severe("Failed to set PetData [" + petData.toString() + "] to Pet through the EchoPetAPI. Pet cannot be null.");
			return;
		}
		EchoPet.getManager().setData(pet, petData, value);
	}
	
	/**
	 * Removes {@link PetData} from a {@link IPet}
	 *
	 * @param pet the {@link IPet} to remove the data from
	 * @param petData {@link PetData} to remove to the {@link IPet}
	 */
	public void removeData(IPet pet, PetData<?> petData){
		if(pet == null){
			EchoPet.LOG.severe("Failed to remove PetData [" + petData.toString() + "] from Pet through the EchoPetAPI. Pet cannot be null.");
			return;
		}
		EchoPet.getManager().setData(pet, petData, false);
	}
	
	/**
	 * Checks if a {@link IPet} has specific {@link PetData}
	 *
	 * @param pet the {@link IPet} to search
	 * @param petData the {@link PetData} searched for in the {@link IPet} instance
	 * @return true if the {@link IPet} has the specified {@link PetData}
	 */
	public boolean hasData(IPet pet, PetData<?> petData){
		if(pet == null){
			EchoPet.LOG.severe("Failed to check PetData [" + petData.toString() + "] of Pet through the EchoPetAPI. Pet cannot be null.");
			return false;
		}
		return pet.getData().containsKey(petData);
	}
	
	/**
	 * Opens the Pet Selector GUI Menu
	 *
	 * @param player{@link Player} to view the Menu
	 * @param sendMessage defines if the plugin sends a message to the target {@link Player}
	 */
	public void openPetSelector(Player player, boolean sendMessage){
		new SelectorMenu(player, 0).open(player);
		if(sendMessage){
			Lang.sendTo(player, Lang.OPEN_SELECTOR.toString());
		}
	}
	
	/**
	 * Opens the Pet Selector GUI Menu
	 *
	 * @param player {@link Player} to view the menu
	 */
	public void openPetSelector(Player player){
		this.openPetSelector(player, false);
	}
	
	/**
	 * Opens the Pet Data GUI Menu
	 *
	 * @param player{@link Player} to view the Menu
	 * @param sendMessage defines if the plugin sends a message to the target {@link Player}
	 */
	public void openPetDataMenu(Player player, boolean sendMessage){
		IPet pet = EchoPet.getManager().getPet(player);
		if(pet == null){
			return;
		}
		new PetMenu(pet).open(sendMessage);
	}
	
	/**
	 * Opens the Pet Data GUI Menu
	 *
	 * @param player {@link Player} to view the menu
	 */
	public void openPetDataMenu(Player player){
		this.openPetDataMenu(player, false);
	}
	
	/**
	 * Add an implementation of {@link PetGoal} to a {@link IPet}
	 *
	 * @param pet the {@link IPet} to add the {@link PetGoal} to
	 * @param priority the priority of the goal.
	 * @param goal the {@link PetGoal} to add
	 */
	public void addGoal(IPet pet, int priority, PetGoal goal){
		if(pet == null){
			EchoPet.LOG.severe("Failed to add PetGoal to Pet AI through the EchoPetAPI. Pet cannot be null.");
			return;
		}
		if(goal == null){
			EchoPet.LOG.severe("Failed to add PetGoal to Pet AI through the EchoPetAPI. Goal cannot be null.");
			return;
		}
		pet.getHandle().getPetGoalSelector().addGoal(priority, goal);
	}
	
	/**
	 * Remove a goal from a {@link IPet}'s AI
	 * <p>
	 * The goal is identified using a string, initiated when the goal is added to the {@link IPet}
	 *
	 * @param pet {@link IPet} to remove the goal from
	 * @param identifier String that identifies a {@link PetGoal}
	 * @return If a goal with this identifier was removed.
	 */
	public boolean removeGoal(IPet pet, String identifier){
		if(pet == null){
			EchoPet.LOG.severe("Failed to remove PetGoal from Pet AI through the EchoPetAPI. Pet cannot be null.");
			return false;
		}
		return pet.getHandle().getPetGoalSelector().removeGoal(identifier);
	}
	
	/**
	 * Remove a goal from a {@link IPet}'s AI
	 *
	 * @param pet {@link IPet} to remove the goal from
	 * @param petGoal {@link PetGoal} to remove
	 * @return If the goal provided was removed.
	 */
	public boolean removeGoal(IPet pet, PetGoal petGoal){
		if(pet == null){
			EchoPet.LOG.severe("Failed to remove PetGoal from Pet AI through the EchoPetAPI. Pet cannot be null.");
			return false;
		}
		if(petGoal == null){
			EchoPet.LOG.severe("Failed to remove PetGoal from Pet AI through the EchoPetAPI. Goal cannot be null.");
			return false;
		}
		return pet.getHandle().getPetGoalSelector().removeGoal(petGoal);
	}
}

