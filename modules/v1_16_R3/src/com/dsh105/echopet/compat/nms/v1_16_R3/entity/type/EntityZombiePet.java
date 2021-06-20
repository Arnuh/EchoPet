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

package com.dsh105.echopet.compat.nms.v1_16_R3.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityZombiePet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.EntityAgeablePet;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.ZOMBIE)
public class EntityZombiePet extends EntityAgeablePet implements IEntityZombiePet{
	
	private static final DataWatcherObject<Integer> bx = DataWatcher.a(EntityZombiePet.class, DataWatcherRegistry.b);// gets registered and that is it.
	private static final DataWatcherObject<Boolean> by = DataWatcher.a(EntityZombiePet.class, DataWatcherRegistry.i);// DROWN_CONVERTING
	
	public EntityZombiePet(EntityTypes<? extends EntityInsentient> type, World world){
		super(type, world);
	}
	
	public EntityZombiePet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet){
		super(type, world, pet);
		// TODO: Broken
		/*new BukkitRunnable() {
		    @Override
		    public void run() {
				setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
		    }
		}.runTaskLater(EchoPet.getPlugin(), 5L);*/
	}
	
	public EntityZombiePet(World world){
		this(EntityTypes.ZOMBIE, world);
	}
	
	public EntityZombiePet(World world, IPet pet){
		this(EntityTypes.ZOMBIE, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		getDataWatcher().register(bx, 0);
		getDataWatcher().register(by, false);
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
