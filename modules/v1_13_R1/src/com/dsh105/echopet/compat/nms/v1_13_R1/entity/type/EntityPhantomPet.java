package com.dsh105.echopet.compat.nms.v1_13_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPhantomPet;

import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.World;

/**
 * @author Arnah
 * @since Aug 12, 2018
*/
@EntitySize(width = 0.9F, height = 0.5F)
@EntityPetType(petType = PetType.PHANTOM)
public class EntityPhantomPet extends EntityFlyingPet implements IEntityPhantomPet{

	public EntityPhantomPet(World world){
		super(EntityTypes.PHANTOM, world);
	}

	public EntityPhantomPet(World world, IPet pet){
		super(EntityTypes.PHANTOM, world, pet);
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
