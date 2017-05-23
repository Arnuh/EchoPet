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

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySheepPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISheepPet;

@EntityPetType(petType = PetType.SHEEP)
public class SheepPet extends Pet implements ISheepPet {

    boolean baby;
    boolean sheared;
	Color color;

    public SheepPet(Player owner) {
        super(owner);
    }

    @Override
    public void setBaby(boolean flag) {
        ((IEntitySheepPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    @Override
    public void setSheared(boolean flag) {
        ((IEntitySheepPet) getEntityPet()).setSheared(flag);
        this.sheared = flag;
    }

    @Override
    public boolean isSheared() {
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

}
