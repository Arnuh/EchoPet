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

package com.dsh105.echopet.nms.entity.handle;

import com.dsh105.echopet.compat.api.entity.nms.IEntityAnimalPet;
import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityAbstractHorsePetHandle;
import com.dsh105.echopet.nms.NMSEntityUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class EntityAbstractHorsePetHandle extends EntityAnimalPetHandle implements IEntityAbstractHorsePetHandle{
	
	private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = NMSEntityUtil.getAccessor(AbstractHorse.class, EntityDataSerializers.BYTE, 0);
	
	private static final int FLAG_TAME = 0x2, FLAG_SADDLE = 0x4, FLAG_BRED = 0x8, FLAG_EATING = 0x10, FLAG_STANDING = 0x20, FLAG_OPEN_MOUTH = 0x40;
	
	public EntityAbstractHorsePetHandle(IEntityAnimalPet entityPet){
		super(entityPet);
	}
	
	@Override
	public AbstractHorse get(){
		return (AbstractHorse) getEntity();
	}
	
	@Override
	public void setSaddled(boolean flag){
		setFlag(FLAG_SADDLE, flag);
	}
	
	protected void setFlag(int bitmask, boolean flag){
		var entity = get();
		byte flags = entity.getEntityData().get(DATA_ID_FLAGS);
		
		if(flag){
			entity.getEntityData().set(DATA_ID_FLAGS, (byte) (flags | bitmask));
		}else{
			entity.getEntityData().set(DATA_ID_FLAGS, (byte) (flags & ~bitmask));
		}
	}
}
