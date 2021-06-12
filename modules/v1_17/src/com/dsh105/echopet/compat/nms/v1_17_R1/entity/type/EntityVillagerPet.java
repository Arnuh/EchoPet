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

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.VillagerLevel;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVillagerDataHolder;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVillagerPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.VILLAGER)
public class EntityVillagerPet extends EntityVillagerAbstractPet implements IEntityVillagerPet, IEntityVillagerDataHolder{
	
	private static final EntityDataAccessor<VillagerData> DATA = SynchedEntityData.defineId(EntityVillagerPet.class, EntityDataSerializers.VILLAGER_DATA);
	
	public EntityVillagerPet(Level world){
		super(EntityType.VILLAGER, world);
	}
	
	public EntityVillagerPet(Level world, IPet pet){
		super(EntityType.VILLAGER, world, pet);
	}
	
	@Override
	public void setProfession(int i){
		try{
			this.entityData.set(DATA, getVillagerData().setProfession((VillagerProfession) VillagerProfession.class.getFields()[i].get(null)));
		}catch(Exception ignored){
		}
	}
	
	@Override
	public void setType(int type){
		try{
			this.entityData.set(DATA, getVillagerData().setType((VillagerType) VillagerType.class.getFields()[type].get(null)));
		}catch(Exception ignored){
		}
	}
	
	@Override
	public void setLevel(int level){
		try{
			this.entityData.set(DATA, getVillagerData().setLevel(level));
		}catch(Exception ignored){
		}
	}
	
	public VillagerData getVillagerData(){
		return this.entityData.get(DATA);
	}
	
	@Override
	public void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, VillagerLevel.NOVICE.ordinal()));
	}
}