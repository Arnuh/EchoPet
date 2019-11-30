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

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.ParrotVariant;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityParrotPet;
import com.dsh105.echopet.compat.nms.v1_13_R2.entity.EntityTameablePet;
import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.DataWatcherRegistry;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.World;

/**
 * @since May 23, 2017
 */
@EntitySize(width = 0.5F, height = 0.9F)
@EntityPetType(petType = PetType.PARROT)
public class EntityParrotPet extends EntityTameablePet implements IEntityParrotPet{
	
	private static final DataWatcherObject<Integer> VARIANT = DataWatcher.a(EntityParrotPet.class, DataWatcherRegistry.b);
	
	public EntityParrotPet(World world){
		super(EntityTypes.PARROT, world);
	}
	
	public EntityParrotPet(World world, IPet pet){
		super(EntityTypes.PARROT, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(VARIANT, 0);
	}
	
	public ParrotVariant getVariant(){
		return ParrotVariant.values()[MathHelper.clamp(this.datawatcher.get(VARIANT), 0, 4)];
	}
	
	public void setVariant(ParrotVariant variant){
		this.datawatcher.set(VARIANT, variant.ordinal());
	}
}
