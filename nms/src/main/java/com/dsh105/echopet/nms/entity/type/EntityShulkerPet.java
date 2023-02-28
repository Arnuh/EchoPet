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
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityShulkerPet;
import com.dsh105.echopet.nms.entity.EntityPet;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.DyeColor;

@EntityPetType(petType = PetType.SHULKER)
public class EntityShulkerPet extends EntityPet implements IEntityShulkerPet{
	
	protected static final EntityDataAccessor<Direction> DATA_ATTACH_FACE_ID = SynchedEntityData.defineId(EntityShulkerPet.class, EntityDataSerializers.DIRECTION);
	protected static final EntityDataAccessor<Byte> DATA_PEEK_ID = SynchedEntityData.defineId(EntityShulkerPet.class, EntityDataSerializers.BYTE);// how many ticks its opened for
	protected static final EntityDataAccessor<Byte> DATA_COLOR_ID = SynchedEntityData.defineId(EntityShulkerPet.class, EntityDataSerializers.BYTE);
	
	public EntityShulkerPet(Level world, IPet pet){
		super(EntityType.SHULKER, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_ATTACH_FACE_ID, Direction.DOWN);
		this.entityData.define(DATA_PEEK_ID, (byte) 0);
		this.entityData.define(DATA_COLOR_ID, (byte) 16); // No Color
	}
	
	@Override
	public void setPeek(byte peek){
		entityData.set(DATA_PEEK_ID, peek); // Default values are 0, 30, 100
	}
	
	@Override
	public void setColor(DyeColor color){
		entityData.set(DATA_COLOR_ID, (byte) (color == null ? 16 : net.minecraft.world.item.DyeColor.byId(color.ordinal()).getId()));
	}
}
