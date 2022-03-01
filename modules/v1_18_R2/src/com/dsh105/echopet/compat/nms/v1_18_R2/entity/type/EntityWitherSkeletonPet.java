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

package com.dsh105.echopet.compat.nms.v1_18_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWitherSkeletonPet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.7F, height = 2.4F)
@EntityPetType(petType = PetType.WITHERSKELETON)
public class EntityWitherSkeletonPet extends EntitySkeletonAbstractPet implements IEntityWitherSkeletonPet{
	
	public EntityWitherSkeletonPet(Level world){
		super(EntityType.WITHER_SKELETON, world);
	}
	
	public EntityWitherSkeletonPet(Level world, final IPet pet){
		super(EntityType.WITHER_SKELETON, world, pet);
	}
}