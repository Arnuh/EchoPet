/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dsh105.echopet.compat.nms.v1_10_R1.entity.type;


import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySkeletonPet;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityPet;

import net.minecraft.server.v1_10_R1.DataWatcher;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;
import net.minecraft.server.v1_10_R1.World;

@EntitySize(width = 0.6F, height = 1.9F)
@EntityPetType(petType = PetType.SKELETON)
public class EntitySkeletonPet extends EntityPet implements IEntitySkeletonPet{

	private static final DataWatcherObject<Integer> TYPE = DataWatcher.a(EntitySkeletonPet.class, DataWatcherRegistry.b);// Skeleton Type
	private static final DataWatcherObject<Boolean> b = DataWatcher.a(EntitySkeletonPet.class, DataWatcherRegistry.h);// Something for PathfinderGoalMeleeAttack

	public EntitySkeletonPet(World world){
		super(world);
	}

	public EntitySkeletonPet(World world, final IPet pet){
		super(world, pet);
		// TODO: Fix
		/*new BukkitRunnable() {
		    @Override
		    public void run() {
		        if (((ISkeletonPet) pet).isWither()) {
					setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
		        } else {
					setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.BOW));
		        }
		    }
		}.runTaskLater(EchoPet.getPlugin(), 5L);*/
	}

	@Override
	public void setSkeletonType(SkeletonType type){
		this.datawatcher.set(TYPE, type.ordinal());
		/*if (flag) {
			setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
		} else {
			setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.BOW));
		}*/
	}

	public SkeletonType getSkeletonType(){
		return SkeletonType.values()[this.datawatcher.get(TYPE)];
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(TYPE, Integer.valueOf(0));
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
