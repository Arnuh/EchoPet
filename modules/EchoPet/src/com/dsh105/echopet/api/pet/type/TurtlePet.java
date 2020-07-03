package com.dsh105.echopet.api.pet.type;

import com.dsh105.echopet.api.pet.AgeablePet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.ITurtlePet;
import org.bukkit.entity.Player;

/**
 * @author Arnah
 * @since Aug 11, 2018
 */
@EntityPetType(petType = PetType.TURTLE)
public class TurtlePet extends AgeablePet implements ITurtlePet{
	
	public TurtlePet(Player owner){
		super(owner);
	}
}