package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWitherSkeletonPet;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntitySize(width = 0.7F, height = 2.4F)
@EntityPetType(petType = PetType.WITHERSKELETON)
public class EntityWitherSkeletonPet extends EntitySkeletonAbstractPet implements IEntityWitherSkeletonPet{
	
	public EntityWitherSkeletonPet(World world){
		super(EntityTypes.WITHER_SKELETON, world);
	}
	
	public EntityWitherSkeletonPet(World world, final IPet pet){
		super(EntityTypes.WITHER_SKELETON, world, pet);
	}
}