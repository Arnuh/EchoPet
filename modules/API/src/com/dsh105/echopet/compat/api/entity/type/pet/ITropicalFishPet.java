package com.dsh105.echopet.compat.api.entity.type.pet;

import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;

public interface ITropicalFishPet extends IFishPet{
	
	void setLarge(boolean large);
	
	void setPattern(TropicalFish.Pattern pattern);
	
	void setColor(DyeColor color);
	
	void setPatternColor(DyeColor patternColor);
}
