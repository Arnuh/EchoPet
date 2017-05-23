package com.dsh105.echopet.compat.nms.v1_12_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityEvokerPet;

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
@EntitySize(width = 0.6F, height = 1.95F)
@EntityPetType(petType = PetType.EVOKER)
public class EntityEvokerPet extends EntityIllagerAbstractPet implements IEntityEvokerPet{

	private static final DataWatcherObject<Byte> c = DataWatcher.a(EntityEvokerPet.class, DataWatcherRegistry.a);// some sorta spell shit

	public EntityEvokerPet(World world){
		super(world);
	}

	public EntityEvokerPet(World world, IPet pet){
		super(world, pet);
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(c, (byte) 0);
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
