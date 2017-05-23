package com.dsh105.echopet.compat.nms.v1_12_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHuskPet;

import net.minecraft.server.v1_11_R1.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 18, 2016
 */
@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.HUSK)
public class EntityHuskPet extends EntityZombiePet implements IEntityHuskPet{

	public EntityHuskPet(World world){
		super(world);
	}

	public EntityHuskPet(World world, IPet pet){
		super(world, pet);
	}

}