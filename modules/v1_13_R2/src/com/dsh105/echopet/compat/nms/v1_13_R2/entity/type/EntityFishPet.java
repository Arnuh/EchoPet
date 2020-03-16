package com.dsh105.echopet.compat.nms.v1_13_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityFishPet;
import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.DataWatcherRegistry;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;

/**
 * @author Arnah
 * @since Aug 2, 2018
 */
public abstract class EntityFishPet extends EntityWaterAnimalPet implements IEntityFishPet{
	
	private static final DataWatcherObject<Boolean> b = DataWatcher.a(EntityFishPet.class, DataWatcherRegistry.i);// "FromBucket". Prevents the fish from despawning.
	
	public EntityFishPet(EntityTypes<? extends Entity> type, World world){
		super(type, world);
	}
	
	public EntityFishPet(EntityTypes<? extends Entity> type, World world, IPet pet){
		super(type, world, pet);
	}
	
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(b, Boolean.valueOf(false));
	}
	
	protected String getStepSound(){
		return "entity." + pet.getPetType().getMinecraftName() + ".flop";
	}
}
