package com.dsh105.echopet.compat.api.entity.type.pet;

import com.dsh105.echopet.compat.api.entity.IAgeablePet;


public interface IPolarBearPet extends IAgeablePet{
	
	void setStandingUp(boolean flag);
	
	boolean isStandingUp();
}
