package com.dsh105.echopet.compat.nms.v1_13_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityIllusionerPet;

import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since May 23, 2017
 */
@EntitySize(width = 0.6F, height = 1.95F)
@EntityPetType(petType = PetType.ILLUSIONER)
public class EntityIllusionerPet extends EntityEvokerPet implements IEntityIllusionerPet{

	public EntityIllusionerPet(World world){
		super(EntityTypes.ILLUSIONER, world);
	}

	public EntityIllusionerPet(World world, IPet pet){
		super(EntityTypes.ILLUSIONER, world, pet);
	}
}
