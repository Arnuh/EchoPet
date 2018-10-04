package com.dsh105.echopet.compat.nms.v1_13_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityTropicalFishPet;

import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;

/**
 * @author Arnah
 * @since Aug 12, 2018
*/
@EntitySize(width = 0.5F, height = 0.4F)
@EntityPetType(petType = PetType.TROPICALFISH)
public class EntityTropicalFishPet extends EntityFishPet implements IEntityTropicalFishPet{

	public EntityTropicalFishPet(World world){
		super(EntityTypes.TROPICAL_FISH, world);
	}

	public EntityTropicalFishPet(World world, IPet pet){
		super(EntityTypes.TROPICAL_FISH, world, pet);
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
}
