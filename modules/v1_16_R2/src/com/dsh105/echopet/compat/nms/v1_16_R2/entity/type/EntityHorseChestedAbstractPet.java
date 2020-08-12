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

package com.dsh105.echopet.compat.nms.v1_16_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorseChestedAbstractPet;
import net.minecraft.server.v1_16_R2.DataWatcher;
import net.minecraft.server.v1_16_R2.DataWatcherObject;
import net.minecraft.server.v1_16_R2.DataWatcherRegistry;
import net.minecraft.server.v1_16_R2.EntityInsentient;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;

public abstract class EntityHorseChestedAbstractPet extends EntityHorseAbstractPet implements IEntityHorseChestedAbstractPet{
	
	// EntityHorseChestedAbstract: Donkey, Mule
	private static final DataWatcherObject<Boolean> CHEST = DataWatcher.a(EntityHorseChestedAbstractPet.class, DataWatcherRegistry.i);
	
	public EntityHorseChestedAbstractPet(EntityTypes<? extends EntityInsentient> type, World world){
		super(type, world);
	}
	
	public EntityHorseChestedAbstractPet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet){
		super(type, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(CHEST, false);
	}
	
	@Override
	public void setChested(boolean flag){
		datawatcher.set(CHEST, flag);
	}
}
