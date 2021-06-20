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

package com.dsh105.echopet.compat.api.registration;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import org.bukkit.entity.Player;

/**
 * @since Nov 18, 2016
 */
public interface IPetRegistry{
	
	PetRegistrationEntry getRegistrationEntry(PetType petType);
	
	void shutdown();
	
	IPet spawn(PetType petType, final Player owner);
	
	void enablePet(PetType petType);
	
	void disablePet(PetType petType);
}
