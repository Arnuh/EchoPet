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
import com.dsh105.echopet.compat.api.entity.HorseArmor;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorsePet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorsePet;
import com.dsh105.echopet.compat.api.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.HORSE)
public class HorsePet extends HorseAbstractPet implements IHorsePet{
	
	protected Color color = Horse.Color.WHITE;
	protected Style style = Style.NONE;
	protected HorseArmor armour = HorseArmor.None;
	
	public HorsePet(Player owner){
		super(owner);
	}
	
	@Override
	public void setColor(Color color){
		((IEntityHorsePet) getEntityPet()).setColor(color);
		this.color = color;
	}
	
	@Override
	public void setStyle(Style style){
		((IEntityHorsePet) getEntityPet()).setStyle(style);
		this.style = style;
	}
	
	@Override
	public void setArmour(HorseArmor armour){
		((IEntityHorsePet) getEntityPet()).setArmour(armour);
		this.armour = armour;
	}
	
	@Override
	public Color getColor(){
		return this.color;
	}
	
	@Override
	public Style getStyle(){
		return this.style;
	}
	
	@Override
	public HorseArmor getArmour(){
		return this.armour;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		info.add(ChatColor.GOLD + " - Color: " + ChatColor.YELLOW + StringUtil.capitalise(getColor().toString().replace("_", " ")));
		info.add(ChatColor.GOLD + " - Style: " + ChatColor.YELLOW + StringUtil.capitalise(getStyle().toString().replace("_", " ")));
		info.add(ChatColor.GOLD + " - Armour: " + ChatColor.YELLOW + StringUtil.capitalise(getArmour().toString().replace("_", " ")));
	}
}