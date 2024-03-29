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
package com.dsh105.echopet.api.pet.type;

import java.util.List;
import com.dsh105.echopet.api.pet.AgeablePet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPolarBearPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPolarBearPet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.POLARBEAR)
public class PolarBearPet extends AgeablePet implements IPolarBearPet{
	
	protected boolean standingUp;
	
	public PolarBearPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setStandingUp(boolean flag){
		((IEntityPolarBearPet) getEntityPet()).setStandingUp(flag);
		standingUp = flag;
	}
	
	@Override
	public boolean isStandingUp(){
		return standingUp;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		info.add(ChatColor.GOLD + " - Standing Up: " + ChatColor.YELLOW + isStandingUp());
	}
}
