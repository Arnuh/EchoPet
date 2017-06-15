package com.dsh105.echopet.compat.nms.v1_12_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGuardianPet;
import com.dsh105.echopet.compat.nms.v1_12_R1.entity.EntityPet;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;

@EntitySize(width = 0.85F, height = 0.85F)
@EntityPetType(petType = PetType.GUARDIAN)
public class EntityGuardianPet extends EntityPet implements IEntityGuardianPet{

	private static final DataWatcherObject<Boolean> bz = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.h);// ?
	private static final DataWatcherObject<Integer> bA = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.b);// some kind of entity id

	public EntityGuardianPet(World world){
		super(world);
	}

	public EntityGuardianPet(World world, IPet pet){
		super(world, pet);
	}

	@Override
	protected String getIdleSound(){
		if(isElder()) return isInWater() ? "entity.elder_guardian.ambient" : "entity.elder_guardian.ambient_land";
		return isInWater() ? "entity.guardian.ambient" : "entity.guardian.ambient_land";
	}

	@Override
	protected String getDeathSound(){
		if(isElder()) return isInWater() ? "entity.elder_guardian.death" : "entity.elder_guardian.death_land";
		return isInWater() ? "entity.guardian.death" : "entity.guardian.death_land";
	}

	@Override
	public SizeCategory getSizeCategory(){
		return isElder() ? SizeCategory.GIANT : SizeCategory.LARGE;
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

	@Override
	public boolean isElder(){
		return false;
	}

	@Override
	public void setElder(boolean flag){}
}
