package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IZombieVillagerPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 18, 2016
 */
@EntityPetType(petType = PetType.ZOMBIEVILLAGER)
public class ZombieVillagerPet extends ZombiePet implements IZombieVillagerPet{

	public ZombieVillagerPet(Player owner){
		super(owner);
	}
}
