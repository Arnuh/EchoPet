package com.dsh105.echopet.compat.nms.v1_13_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityCodPet;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;


/**
 * @author Arnah
 * @since Aug 2, 2018
 */
@EntitySize(width = 0.5F, height = 0.3F)
@EntityPetType(petType = PetType.COD)
public class EntityCodPet extends EntityFishPet implements IEntityCodPet{
	
	public EntityCodPet(World world){
		super(EntityTypes.COD, world);
	}
	
	public EntityCodPet(World world, IPet pet){
		super(EntityTypes.COD, world, pet);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
}
