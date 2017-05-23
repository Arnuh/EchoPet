package com.dsh105.echopet.compat.nms.v1_12_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWitherSkeletonPet;

import net.minecraft.server.v1_12_R1.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntitySize(width = 0.7F, height = 2.4F)
@EntityPetType(petType = PetType.WITHERSKELETON)
public class EntityWitherSkeletonPet extends EntitySkeletonPet implements IEntityWitherSkeletonPet{

	public EntityWitherSkeletonPet(World world){
		super(world);
	}

	public EntityWitherSkeletonPet(World world, final IPet pet){
		super(world, pet);
	}

	public SkeletonType getSkeletonType(){
		return SkeletonType.WITHER;
	}
}
