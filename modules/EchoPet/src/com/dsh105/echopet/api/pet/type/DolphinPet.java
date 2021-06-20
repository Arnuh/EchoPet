package com.dsh105.echopet.api.pet.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IDolphinPet;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.DOLPHIN)
public class DolphinPet extends WaterAnimalPet implements IDolphinPet{
	
	public DolphinPet(Player owner){
		super(owner);
	}
}
