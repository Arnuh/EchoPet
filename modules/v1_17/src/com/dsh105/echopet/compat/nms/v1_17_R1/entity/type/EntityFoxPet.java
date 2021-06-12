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
package com.dsh105.echopet.compat.nms.v1_17_R1.entity.type;

import java.util.Optional;
import java.util.UUID;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityFoxPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityAgeablePet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.6F, height = 0.7F)
@EntityPetType(petType = PetType.FOX)
public class EntityFoxPet extends EntityAgeablePet implements IEntityFoxPet{
	
	private static final EntityDataAccessor<Integer> Type = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Byte> StanceFlag = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Optional<UUID>> bB = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Optional<UUID>> bD = SynchedEntityData.defineId(EntityFoxPet.class, EntityDataSerializers.OPTIONAL_UUID);
	// Crouch is when they are starting to attack, shaking ass
	// Pounce tilts his head down
	// Pounce2 tilts his head + shakes legs or just shakes legs?
	// Kind of weird because sometimes neither will tilt his head down until you respawn him which might be due to StanceFlag going < 0
	private static final int Sitting = 0x1, Crouching = 0x4, HeadTilt = 0x8, Pounce = 0x10, Sleeping = 0x20, Pounce2 = 0x40, Unknown = 0x80;
	
	public EntityFoxPet(Level world){
		super(EntityType.FOX, world);
	}
	
	public EntityFoxPet(Level world, IPet pet){
		super(EntityType.FOX, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(bB, Optional.empty());
		this.entityData.define(bD, Optional.empty());
		this.entityData.define(Type, 0);
		this.entityData.define(StanceFlag, (byte) 0);
	}
	
	@Override
	public void setType(int type){
		this.entityData.set(Type, type);
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
		if(tilt) addFlag(HeadTilt);
		else removeFlag(HeadTilt);
	}
	
	@Override
	public void setPounce(boolean pounce){
		if(pounce) addFlag(Pounce);
		else removeFlag(Pounce);
	}
	
	@Override
	public void setSleeping(boolean sleeping){
		if(sleeping) addFlag(Sleeping);
		else removeFlag(Sleeping);
	}
	
	public void setLegShake(boolean shake){
		if(shake) addFlag(Pounce2);
		else removeFlag(Pounce2);
	}
	
	private void addFlag(int flag){
		entityData.set(StanceFlag, (byte) (getFlag() | flag));
	}
	
	private void removeFlag(int flag){
		entityData.set(StanceFlag, (byte) (getFlag() & ~flag));
	}
	
	public int getFlag(){
		return entityData.get(StanceFlag);
	}
}