package com.dsh105.echopet.api.pet;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.IAgeablePet;
import com.dsh105.echopet.compat.api.entity.IEntityAgeablePet;

/**
 * @author Arnah
 * @since Apr 26, 2019
*/
public class AgeablePet extends Pet implements IAgeablePet{

	public AgeablePet(Player owner){
		super(owner);
	}

	private boolean baby;

	@Override
	public void setBaby(boolean flag){
		((IEntityAgeablePet) getEntityPet()).setBaby(flag);
		this.baby = flag;
	}

	@Override
	public boolean isBaby(){
		return this.baby;
	}
}
