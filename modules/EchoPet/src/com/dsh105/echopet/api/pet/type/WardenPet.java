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
import com.dsh105.echopet.api.pet.LivingPet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.handle.IEntityWardenPetHandle;
import com.dsh105.echopet.compat.api.entity.type.pet.IWardenPet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.WARDEN)
public class WardenPet extends LivingPet implements IWardenPet{
	
	private AngerLevel angerLevel;
	
	public WardenPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setAngerLevel(AngerLevel angerLevel){
		((IEntityWardenPetHandle) getEntityPet().getHandle()).setAnger(angerLevel.getMinimumAnger());
		this.angerLevel = angerLevel;
	}
	
	@Override
	public AngerLevel getAngerLevel(){
		return angerLevel;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		info.add(ChatColor.GOLD + " - Anger Level: " + ChatColor.YELLOW + getAngerLevel());
	}
}
