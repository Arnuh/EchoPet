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
import com.dsh105.echopet.compat.api.entity.type.nms.handle.IEntityLlamaPetHandle;
import com.dsh105.echopet.nms.NMSEntityUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.animal.horse.Llama;
import org.bukkit.DyeColor;

public class EntityLlamaPetHandle extends EntityAbstractChestedHorsePetHandle implements IEntityLlamaPetHandle{
	
	private static final EntityDataAccessor<Integer> DATA_SWAG_ID = NMSEntityUtil.getAccessor(Llama.class, EntityDataSerializers.INT, 1);
	
	public EntityLlamaPetHandle(IEntityAnimalPet entityPet){
		super(entityPet);
	}
	
	@Override
	public Llama get(){
		return (Llama) getEntity();
	}
	
	@Override
	public void setCarpetColor(DyeColor color){
		get().getEntityData().set(DATA_SWAG_ID, color == null ? -1 : color.ordinal());
	}
	
	@Override
	public void setSkinColor(org.bukkit.entity.Llama.Color skinColor){
		get().setVariant(Llama.Variant.byId(skinColor.ordinal()));
	}
}
