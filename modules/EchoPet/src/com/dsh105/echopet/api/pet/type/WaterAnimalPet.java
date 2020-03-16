package com.dsh105.echopet.api.pet.type;

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.type.pet.IWaterAnimalPet;
import org.bukkit.entity.Player;

/**
 * @author Arnah
 * @since Aug 2, 2018
 */
public class WaterAnimalPet extends Pet implements IWaterAnimalPet{
	
	public WaterAnimalPet(Player owner){
		super(owner);
	}
}
