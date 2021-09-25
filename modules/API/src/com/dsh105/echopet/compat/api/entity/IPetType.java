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

import java.util.List;
import com.dsh105.echopet.compat.api.util.Version;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface IPetType{
	
	String name();
	
	String getDefaultName();
	
	String getDefaultName(String name);
	
	String getMinecraftName();
	
	List<PetDataCategory> getAllowedCategories();
	
	List<PetData> getAllowedDataTypes();
	
	boolean isDataAllowed(PetData data);
	
	IEntityPet getNewEntityPetInstance(Object world, IPet pet);
	
	IPet getNewPetInstance(Player owner);
	
	Class<? extends IEntityPet> getEntityClass();
	
	Class<? extends IPet> getPetClass();
	
	Version getVersion();
	
	boolean isCompatible();
	
	Material getUIMaterial();
	
	String getConfigKeyName();
	
	double getStartFollowDistance();
	
	double getStopFollowDistance();
	
	double getTeleportDistance();
	
	double getFollowSpeedModifier();
}
