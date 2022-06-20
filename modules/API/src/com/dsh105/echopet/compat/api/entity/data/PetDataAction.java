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

package com.dsh105.echopet.compat.api.entity.data;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.entity.IPet;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface PetDataAction<T>{
	
	/**
	 * Provides a setter used for the respective {@link PetData} to set the value of the {@link PetData} on the provided pet.
	 */
	Consumer<T> get(Player player, IPet pet, @Nullable PetDataCategory category);
}
