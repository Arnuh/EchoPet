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
package com.dsh105.echopet.compat.nms.v1_13_R2.entity;

import java.util.Optional;
import java.util.UUID;
import com.dsh105.echopet.compat.api.entity.IEntityTameablePet;
import com.dsh105.echopet.compat.api.entity.IPet;
import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.DataWatcherRegistry;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;

/**
 * @since Mar 6, 2016
 */
// These are not actually tameable and only here to add missing datawatchers
public class EntityTameablePet extends EntityAgeablePet implements IEntityTameablePet{
	
	protected static final DataWatcherObject<Byte> bv = DataWatcher.a(EntityTameablePet.class, DataWatcherRegistry.a);
	protected static final DataWatcherObject<Optional<UUID>> OWNER = DataWatcher.a(EntityTameablePet.class, DataWatcherRegistry.o);// Owner
	
	public EntityTameablePet(EntityTypes<? extends Entity> type, World world){
		super(type, world);
	}
	
	public EntityTameablePet(EntityTypes<? extends Entity> type, World world, IPet pet){
		super(type, world, pet);
	}
	
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(bv, (byte) 0);
		this.datawatcher.register(OWNER, Optional.empty());
	}
	
	// These are all useless
	public boolean isTamed(){
		return (this.datawatcher.get(bv) & 0x4) != 0;
	}
	
	public void setTamed(boolean paramBoolean){
		int i = this.datawatcher.get(bv);
		if(paramBoolean){
			this.datawatcher.set(bv, Byte.valueOf((byte) (i | 0x4)));
		}else{
			this.datawatcher.set(bv, Byte.valueOf((byte) (i & 0xFFFFFFFB)));
		}
	}
	
	public boolean isSitting(){
		return (this.datawatcher.get(bv) & 0x1) != 0;
	}
	
	public void setSitting(boolean paramBoolean){
		int i = this.datawatcher.get(bv);
		if(paramBoolean){
			this.datawatcher.set(bv, Byte.valueOf((byte) (i | 0x1)));
		}else{
			this.datawatcher.set(bv, Byte.valueOf((byte) (i & 0xFFFFFFFE)));
		}
	}
	
	public UUID getOwnerUUID(){
		return this.datawatcher.get(OWNER).orElse(null);
	}
	
	public void setOwnerUUID(UUID paramUUID){
		this.datawatcher.set(OWNER, Optional.ofNullable(paramUUID));
	}
}
