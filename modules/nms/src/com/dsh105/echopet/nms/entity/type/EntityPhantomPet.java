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
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPhantomPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.9F, height = 0.5F)
@EntityPetType(petType = PetType.PHANTOM)
public class EntityPhantomPet extends EntityFlyingPet implements IEntityPhantomPet{
	
	// Changes size and damage
	private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(EntityPhantomPet.class, EntityDataSerializers.INT);
	
	public EntityPhantomPet(Level world){
		super(EntityType.PHANTOM, world);
	}
	
	public EntityPhantomPet(Level world, IPet pet){
		super(EntityType.PHANTOM, world, pet);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(ID_SIZE, 0);
	}
	
	public void setPhantomSize(int i){
		this.entityData.set(ID_SIZE, Mth.clamp(i, 0, 64));
	}
	
	public int getPhantomSize(){
		return this.entityData.get(ID_SIZE);
	}
}
