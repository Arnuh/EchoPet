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

import com.dsh105.echopet.api.pet.LivingPet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityShulkerPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IShulkerPet;
import com.dsh105.echopet.compat.api.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @since Mar 7, 2016
 */

@EntityPetType(petType = PetType.SHULKER)
public class ShulkerPet extends LivingPet implements IShulkerPet{
	
	protected byte peek;
	protected DyeColor color;
	
	public ShulkerPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setPeek(byte peek){
		this.peek = peek;
		((IEntityShulkerPet) getEntityPet()).setPeek(peek);
	}
	
	@Override
	public byte getPeek(){
		return peek;
	}
	
	@Override
	public void setColor(DyeColor color){
		this.color = color;
		((IEntityShulkerPet) getEntityPet()).setColor(color);
	}
	
	@Override
	public DyeColor getColor(){
		return color;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		info.add(ChatColor.GOLD + " - Peek: " + ChatColor.YELLOW + getPeek());
		info.add(ChatColor.GOLD + " - Color: " + ChatColor.YELLOW + (color == null ? "No Color" : StringUtil.capitalise(getColor().toString().replace("_", " "))));
	}
}
