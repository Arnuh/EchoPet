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

package com.dsh105.echopet.compat.nms.v1_17_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityBatPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@EntitySize(width = 0.5F, height = 0.9F)
@EntityPetType(petType = PetType.BAT)
public class EntityBatPet extends EntityPet implements IEntityBatPet{
	
	private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(EntityBatPet.class, EntityDataSerializers.BYTE);
	private static final int FLAG_RESTING = 0x1;
	
	public EntityBatPet(Level world){
		super(EntityType.BAT, world);
	}
	
	public EntityBatPet(Level world, IPet pet){
		super(EntityType.BAT, world, pet);
	}
	
	@Override
	public void setHanging(boolean flag){
		int i = this.entityData.get(DATA_ID_FLAGS);
		if(flag){
			this.entityData.set(DATA_ID_FLAGS, (byte) (i | 0x1));
		}else{
			this.entityData.set(DATA_ID_FLAGS, (byte) (i & -0x2));
		}
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_ID_FLAGS, (byte) 0);
	}
	
	@Override
	public SoundEvent getAmbientSound(){
		return this.isResting() && this.random.nextInt(4) != 0 ? null : SoundEvents.BAT_AMBIENT;
	}
	
	@Override
	public void tick(){
		super.tick();
		if(this.isResting()){
			this.setDeltaMovement(Vec3.ZERO);
			this.setPosRaw(this.getX(), (double) Mth.floor(this.getY()) + 1.0D - (double) this.getBbHeight(), this.getZ());
		}else{
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
		}
	}
	
	public boolean isResting(){
		return (this.entityData.get(DATA_ID_FLAGS) & FLAG_RESTING) != 0;
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
}
