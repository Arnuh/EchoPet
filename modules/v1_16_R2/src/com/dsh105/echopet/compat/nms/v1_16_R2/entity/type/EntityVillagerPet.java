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
package com.dsh105.echopet.compat.nms.v1_16_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.VillagerLevel;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVillagerDataHolder;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVillagerPet;
import net.minecraft.server.v1_16_R2.DataWatcher;
import net.minecraft.server.v1_16_R2.DataWatcherObject;
import net.minecraft.server.v1_16_R2.DataWatcherRegistry;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.VillagerData;
import net.minecraft.server.v1_16_R2.VillagerProfession;
import net.minecraft.server.v1_16_R2.VillagerType;
import net.minecraft.server.v1_16_R2.World;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.VILLAGER)
public class EntityVillagerPet extends EntityVillagerAbstractPet implements IEntityVillagerPet, IEntityVillagerDataHolder{
	
	private static final DataWatcherObject<VillagerData> DATA = DataWatcher.a(EntityVillagerPet.class, DataWatcherRegistry.q);
	
	public EntityVillagerPet(World world){
		super(EntityTypes.VILLAGER, world);
	}
	
	public EntityVillagerPet(World world, IPet pet){
		super(EntityTypes.VILLAGER, world, pet);
	}
	
	@Override
	public void setProfession(int i){
		try{
			this.datawatcher.set(DATA, getVillagerData().withProfession((VillagerProfession) VillagerProfession.class.getFields()[i].get(null)));
		}catch(Exception ignored){
		}
	}
	
	@Override
	public void setType(int type){
		try{
			this.datawatcher.set(DATA, getVillagerData().withType((VillagerType) VillagerType.class.getFields()[type].get(null)));
		}catch(Exception ignored){
		}
	}
	
	@Override
	public void setLevel(int level){
		try{
			this.datawatcher.set(DATA, getVillagerData().withLevel(level));
		}catch(Exception ignored){
		}
	}
	
	public VillagerData getVillagerData(){
		return this.datawatcher.get(DATA);
	}
	
	@Override
	public void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, VillagerLevel.NOVICE.ordinal()));
	}
}