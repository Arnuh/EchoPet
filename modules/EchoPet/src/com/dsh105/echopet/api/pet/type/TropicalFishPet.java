package com.dsh105.echopet.api.pet.type;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.TropicalFish.Pattern;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityTropicalFishPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ITropicalFishPet;


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
