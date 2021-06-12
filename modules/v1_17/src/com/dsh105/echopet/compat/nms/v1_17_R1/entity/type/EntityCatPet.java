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

import com.dsh105.echopet.compat.api.entity.CatType;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityCatPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityTameablePet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.DyeColor;

@EntitySize(width = 0.6F, height = 0.7F)
@EntityPetType(petType = PetType.CAT)
public class EntityCatPet extends EntityTameablePet implements IEntityCatPet{
	
	private static final EntityDataAccessor<Integer> Type = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> bG = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> bH = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> CollarColor = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.INT);
	
	public EntityCatPet(Level world){
		super(EntityType.CAT, world);
	}
	
	public EntityCatPet(Level world, IPet pet){
		super(EntityType.CAT, world, pet);
	}
	
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(Type, CatType.Black.ordinal());
		this.entityData.define(bG, false);
		this.entityData.define(bH, false);
		this.entityData.define(CollarColor, net.minecraft.world.item.DyeColor.RED.getId());
	}
	
	@Override
	public void setType(CatType type){
		entityData.set(Type, type.ordinal());
	}
	
	@Override
	public void setCollarColor(DyeColor color){
		entityData.set(CollarColor, color.ordinal());
	}
}