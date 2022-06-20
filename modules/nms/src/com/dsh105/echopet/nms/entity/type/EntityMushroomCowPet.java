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
import com.dsh105.echopet.compat.api.entity.data.type.MushroomCowType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityMushroomCowPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.entity.MushroomCow;

@EntitySize(width = 0.9F, height = 1.3F)
@EntityPetType(petType = PetType.MUSHROOMCOW)
public class EntityMushroomCowPet extends EntityCowPet implements IEntityMushroomCowPet{
	
	private static final EntityDataAccessor<String> DATA_TYPE = SynchedEntityData.defineId(EntityMushroomCowPet.class, EntityDataSerializers.STRING);
	
	public EntityMushroomCowPet(Level world){
		super(EntityType.MOOSHROOM, world);
	}
	
	public EntityMushroomCowPet(Level world, IPet pet){
		super(EntityType.MOOSHROOM, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_TYPE, MushroomCow.Variant.RED.name().toLowerCase());// Mojang grabs the string variable but we can't
	}
	
	@Override
	public void setType(MushroomCowType type){
		entityData.set(DATA_TYPE, type.name().toLowerCase());
	}
}