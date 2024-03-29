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
import com.dsh105.echopet.api.pet.TameablePet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWolfPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IWolfPet;
import com.dsh105.echopet.compat.api.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.WOLF)
public class WolfPet extends TameablePet implements IWolfPet{
	
	protected DyeColor collar = DyeColor.RED;
	protected boolean angry = false;
	
	public WolfPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setCollarColor(DyeColor dc){
		((IEntityWolfPet) getEntityPet()).setCollarColor(dc);
		this.collar = dc;
	}
	
	@Override
	public DyeColor getCollarColor(){
		return this.collar;
	}
	
	@Override
	public void setAngry(boolean flag){
		((IEntityWolfPet) getEntityPet()).setAngry(flag);
		this.angry = flag;
	}
	
	@Override
	public boolean isAngry(){
		return this.angry;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		info.add(ChatColor.GOLD + " - Angry: " + ChatColor.YELLOW + isAngry());
		String color = getCollarColor() == null ? "Red" : StringUtil.capitalise(getCollarColor().toString().replace("_", " "));
		info.add(ChatColor.GOLD + " - Collar Colour: " + ChatColor.YELLOW + color);
	}
}