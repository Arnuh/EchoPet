package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorseChestedAbstractPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseChestedAbstractPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
public abstract class HorseChestedAbstractPet extends HorseAbstractPet implements IHorseChestedAbstractPet{

	boolean chested = false;

	public HorseChestedAbstractPet(Player owner){
		super(owner);
	}

	@Override
	public void setChested(boolean flag){
		((IEntityHorseChestedAbstractPet) getEntityPet()).setChested(flag);
		this.chested = flag;
	}

	@Override
	public boolean isChested(){
		return this.chested;
	}
}
