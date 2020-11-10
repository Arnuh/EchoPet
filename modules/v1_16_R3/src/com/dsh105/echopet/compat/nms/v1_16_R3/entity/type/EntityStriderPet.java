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

package com.dsh105.echopet.compat.nms.v1_16_R3.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityStriderPet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.EntityAgeablePet;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

@EntitySize(width = 0.9F, height = 1.7F)
@EntityPetType(petType = PetType.STRIDER)
public class EntityStriderPet extends EntityAgeablePet implements IEntityStriderPet{
	
	private static final DataWatcherObject<Integer> boostTicks = DataWatcher.a(EntityStriderPet.class, DataWatcherRegistry.b);
	private static final DataWatcherObject<Boolean> isShivering = DataWatcher.a(EntityStriderPet.class, DataWatcherRegistry.i);
	private static final DataWatcherObject<Boolean> hasSaddle = DataWatcher.a(EntityStriderPet.class, DataWatcherRegistry.i);
	
	public EntityStriderPet(World world){
		super(EntityTypes.STRIDER, world);
	}
	
	public EntityStriderPet(World world, IPet pet){
		super(EntityTypes.STRIDER, world, pet);
	}
	
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(boostTicks, 0);
		this.datawatcher.register(isShivering, false);
		this.datawatcher.register(hasSaddle, false);
	}
}