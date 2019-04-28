package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IStrayPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntityPetType(petType = PetType.STRAY)
public class StrayPet extends SkeletonPet implements IStrayPet{

	public StrayPet(Player owner){
		super(owner);
	}

}