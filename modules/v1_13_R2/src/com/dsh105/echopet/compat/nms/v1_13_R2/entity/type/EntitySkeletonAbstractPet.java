package com.dsh105.echopet.compat.nms.v1_13_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySkeletonAbstractPet;
import com.dsh105.echopet.compat.nms.v1_13_R2.entity.EntityPet;
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
public class EntitySkeletonAbstractPet extends EntityPet implements IEntitySkeletonAbstractPet{
	
	private static final DataWatcherObject<Boolean> b = DataWatcher.a(EntitySkeletonPet.class, DataWatcherRegistry.i);// Something for PathfinderGoalMeleeAttack
	
	public EntitySkeletonAbstractPet(EntityTypes<? extends Entity> type, World world){
		super(type, world);
	}
	
	public EntitySkeletonAbstractPet(EntityTypes<? extends Entity> type, World world, final IPet pet){
		super(type, world, pet);
	}
	
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(b, Boolean.valueOf(false));
	}
	
	protected String getIdleSound(){
		return "entity.skeleton.ambient";
	}
	
	protected String getHurtSound(){
		return "entity.skeleton.hurt";
	}
	
	protected String getDeathSound(){
		return "entity.skeleton.death";
	}
	
	protected String getStepSound(){
		return "entity.skeleton.step";
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}