package com.dsh105.echopet.compat.api.entity.type.nms;

import org.bukkit.DyeColor;

import com.dsh105.echopet.compat.api.entity.LlamaSkin;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
public interface IEntityLlamaPet extends IEntityHorseChestedAbstractPet{

	public void setCarpetColor(DyeColor color);

	public void setSkinColor(LlamaSkin skinColor);
}
