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

package com.dsh105.echopet.nms.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityZombiePet;
import com.dsh105.echopet.nms.entity.EntityAgeablePet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

@EntityPetType(petType = PetType.ZOMBIE)
public class EntityZombiePet extends EntityAgeablePet implements IEntityZombiePet{
	
	private static final EntityDataAccessor<Integer> DATA_SPECIAL_TYPE_ID = SynchedEntityData.defineId(EntityZombiePet.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_DROWNED_CONVERSION_ID = SynchedEntityData.defineId(EntityZombiePet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityZombiePet(EntityType<? extends Mob> type, Level world){
		super(type, world);
	}
	
	public EntityZombiePet(EntityType<? extends Mob> type, Level world, IPet pet){
		super(type, world, pet);
		// TODO: Broken
		/*new BukkitRunnable() {
		    @Override
		    public void run() {
				setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
		    }
		}.runTaskLater(EchoPet.getPlugin(), 5L);*/
	}
	
	public EntityZombiePet(Level world){
		this(EntityType.ZOMBIE, world);
	}
	
	public EntityZombiePet(Level world, IPet pet){
		this(EntityType.ZOMBIE, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		entityData.define(DATA_SPECIAL_TYPE_ID, 0);
		entityData.define(DATA_DROWNED_CONVERSION_ID, false);
	}
}
