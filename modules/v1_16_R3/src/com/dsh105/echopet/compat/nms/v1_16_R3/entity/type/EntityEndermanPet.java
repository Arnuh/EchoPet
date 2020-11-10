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
package com.dsh105.echopet.compat.nms.v1_16_R3.entity.type;

import java.util.Optional;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityEndermanPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IEndermanPet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.EntityPet;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.IBlockData;
import net.minecraft.server.v1_16_R3.World;

@EntitySize(width = 0.6F, height = 2.9F)
@EntityPetType(petType = PetType.ENDERMAN)
public class EntityEndermanPet extends EntityPet implements IEntityEndermanPet{
	
	private static final DataWatcherObject<Optional<IBlockData>> BLOCK = DataWatcher.a(EntityEndermanPet.class, DataWatcherRegistry.h);
	private static final DataWatcherObject<Boolean> SCREAMING = DataWatcher.a(EntityEndermanPet.class, DataWatcherRegistry.i);
	
	public EntityEndermanPet(World world){
		super(EntityTypes.ENDERMAN, world);
	}
	
	public EntityEndermanPet(World world, IPet pet){
		super(EntityTypes.ENDERMAN, world, pet);
	}
	
	public void setScreaming(boolean flag){
		this.datawatcher.set(SCREAMING, Boolean.valueOf(flag));
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(BLOCK, Optional.empty());
		this.datawatcher.register(SCREAMING, Boolean.valueOf(false));
	}
	
	public boolean isScreaming(){
		return this.datawatcher.get(SCREAMING);
	}
	
	public void setCarried(IBlockData iblockdata){
		this.datawatcher.set(BLOCK, Optional.ofNullable(iblockdata));
	}
	
	public IBlockData getCarried(){
		return this.datawatcher.get(BLOCK).orElse(null);
	}
	
	@Override
	protected String getIdleSound(){
		return ((IEndermanPet) pet).isScreaming() ? "entity.endermen.scream" : "entity.endermen.ambient";
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
