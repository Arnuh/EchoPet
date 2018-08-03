package com.dsh105.echopet.compat.nms.v1_13_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityDrownedPet;

import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.World;

/**
 * @author Arnah
 * @since Aug 2, 2018
*/
@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.DROWNED)
public class EntityDrownedPet extends EntityZombiePet implements IEntityDrownedPet{

	public EntityDrownedPet(World world){
		super(EntityTypes.DROWNED, world);
	}

	public EntityDrownedPet(World world, IPet pet){
		super(EntityTypes.DROWNED, world, pet);
	}
}
