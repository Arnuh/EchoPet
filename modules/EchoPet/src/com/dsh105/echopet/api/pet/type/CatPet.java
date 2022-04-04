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
import com.dsh105.echopet.compat.api.entity.CatType;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityCatPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ICatPet;
import com.dsh105.echopet.compat.api.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.CAT)
public class CatPet extends TameablePet implements ICatPet{
	
	protected CatType catType = CatType.Tabby;
	protected DyeColor collarCollar = DyeColor.RED;
	
	public CatPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setType(CatType type){
		((IEntityCatPet) getEntityPet()).setType(type);
		this.catType = type;
	}
	
	@Override
	public CatType getType(){
		return this.catType;
	}
	
	@Override
	public void setCollarColor(DyeColor color){
		((IEntityCatPet) getEntityPet()).setCollarColor(color);
		this.collarCollar = color;
	}
	
	@Override
	public DyeColor getCollarColor(){
		return this.collarCollar;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		info.add(ChatColor.GOLD + " - Type: " + ChatColor.YELLOW + getType());
		info.add(ChatColor.GOLD + " - Collar Color: " + ChatColor.YELLOW + StringUtil.capitalise(getCollarColor().name().replace("_", " ")));
	}
	
}