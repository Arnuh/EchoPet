package com.dsh105.echopet.compat.api.entity.type.nms;

import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;

public interface IEntityTropicalFishPet extends IEntityFishPet{
	
	void setVariantData(boolean large, TropicalFish.Pattern pattern, DyeColor bodyColor, DyeColor patternColor);
}
