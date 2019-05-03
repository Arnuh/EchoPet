package com.dsh105.echopet.compat.api.entity.type.pet;

import org.bukkit.DyeColor;
import org.bukkit.entity.Llama;

public interface ILlamaPet extends IHorseChestedAbstractPet{

	public DyeColor getCarpetColor();

	public Llama.Color getSkinColor();

	public void setCarpetColor(DyeColor color);

	public void setSkinColor(Llama.Color skinColor);
}
