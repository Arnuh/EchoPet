package com.dsh105.echopet.compat.nms.v1_13_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityTurtlePet;
import com.dsh105.echopet.compat.nms.v1_13_R1.entity.EntityAgeablePet;

import net.minecraft.server.v1_13_R1.*;

/**
 * @author Arnah
 * @since Aug 11, 2018
*/
@EntitySize(width = 1.2F, height = 0.4F)
@EntityPetType(petType = PetType.TURTLE)
public class EntityTurtlePet extends EntityAgeablePet implements IEntityTurtlePet{

    private static final DataWatcherObject<BlockPosition> bD = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.l);
    private static final DataWatcherObject<Boolean> bE = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Boolean> bG = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<BlockPosition> bH = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.l);
    private static final DataWatcherObject<Boolean> bI = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Boolean> bJ = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.i);
	public EntityTurtlePet(World world){
		super(EntityTypes.TURTLE, world);
	}

	public EntityTurtlePet(World world, IPet pet){
		super(EntityTypes.TURTLE, world, pet);
	}

	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(bD, BlockPosition.ZERO);
		this.datawatcher.register(bE, Boolean.valueOf(false));
		this.datawatcher.register(bH, BlockPosition.ZERO);
		this.datawatcher.register(bI, Boolean.valueOf(false));
		this.datawatcher.register(bJ, Boolean.valueOf(false));
		this.datawatcher.register(bG, Boolean.valueOf(false));
	}
}
