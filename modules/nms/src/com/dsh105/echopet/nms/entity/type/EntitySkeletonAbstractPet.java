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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.nms.entity.type;

import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySkeletonAbstractPet;
import com.dsh105.echopet.nms.entity.EntityPet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class EntitySkeletonAbstractPet extends EntityPet implements IEntitySkeletonAbstractPet{
	
	public EntitySkeletonAbstractPet(EntityType<? extends Mob> type, Level world){
		super(type, world);
	}
	
	public EntitySkeletonAbstractPet(EntityType<? extends Mob> type, Level world, final IPet pet){
		super(type, world, pet);
	}
	
	@Override
	protected String getAmbientSoundString(){
		return "entity.skeleton.ambient";
	}
	
	@Override
	protected String getHurtSoundString(){
		return "entity.skeleton.hurt";
	}
	
	@Override
	protected String getDeathSoundString(){
		return "entity.skeleton.death";
	}
	
	protected String getStepSoundString(){
		return "entity.skeleton.step";
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}