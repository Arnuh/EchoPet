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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityDolphinPet;
import net.minecraft.server.v1_17_R1.BlockPosition;
import net.minecraft.server.v1_17_R1.DataWatcher;
import net.minecraft.server.v1_17_R1.DataWatcherObject;
import net.minecraft.server.v1_17_R1.DataWatcherRegistry;
import net.minecraft.server.v1_17_R1.EntityTypes;
import net.minecraft.server.v1_17_R1.World;

/**
 * @author Arnah
 * @since Aug 2, 2018
 */
@EntitySize(width = 0.9F, height = 0.6F)
@EntityPetType(petType = PetType.COD)
public class EntityDolphinPet extends EntityWaterAnimalPet implements IEntityDolphinPet{
	
	private static final DataWatcherObject<BlockPosition> b = DataWatcher.a(EntityDolphinPet.class, DataWatcherRegistry.l);// "TreasurePos" - Some target to swim to.
	private static final DataWatcherObject<Boolean> c = DataWatcher.a(EntityDolphinPet.class, DataWatcherRegistry.i);// "GotFish" - Used for the pathfinder goal to go to "TreasurePog"
	private static final DataWatcherObject<Integer> bC = DataWatcher.a(EntityDolphinPet.class, DataWatcherRegistry.b);// "Moistness" - Takes damage when < 0.
	
	public EntityDolphinPet(World world){
		super(EntityTypes.DOLPHIN, world);
	}
	
	public EntityDolphinPet(World world, IPet pet){
		super(EntityTypes.DOLPHIN, world, pet);
	}
	
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(b, BlockPosition.ZERO);
		this.datawatcher.register(c, Boolean.valueOf(false));
		this.datawatcher.register(bC, Integer.valueOf(2400));
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
