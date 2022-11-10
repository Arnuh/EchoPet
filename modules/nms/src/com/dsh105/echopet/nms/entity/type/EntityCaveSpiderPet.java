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

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityCaveSpiderPet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntityPetType(petType = PetType.CAVESPIDER)
public class EntityCaveSpiderPet extends EntitySpiderPet implements IEntityCaveSpiderPet{
	
	public EntityCaveSpiderPet(Level world){
		super(EntityType.CAVE_SPIDER, world);
	}
	
	public EntityCaveSpiderPet(Level world, IPet pet){
		super(EntityType.CAVE_SPIDER, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
	}
}
