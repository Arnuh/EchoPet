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

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySalmonPet;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

/**
 * @author Arnah
 * @since Aug 12, 2018
 */
@EntitySize(width = 0.7F, height = 0.4F)
@EntityPetType(petType = PetType.SALMON)
public class EntitySalmonPet extends EntityFishPet implements IEntitySalmonPet{
	
	public EntitySalmonPet(World world){
		super(EntityTypes.SALMON, world);
	}
	
	public EntitySalmonPet(World world, IPet pet){
		super(EntityTypes.SALMON, world, pet);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
}
