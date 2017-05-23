package com.dsh105.echopet.compat.nms.v1_12_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityZombieVillagerPet;

import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 18, 2016
 */
@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.ZOMBIEVILLAGER)
public class EntityZombieVillagerPet extends EntityZombiePet implements IEntityZombieVillagerPet{

	private static final DataWatcherObject<Boolean> CONVERTING = DataWatcher.a(EntityZombieVillagerPet.class, DataWatcherRegistry.h);
	private static final DataWatcherObject<Integer> PROFESSION = DataWatcher.a(EntityZombieVillagerPet.class, DataWatcherRegistry.b);

	public EntityZombieVillagerPet(World world){
		super(world);
	}

	public EntityZombieVillagerPet(World world, IPet pet){
		super(world, pet);
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(CONVERTING, false);
		this.datawatcher.register(PROFESSION, 0);
	}

	public void setVillagerProfession(Profession profession){
		this.datawatcher.set(PROFESSION, profession.ordinal());
	}

}