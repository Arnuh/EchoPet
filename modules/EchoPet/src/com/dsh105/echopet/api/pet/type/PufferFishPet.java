package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IPufferFishPet;

/**
 * @author Arnah
 * @since Aug 12, 2018
*/
@EntityPetType(petType = PetType.PUFFERFISH)
public class PufferFishPet extends FishPet implements IPufferFishPet{

	public PufferFishPet(Player owner){
		super(owner);
	}
}
