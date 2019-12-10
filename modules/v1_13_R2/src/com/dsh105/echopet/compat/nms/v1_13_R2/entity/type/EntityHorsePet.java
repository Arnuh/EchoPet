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
package com.dsh105.echopet.compat.nms.v1_13_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.HorseArmor;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorsePet;
import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.DataWatcherRegistry;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.EnumHorseArmor;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.entity.Horse;

@EntitySize(width = 1.4F, height = 1.6F)
@EntityPetType(petType = PetType.HORSE)
public class EntityHorsePet extends EntityHorseAbstractPet implements IEntityHorsePet{
	
	// EntityHorse
	private static final DataWatcherObject<Integer> STYLE = DataWatcher.a(EntityHorsePet.class, DataWatcherRegistry.b);// Pattern
	private static final DataWatcherObject<Integer> ARMOR = DataWatcher.a(EntityHorsePet.class, DataWatcherRegistry.b);// Self explanatory.
	
	public EntityHorsePet(World world){
		super(EntityTypes.HORSE, world);
	}
	
	public EntityHorsePet(World world, IPet pet){
		super(EntityTypes.HORSE, world, pet);
	}
	
	public int getVariant(){
		return datawatcher.get(STYLE);
	}
	
	public void setColor(Horse.Color color){
		datawatcher.set(STYLE, (color.ordinal() & 0xFF | getStyle().ordinal() << 8));
	}
	
	public void setStyle(Horse.Style style){
		datawatcher.set(STYLE, getColor().ordinal() & 0xFF | style.ordinal() << 8);
	}
	
	public Horse.Style getStyle(){
		return Horse.Style.values()[(getVariant() >>> 8)];
	}
	
	public Horse.Color getColor(){
		return Horse.Color.values()[(getVariant() & 0xFF)];
	}
	
	@Override
	public void setArmour(HorseArmor a){
		this.datawatcher.set(ARMOR, EnumHorseArmor.values()[a.ordinal()].a());
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(STYLE, 0);
		this.datawatcher.register(ARMOR, EnumHorseArmor.NONE.a());
	}
}