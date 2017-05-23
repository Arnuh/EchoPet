package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IIllusionerPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since May 23, 2017
 */
@EntityPetType(petType = PetType.ILLUSIONER)
public class IllusionerPet extends IllagerAbstractPet implements IIllusionerPet{

	public IllusionerPet(Player owner){
		super(owner);
	}
}
