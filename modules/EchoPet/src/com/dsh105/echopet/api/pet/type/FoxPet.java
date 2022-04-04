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
import com.dsh105.echopet.compat.api.entity.FoxType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityFoxPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IFoxPet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.FOX)
public class FoxPet extends AgeablePet implements IFoxPet{
	
	private FoxType type = FoxType.Red;
	private boolean sitting, crouching, headtilt, pounce, sleeping, shake;
	
	public FoxPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setType(FoxType type){
		this.type = type;
		((IEntityFoxPet) getEntityPet()).setType(type.ordinal());
	}
	
	@Override
	public void setSitting(boolean sitting){
		this.sitting = sitting;
		((IEntityFoxPet) getEntityPet()).setSitting(sitting);
	}
	
	@Override
	public void setCrouching(boolean crouching){
		this.crouching = crouching;
		((IEntityFoxPet) getEntityPet()).setCrouching(crouching);
	}
	
	@Override
	public void setHeadTilt(boolean tilted){
		this.headtilt = tilted;
		((IEntityFoxPet) getEntityPet()).setHeadTilt(tilted);
	}
	
	@Override
	public void setPounce(boolean pounce){
		this.pounce = pounce;
		((IEntityFoxPet) getEntityPet()).setPounce(pounce);
	}
	
	@Override
	public void setSleeping(boolean sleeping){
		this.sleeping = sleeping;
		((IEntityFoxPet) getEntityPet()).setSleeping(sleeping);
	}
	
	@Override
	public void setLegShake(boolean shake){
		this.shake = shake;
		((IEntityFoxPet) getEntityPet()).setLegShake(shake);
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		info.add(ChatColor.GOLD + " - Type: " + ChatColor.YELLOW + type.toString());
		info.add(ChatColor.GOLD + " - Sitting: " + ChatColor.YELLOW + sitting);
		info.add(ChatColor.GOLD + " - Crouching: " + ChatColor.YELLOW + crouching);
		info.add(ChatColor.GOLD + " - Head Tilt: " + ChatColor.YELLOW + headtilt);
		info.add(ChatColor.GOLD + " - Pouncing: " + ChatColor.YELLOW + pounce);
		info.add(ChatColor.GOLD + " - Sleeping: " + ChatColor.YELLOW + sleeping);
		info.add(ChatColor.GOLD + " - Shaking: " + ChatColor.YELLOW + shake);
	}
}
