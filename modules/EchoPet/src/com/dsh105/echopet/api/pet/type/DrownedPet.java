package com.dsh105.echopet.api.pet.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IDrownedPet;
import org.bukkit.entity.Player;

/**
 * @author Arnah
 * @since Aug 2, 2018
 */
@EntityPetType(petType = PetType.DROWNED)
public class DrownedPet extends ZombiePet implements IDrownedPet{
	
	public DrownedPet(Player owner){
		super(owner);
	}
}
