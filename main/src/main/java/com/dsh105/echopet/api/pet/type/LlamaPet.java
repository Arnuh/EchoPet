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
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.handle.IEntityLlamaPetHandle;
import com.dsh105.echopet.compat.api.entity.type.pet.ILlamaPet;
import com.dsh105.echopet.compat.api.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;


@EntityPetType(petType = PetType.LLAMA)
public class LlamaPet extends HorseChestedAbstractPet implements ILlamaPet{
	
	protected DyeColor carpetColor = null;
	protected Llama.Color skinColor = Llama.Color.WHITE;
	
	public LlamaPet(Player owner){
		super(owner);
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
		((IEntityLlamaPetHandle) getHandle()).setCarpetColor(color);
		carpetColor = color;
	}
	
	@Override
	public void setSkinColor(Llama.Color skinColor){
		((IEntityLlamaPetHandle) getHandle()).setSkinColor(skinColor);
		this.skinColor = skinColor;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		DyeColor carpetColor = getCarpetColor();
		info.add(ChatColor.GOLD + " - Carpet Colour: " + ChatColor.YELLOW + (carpetColor == null ? "None" : StringUtil.capitalise(carpetColor.toString()
			.replace("_", " "))));
		info.add(ChatColor.GOLD + " - Variant: " + ChatColor.YELLOW + StringUtil.capitalise(getSkinColor().toString().replace("_", " ")));
	}
}