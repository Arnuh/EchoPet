package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.ITropicalFishPet;

/**
 * @author Arnah
 * @since Aug 12, 2018
*/
@EntityPetType(petType = PetType.TROPICALFISH)
public class TropicalFishPet extends FishPet implements ITropicalFishPet{

	public TropicalFishPet(Player owner){
		super(owner);
	}
}
