package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityStrayPet;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;

/**
 * @author Arnah
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
