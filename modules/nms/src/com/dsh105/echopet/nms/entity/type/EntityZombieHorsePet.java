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
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityZombieHorsePet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 1.4F, height = 1.6F)
@EntityPetType(petType = PetType.ZOMBIEHORSE)
public class EntityZombieHorsePet extends EntityHorseAbstractPet implements IEntityZombieHorsePet{
	
	public EntityZombieHorsePet(Level world){
		super(EntityType.ZOMBIE_HORSE, world);
	}
	
	public EntityZombieHorsePet(Level world, IPet pet){
		super(EntityType.ZOMBIE_HORSE, world, pet);
	}
	
	@Override
	protected SoundEvent getAmbientSound(){
		super.getAmbientSound();
		return SoundEvents.ZOMBIE_HORSE_AMBIENT;
	}
	
	@Override
	protected SoundEvent getDeathSound(){
		super.getDeathSound();
		return SoundEvents.ZOMBIE_HORSE_DEATH;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource var0){
		super.getHurtSound(var0);
		return SoundEvents.ZOMBIE_HORSE_HURT;
	}
}
