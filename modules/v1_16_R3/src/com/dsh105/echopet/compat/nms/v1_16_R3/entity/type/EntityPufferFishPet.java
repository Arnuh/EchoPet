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

package com.dsh105.echopet.compat.nms.v1_16_R3.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPufferFishPet;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

@EntitySize(width = 0.7F, height = 0.7F)
@EntityPetType(petType = PetType.PUFFERFISH)
public class EntityPufferFishPet extends EntityFishPet implements IEntityPufferFishPet{
	
	private static final DataWatcherObject<Integer> STATE = DataWatcher.a(EntityPufferFishPet.class, DataWatcherRegistry.b);
	
	public EntityPufferFishPet(World world){
		super(EntityTypes.PUFFERFISH, world);
	}
	
	public EntityPufferFishPet(World world, IPet pet){
		super(EntityTypes.PUFFERFISH, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(STATE, 0);
	}
	
	@Override
	public void setPuffState(int state){
		datawatcher.set(STATE, state);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
}
