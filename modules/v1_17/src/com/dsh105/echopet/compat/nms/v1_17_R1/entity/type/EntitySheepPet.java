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

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySheepPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityAgeablePet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.DyeColor;

@EntitySize(width = 0.9F, height = 1.3F)
@EntityPetType(petType = PetType.SHEEP)
public class EntitySheepPet extends EntityAgeablePet implements IEntitySheepPet{
	
	private static final EntityDataAccessor<Byte> DATA_WOOL_ID = SynchedEntityData.defineId(EntitySheepPet.class, EntityDataSerializers.BYTE);// Fuck you mojang for doing two values in one byte
	
	public EntitySheepPet(Level world){
		super(EntityType.SHEEP, world);
	}
	
	public EntitySheepPet(Level world, IPet pet){
		super(EntityType.SHEEP, world, pet);
	}
	
	public int getColor(){
		return this.entityData.get(DATA_WOOL_ID) & 0xF;
	}
	
	@Override
	public void setDyeColor(DyeColor color){
		byte b0 = this.entityData.get(DATA_WOOL_ID);
		this.entityData.set(DATA_WOOL_ID, (byte) (b0 & 0xF0 | color.ordinal() & 0xF));
	}
	
	public boolean isSheared(){
		return (this.entityData.get(DATA_WOOL_ID) & 0x10) != 0;
	}
	
	@Override
	public void setSheared(boolean flag){
		byte b0 = this.entityData.get(DATA_WOOL_ID);
		if(flag){
			this.entityData.set(DATA_WOOL_ID, (byte) (b0 | 0x10));
		}else{
			this.entityData.set(DATA_WOOL_ID, (byte) (b0 & -0x11));
		}
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_WOOL_ID, (byte) 0);
	}
}
