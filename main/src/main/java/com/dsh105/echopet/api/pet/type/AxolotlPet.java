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
import com.dsh105.echopet.compat.api.entity.type.nms.handle.IEntityAxolotlPetHandle;
import com.dsh105.echopet.compat.api.entity.type.pet.IAxolotlPet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

@EntityPetType(petType = PetType.AXOLOTL)
public class AxolotlPet extends AgeablePet implements IAxolotlPet{
	
	protected Variant variant;
	protected boolean playingDead;
	
	public AxolotlPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setVariant(Variant variant){
		((IEntityAxolotlPetHandle) getHandle()).setVariant(variant);
		this.variant = variant;
	}
	
	@Override
	public Variant getVariant(){
		return variant;
	}
	
	@Override
	public void setPlayingDead(boolean flag){
		((IEntityAxolotlPetHandle) getHandle()).setPlayingDead(flag);
		this.playingDead = flag;
	}
	
	@Override
	public boolean isPlayingDead(){
		return playingDead;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		info.add(ChatColor.GOLD + " - Variant: " + ChatColor.YELLOW + getVariant());
		info.add(ChatColor.GOLD + " - Playing Dead: " + ChatColor.YELLOW + isPlayingDead());
	}
}
