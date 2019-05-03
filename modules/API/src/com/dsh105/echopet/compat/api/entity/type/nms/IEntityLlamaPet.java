package com.dsh105.echopet.compat.api.entity.type.nms;

import org.bukkit.DyeColor;

import com.dsh105.echopet.compat.api.entity.LlamaSkin;


public interface IEntityLlamaPet extends IEntityHorseChestedAbstractPet{

	public void setCarpetColor(DyeColor color);

	public void setSkinColor(LlamaSkin skinColor);
}
