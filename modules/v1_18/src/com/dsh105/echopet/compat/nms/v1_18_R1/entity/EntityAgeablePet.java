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

package com.dsh105.echopet.compat.nms.v1_18_R1.entity;

import com.dsh105.echopet.compat.api.entity.IEntityAgeablePet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.nms.v1_18_R1.entity.base.EntityAgeablePetBase;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

@Deprecated
public abstract class EntityAgeablePet extends EntityPet implements IEntityAgeablePet{
	
	private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(EntityAgeablePet.class, EntityDataSerializers.BOOLEAN);
	protected int age;
	private boolean ageLocked = true;
	
	public EntityAgeablePet(EntityType<? extends Mob> type, Level world){
		super(type, world);
	}
	
	public EntityAgeablePet(EntityType<? extends Mob> type, Level world, IPet pet){
		super(type, world, pet);
	}
	
	@Override
	public INMSEntityPetBase createPetBase(){
		return new EntityAgeablePetBase(this);
	}
	
	public int getAge(){
		return this.entityData.get(DATA_BABY_ID) ? -1 : this.age;
	}
	
	public void setAge(int i, boolean flag){
		int j = getAge();
		j += i * 20;
		if(j > 0){
			j = 0;
		}
		setAgeRaw(j);
	}
	
	public void setAge(int i){
		setAge(i, false);
	}
	
	public void setAgeRaw(int i){
		this.entityData.set(DATA_BABY_ID, i < 0);
		this.age = i;
	}
	
	public boolean isAgeLocked(){
		return ageLocked;
	}
	
	public void setAgeLocked(boolean ageLocked){
		this.ageLocked = ageLocked;
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_BABY_ID, false);
	}
	
	@Override
	public void inactiveTick(){
		super.inactiveTick();
		if(!(this.level.isClientSide || this.ageLocked)){
			int i = this.getAge();
			if(i < 0){
				++i;
				this.setAge(i);
			}else if(i > 0){
				--i;
				this.setAge(i);
			}
		}
	}
	
	@Override
	public void setBaby(boolean flag){
		this.entityData.set(DATA_BABY_ID, flag);
	}
	
	@Override
	public boolean isBaby(){
		return this.entityData.get(DATA_BABY_ID);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
