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
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityBlazePet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.EntityPet;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

@EntitySize(width = 0.6F, height = 1.7F)
@EntityPetType(petType = PetType.BLAZE)
public class EntityBlazePet extends EntityPet implements IEntityBlazePet{
	
	private static final DataWatcherObject<Byte> ANGERED = DataWatcher.a(EntityBlazePet.class, DataWatcherRegistry.a);
	
	public EntityBlazePet(World world){
		super(EntityTypes.BLAZE, world);
	}
	
	public EntityBlazePet(World world, IPet pet){
		super(EntityTypes.BLAZE, world, pet);
	}
	
	public void setOnFire(boolean flag){
		byte b1 = this.datawatcher.get(ANGERED).byteValue();
		if(flag){
			b1 = (byte) (b1 | 0x1);
		}else{
			b1 = (byte) (b1 & 0xFFFFFFFE);
		}
		this.datawatcher.set(ANGERED, Byte.valueOf(b1));
	}
	
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(ANGERED, Byte.valueOf((byte) 0));
	}
	
	protected String getIdleSound(){
		return "entity.blaze.ambient";
	}
	
	protected String getDeathSound(){
		return "entity.blaze.death";
	}
	
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
