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

import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.plugin.action.ActionChain;
import org.bukkit.entity.Player;

public interface IStorageManager{
	
	boolean init();
	
	void shutdown();
	
	ActionChain<IPet> save(Player player, IPet pet, SavedType savedType);
	
	ActionChain<Boolean> save(Player player, PetStorage pet, PetStorage rider, SavedType savedType);
	
	ActionChain<PetStorage> load(Player player, SavedType savedType);
	
	ActionChain<Boolean> remove(Player player, SavedType savedType);
	
	ActionChain<Boolean> removeAll();
}
