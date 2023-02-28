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
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityDonkeyPet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntityPetType(petType = PetType.DONKEY)
public class EntityDonkeyPet extends EntityHorseChestedAbstractPet implements IEntityDonkeyPet{
	
	public EntityDonkeyPet(Level world){
		super(EntityType.DONKEY, world);
	}
	
	public EntityDonkeyPet(Level world, IPet pet){
		super(EntityType.DONKEY, world, pet);
	}
	
	@Override
	protected SoundEvent getAmbientSound(){
		return SoundEvents.DONKEY_AMBIENT;
	}
	
	@Override
	protected SoundEvent getAngrySound(){
		return SoundEvents.DONKEY_ANGRY;
	}
	
	@Override
	public SoundEvent getDeathSound(){
		return SoundEvents.DONKEY_DEATH;
	}
	
	@Override
	@Nullable
	protected SoundEvent getEatingSound(){
		return SoundEvents.DONKEY_EAT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource var0){
		return SoundEvents.DONKEY_HURT;
	}
}
