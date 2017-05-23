package com.dsh105.echopet.compat.api.entity.type.pet;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.ParrotVariant;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since May 23, 2017
 */
public interface IParrotPet extends IPet{

	public ParrotVariant getVariant();

	public void setVariant(ParrotVariant variant);
}
