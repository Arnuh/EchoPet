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
package com.dsh105.echopet.compat.nms.v1_12_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySnowmanPet;
import com.dsh105.echopet.compat.nms.v1_12_R1.entity.EntityPet;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;

@EntitySize(width = 0.4F, height = 1.8F)
@EntityPetType(petType = PetType.SNOWMAN)
public class EntitySnowmanPet extends EntityPet implements IEntitySnowmanPet{

	private static final DataWatcherObject<Byte> SHEARED = DataWatcher.a(EntitySnowmanPet.class, DataWatcherRegistry.a);// SHEARED(Removes pumpkin)

	public EntitySnowmanPet(World world){
		super(world);
	}

	public EntitySnowmanPet(World world, IPet pet){
		super(world, pet);
	}

	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(SHEARED, (byte) 16);
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}

	public void setSheared(boolean flag){
		byte b0 = ((Byte) this.datawatcher.get(SHEARED)).byteValue();
		if(!flag){
			this.datawatcher.set(SHEARED, Byte.valueOf((byte) (b0 | 0x10)));
		}else{
			this.datawatcher.set(SHEARED, Byte.valueOf((byte) (b0 & 0xFFFFFFEF)));
		}
	}
}
