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
package com.dsh105.echopet.compat.nms.v1_10_R1.entity.type;

import org.bukkit.DyeColor;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySheepPet;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityAgeablePet;

import net.minecraft.server.v1_10_R1.DataWatcher;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;
import net.minecraft.server.v1_10_R1.World;

@EntitySize(width = 0.9F, height = 1.3F)
@EntityPetType(petType = PetType.SHEEP)
public class EntitySheepPet extends EntityAgeablePet implements IEntitySheepPet{

	private static final DataWatcherObject<Byte> COLOR_SHEARED = DataWatcher.a(EntitySheepPet.class, DataWatcherRegistry.a);// Fuck you mojang for doing two values in one byte

	public EntitySheepPet(World world){
		super(world);
	}

	public EntitySheepPet(World world, IPet pet){
		super(world, pet);
	}

	public int getColor(){
		return ((Byte) this.datawatcher.get(COLOR_SHEARED)).byteValue() & 0xF;
	}

	@Override
	public void setDyeColor(DyeColor color){
		byte b0 = ((Byte) this.datawatcher.get(COLOR_SHEARED)).byteValue();
		this.datawatcher.set(COLOR_SHEARED, Byte.valueOf((byte) (b0 & 0xF0 | color.ordinal() & 0xF)));
	}

	public boolean isSheared(){
		return (((Byte) this.datawatcher.get(COLOR_SHEARED)).byteValue() & 0x10) != 0;
	}

	@Override
	public void setSheared(boolean flag){
		byte b0 = ((Byte) this.datawatcher.get(COLOR_SHEARED)).byteValue();
		if(flag){
			this.datawatcher.set(COLOR_SHEARED, Byte.valueOf((byte) (b0 | 0x10)));
		}else{
			this.datawatcher.set(COLOR_SHEARED, Byte.valueOf((byte) (b0 & 0xFFFFFFEF)));
		}
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(COLOR_SHEARED, Byte.valueOf((byte) 0));
	}
}
