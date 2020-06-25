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

package com.dsh105.echopet.compat.nms.v1_16_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityTropicalFishPet;
import net.minecraft.server.v1_16_R1.DataWatcher;
import net.minecraft.server.v1_16_R1.DataWatcherObject;
import net.minecraft.server.v1_16_R1.DataWatcherRegistry;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.World;
import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;


@EntitySize(width = 0.5F, height = 0.4F)
@EntityPetType(petType = PetType.TROPICALFISH)
public class EntityTropicalFishPet extends EntityFishPet implements IEntityTropicalFishPet{
	
	private static final DataWatcherObject<Integer> DATA = DataWatcher.a(EntityTropicalFishPet.class, DataWatcherRegistry.b);
	
	public EntityTropicalFishPet(World world){
		super(EntityTypes.TROPICAL_FISH, world);
	}
	
	public EntityTropicalFishPet(World world, IPet pet){
		super(EntityTypes.TROPICAL_FISH, world, pet);
	}
	
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(DATA, 0);
	}
	
	public void setVariantData(boolean large, TropicalFish.Pattern pattern, DyeColor bodyColor, DyeColor patternColor){
		int variantData = patternColor.ordinal() << 24;
		variantData |= bodyColor.ordinal() << 16;
		variantData |= pattern.ordinal() << 8;
		variantData |= (large ? 1 : 0);
		datawatcher.set(DATA, variantData);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
}
