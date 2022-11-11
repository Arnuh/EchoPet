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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWitherPet;
import com.dsh105.echopet.nms.entity.EntityPet;
import com.dsh105.echopet.nms.entity.ai.BiMoveControl;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;

@EntityPetType(petType = PetType.WITHER)
public class EntityWitherPet extends EntityPet implements IEntityWitherPet{
	
	// a,b,c are in an array.. and are used for shit.. too lazy to figure out
	private static final EntityDataAccessor<Integer> a = SynchedEntityData.defineId(EntityWitherPet.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> b = SynchedEntityData.defineId(EntityWitherPet.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> c = SynchedEntityData.defineId(EntityWitherPet.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> SHIELDED = SynchedEntityData.defineId(EntityWitherPet.class, EntityDataSerializers.INT);
	
	public EntityWitherPet(Level world, IPet pet){
		super(EntityType.WITHER, world, pet);
		this.moveControl = new BiMoveControl(this, new FlyingMoveControl(this, 10, true), new MoveControl(this), Entity::isVehicle);
		this.navigation = createNavigation(world);
	}
	
	@Override
	protected PathNavigation createNavigation(Level level){
		FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
		nav.setCanOpenDoors(false);
		nav.setCanFloat(true);
		nav.setCanPassDoors(true);
		return nav;
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(a, Integer.valueOf(0));
		this.entityData.define(b, Integer.valueOf(0));
		this.entityData.define(c, Integer.valueOf(0));
		this.entityData.define(SHIELDED, Integer.valueOf(0));
	}
	
	@Override
	public void setShielded(boolean flag){
		this.entityData.set(SHIELDED, Integer.valueOf(flag ? 1 : 0));
		this.setHealth((float) (flag ? 150 : 300));
	}
}
