package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorseAbstractPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseAbstractPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
public abstract class HorseAbstractPet extends Pet implements IHorseAbstractPet{

	HorseVariant horseVariant = HorseVariant.HORSE;
	boolean baby = false;
	boolean saddle = false;

	public HorseAbstractPet(Player owner){
        super(owner);
    }

	@Override
	public boolean isBaby(){
		return this.baby;
	}

	@Override
	public boolean isSaddled(){
		return this.saddle;
	}

	@Override
	public HorseVariant getVariant(){
		return this.horseVariant;
	}

	@Override
	public void setBaby(boolean flag){
		((IEntityHorseAbstractPet) getEntityPet()).setBaby(flag);
		this.baby = flag;
	}

	@Override
	public void setSaddled(boolean flag){
		((IEntityHorseAbstractPet) getEntityPet()).setSaddled(flag);
		this.saddle = flag;
	}

	@Override
	public void setVariant(HorseVariant variant){
		((IEntityHorseAbstractPet) getEntityPet()).setVariant(variant);
		this.horseVariant = variant;
	}
}
