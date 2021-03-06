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

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorseAbstractPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseAbstractPet;
import org.bukkit.entity.Player;

/**
 * @since Nov 19, 2016
 */
public abstract class HorseAbstractPet extends Pet implements IHorseAbstractPet{
	
	HorseVariant horseVariant = HorseVariant.HORSE;
	boolean baby = false;
	boolean saddle = false;
	
	public HorseAbstractPet(Player owner){
		super(owner);
	}
	
	@Override
	public boolean isBaby(){
		return this.baby;
	}
	
	@Override
	public boolean isSaddled(){
		return this.saddle;
	}
	
	@Override
	public HorseVariant getVariant(){
		return this.horseVariant;
	}
	
	@Override
	public void setBaby(boolean flag){
		((IEntityHorseAbstractPet) getEntityPet()).setBaby(flag);
		this.baby = flag;
	}
	
	@Override
	public void setSaddled(boolean flag){
		((IEntityHorseAbstractPet) getEntityPet()).setSaddled(flag);
		this.saddle = flag;
	}
	
	@Override
	public void setVariant(HorseVariant variant){
		((IEntityHorseAbstractPet) getEntityPet()).setVariant(variant);
		this.horseVariant = variant;
	}
}
