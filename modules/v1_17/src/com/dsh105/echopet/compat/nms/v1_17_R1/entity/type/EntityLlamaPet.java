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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityLlamaPet;
import net.minecraft.server.v1_17_R1.DataWatcher;
import net.minecraft.server.v1_17_R1.DataWatcherObject;
import net.minecraft.server.v1_17_R1.DataWatcherRegistry;
import net.minecraft.server.v1_17_R1.EntityInsentient;
import net.minecraft.server.v1_17_R1.EntityTypes;
import net.minecraft.server.v1_17_R1.World;
import org.bukkit.DyeColor;
import org.bukkit.entity.Llama;

@EntitySize(width = 0.9F, height = 1.87F)
@EntityPetType(petType = PetType.LLAMA)
public class EntityLlamaPet extends EntityHorseChestedAbstractPet implements IEntityLlamaPet{
	
	private static final DataWatcherObject<Integer> STRENGTH = DataWatcher.a(EntityLlamaPet.class, DataWatcherRegistry.b);// changes storage
	private static final DataWatcherObject<Integer> COLOR = DataWatcher.a(EntityLlamaPet.class, DataWatcherRegistry.b);// carpet color
	private static final DataWatcherObject<Integer> VARIANT = DataWatcher.a(EntityLlamaPet.class, DataWatcherRegistry.b);// Like an outfit
	
	public EntityLlamaPet(EntityTypes<? extends EntityInsentient> type, World world){
		super(type, world);
	}
	
	public EntityLlamaPet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet){
		super(type, world, pet);
	}
	
	public EntityLlamaPet(World world){
		this(EntityTypes.LLAMA, world);
	}
	
	public EntityLlamaPet(World world, IPet pet){
		this(EntityTypes.LLAMA, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(STRENGTH, 0);
		this.datawatcher.register(COLOR, -1);
		this.datawatcher.register(VARIANT, 0);
	}
	
	@Override
	public void setCarpetColor(DyeColor color){
		this.datawatcher.set(COLOR, color == null ? -1 : color.ordinal());
	}
	
	@Override
	public void setSkinColor(Llama.Color skinColor){
		this.datawatcher.set(VARIANT, skinColor.ordinal());
	}
}
