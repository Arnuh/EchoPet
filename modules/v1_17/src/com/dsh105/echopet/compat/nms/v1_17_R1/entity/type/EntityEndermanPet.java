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
package com.dsh105.echopet.compat.nms.v1_17_R1.entity.type;

import java.util.Optional;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityEndermanPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IEndermanPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@EntitySize(width = 0.6F, height = 2.9F)
@EntityPetType(petType = PetType.ENDERMAN)
public class EntityEndermanPet extends EntityPet implements IEntityEndermanPet{
	
	private static final EntityDataAccessor<Optional<BlockState>> BLOCK = SynchedEntityData.defineId(EntityEndermanPet.class, EntityDataSerializers.BLOCK_STATE);
	private static final EntityDataAccessor<Boolean> SCREAMING = SynchedEntityData.defineId(EntityEndermanPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityEndermanPet(Level world){
		super(EntityType.ENDERMAN, world);
	}
	
	public EntityEndermanPet(Level world, IPet pet){
		super(EntityType.ENDERMAN, world, pet);
	}
	
	public void setScreaming(boolean flag){
		this.entityData.set(SCREAMING, Boolean.valueOf(flag));
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(BLOCK, Optional.empty());
		this.entityData.define(SCREAMING, Boolean.valueOf(false));
	}
	
	public boolean isScreaming(){
		return this.entityData.get(SCREAMING);
	}
	
	public void setCarried(BlockState iblockdata){
		this.entityData.set(BLOCK, Optional.ofNullable(iblockdata));
	}
	
	public BlockState getCarried(){
		return this.entityData.get(BLOCK).orElse(null);
	}
	
	@Override
	protected String getAmbientSoundString(){
		return ((IEndermanPet) pet).isScreaming() ? "entity.endermen.scream" : "entity.endermen.ambient";
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
