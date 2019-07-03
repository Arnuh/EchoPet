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
package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import java.util.Optional;
import java.util.UUID;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityFoxPet;
import com.dsh105.echopet.compat.nms.v1_14_R1.entity.EntityAgeablePet;

import net.minecraft.server.v1_14_R1.DataWatcher;
import net.minecraft.server.v1_14_R1.DataWatcherObject;
import net.minecraft.server.v1_14_R1.DataWatcherRegistry;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;

@EntitySize(width = 0.6F, height = 0.7F)
@EntityPetType(petType = PetType.FOX)
public class EntityFoxPet extends EntityAgeablePet implements IEntityFoxPet{

	private static final DataWatcherObject<Integer> Type = DataWatcher.a(EntityFoxPet.class, DataWatcherRegistry.b);
	private static final DataWatcherObject<Byte> StanceFlag = DataWatcher.a(EntityFoxPet.class, DataWatcherRegistry.a);
	private static final DataWatcherObject<Optional<UUID>> bB = DataWatcher.a(EntityFoxPet.class, DataWatcherRegistry.o);
	private static final DataWatcherObject<Optional<UUID>> bD = DataWatcher.a(EntityFoxPet.class, DataWatcherRegistry.o);
	// Crouch is when they are starting to attack, shaking ass
	// Pounce tilts his head down
	// Pounce2 tilts his head + shakes legs or just shakes legs?
	// Kind of weird because sometimes neither will tilt his head down until you respawn him which might be due to StanceFlag going < 0
	private static final int Sitting = 0x1, Crouching = 0x4, HeadTilt = 0x8, Pounce = 0x10, Sleeping = 0x20, Pounce2 = 0x40, Unknown = 0x80;

	public EntityFoxPet(World world){
		super(EntityTypes.FOX, world);
	}

	public EntityFoxPet(World world, IPet pet){
		super(EntityTypes.FOX, world, pet);
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(bB, Optional.empty());
		this.datawatcher.register(bD, Optional.empty());
		this.datawatcher.register(Type, 0);
		this.datawatcher.register(StanceFlag, (byte) 0);
	}

	@Override
	public void setType(int type){
		this.datawatcher.set(Type, type);
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

	public void addFlag(int flag){
		datawatcher.set(StanceFlag, (byte) (getFlag() | flag));
	}

	public void removeFlag(int flag){
		if(hasFlag(flag)){
			datawatcher.set(StanceFlag, (byte) (getFlag() - flag));
		}
	}

	public boolean hasFlag(int flag){
		return (getFlag() & flag) != 0;
	}

	public int getFlag(){
		return datawatcher.get(StanceFlag);
	}
}