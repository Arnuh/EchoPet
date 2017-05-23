package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVexPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IVexPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntityPetType(petType = PetType.VEX)
public class VexPet extends Pet implements IVexPet{

	private boolean powered;

	public VexPet(Player owner){
		super(owner);
	}

	@Override
	public void setPowered(boolean flag){
		((IEntityVexPet) getEntityPet()).setPowered(flag);
		powered = flag;
	}

	@Override
	public boolean isPowered(){
		return powered;
	}
}
