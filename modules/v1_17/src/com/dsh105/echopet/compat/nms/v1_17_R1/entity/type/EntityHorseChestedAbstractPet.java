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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.nms.v1_17_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorseChestedAbstractPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public abstract class EntityHorseChestedAbstractPet extends EntityHorseAbstractPet implements IEntityHorseChestedAbstractPet{
	
	// EntityHorseChestedAbstract: Donkey, Mule
	private static final EntityDataAccessor<Boolean> DATA_ID_CHEST = SynchedEntityData.defineId(EntityHorseChestedAbstractPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityHorseChestedAbstractPet(EntityType<? extends Mob> type, Level world){
		super(type, world);
	}
	
	public EntityHorseChestedAbstractPet(EntityType<? extends Mob> type, Level world, IPet pet){
		super(type, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_ID_CHEST, false);
	}
	
	@Override
	public void setChested(boolean flag){
		entityData.set(DATA_ID_CHEST, flag);
	}
}
