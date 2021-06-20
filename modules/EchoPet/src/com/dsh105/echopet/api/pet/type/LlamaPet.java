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

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityLlamaPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ILlamaPet;
import org.bukkit.DyeColor;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;


@EntityPetType(petType = PetType.LLAMA)
public class LlamaPet extends HorseChestedAbstractPet implements ILlamaPet{
	
	private DyeColor carpetColor = null;
	private Llama.Color skinColor = Llama.Color.WHITE;
	
	public LlamaPet(Player owner){
		super(owner);
	}
	
	@Override
	public HorseVariant getVariant(){
		return HorseVariant.LLAMA;
	}
	
	@Override
	public DyeColor getCarpetColor(){
		return carpetColor;
	}
	
	@Override
	public Llama.Color getSkinColor(){
		return skinColor;
	}
	
	@Override
	public void setCarpetColor(DyeColor color){
		((IEntityLlamaPet) getEntityPet()).setCarpetColor(color);
		carpetColor = color;
	}
	
	@Override
	public void setSkinColor(Llama.Color skinColor){
		((IEntityLlamaPet) getEntityPet()).setSkinColor(skinColor);
		this.skinColor = skinColor;
	}
}