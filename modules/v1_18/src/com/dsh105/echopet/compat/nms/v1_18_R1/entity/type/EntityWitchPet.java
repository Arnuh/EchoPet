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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWitchPet;
import com.dsh105.echopet.compat.nms.v1_18_R1.entity.EntityPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.6F, height = 1.9F)
@EntityPetType(petType = PetType.WITCH)
public class EntityWitchPet extends EntityPet implements IEntityWitchPet{
	
	private static final EntityDataAccessor<Boolean> DATA_USING_ITEM = SynchedEntityData.defineId(EntityWitchPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityWitchPet(Level world){
		super(EntityType.WITCH, world);
	}
	
	public EntityWitchPet(Level world, IPet pet){
		super(EntityType.WITCH, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		entityData.define(DATA_USING_ITEM, true);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
