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

package com.dsh105.echopet.compat.nms.v1_18_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityZoglinPet;
import com.dsh105.echopet.compat.nms.v1_18_R1.entity.EntityAgeablePet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 1.3965F, height = 1.4F)
@EntityPetType(petType = PetType.ZOGLIN)
public class EntityZoglinPet extends EntityAgeablePet implements IEntityZoglinPet{
	
	// Not actually an ageable but its same datawatcher.
	
	public EntityZoglinPet(Level world){
		super(EntityType.ZOGLIN, world);
	}
	
	public EntityZoglinPet(Level world, IPet pet){
		super(EntityType.ZOGLIN, world, pet);
	}
}
