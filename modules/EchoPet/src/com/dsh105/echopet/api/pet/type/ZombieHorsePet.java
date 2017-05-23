package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IZombieHorsePet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntityPetType(petType = PetType.ZOMBIEHORSE)
public class ZombieHorsePet extends HorseAbstractPet implements IZombieHorsePet{

	public ZombieHorsePet(Player owner){
		super(owner);
	}

	@Override
	public HorseVariant getVariant(){
		return HorseVariant.UNDEAD_HORSE;
	}
}
