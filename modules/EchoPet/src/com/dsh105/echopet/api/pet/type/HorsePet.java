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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.HorseArmour;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorsePet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorsePet;

@EntityPetType(petType = PetType.HORSE)
public class HorsePet extends HorseAbstractPet implements IHorsePet{

	Color color = Horse.Color.WHITE;
	Style style = Style.NONE;
    HorseArmour armour = HorseArmour.NONE;

    public HorsePet(Player owner) {
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
    public void setArmour(HorseArmour armour) {
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
    public HorseArmour getArmour() {
        return this.armour;
    }
}