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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityTropicalFishPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;

@EntityPetType(petType = PetType.TROPICALFISH)
public class EntityTropicalFishPet extends EntityFishPet implements IEntityTropicalFishPet{
	
	private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(EntityTropicalFishPet.class, EntityDataSerializers.INT);
	
	public EntityTropicalFishPet(Level world){
		super(EntityType.TROPICAL_FISH, world);
	}
	
	public EntityTropicalFishPet(Level world, IPet pet){
		super(EntityType.TROPICAL_FISH, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
	}
	
	@Override
	public void setVariantData(boolean large, TropicalFish.Pattern pattern, DyeColor bodyColor, DyeColor patternColor){
		int variantData = patternColor.ordinal() << 24;
		variantData |= bodyColor.ordinal() << 16;
		variantData |= pattern.ordinal() << 8;
		variantData |= (large ? 1 : 0);
		entityData.set(DATA_ID_TYPE_VARIANT, variantData);
	}
}
