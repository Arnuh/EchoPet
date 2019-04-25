package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityElderGuardianPet;

import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 18, 2016
 */
@EntitySize(width = 0.85F * 2.35F, height = 0.85F * 2.35F)
@EntityPetType(petType = PetType.ELDERGUARDIAN)
public class EntityElderGuardianPet extends EntityGuardianPet implements IEntityElderGuardianPet{

	public EntityElderGuardianPet(World world){
		super(EntityTypes.ELDER_GUARDIAN, world);
	}

	public EntityElderGuardianPet(World world, IPet pet){
		super(EntityTypes.ELDER_GUARDIAN, world, pet);
	}

	@Override
	public boolean isElder(){
		return true;
	}
}
