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
package com.dsh105.echopet.compat.nms.v1_9_R2.entity;

import java.util.UUID;

import com.dsh105.echopet.compat.api.entity.IEntityTameablePet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.google.common.base.Optional;

import net.minecraft.server.v1_9_R2.DataWatcher;
import net.minecraft.server.v1_9_R2.DataWatcherObject;
import net.minecraft.server.v1_9_R2.DataWatcherRegistry;
import net.minecraft.server.v1_9_R2.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Mar 6, 2016
 */
// These are not actually tameable and only here to add missing datawatchers
public class EntityTameablePet extends EntityAgeablePet implements IEntityTameablePet{

	protected static final DataWatcherObject<Byte> bv = DataWatcher.a(EntityTameablePet.class, DataWatcherRegistry.a);
	protected static final DataWatcherObject<Optional<UUID>> bw = DataWatcher.a(EntityTameablePet.class, DataWatcherRegistry.m);// Owner

	public EntityTameablePet(World world){
		super(world);
	}

	public EntityTameablePet(World world, IPet pet){
		super(world, pet);
	}

	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(bv, Byte.valueOf((byte) 0));
		this.datawatcher.register(bw, Optional.absent());
	}

	// These are all useless
	public boolean isTamed(){
		return (((Byte) this.datawatcher.get(bv)).byteValue() & 0x4) != 0;
	}

	public void setTamed(boolean paramBoolean){
		int i = ((Byte) this.datawatcher.get(bv)).byteValue();
		if(paramBoolean){
			this.datawatcher.set(bv, Byte.valueOf((byte) (i | 0x4)));
		}else{
			this.datawatcher.set(bv, Byte.valueOf((byte) (i & 0xFFFFFFFB)));
		}
	}

	public boolean isSitting(){
		return (((Byte) this.datawatcher.get(bv)).byteValue() & 0x1) != 0;
	}

	public void setSitting(boolean paramBoolean){
		int i = ((Byte) this.datawatcher.get(bv)).byteValue();
		if(paramBoolean){
			this.datawatcher.set(bv, Byte.valueOf((byte) (i | 0x1)));
		}else{
			this.datawatcher.set(bv, Byte.valueOf((byte) (i & 0xFFFFFFFE)));
		}
	}

	public UUID getOwnerUUID(){
		return (UUID) ((Optional<UUID>) this.datawatcher.get(bw)).orNull();
	}

	public void setOwnerUUID(UUID paramUUID){
		this.datawatcher.set(bw, Optional.fromNullable(paramUUID));
	}
}
