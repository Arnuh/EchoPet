/*
 * This file is part of EchoPet.
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.nms.v1_16_R3.entity.type;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySkeletonAbstractPet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.EntityPet;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

public class EntitySkeletonAbstractPet extends EntityPet implements IEntitySkeletonAbstractPet{
	
	private static final DataWatcherObject<Boolean> b = DataWatcher.a(EntitySkeletonPet.class, DataWatcherRegistry.i);// Something for PathfinderGoalMeleeAttack
	
	public EntitySkeletonAbstractPet(EntityTypes<? extends EntityInsentient> type, World world){
		super(type, world);
	}
	
	public EntitySkeletonAbstractPet(EntityTypes<? extends EntityInsentient> type, World world, final IPet pet){
		super(type, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(b, Boolean.valueOf(false));
	}
	
	@Override
	protected String getIdleSound(){
		return "entity.skeleton.ambient";
	}
	
	@Override
	protected String getHurtSound(){
		return "entity.skeleton.hurt";
	}
	
	@Override
	protected String getDeathSound(){
		return "entity.skeleton.death";
	}
	
	@Override
	protected String getStepSound(){
		return "entity.skeleton.step";
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}