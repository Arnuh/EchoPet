package com.dsh105.echopet.compat.api.entity.type.nms;

import org.bukkit.DyeColor;
import org.bukkit.entity.Llama;


public interface IEntityLlamaPet extends IEntityHorseChestedAbstractPet{
	
	void setCarpetColor(DyeColor color);
	
	void setSkinColor(Llama.Color skinColor);
}