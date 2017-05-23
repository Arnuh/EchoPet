package com.dsh105.echopet.compat.api.entity.type.pet;

import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.dsh105.echopet.compat.api.entity.IAgeablePet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
public interface IHorseAbstractPet extends IAgeablePet{

	public boolean isBaby();

	public boolean isSaddled();

	public HorseVariant getVariant();

	public void setBaby(boolean flag);

	public void setSaddled(boolean flag);

	public void setVariant(HorseVariant variant);
}
