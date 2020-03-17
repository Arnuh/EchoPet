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
package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.CatType;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityCatPet;
import com.dsh105.echopet.compat.nms.v1_14_R1.entity.EntityTameablePet;
import net.minecraft.server.v1_14_R1.DataWatcher;
import net.minecraft.server.v1_14_R1.DataWatcherObject;
import net.minecraft.server.v1_14_R1.DataWatcherRegistry;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EnumColor;
import net.minecraft.server.v1_14_R1.World;
import org.bukkit.DyeColor;

@EntitySize(width = 0.6F, height = 0.7F)
@EntityPetType(petType = PetType.CAT)
public class EntityCatPet extends EntityTameablePet implements IEntityCatPet{
	
	private static final DataWatcherObject<Integer> Type = DataWatcher.a(EntityCatPet.class, DataWatcherRegistry.b);
	private static final DataWatcherObject<Boolean> bG = DataWatcher.a(EntityCatPet.class, DataWatcherRegistry.i);
	private static final DataWatcherObject<Boolean> bH = DataWatcher.a(EntityCatPet.class, DataWatcherRegistry.i);
	private static final DataWatcherObject<Integer> CollarColor = DataWatcher.a(EntityCatPet.class, DataWatcherRegistry.b);
	
	public EntityCatPet(World world){
		super(EntityTypes.CAT, world);
	}
	
	public EntityCatPet(World world, IPet pet){
		super(EntityTypes.CAT, world, pet);
	}
	
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(Type, CatType.Black.ordinal());
		this.datawatcher.register(bG, false);
		this.datawatcher.register(bH, false);
		this.datawatcher.register(CollarColor, EnumColor.RED.getColorIndex());
	}
	
	
	@Override
	public void setType(CatType type){
		datawatcher.set(Type, type.ordinal());
	}
	
	@Override
	public void setCollarColor(DyeColor color){
		datawatcher.set(CollarColor, color.ordinal());
	}
}