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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.nms.v1_16_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPiglinPet;
import com.dsh105.echopet.compat.nms.v1_16_R1.entity.EntityAgeablePet;
import net.minecraft.server.v1_16_R1.DataWatcher;
import net.minecraft.server.v1_16_R1.DataWatcherObject;
import net.minecraft.server.v1_16_R1.DataWatcherRegistry;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.World;

@EntitySize(width = 0.6F, height = 1.95F)
@EntityPetType(petType = PetType.PIGLIN)
public class EntityPiglinPet extends EntityAgeablePet implements IEntityPiglinPet{
	
	//Technically not an animal/ageable but its the first datawatcher so we can take advantage of that.
	private static final DataWatcherObject<Boolean> immuneToZombification = DataWatcher.a(EntityPiglinPet.class, DataWatcherRegistry.i);
	private static final DataWatcherObject<Boolean> crossbowCharged = DataWatcher.a(EntityPiglinPet.class, DataWatcherRegistry.i);
	private static final DataWatcherObject<Boolean> dancing = DataWatcher.a(EntityPiglinPet.class, DataWatcherRegistry.i);
	
	public EntityPiglinPet(World world){
		super(EntityTypes.PIGLIN, world);
	}
	
	public EntityPiglinPet(World world, IPet pet){
		super(EntityTypes.PIGLIN, world, pet);
	}
	
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(crossbowCharged, false);
		this.datawatcher.register(immuneToZombification, true);//Default to true to fix shaking when in overworld.
		this.datawatcher.register(dancing, false);
	}
	
	@Override
	public void setDancing(boolean flag){
		datawatcher.set(dancing, flag);
	}
}