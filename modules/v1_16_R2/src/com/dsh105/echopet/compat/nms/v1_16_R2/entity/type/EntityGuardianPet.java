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

package com.dsh105.echopet.compat.nms.v1_16_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGuardianPet;
import com.dsh105.echopet.compat.nms.v1_16_R2.entity.EntityPet;
import net.minecraft.server.v1_16_R2.DataWatcher;
import net.minecraft.server.v1_16_R2.DataWatcherObject;
import net.minecraft.server.v1_16_R2.DataWatcherRegistry;
import net.minecraft.server.v1_16_R2.EntityInsentient;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;

@EntitySize(width = 0.85F, height = 0.85F)
@EntityPetType(petType = PetType.GUARDIAN)
public class EntityGuardianPet extends EntityPet implements IEntityGuardianPet{
	
	private static final DataWatcherObject<Boolean> bz = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.i);// ?
	private static final DataWatcherObject<Integer> bA = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.b);// some kind of entity id
	
	public EntityGuardianPet(EntityTypes<? extends EntityInsentient> type, World world){
		super(type, world);
	}
	
	public EntityGuardianPet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet){
		super(type, world, pet);
	}
	
	public EntityGuardianPet(World world){
		this(EntityTypes.GUARDIAN, world);
	}
	
	public EntityGuardianPet(World world, IPet pet){
		this(EntityTypes.GUARDIAN, world, pet);
	}
	
	@Override
	protected String getIdleSound(){
		return isInWater() ? "entity.guardian.ambient" : "entity.guardian.ambient_land";
	}
	
	@Override
	protected String getDeathSound(){
		return isInWater() ? "entity.guardian.death" : "entity.guardian.death_land";
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.LARGE;
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(bz, Boolean.valueOf(false));
		this.datawatcher.register(bA, Integer.valueOf(0));
	}
	
	@Override
	public void onLive(){
		super.onLive();
	}
}
