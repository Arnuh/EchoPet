package com.dsh105.echopet.compat.nms.v1_12_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorseChestedAbstractPet;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
public abstract class EntityHorseChestedAbstractPet extends EntityHorseAbstractPet implements IEntityHorseChestedAbstractPet{

	// EntityHorseChestedAbstract: Donkey, Mule
	private static final DataWatcherObject<Boolean> CHEST = DataWatcher.a(EntityHorseChestedAbstractPet.class, DataWatcherRegistry.h);

	public EntityHorseChestedAbstractPet(World world){
		super(world);
	}

	public EntityHorseChestedAbstractPet(World world, IPet pet){
		super(world, pet);
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(CHEST, false);
	}

	@Override
	public void setChested(boolean flag){
		datawatcher.set(CHEST, flag);
	}
}
