package com.dsh105.echopet.api.pet.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.ISalmonPet;
import org.bukkit.entity.Player;

/**
 * @author Arnah
 * @since Aug 12, 2018
 */
@EntityPetType(petType = PetType.SALMON)
public class SalmonPet extends FishPet implements ISalmonPet{
	
	public SalmonPet(Player owner){
		super(owner);
	}
}
