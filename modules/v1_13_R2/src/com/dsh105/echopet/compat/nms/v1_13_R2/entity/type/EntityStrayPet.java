package com.dsh105.echopet.compat.nms.v1_13_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityStrayPet;

import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntitySize(width = 0.6F, height = 1.9F)
@EntityPetType(petType = PetType.STRAY)
public class EntityStrayPet extends EntitySkeletonAbstractPet implements IEntityStrayPet{

	public EntityStrayPet(World world){
		super(EntityTypes.STRAY, world);
	}

	public EntityStrayPet(World world, final IPet pet){
		super(EntityTypes.STRAY, world, pet);
	}
}
