package com.dsh105.echopet.compat.nms.v1_12_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityStrayPet;

import net.minecraft.server.v1_11_R1.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntitySize(width = 0.6F, height = 1.9F)
@EntityPetType(petType = PetType.STRAY)
public class EntityStrayPet extends EntitySkeletonPet implements IEntityStrayPet{

	public EntityStrayPet(World world){
		super(world);
	}

	public EntityStrayPet(World world, final IPet pet){
		super(world, pet);
	}

	public SkeletonType getSkeletonType(){
		return SkeletonType.STRAY;
	}
}
