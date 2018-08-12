package com.dsh105.echopet.compat.nms.v1_13_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityEvokerPet;

import net.minecraft.server.v1_13_R1.*;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntitySize(width = 0.6F, height = 1.95F)
@EntityPetType(petType = PetType.EVOKER)
public class EntityEvokerPet extends EntityIllagerAbstractPet implements IEntityEvokerPet{

	// EntityIllagerWizard
	private static final DataWatcherObject<Byte> c = DataWatcher.a(EntityEvokerPet.class, DataWatcherRegistry.a);// some sorta spell shit

	public EntityEvokerPet(EntityTypes<? extends Entity> type, World world){
		super(type, world);
	}

	public EntityEvokerPet(EntityTypes<? extends Entity> type, World world, IPet pet){
		super(type, world, pet);
	}

	public EntityEvokerPet(World world){
		this(EntityTypes.EVOKER, world);
	}

	public EntityEvokerPet(World world, IPet pet){
		this(EntityTypes.EVOKER, world, pet);
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
