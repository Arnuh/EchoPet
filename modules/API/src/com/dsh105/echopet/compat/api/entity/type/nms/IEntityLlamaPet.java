package com.dsh105.echopet.compat.api.entity.type.nms;

import org.bukkit.DyeColor;
import org.bukkit.entity.Llama;


public interface IEntityLlamaPet extends IEntityHorseChestedAbstractPet{
	
	public void setCarpetColor(DyeColor color);
	
	public void setSkinColor(Llama.Color skinColor);
}