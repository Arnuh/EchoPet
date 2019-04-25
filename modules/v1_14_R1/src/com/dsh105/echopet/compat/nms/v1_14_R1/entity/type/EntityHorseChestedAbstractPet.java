package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorseChestedAbstractPet;

import net.minecraft.server.v1_14_R1.*;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
public abstract class EntityHorseChestedAbstractPet extends EntityHorseAbstractPet implements IEntityHorseChestedAbstractPet{

	// EntityHorseChestedAbstract: Donkey, Mule
	private static final DataWatcherObject<Boolean> CHEST = DataWatcher.a(EntityHorseChestedAbstractPet.class, DataWatcherRegistry.i);

	public EntityHorseChestedAbstractPet(EntityTypes<? extends EntityInsentient> type, World world){
		super(type, world);
	}

	public EntityHorseChestedAbstractPet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet){
		super(type, world, pet);
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
