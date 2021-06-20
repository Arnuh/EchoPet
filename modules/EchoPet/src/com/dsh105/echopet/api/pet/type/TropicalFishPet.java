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
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityTropicalFishPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ITropicalFishPet;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.TropicalFish.Pattern;


@EntityPetType(petType = PetType.TROPICALFISH)
public class TropicalFishPet extends FishPet implements ITropicalFishPet{
	
	private boolean large = false;
	private TropicalFish.Pattern pattern = TropicalFish.Pattern.KOB;
	private DyeColor color = DyeColor.WHITE, patternColor = DyeColor.WHITE;
	
	public TropicalFishPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setLarge(boolean large){
		this.large = large;
		((IEntityTropicalFishPet) getEntityPet()).setVariantData(large, pattern, color, patternColor);
	}
	
	@Override
	public void setPattern(Pattern pattern){
		this.pattern = pattern;
		((IEntityTropicalFishPet) getEntityPet()).setVariantData(large, pattern, color, patternColor);
	}
	
	@Override
	public void setColor(DyeColor color){
		this.color = color;
		((IEntityTropicalFishPet) getEntityPet()).setVariantData(large, pattern, color, patternColor);
	}
	
	@Override
	public void setPatternColor(DyeColor patternColor){
		this.patternColor = patternColor;
		((IEntityTropicalFishPet) getEntityPet()).setVariantData(large, pattern, color, patternColor);
	}
	
}
