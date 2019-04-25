package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPufferFishPet;

import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;

/**
 * @author Arnah
 * @since Aug 12, 2018
*/
@EntitySize(width = 0.7F, height = 0.7F)
@EntityPetType(petType = PetType.PUFFERFISH)
public class EntityPufferFishPet extends EntityFishPet implements IEntityPufferFishPet{

	public EntityPufferFishPet(World world){
		super(EntityTypes.PUFFERFISH, world);
	}

	public EntityPufferFishPet(World world, IPet pet){
		super(EntityTypes.PUFFERFISH, world, pet);
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
}
