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

import com.dsh105.echopet.api.pet.AgeablePet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.handle.IEntityBeePetHandle;
import com.dsh105.echopet.compat.api.entity.type.pet.IBeePet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

@EntityPetType(petType = PetType.BEE)
public class BeePet extends AgeablePet implements IBeePet{
	
	protected boolean hasStung, hasNectar, angry;
	
	public BeePet(Player owner){
		super(owner);
	}
	
	@Override
	public void setHasStung(boolean hasStung){
		this.hasStung = hasStung;
		((IEntityBeePetHandle) getHandle()).setHasStung(hasStung);
	}
	
	@Override
	public boolean hasStung(){
		return hasStung;
	}
	
	@Override
	public void setHasNectar(boolean hasNectar){
		this.hasNectar = hasNectar;
		((IEntityBeePetHandle) getHandle()).setHasNectar(hasNectar);
	}
	
	@Override
	public boolean hasNectar(){
		return hasNectar;
	}
	
	@Override
	public void setAngry(boolean angry){
		this.angry = angry;
		((IEntityBeePetHandle) getHandle()).setAngry(angry);
	}
	
	@Override
	public boolean isAngry(){
		return angry;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		info.add(ChatColor.GOLD + " - Stinger: " + ChatColor.YELLOW + (!hasStung()));
		info.add(ChatColor.GOLD + " - Nectar: " + ChatColor.YELLOW + hasNectar());
		info.add(ChatColor.GOLD + " - Angry: " + ChatColor.YELLOW + isAngry());
	}
}