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
import com.dsh105.echopet.compat.api.entity.HorseArmor;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorsePet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.bukkit.entity.Horse;

@EntitySize(width = 1.4F, height = 1.6F)
@EntityPetType(petType = PetType.HORSE)
public class EntityHorsePet extends EntityHorseAbstractPet implements IEntityHorsePet{
	
	// EntityHorse
	private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(EntityHorsePet.class, EntityDataSerializers.INT);// Pattern
	
	public EntityHorsePet(Level world){
		super(EntityType.HORSE, world);
	}
	
	public EntityHorsePet(Level world, IPet pet){
		super(EntityType.HORSE, world, pet);
	}
	
	public int getVariant(){
		return entityData.get(DATA_ID_TYPE_VARIANT);
	}
	
	public void setColor(Horse.Color color){
		entityData.set(DATA_ID_TYPE_VARIANT, (color.ordinal() & 0xFF | getStyle().ordinal() << 8));
	}
	
	public void setStyle(Horse.Style style){
		entityData.set(DATA_ID_TYPE_VARIANT, getColor().ordinal() & 0xFF | style.ordinal() << 8);
	}
	
	public Horse.Style getStyle(){
		return Horse.Style.values()[(getVariant() >>> 8)];
	}
	
	public Horse.Color getColor(){
		return Horse.Color.values()[(getVariant() & 0xFF)];
	}
	
	@Override
	public void setArmour(HorseArmor a){
		Item item;
		switch(a){
			case Iron:
				item = Items.IRON_HORSE_ARMOR;
				break;
			case Gold:
				item = Items.GOLDEN_HORSE_ARMOR;
				break;
			case Diamond:
				item = Items.DIAMOND_HORSE_ARMOR;
				break;
			default:
				item = Items.AIR;
				break;
		}
		setSlot(EquipmentSlot.CHEST, item == null ? null : new ItemStack(item), true);
		setDropChance(EquipmentSlot.CHEST, 0);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
		
		// this.entityData.define(ARMOR, EnumHorseArmor.NONE.a());
	}
}