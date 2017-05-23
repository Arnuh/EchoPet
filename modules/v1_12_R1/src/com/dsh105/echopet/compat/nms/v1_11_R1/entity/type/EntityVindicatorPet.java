package com.dsh105.echopet.compat.nms.v1_11_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVindicatorPet;
import com.dsh105.echopet.compat.nms.v1_11_R1.entity.EntityPet;

import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntitySize(width = 0.6F, height = 1.95F)
@EntityPetType(petType = PetType.VINDICATOR)
public class EntityVindicatorPet extends EntityPet implements IEntityVindicatorPet{
	
	protected static final DataWatcherObject<Byte> a = DataWatcher.a(EntityVindicatorPet.class, DataWatcherRegistry.a);// prob puts its hands out for when it has an axe.

	public EntityVindicatorPet(World world){
		super(world);
	}

	public EntityVindicatorPet(World world, IPet pet){
		super(world, pet);
	}

	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(a, (byte) 0);
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
