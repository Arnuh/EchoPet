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

package com.dsh105.echopet.compat.api.plugin;

import java.util.ArrayList;
import java.util.List;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface IPetManager{
	
	ArrayList<IPet> getPets();
	
	IPet loadPets(Player p, boolean findDefault, boolean sendMessage, boolean checkWorldOverride);
	
	void removeAllPets();
	
	IPet createPet(Player owner, IPetType petType, boolean sendMessageOnFail);
	
	IPet createPet(Player owner, IPetType petType, IPetType riderType);
	
	IPet getPet(Player player);
	
	IPet getPet(Entity pet);
	
	void forceAllValidData(IPet pet);
	
	void updateFileData(String type, IPet pet, ArrayList<PetData> list, boolean b);
	
	IPet createPetFromFile(String type, Player p);
	
	void loadRiderFromFile(IPet pet);
	
	void loadRiderFromFile(String type, IPet pet);
	
	void removePets(Player player, boolean makeDeathSound);
	
	void removePet(IPet pet, boolean makeDeathSound);
	
	void saveFileData(String type, IPet pet);
	
	void saveFileData(String type, Player p, PetStorage UPD, PetStorage UMD);
	
	void saveFileData(String type, Player p, PetStorage UPD);
	
	void clearAllFileData();
	
	void clearFileData(String type, IPet pet);
	
	void clearFileData(String type, Player p);
	
	void setData(IPet pet, List<PetData> data, boolean b);
	
	void setData(IPet pet, PetData pd, boolean b);
}