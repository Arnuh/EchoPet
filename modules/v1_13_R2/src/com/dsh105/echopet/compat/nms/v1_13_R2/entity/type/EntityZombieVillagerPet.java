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
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVillagerDataHolder;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityZombieVillagerPet;
import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.DataWatcherRegistry;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.ZOMBIEVILLAGER)
public class EntityZombieVillagerPet extends EntityZombiePet implements IEntityZombieVillagerPet, IEntityVillagerDataHolder{
	
	private static final DataWatcherObject<Boolean> CONVERTING = DataWatcher.a(EntityZombieVillagerPet.class, DataWatcherRegistry.i);
	private static final DataWatcherObject<Integer> PROFESSION = DataWatcher.a(EntityZombieVillagerPet.class, DataWatcherRegistry.b);
	
	public EntityZombieVillagerPet(World world){
		super(EntityTypes.ZOMBIE_VILLAGER, world);
	}
	
	public EntityZombieVillagerPet(World world, IPet pet){
		super(EntityTypes.ZOMBIE_VILLAGER, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(CONVERTING, false);
		this.datawatcher.register(PROFESSION, 0);
	}
	
	@Override
	public void setProfession(int i){
		this.datawatcher.set(PROFESSION, Integer.valueOf(i));
	}
	
	@Override
	public void setType(int type){}
	
	@Override
	public void setLevel(int level){}
	
}