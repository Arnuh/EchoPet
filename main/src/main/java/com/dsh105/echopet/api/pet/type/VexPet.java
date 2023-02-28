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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVexPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IVexPet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @since Nov 19, 2016
 */
@EntityPetType(petType = PetType.VEX)
public class VexPet extends LivingPet implements IVexPet{
	
	protected boolean powered;
	
	public VexPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setPowered(boolean flag){
		((IEntityVexPet) getEntityPet()).setIsCharging(flag);
		powered = flag;
	}
	
	@Override
	public boolean isPowered(){
		return powered;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		info.add(ChatColor.GOLD + " - Powered: " + ChatColor.YELLOW + isPowered());
	}
}