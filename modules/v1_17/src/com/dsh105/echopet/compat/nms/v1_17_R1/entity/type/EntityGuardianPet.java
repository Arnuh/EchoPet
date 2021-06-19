/*
 * This file is part of EchoPet.
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.nms.v1_17_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGuardianPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.85F, height = 0.85F)
@EntityPetType(petType = PetType.GUARDIAN)
public class EntityGuardianPet extends EntityPet implements IEntityGuardianPet{
	
	// Does some particle stuff
	private static final EntityDataAccessor<Boolean> DATA_ID_MOVING = SynchedEntityData.defineId(EntityGuardianPet.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(EntityGuardianPet.class, EntityDataSerializers.INT);
	
	public EntityGuardianPet(EntityType<? extends Mob> type, Level world){
		super(type, world);
	}
	
	public EntityGuardianPet(EntityType<? extends Mob> type, Level world, IPet pet){
		super(type, world, pet);
	}
	
	public EntityGuardianPet(Level world){
		this(EntityType.GUARDIAN, world);
	}
	
	public EntityGuardianPet(Level world, IPet pet){
		this(EntityType.GUARDIAN, world, pet);
	}
	
	@Override
	protected String getAmbientSoundString(){
		return isInWater() ? "entity.guardian.ambient" : "entity.guardian.ambient_land";
	}
	
	@Override
	protected String getDeathSoundString(){
		return isInWater() ? "entity.guardian.death" : "entity.guardian.death_land";
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.LARGE;
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_ID_MOVING, false);
		this.entityData.define(DATA_ID_ATTACK_TARGET, 0);
	}
	
	@Override
	public void onLive(){
		super.onLive();
	}
}
