package com.dsh105.echopet.compat.api.entity.type.pet;

import com.dsh105.echopet.compat.api.entity.IPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
public interface IVexPet extends IPet{

	public void setPowered(boolean flag);

	public boolean isPowered();
}
