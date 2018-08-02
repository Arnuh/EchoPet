package com.dsh105.echopet.compat.nms.v1_13_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.SkeletonType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySkeletonAbstractPet;
import com.dsh105.echopet.compat.nms.v1_13_R1.entity.EntityPet;

import net.minecraft.server.v1_13_R1.*;

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
	public void setSkeletonType(SkeletonType type){}

	public SkeletonType getSkeletonType(){
		return null;
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(b, Boolean.valueOf(false));
	}

	protected String getIdleSound(){
		switch (getSkeletonType()){
			case WITHER:
				return "entity.wither_skeleton.ambient";
			case STRAY:
				return "entity.stray.ambient";
			default:
				return "entity.skeleton.ambient";
		}
	}

	protected String getHurtSound(){
		switch (getSkeletonType()){
			case WITHER:
				return "entity.wither_skeleton.hurt";
			case STRAY:
				return "entity.stray.hurt";
			default:
				return "entity.skeleton.hurt";
		}
	}

	protected String getDeathSound(){
		switch (getSkeletonType()){
			case WITHER:
				return "entity.wither_skeleton.death";
			case STRAY:
				return "entity.stray.death";
			default:
				return "entity.skeleton.death";
		}
	}

	protected String getStepSound(){
		switch (getSkeletonType()){
			case WITHER:
				return "entity.wither_skeleton.step";
			case STRAY:
				return "entity.stray.step";
			default:
				return "entity.skeleton.step";
		}
	}

	@Override
	public SizeCategory getSizeCategory(){
		if(this.getSkeletonType() == SkeletonType.WITHER){
			return SizeCategory.LARGE;
		}else{
			return SizeCategory.REGULAR;
		}
	}
}