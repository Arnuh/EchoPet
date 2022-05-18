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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySlimePet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISlimePet;
import com.dsh105.echopet.compat.api.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.SLIME)
public class SlimePet extends LivingPet implements ISlimePet{
	
	protected int size = 1;
	
	public SlimePet(Player owner){
		super(owner);
	}
	
	@Override
	public void setSize(int i){
		((IEntitySlimePet) getEntityPet()).setSize(i);
		this.size = i;
	}
	
	@Override
	public int getSize(){
		return this.size;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		String size = StringUtil.capitalise(String.valueOf(getSize()));
		String s = " (Small)";
		if(size.equals("2")) s = " (Medium)";
		if(size.equals("4")) s = " (Large)";
		info.add(ChatColor.GOLD + " - Slime Size: " + ChatColor.YELLOW + size + s);
	}
}