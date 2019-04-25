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
package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySpiderPet;
import com.dsh105.echopet.compat.nms.v1_14_R1.entity.EntityPet;

import net.minecraft.server.v1_14_R1.*;

@EntitySize(width = 1.4F, height = 0.9F)
@EntityPetType(petType = PetType.SPIDER)
public class EntitySpiderPet extends EntityPet implements IEntitySpiderPet{

	private static final DataWatcherObject<Byte> a = DataWatcher.a(EntitySpiderPet.class, DataWatcherRegistry.a);// Some position changed or shit..

	public EntitySpiderPet(EntityTypes<? extends Entity> type, World world){
		super(type, world);
	}

	public EntitySpiderPet(EntityTypes<? extends Entity> type, World world, IPet pet){
		super(type, world, pet);
	}

	public EntitySpiderPet(World world){
		this(EntityTypes.SPIDER, world);
	}

	public EntitySpiderPet(World world, IPet pet){
		this(EntityTypes.SPIDER, world, pet);
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(a, Byte.valueOf((byte) 0));
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
