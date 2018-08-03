package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.ICodPet;

/**
 * @author Arnah
 * @since Aug 2, 2018
*/
@EntityPetType(petType = PetType.COD)
public class CodPet extends FishPet implements ICodPet{

	public CodPet(Player owner){
		super(owner);
	}
}
