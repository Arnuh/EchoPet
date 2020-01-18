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

package com.dsh105.echopet.compat.nms.v1_15_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityBeePet;
import com.dsh105.echopet.compat.nms.v1_15_R1.entity.EntityAgeablePet;
import net.minecraft.server.v1_15_R1.DataWatcher;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;

/**
 * @author Arnah
 * @since Jan 05, 2020
 */
@EntitySize(width = 0.7F, height = 0.6F)
@EntityPetType(petType = PetType.BEE)
public class EntityBeePet extends EntityAgeablePet implements IEntityBeePet{
	
	private static final int Stung = 0x4, Nectar = 0x8;
	//0x1 is set by
	//final boolean flag = this.isAngry() && !this.hasStung() && this.getGoalTarget() != null && this.getGoalTarget().h(this) < 4.0;
	//this.t(flag);
	//0x2 is skipped? ok
	private static final DataWatcherObject<Byte> Flag = DataWatcher.a(EntityBeePet.class, DataWatcherRegistry.a);
	private static final DataWatcherObject<Integer> Anger = DataWatcher.a(EntityBeePet.class, DataWatcherRegistry.b);
	
	public EntityBeePet(World world){
		super(EntityTypes.BEE, world);
	}
	
	public EntityBeePet(World world, IPet pet){
		super(EntityTypes.BEE, world, pet);
	}
	
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(Flag, (byte) 0);
		this.datawatcher.register(Anger, 0);
	}
	
	@Override
	public void setHasStung(boolean hasStung){
		if(hasStung) addFlag(Stung);
		else removeFlag(Stung);
	}
	
	@Override
	public void setHasNectar(boolean hasNectar){
		if(hasNectar) addFlag(Nectar);
		else removeFlag(Nectar);
	}
	
	@Override
	public void setAngry(boolean angry){
		datawatcher.set(Anger, angry ? 1 : 0);
	}
	
	private void addFlag(int flag){
		datawatcher.set(Flag, (byte) (getFlag() | flag));
	}
	
	private void removeFlag(int flag){
		datawatcher.set(Flag, (byte) (getFlag() & ~flag));
	}
	
	public int getFlag(){
		return datawatcher.get(Flag);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
}