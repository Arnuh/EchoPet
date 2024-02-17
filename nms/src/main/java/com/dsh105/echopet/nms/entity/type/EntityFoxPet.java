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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityFoxPet;
import com.dsh105.echopet.nms.entity.EntityAgeablePet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

@EntityPetType(petType = PetType.FOX)
public class EntityFoxPet extends EntityAgeablePet implements IEntityFoxPet{
	
	private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Optional<UUID>> DATA_TRUSTED_ID_0 = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Optional<UUID>> DATA_TRUSTED_ID_1 = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.OPTIONAL_UUID);
	// Crouch is when they are starting to attack, shaking ass
	// Pounce tilts his head down
	// Pounce2 tilts his head + shakes legs or just shakes legs?
	// Kind of weird because sometimes neither will tilt his head down until you respawn him which might be due to StanceFlag going < 0
	private static final int Sitting = 0x1, Crouching = 0x4, Interested = 0x8, Pouncing = 0x10, Sleeping = 0x20, FacePlanted = 0x40, Defending = 0x80;
	
	public EntityFoxPet(Level world){
		super(EntityType.FOX, world);
	}
	
	public EntityFoxPet(Level world, IPet pet){
		super(EntityType.FOX, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_TRUSTED_ID_0, Optional.empty());
		this.entityData.define(DATA_TRUSTED_ID_1, Optional.empty());
		this.entityData.define(DATA_TYPE_ID, 0);
		this.entityData.define(DATA_FLAGS_ID, (byte) 0);
	}
	
	@Override
	public void setType(int type){
		this.entityData.set(DATA_TYPE_ID, type);
	}
	
	@Override
	public void setSitting(boolean sitting){
		if(sitting) addFlag(Sitting);
		else removeFlag(Sitting);
	}
	
	@Override
	public void setCrouching(boolean crouching){
		if(crouching) addFlag(Crouching);
		else removeFlag(Crouching);
	}
	
	@Override
	public void setHeadTilt(boolean tilt){
		if(tilt) addFlag(Interested);
		else removeFlag(Interested);
	}
	
	@Override
	public void setPounce(boolean pounce){
		if(pounce) addFlag(Pouncing);
		else removeFlag(Pouncing);
	}
	
	@Override
	public void setSleeping(boolean sleeping){
		if(sleeping) addFlag(Sleeping);
		else removeFlag(Sleeping);
	}
	
	@Override
	public void setLegShake(boolean shake){
		if(shake) addFlag(FacePlanted);
		else removeFlag(FacePlanted);
	}
	
	private void addFlag(int flag){
		entityData.set(DATA_FLAGS_ID, (byte) (getFlag() | flag));
	}
	
	private void removeFlag(int flag){
		entityData.set(DATA_FLAGS_ID, (byte) (getFlag() & ~flag));
	}
	
	public int getFlag(){
		return entityData.get(DATA_FLAGS_ID);
	}
}