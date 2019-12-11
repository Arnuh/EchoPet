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

package com.dsh105.echopet.compat.nms.v1_15_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityZombieHorsePet;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;


@EntitySize(width = 1.4F, height = 1.6F)
@EntityPetType(petType = PetType.ZOMBIEHORSE)
public class EntityZombieHorsePet extends EntityHorseAbstractPet implements IEntityZombieHorsePet{
	
	public EntityZombieHorsePet(World world){
		super(EntityTypes.ZOMBIE_HORSE, world);
	}
	
	public EntityZombieHorsePet(World world, IPet pet){
		super(EntityTypes.ZOMBIE_HORSE, world, pet);
	}
}
