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

package com.dsh105.echopet.compat.nms.v1_16_R3.entity;

import java.util.Optional;
import java.util.UUID;
import com.dsh105.echopet.compat.api.entity.IEntityTameablePet;
import com.dsh105.echopet.compat.api.entity.IPet;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

/**
 * @since Mar 6, 2016
 */
public class EntityTameablePet extends EntityAgeablePet implements IEntityTameablePet{
	
	protected static final int Sitting = 0x1, Angry = 0x2, Tamed = 0x4;
	protected static final DataWatcherObject<Byte> Flag = DataWatcher.a(EntityTameablePet.class, DataWatcherRegistry.a);
	protected static final DataWatcherObject<Optional<UUID>> OWNER = DataWatcher.a(EntityTameablePet.class, DataWatcherRegistry.o);
	
	public EntityTameablePet(EntityTypes<? extends EntityInsentient> type, World world){
		super(type, world);
	}
	
	public EntityTameablePet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet){
		super(type, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(Flag, (byte) 0);
		this.datawatcher.register(OWNER, Optional.empty());
	}
	
	public boolean isTamed(){
		return (getFlag() & Tamed) != 0;
	}
	
	@Override
	public void setSitting(boolean sitting){
		if(sitting) addFlag(Sitting);
		else removeFlag(Sitting);
	}
	
	@Override
	public void setTamed(boolean tamed){
		if(tamed) addFlag(Tamed);
		else removeFlag(Tamed);
	}
	
	protected void addFlag(int flag){
		datawatcher.set(Flag, (byte) (getFlag() | flag));
	}
	
	protected void removeFlag(int flag){
		datawatcher.set(Flag, (byte) (getFlag() & ~flag));
	}
	
	protected int getFlag(){
		return datawatcher.get(Flag);
	}
}