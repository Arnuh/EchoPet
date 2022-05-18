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

package com.dsh105.echopet.nms.entity.base;


import com.dsh105.echopet.compat.api.entity.ILivingEntityPet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LivingEntityPetBase extends EntityPetBase{
	
	public LivingEntityPetBase(ILivingEntityPet entityPet){
		super(entityPet);
	}
	
	@Override
	public LivingEntity getEntity(){
		return (LivingEntity) getEntityPet();
	}
	
	@Override
	protected void initiateEntityPet(){
		super.initiateEntityPet();
		AttributeInstance attributeInstance = getEntity().getAttribute(Attributes.MOVEMENT_SPEED);
		if(attributeInstance != null){
			attributeInstance.setBaseValue(getPet().getPetType().getWalkSpeed());
		}
	}
	
	@Override
	protected void adjustFlyingSpeed(){
		LivingEntity entity = getEntity();
		entity.flyingSpeed = entity.getSpeed() * 0.1F;
	}
}
