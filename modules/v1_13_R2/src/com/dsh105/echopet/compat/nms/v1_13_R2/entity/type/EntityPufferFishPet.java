package com.dsh105.echopet.compat.nms.v1_13_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPufferFishPet;

import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.DataWatcherRegistry;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;


@EntitySize(width = 0.7F, height = 0.7F)
@EntityPetType(petType = PetType.PUFFERFISH)
public class EntityPufferFishPet extends EntityFishPet implements IEntityPufferFishPet{

	private static final DataWatcherObject<Integer> STATE = DataWatcher.a(EntityPufferFishPet.class, DataWatcherRegistry.b);

	public EntityPufferFishPet(World world){
		super(EntityTypes.PUFFERFISH, world);
	}

	public EntityPufferFishPet(World world, IPet pet){
		super(EntityTypes.PUFFERFISH, world, pet);
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(STATE, 0);
	}

	@Override
	public void setPuffState(int state){
		datawatcher.set(STATE, state);
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
}
