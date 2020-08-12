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
package com.dsh105.echopet.compat.nms.v1_16_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPolarBearPet;
import com.dsh105.echopet.compat.nms.v1_16_R2.entity.EntityAgeablePet;
import net.minecraft.server.v1_16_R2.DataWatcher;
import net.minecraft.server.v1_16_R2.DataWatcherObject;
import net.minecraft.server.v1_16_R2.DataWatcherRegistry;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;

@EntitySize(width = 1.4F, height = 1.4F)
@EntityPetType(petType = PetType.POLARBEAR)
public class EntityPolarBearPet extends EntityAgeablePet implements IEntityPolarBearPet{
	
	private static final DataWatcherObject<Boolean> STANDING_UP = DataWatcher.a(EntityPolarBearPet.class, DataWatcherRegistry.i);
	
	public EntityPolarBearPet(World world){
		super(EntityTypes.POLAR_BEAR, world);
	}
	
	public EntityPolarBearPet(World world, IPet pet){
		super(EntityTypes.POLAR_BEAR, world, pet);
	}
	
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(STANDING_UP, false);
	}
	
	public void setStandingUp(boolean flag){
		datawatcher.set(STANDING_UP, flag);
	}
	
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
