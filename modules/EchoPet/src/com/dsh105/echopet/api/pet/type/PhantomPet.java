package com.dsh105.echopet.api.pet.type;

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IPhantomPet;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.PHANTOM)
public class PhantomPet extends Pet implements IPhantomPet{
	
	public PhantomPet(Player owner){
		super(owner);
	}
}
