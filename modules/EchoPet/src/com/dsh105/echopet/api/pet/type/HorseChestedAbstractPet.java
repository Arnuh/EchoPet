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

import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorseChestedAbstractPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseChestedAbstractPet;
import org.bukkit.entity.Player;

/**
 * @since Nov 19, 2016
 */
public abstract class HorseChestedAbstractPet extends HorseAbstractPet implements IHorseChestedAbstractPet{
	
	boolean chested = false;
	
	public HorseChestedAbstractPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setChested(boolean flag){
		((IEntityHorseChestedAbstractPet) getEntityPet()).setChested(flag);
		this.chested = flag;
	}
	
	@Override
	public boolean isChested(){
		return this.chested;
	}
}
