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
package com.dsh105.echopet.compat.nms.v1_13_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityIronGolemPet;
import com.dsh105.echopet.compat.nms.v1_13_R2.entity.EntityPet;

import net.minecraft.server.v1_13_R2.*;

@EntitySize(width = 1.4F, height = 2.9F)
@EntityPetType(petType = PetType.IRONGOLEM)
public class EntityIronGolemPet extends EntityPet implements IEntityIronGolemPet{

	protected static final DataWatcherObject<Byte> PLAYER_CREATED = DataWatcher.a(EntityIronGolemPet.class, DataWatcherRegistry.a);

	public EntityIronGolemPet(World world){
		super(EntityTypes.IRON_GOLEM, world);
	}

	public EntityIronGolemPet(World world, IPet pet){
		super(EntityTypes.IRON_GOLEM, world, pet);
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(PLAYER_CREATED, Byte.valueOf((byte) 0));
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.LARGE;
	}
}
