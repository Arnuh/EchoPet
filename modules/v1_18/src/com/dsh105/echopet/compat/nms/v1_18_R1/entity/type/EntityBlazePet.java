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

package com.dsh105.echopet.compat.nms.v1_18_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityBlazePet;
import com.dsh105.echopet.compat.nms.v1_18_R1.entity.EntityPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.6F, height = 1.7F)
@EntityPetType(petType = PetType.BLAZE)
public class EntityBlazePet extends EntityPet implements IEntityBlazePet{
	
	private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(EntityBlazePet.class, EntityDataSerializers.BYTE);
	
	public EntityBlazePet(Level world){
		super(EntityType.BLAZE, world);
	}
	
	public EntityBlazePet(Level world, IPet pet){
		super(EntityType.BLAZE, world, pet);
	}
	
	@Override
	public void setOnFire(boolean flag){
		byte b1 = this.entityData.get(DATA_FLAGS_ID);
		if(flag){
			b1 = (byte) (b1 | 0x1);
		}else{
			b1 = (byte) (b1 & -0x2);
		}
		this.entityData.set(DATA_FLAGS_ID, b1);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_FLAGS_ID, (byte) 0);
	}
	
	@Override
	protected String getAmbientSoundString(){
		return "entity.blaze.ambient";
	}
	
	@Override
	protected String getDeathSoundString(){
		return "entity.blaze.death";
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
