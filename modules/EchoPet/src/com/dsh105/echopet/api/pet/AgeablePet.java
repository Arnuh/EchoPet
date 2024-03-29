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

package com.dsh105.echopet.api.pet;

import java.util.List;
import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityAgeablePetHandle;
import com.dsh105.echopet.compat.api.entity.pet.IAgeablePet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public abstract class AgeablePet extends LivingPet implements IAgeablePet{
	
	public AgeablePet(Player owner){
		super(owner);
	}
	
	private boolean baby;
	
	@Override
	public void setBaby(boolean flag){
		((IEntityAgeablePetHandle) getEntityPet().getHandle()).setBaby(flag);
		this.baby = flag;
	}
	
	@Override
	public boolean isBaby(){
		return this.baby;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		info.add(ChatColor.GOLD + " - Baby: " + ChatColor.YELLOW + isBaby());
	}
}
