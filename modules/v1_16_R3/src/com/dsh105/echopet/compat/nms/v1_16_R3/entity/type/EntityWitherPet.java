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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWitherPet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.EntityPet;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

@EntitySize(width = 0.9F, height = 4.0F)
@EntityPetType(petType = PetType.WITHER)
public class EntityWitherPet extends EntityPet implements IEntityWitherPet{
	
	// a,b,c are in an array.. and are used for shit.. too lazy to figure out
	private static final DataWatcherObject<Integer> a = DataWatcher.a(EntityWitherPet.class, DataWatcherRegistry.b);
	private static final DataWatcherObject<Integer> b = DataWatcher.a(EntityWitherPet.class, DataWatcherRegistry.b);
	private static final DataWatcherObject<Integer> c = DataWatcher.a(EntityWitherPet.class, DataWatcherRegistry.b);
	private static final DataWatcherObject<Integer> SHIELDED = DataWatcher.a(EntityWitherPet.class, DataWatcherRegistry.b);
	
	public EntityWitherPet(World world){
		super(EntityTypes.WITHER, world);
	}
	
	public EntityWitherPet(World world, IPet pet){
		super(EntityTypes.WITHER, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(a, Integer.valueOf(0));
		this.datawatcher.register(b, Integer.valueOf(0));
		this.datawatcher.register(c, Integer.valueOf(0));
		this.datawatcher.register(SHIELDED, Integer.valueOf(0));
	}
	
	public void setShielded(boolean flag){
		this.datawatcher.set(SHIELDED, Integer.valueOf(flag ? 1 : 0));
		this.setHealth((float) (flag ? 150 : 300));
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.LARGE;
	}
}
