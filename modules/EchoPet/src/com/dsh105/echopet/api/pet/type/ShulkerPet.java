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

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityShulkerPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IShulkerPet;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

/**
 * @since Mar 7, 2016
 */

@EntityPetType(petType = PetType.SHULKER)
public class ShulkerPet extends Pet implements IShulkerPet{
	
	protected boolean open;
	protected DyeColor color;
	
	public ShulkerPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setOpen(boolean open){
		this.open = open;
		((IEntityShulkerPet) getEntityPet()).setOpen(open);
	}
	
	public boolean isOpen(){
		return open;
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
}
