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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySkeletonPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntityPetType(petType = PetType.SKELETON)
public class EntitySkeletonPet extends EntitySkeletonAbstractPet implements IEntitySkeletonPet{
	
	// isFreezeConverting / isShaking
	private static final EntityDataAccessor<Boolean> DATA_STRAY_CONVERSION_ID = SynchedEntityData.defineId(EntitySkeletonPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntitySkeletonPet(Level world){
		super(EntityType.SKELETON, world);
	}
	
	public EntitySkeletonPet(Level world, final IPet pet){
		super(EntityType.SKELETON, world, pet);
		// TODO: Fix
		/*new BukkitRunnable() {
		    @Override
		    public void run() {
		        if (((ISkeletonPet) pet).isWither()) {
					setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
		        } else {
					setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.BOW));
		        }
		    }
		}.runTaskLater(EchoPet.getPlugin(), 5L);*/
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_STRAY_CONVERSION_ID, false);
	}
}