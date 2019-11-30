package com.dsh105.echopet.compat.api.entity.type.pet;

import org.bukkit.DyeColor;
import org.bukkit.entity.Llama;

public interface ILlamaPet extends IHorseChestedAbstractPet{
	
	DyeColor getCarpetColor();
	
	Llama.Color getSkinColor();
	
	void setCarpetColor(DyeColor color);
	
	void setSkinColor(Llama.Color skinColor);
}
