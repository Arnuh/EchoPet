package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IElderGuardianPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 18, 2016
 */
@EntityPetType(petType = PetType.ELDERGUARDIAN)
public class ElderGuardianPet extends GuardianPet implements IElderGuardianPet{

	public ElderGuardianPet(Player owner){
		super(owner);
	}

	@Override
	public boolean isElder(){
		return true;
	}

	@Override
	public void setElder(boolean flag){}
}
