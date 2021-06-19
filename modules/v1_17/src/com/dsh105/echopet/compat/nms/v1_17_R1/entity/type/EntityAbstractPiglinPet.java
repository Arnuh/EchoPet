package com.dsh105.echopet.compat.nms.v1_17_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

/**
 * @author Arnah
 * @since Jun 19, 2021
 **/
public abstract class EntityAbstractPiglinPet extends EntityPet{
	
	protected static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(EntityAbstractPiglinPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityAbstractPiglinPet(EntityType<? extends Mob> type, Level world){
		super(type, world);
	}
	
	public EntityAbstractPiglinPet(EntityType<? extends Mob> type, Level world, IPet pet){
		super(type, world, pet);
	}
	
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_IMMUNE_TO_ZOMBIFICATION, true); // Default to true to remove shaking
	}
}
