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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityCreeperPet;
import com.dsh105.echopet.nms.entity.EntityPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.6F, height = 1.9F)
@EntityPetType(petType = PetType.CREEPER)
public class EntityCreeperPet extends EntityPet implements IEntityCreeperPet{
	
	// Igniting using flint & steel triggers explosion, 2 useless datawatchers for pets.
	private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(EntityCreeperPet.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(EntityCreeperPet.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(EntityCreeperPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityCreeperPet(Level world){
		super(EntityType.CREEPER, world);
	}
	
	public EntityCreeperPet(Level world, IPet pet){
		super(EntityType.CREEPER, world, pet);
	}
	
	@Override
	public void setPowered(boolean flag){
		this.entityData.set(DATA_IS_POWERED, flag);
	}
	
	@Override
	public void setIgnited(boolean flag){
		this.entityData.set(DATA_IS_IGNITED, flag);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_SWELL_DIR, -1);
		this.entityData.define(DATA_IS_POWERED, false);
		this.entityData.define(DATA_IS_IGNITED, false);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
