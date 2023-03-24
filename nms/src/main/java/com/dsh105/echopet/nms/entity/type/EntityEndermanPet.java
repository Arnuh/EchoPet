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

import java.util.Optional;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityEndermanPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IEndermanPet;
import com.dsh105.echopet.nms.VersionBreaking;
import com.dsh105.echopet.nms.entity.EntityPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@EntityPetType(petType = PetType.ENDERMAN)
public class EntityEndermanPet extends EntityPet implements IEntityEndermanPet{
	
	private static final EntityDataAccessor<Optional<BlockState>> DATA_CARRY_STATE = SynchedEntityData.defineId(EntityEndermanPet.class, VersionBreaking.OPTIONAL_BLOCK_STATE);
	// Changes ambient sound to scream
	private static final EntityDataAccessor<Boolean> DATA_CREEPY = SynchedEntityData.defineId(EntityEndermanPet.class, EntityDataSerializers.BOOLEAN);
	// Plays an initial sound when set as true
	// SoundEvents.ENDERMAN_STARE
	private static final EntityDataAccessor<Boolean> DATA_STARED_AT = SynchedEntityData.defineId(EntityEndermanPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityEndermanPet(Level world){
		super(EntityType.ENDERMAN, world);
	}
	
	public EntityEndermanPet(Level world, IPet pet){
		super(EntityType.ENDERMAN, world, pet);
	}
	
	@Override
	public void setScreaming(boolean flag){
		this.entityData.set(DATA_CREEPY, flag);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_CARRY_STATE, Optional.empty());
		this.entityData.define(DATA_CREEPY, false);
		this.entityData.define(DATA_STARED_AT, false);
	}
	
	public boolean isScreaming(){
		return this.entityData.get(DATA_CREEPY);
	}
	
	public void setCarried(BlockState iblockdata){
		this.entityData.set(DATA_CARRY_STATE, Optional.ofNullable(iblockdata));
	}
	
	public BlockState getCarried(){
		return this.entityData.get(DATA_CARRY_STATE).orElse(null);
	}
	
	@Override
	protected String getAmbientSoundString(){
		return ((IEndermanPet) pet).isScreaming() ? "entity.endermen.scream" : "entity.endermen.ambient";
	}
}
