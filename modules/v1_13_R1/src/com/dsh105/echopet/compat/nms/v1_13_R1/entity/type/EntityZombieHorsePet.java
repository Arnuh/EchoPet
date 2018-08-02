package com.dsh105.echopet.compat.nms.v1_13_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityZombieHorsePet;

import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntitySize(width = 1.4F, height = 1.6F)
@EntityPetType(petType = PetType.ZOMBIEHORSE)
public class EntityZombieHorsePet extends EntityHorseChestedAbstractPet implements IEntityZombieHorsePet{

	public EntityZombieHorsePet(World world){
		super(EntityTypes.ZOMBIE_HORSE, world);
	}

	public EntityZombieHorsePet(World world, IPet pet){
		super(EntityTypes.ZOMBIE_HORSE, world, pet);
	}
}
