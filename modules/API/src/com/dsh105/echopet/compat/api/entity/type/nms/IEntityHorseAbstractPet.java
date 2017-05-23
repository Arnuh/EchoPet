package com.dsh105.echopet.compat.api.entity.type.nms;

import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.dsh105.echopet.compat.api.entity.IEntityAgeablePet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
public interface IEntityHorseAbstractPet extends IEntityAgeablePet{

	public void setSaddled(boolean flag);

	public void setVariant(HorseVariant variant);
}
