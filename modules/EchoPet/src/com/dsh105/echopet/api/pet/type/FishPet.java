package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.type.pet.IFishPet;

/**
 * @author Arnah
 * @since Aug 2, 2018
*/
public class FishPet extends WaterAnimalPet implements IFishPet{

	public FishPet(Player owner){
		super(owner);
	}
}
