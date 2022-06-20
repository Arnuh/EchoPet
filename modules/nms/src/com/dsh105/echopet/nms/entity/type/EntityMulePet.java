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

import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityMulePet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 1.4F, height = 1.6F)
@EntityPetType(petType = PetType.MULE)
public class EntityMulePet extends EntityHorseChestedAbstractPet implements IEntityMulePet{
	
	public EntityMulePet(Level world){
		super(EntityType.MULE, world);
	}
	
	public EntityMulePet(Level world, IPet pet){
		super(EntityType.MULE, world, pet);
	}
	
	@Override
	protected SoundEvent getAmbientSound(){
		super.getAmbientSound();
		return SoundEvents.MULE_AMBIENT;
	}
	
	@Override
	protected SoundEvent getAngrySound(){
		super.getAngrySound();
		return SoundEvents.MULE_ANGRY;
	}
	
	@Override
	protected SoundEvent getDeathSound(){
		super.getDeathSound();
		return SoundEvents.MULE_DEATH;
	}
	
	@Override
	@Nullable
	protected SoundEvent getEatingSound(){
		return SoundEvents.MULE_EAT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource var0){
		super.getHurtSound(var0);
		return SoundEvents.MULE_HURT;
	}
	
	@Override
	protected void playChestEquipsSound(){
		this.playSound(SoundEvents.MULE_CHEST, 1.0F, (random().nextFloat() - random().nextFloat()) * 0.2F + 1.0F);
	}
}
