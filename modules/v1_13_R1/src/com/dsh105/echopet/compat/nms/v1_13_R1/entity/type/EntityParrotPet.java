package com.dsh105.echopet.compat.nms.v1_13_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityParrotPet;
import com.dsh105.echopet.compat.nms.v1_13_R1.entity.EntityTameablePet;

import net.minecraft.server.v1_13_R1.*;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since May 23, 2017
 */
@EntitySize(width = 0.5F, height = 0.9F)
@EntityPetType(petType = PetType.PARROT)
public class EntityParrotPet extends EntityTameablePet implements IEntityParrotPet{

	private static final DataWatcherObject<Integer> VARIANT = DataWatcher.a(EntityParrotPet.class, DataWatcherRegistry.b);

	public EntityParrotPet(World world){
		super(EntityTypes.PARROT, world);
	}

	public EntityParrotPet(World world, IPet pet){
		super(EntityTypes.PARROT, world, pet);
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(VARIANT, 0);
	}

	public ParrotVariant getVariant(){
		return ParrotVariant.values()[MathHelper.clamp(this.datawatcher.get(VARIANT), 0, 4)];
	}

	public void setVariant(ParrotVariant variant){
		this.datawatcher.set(VARIANT, variant.ordinal());
	}
}
