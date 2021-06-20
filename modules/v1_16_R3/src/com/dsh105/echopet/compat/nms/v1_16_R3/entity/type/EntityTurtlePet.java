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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityTurtlePet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.EntityAgeablePet;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

@EntitySize(width = 1.2F, height = 0.4F)
@EntityPetType(petType = PetType.TURTLE)
public class EntityTurtlePet extends EntityAgeablePet implements IEntityTurtlePet{
	
	private static final DataWatcherObject<BlockPosition> bD = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.l);// "HomePos" - beach they spawned at
	private static final DataWatcherObject<Boolean> bE = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.i);// "HasEgg" If the turtle is carrying an egg.
	private static final DataWatcherObject<Boolean> bG = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.i);// Set to false when egg is placed("HasEgg" to false right after)
	private static final DataWatcherObject<BlockPosition> bH = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.l);// "TravelPos"
	private static final DataWatcherObject<Boolean> bI = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.i);// set in c() and d() of PathfinderGoal
	private static final DataWatcherObject<Boolean> bJ = DataWatcher.a(EntityTurtlePet.class, DataWatcherRegistry.i);// set to true when "TravelPos" is set to a position.
	
	public EntityTurtlePet(World world){
		super(EntityTypes.TURTLE, world);
	}
	
	public EntityTurtlePet(World world, IPet pet){
		super(EntityTypes.TURTLE, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(bD, BlockPosition.ZERO);
		this.datawatcher.register(bE, Boolean.valueOf(false));
		this.datawatcher.register(bH, BlockPosition.ZERO);
		this.datawatcher.register(bI, Boolean.valueOf(false));
		this.datawatcher.register(bJ, Boolean.valueOf(false));
		this.datawatcher.register(bG, Boolean.valueOf(false));
	}
}
