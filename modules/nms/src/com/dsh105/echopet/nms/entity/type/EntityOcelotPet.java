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
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityOcelotPet;
import com.dsh105.echopet.nms.entity.EntityAgeablePet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.6F, height = 0.8F)
@EntityPetType(petType = PetType.OCELOT)
public class EntityOcelotPet extends EntityAgeablePet implements IEntityOcelotPet{
	
	private static final EntityDataAccessor<Boolean> DATA_TRUSTING = SynchedEntityData.defineId(EntityOcelotPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityOcelotPet(Level world){
		super(EntityType.OCELOT, world);
	}
	
	public EntityOcelotPet(Level world, IPet pet){
		super(EntityType.OCELOT, world, pet);
	}
	
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_TRUSTING, false);
	}
	
	@Override
	protected String getAmbientSoundString(){
		return (random().nextInt(4) == 0 ? "entity.cat.purreow" : null);
	}
	
	@Override
	protected String getDeathSoundString(){
		return "entity.cat.death";
	}
	
}