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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWaterAnimalPet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.EntityPet;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

/**
 * @author Arnah
 * @since Aug 2, 2018
 */
public abstract class EntityWaterAnimalPet extends EntityPet implements IEntityWaterAnimalPet{
	
	
	public EntityWaterAnimalPet(EntityTypes<? extends EntityInsentient> type, World world){
		super(type, world);
	}
	
	public EntityWaterAnimalPet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet){
		super(type, world, pet);
	}
	
	protected String getStepSound(){
		return "entity." + pet.getPetType().getMinecraftName() + ".swim";
	}
}
