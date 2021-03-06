package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySkeletonHorsePet;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;


@EntitySize(width = 1.4F, height = 1.6F)
@EntityPetType(petType = PetType.SKELETONHORSE)
public class EntitySkeletonHorsePet extends EntityHorseAbstractPet implements IEntitySkeletonHorsePet{
	
	public EntitySkeletonHorsePet(World world){
		super(EntityTypes.SKELETON_HORSE, world);
	}
	
	public EntitySkeletonHorsePet(World world, IPet pet){
		super(EntityTypes.SKELETON_HORSE, world, pet);
	}
}
