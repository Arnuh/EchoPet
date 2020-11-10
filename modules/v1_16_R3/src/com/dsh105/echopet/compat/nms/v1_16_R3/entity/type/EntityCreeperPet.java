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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityCreeperPet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.EntityPet;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

@EntitySize(width = 0.6F, height = 1.9F)
@EntityPetType(petType = PetType.CREEPER)
public class EntityCreeperPet extends EntityPet implements IEntityCreeperPet{
	
	private static final DataWatcherObject<Integer> a = DataWatcher.a(EntityCreeperPet.class, DataWatcherRegistry.b);// No clue
	private static final DataWatcherObject<Boolean> POWERED = DataWatcher.a(EntityCreeperPet.class, DataWatcherRegistry.i);
	private static final DataWatcherObject<Boolean> IGNITED = DataWatcher.a(EntityCreeperPet.class, DataWatcherRegistry.i);// What is this?
	
	public EntityCreeperPet(World world){
		super(EntityTypes.CREEPER, world);
	}
	
	public EntityCreeperPet(World world, IPet pet){
		super(EntityTypes.CREEPER, world, pet);
	}
	
	public void setPowered(boolean flag){
		this.datawatcher.set(POWERED, flag);
	}
	
	public void setIgnited(boolean flag){
		this.datawatcher.set(IGNITED, flag);
	}
	
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(a, Integer.valueOf(-1));
		this.datawatcher.register(POWERED, Boolean.valueOf(false));
		this.datawatcher.register(IGNITED, Boolean.valueOf(false));
	}
	
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
