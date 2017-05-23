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
package com.dsh105.echopet.compat.nms.v1_10_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPigZombiePet;

import net.minecraft.server.v1_10_R1.World;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.PIGZOMBIE)
public class EntityPigZombiePet extends EntityZombiePet implements IEntityPigZombiePet{

	public EntityPigZombiePet(World world){
		super(world);
	}

	public EntityPigZombiePet(World world, IPet pet){
		super(world, pet);
		// TODO: Fix
		/*new BukkitRunnable(){
		
			@Override
			public void run(){
				setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
			}
		}.runTaskLater(EchoPet.getPlugin(), 5L);*/
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
	}

	@Override
	public SizeCategory getSizeCategory(){
		if(this.isBaby()){
			return SizeCategory.TINY;
		}else{
			return SizeCategory.REGULAR;
		}
	}
}
