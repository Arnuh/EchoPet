package com.dsh105.echopet.compat.nms.v1_13_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGuardianPet;
import com.dsh105.echopet.compat.nms.v1_13_R2.entity.EntityPet;

import net.minecraft.server.v1_13_R2.*;

@EntitySize(width = 0.85F, height = 0.85F)
@EntityPetType(petType = PetType.GUARDIAN)
public class EntityGuardianPet extends EntityPet implements IEntityGuardianPet{

	private static final DataWatcherObject<Boolean> bz = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.i);// ?
	private static final DataWatcherObject<Integer> bA = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.b);// some kind of entity id

	public EntityGuardianPet(EntityTypes<? extends Entity> type, World world){
		super(type, world);
	}

	public EntityGuardianPet(EntityTypes<? extends Entity> type, World world, IPet pet){
		super(type, world, pet);
	}

	public EntityGuardianPet(World world){
		this(EntityTypes.GUARDIAN, world);
	}

	public EntityGuardianPet(World world, IPet pet){
		this(EntityTypes.GUARDIAN, world, pet);
	}

	@Override
	protected String getIdleSound(){
		return isInWater() ? "entity.guardian.ambient" : "entity.guardian.ambient_land";
	}

	@Override
	protected String getDeathSound(){
		return isInWater() ? "entity.guardian.death" : "entity.guardian.death_land";
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.LARGE;
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(bz, Boolean.valueOf(false));
		this.datawatcher.register(bA, Integer.valueOf(0));
	}

	@Override
	public void onLive(){
		super.onLive();
	}
}
