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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySheepPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISheepPet;
import com.dsh105.echopet.compat.api.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.SHEEP)
public class SheepPet extends AgeablePet implements ISheepPet{
	
	boolean sheared;
	Color color;
	
	public SheepPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setSheared(boolean flag){
		((IEntitySheepPet) getEntityPet()).setSheared(flag);
		this.sheared = flag;
	}
	
	@Override
	public boolean isSheared(){
		return this.sheared;
	}
	
	@Override
	public DyeColor getDyeColor(){
		return DyeColor.getByColor(color);
	}
	
	@Override
	public Color getColor(){
		return color;
	}
	
	@Override
	public void setDyeColor(DyeColor c){
		((IEntitySheepPet) getEntityPet()).setDyeColor(c);
		this.color = c.getColor();
	}
	
	@Override
	public void setColor(Color c){
		this.color = c;
		((IEntitySheepPet) getEntityPet()).setDyeColor(getDyeColor());
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		String color = getColor() == null ? "Default" : StringUtil.capitalise(getColor().toString().replace("_", " "));
		info.add(ChatColor.GOLD + " - Wool Colour: " + ChatColor.YELLOW + color);
	}
}
