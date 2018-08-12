package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityTurtlePet;
import com.dsh105.echopet.compat.api.entity.type.pet.ITurtlePet;

/**
 * @author Arnah
 * @since Aug 11, 2018
*/
@EntityPetType(petType = PetType.TURTLE)
public class TurtlePet extends Pet implements ITurtlePet{

	private boolean baby;

	public TurtlePet(Player owner){
		super(owner);
	}

	@Override
	public void setBaby(boolean flag){
		((IEntityTurtlePet) getEntityPet()).setBaby(flag);
		this.baby = flag;
	}

	@Override
	public boolean isBaby(){
		return this.baby;
	}
}