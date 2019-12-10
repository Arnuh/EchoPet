package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPufferFishPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPufferFishPet;


@EntityPetType(petType = PetType.PUFFERFISH)
public class PufferFishPet extends FishPet implements IPufferFishPet{

	private int state = 0;

	public PufferFishPet(Player owner){
		super(owner);
	}

	@Override
	public void setPuffState(int state){
		this.state = state;
		((IEntityPufferFishPet) getEntityPet()).setPuffState(state);
	}
}
