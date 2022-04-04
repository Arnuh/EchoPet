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
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPufferFishPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPufferFishPet;
import com.dsh105.echopet.compat.api.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


@EntityPetType(petType = PetType.PUFFERFISH)
public class PufferFishPet extends FishPet implements IPufferFishPet{
	
	protected int state = 0;
	
	public PufferFishPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setPuffState(int state){
		this.state = state;
		((IEntityPufferFishPet) getEntityPet()).setPuffState(state);
	}
	
	@Override
	public int getPuffState(){
		return state;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		String size = StringUtil.capitalise(String.valueOf(getPuffState()));
		String s = " (Small)";
		if(size.equals("1")) s = " (Mid)";
		if(size.equals("2")) s = " (Full)";
		info.add(ChatColor.GOLD + " - Slime Size: " + ChatColor.YELLOW + size + s);
	}
}
