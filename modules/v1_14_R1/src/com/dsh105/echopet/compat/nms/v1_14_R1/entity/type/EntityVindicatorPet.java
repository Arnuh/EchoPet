package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVindicatorPet;

import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntitySize(width = 0.6F, height = 1.95F)
@EntityPetType(petType = PetType.VINDICATOR)
public class EntityVindicatorPet extends EntityIllagerAbstractPet implements IEntityVindicatorPet{

	public EntityVindicatorPet(World world){
		super(EntityTypes.VINDICATOR, world);
	}

	public EntityVindicatorPet(World world, IPet pet){
		super(EntityTypes.VINDICATOR, world, pet);
	}
}
