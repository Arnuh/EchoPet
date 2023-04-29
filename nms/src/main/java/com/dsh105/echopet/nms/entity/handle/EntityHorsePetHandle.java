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

import com.dsh105.echopet.compat.api.entity.data.type.HorseArmor;
import com.dsh105.echopet.compat.api.entity.nms.IEntityAnimalPet;
import com.dsh105.echopet.compat.api.entity.type.nms.handle.IEntityHorsePetHandle;
import com.dsh105.echopet.nms.VersionBreaking;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Markings;
import net.minecraft.world.entity.animal.horse.Variant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class EntityHorsePetHandle extends EntityAbstractHorsePetHandle implements IEntityHorsePetHandle{
	
	public EntityHorsePetHandle(IEntityAnimalPet entityPet){
		super(entityPet);
	}
	
	@Override
	public Horse get(){
		return (Horse) getEntity();
	}
	
	
	@Override
	public void setVariant(org.bukkit.entity.Horse.Color color){
		get().setVariant(Variant.byId(color.ordinal()));
	}
	
	@Override
	public void setMarkings(org.bukkit.entity.Horse.Style style){
		get().setVariantAndMarkings(get().getVariant(), Markings.byId(style.ordinal()));
	}
	
	@Override
	public void setArmour(HorseArmor armor){
		Item item = switch(armor){
			case Iron -> Items.IRON_HORSE_ARMOR;
			case Gold -> Items.GOLDEN_HORSE_ARMOR;
			case Diamond -> Items.DIAMOND_HORSE_ARMOR;
			default -> Items.AIR;
		};
		var entity = get();
		// setArmor is private, duplicate the code here.
		VersionBreaking.setItemSlot(entity, EquipmentSlot.CHEST, item == null ? null : new ItemStack(item), true);
		entity.setDropChance(EquipmentSlot.CHEST, 0);
	}
}
