package com.dsh105.echopet.compat.api.entity.type.pet;

import org.bukkit.DyeColor;

import com.dsh105.echopet.compat.api.entity.LlamaSkin;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
public interface ILlamaPet extends IHorseChestedAbstractPet{

	public DyeColor getCarpetColor();

	public LlamaSkin getSkinColor();

	public void setCarpetColor(DyeColor color);

	public void setSkinColor(LlamaSkin skinColor);
}
