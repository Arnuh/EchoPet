package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySalmonPet;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;

/**
 * @author Arnah
 * @since Aug 12, 2018
 */
@EntitySize(width = 0.7F, height = 0.4F)
@EntityPetType(petType = PetType.SALMON)
public class EntitySalmonPet extends EntityFishPet implements IEntitySalmonPet{
	
	public EntitySalmonPet(World world){
		super(EntityTypes.SALMON, world);
	}
	
	public EntitySalmonPet(World world, IPet pet){
		super(EntityTypes.SALMON, world, pet);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
}
