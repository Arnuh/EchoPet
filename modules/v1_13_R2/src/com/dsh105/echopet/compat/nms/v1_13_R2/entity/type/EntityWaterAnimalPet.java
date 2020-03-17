package com.dsh105.echopet.compat.nms.v1_13_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWaterAnimalPet;
import com.dsh105.echopet.compat.nms.v1_13_R2.entity.EntityPet;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;

/**
 * @author Arnah
 * @since Aug 2, 2018
 */
public abstract class EntityWaterAnimalPet extends EntityPet implements IEntityWaterAnimalPet{
	
	
	public EntityWaterAnimalPet(EntityTypes<? extends Entity> type, World world){
		super(type, world);
	}
	
	public EntityWaterAnimalPet(EntityTypes<? extends Entity> type, World world, IPet pet){
		super(type, world, pet);
	}
	
	protected String getStepSound(){
		return "entity." + pet.getPetType().getMinecraftName() + ".swim";
	}
}
