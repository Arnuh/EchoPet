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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGoatPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IGoatPet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.GOAT)
public class GoatPet extends AgeablePet implements IGoatPet{
	
	protected boolean screaming;
	protected boolean leftHorn, rightHorn;
	
	public GoatPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setScreaming(boolean flag){
		((IEntityGoatPet) getEntityPet()).setScreaming(flag);
		this.screaming = flag;
	}
	
	@Override
	public void setLeftHorn(boolean flag){
		((IEntityGoatPet) getEntityPet()).setLeftHorn(flag);
		this.leftHorn = flag;
		
	}
	
	@Override
	public void setRightHorn(boolean flag){
		((IEntityGoatPet) getEntityPet()).setRightHorn(flag);
		this.rightHorn = flag;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		info.add(ChatColor.GOLD + " - Screaming: " + ChatColor.YELLOW + screaming);
		info.add(ChatColor.GOLD + " - Left Horn: " + ChatColor.YELLOW + leftHorn);
		info.add(ChatColor.GOLD + " - Right Horn: " + ChatColor.YELLOW + rightHorn);
	}
}
