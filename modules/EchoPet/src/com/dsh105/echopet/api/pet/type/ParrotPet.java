package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.ParrotVariant;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityParrotPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IParrotPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since May 23, 2017
 */
@EntityPetType(petType = PetType.PARROT)
public class ParrotPet extends Pet implements IParrotPet{

	protected ParrotVariant variant = ParrotVariant.BLUE;// its random?

	public ParrotPet(Player owner){
		super(owner);
	}

	@Override
	public ParrotVariant getVariant(){
		return variant;
	}

	@Override
	public void setVariant(ParrotVariant variant){
		((IEntityParrotPet) getEntityPet()).setVariant(variant);
		this.variant = variant;
	}
}
