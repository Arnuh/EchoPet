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

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityIllagerAbstractPet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.EntityPet;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

/**
 * @since May 23, 2017
 */
public class EntityIllagerAbstractPet extends EntityPet implements IEntityIllagerAbstractPet{
	
	protected static final DataWatcherObject<Byte> a = DataWatcher.a(EntityIllagerAbstractPet.class, DataWatcherRegistry.a);
	
	public EntityIllagerAbstractPet(EntityTypes<? extends EntityInsentient> type, World world){
		super(type, world);
	}
	
	public EntityIllagerAbstractPet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet){
		super(type, world, pet);
	}
	
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(a, (byte) 0);
	}
	
	protected void a(int paramInt, boolean paramBoolean){
		int i = this.datawatcher.get(a);
		if(paramBoolean){
			i |= paramInt;
		}else{
			i &= (paramInt ^ 0xFFFFFFFF);
		}
		this.datawatcher.set(a, (byte) (i & 0xFF));
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
