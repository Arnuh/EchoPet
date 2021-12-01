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

package com.dsh105.echopet.compat.nms.v1_18_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySlimePet;
import com.dsh105.echopet.compat.nms.v1_18_R1.entity.EntityPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

@EntitySize(width = 0.6F, height = 0.6F)
@EntityPetType(petType = PetType.SLIME)
public class EntitySlimePet extends EntityPet implements IEntitySlimePet{
	
	public static final int MIN_SIZE = 1;
	public static final int MAX_SIZE = 127;
	private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(EntitySlimePet.class, EntityDataSerializers.INT);
	int jumpDelay;
	
	public EntitySlimePet(EntityType<? extends Mob> type, Level world){
		super(type, world);
	}
	
	public EntitySlimePet(EntityType<? extends Mob> type, Level world, IPet pet){
		super(type, world, pet);
		/*if(!Perm.hasDataPerm(pet.getOwner(), false, pet.getPetType(), PetData.MEDIUM, false)){
			if(!Perm.hasDataPerm(pet.getOwner(), false, pet.getPetType(), PetData.SMALL, false)){
				this.setSize(4);
			}else{
				this.setSize(1);
			}
		}else{
			this.setSize(2);
		}*/
		this.jumpDelay = this.random.nextInt(15) + 10;
	}
	
	public EntitySlimePet(Level world){
		this(EntityType.SLIME, world);
	}
	
	public EntitySlimePet(Level world, IPet pet){
		this(EntityType.SLIME, world, pet);
	}
	
	@Override
	public void setSize(int i){
		int j = Mth.clamp(i, MIN_SIZE, MAX_SIZE);
		this.entityData.set(ID_SIZE, j);
		//EntitySize es = this.getClass().getAnnotation(EntitySize.class);
		//this.a(es.width() * (float) i, es.height() * (float) i);
		this.setPos(getX(), getY(), getZ());
		this.setHealth(this.getMaxHealth());
	}
	
	public int getSize(){
		return this.entityData.get(ID_SIZE);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(ID_SIZE, 1);
	}
	
	@Override
	protected String getAmbientSoundString(){
		return isSmall() ? "entity.small_slime.squish" : "entity.slime.squish";
	}
	
	@Override
	protected String getDeathSoundString(){
		return isSmall() ? "entity.small_slime.death" : "entity.slime.death";
	}
	
	public boolean isSmall(){// ?
		return getSize() <= 1;
	}
	
	@Override
	public void onLive(){
		super.onLive();
		if(this.onGround && this.jumpDelay-- <= 0){
			this.jumpDelay = this.random.nextInt(15) + 10;
			makeSound("entity.slime.attack", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
			getJumpControl().jump();
		}
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		if(this.getSize() == 1){
			return SizeCategory.TINY;
		}else if(this.getSize() == 4){
			return SizeCategory.LARGE;
		}else{
			return SizeCategory.REGULAR;
		}
	}
}
