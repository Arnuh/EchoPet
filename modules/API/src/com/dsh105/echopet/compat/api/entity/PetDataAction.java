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

import javax.annotation.Nullable;
import org.bukkit.entity.Player;

public interface PetDataAction{
	
	/**
	 * Turns on or off the respective PetData for the provided Pet.<br>
	 * This method assumes you will call {@link com.dsh105.echopet.compat.api.plugin.IPetManager#setData(IPet, PetData, boolean)}
	 * to properly save the state of the flag across restarts, logouts, or respawns.<br>
	 * @return If the PetData was properly processed for the respective Pet Type.
	 */
	boolean click(Player player, IPet pet, @Nullable PetDataCategory category, boolean flag);
}
