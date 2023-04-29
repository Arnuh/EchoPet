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

package com.dsh105.echopet.nms.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPiglinPet;
import com.dsh105.echopet.nms.entity.INMSEntityPetHandle;
import com.dsh105.echopet.nms.entity.handle.EntityAgeablePetHandle;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntityPetType(petType = PetType.PIGLIN)
public class EntityPiglinPet extends EntityAbstractPiglinPet implements IEntityPiglinPet{
	
	private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(EntityPiglinPet.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(EntityPiglinPet.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_IS_DANCING = SynchedEntityData.defineId(EntityPiglinPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityPiglinPet(Level world){
		super(EntityType.PIGLIN, world);
	}
	
	public EntityPiglinPet(Level world, IPet pet){
		super(EntityType.PIGLIN, world, pet);
	}
	
	@Override
	public INMSEntityPetHandle createPetHandle(){
		return new EntityAgeablePetHandle(this);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_BABY_ID, false);
		this.entityData.define(DATA_IS_CHARGING_CROSSBOW, false);
		this.entityData.define(DATA_IS_DANCING, false);
	}
	
	@Override
	public void setBaby(boolean flag){
		getEntityData().set(DATA_BABY_ID, flag);
	}
	
	@Override
	public void setDancing(boolean flag){
		entityData.set(DATA_IS_DANCING, flag);
	}
}