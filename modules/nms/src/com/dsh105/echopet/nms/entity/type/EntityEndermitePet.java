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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityEndermitePet;
import com.dsh105.echopet.nms.entity.EntityPet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntityPetType(petType = PetType.ENDERMITE)
public class EntityEndermitePet extends EntityPet implements IEntityEndermitePet{
	
	public EntityEndermitePet(Level world){
		super(EntityType.ENDERMITE, world);
	}
	
	public EntityEndermitePet(Level world, IPet pet){
		super(EntityType.ENDERMITE, world, pet);
	}
	
	@Override
	public void onLive(){
		super.onLive();
		// Is this clientside?
		for(int i = 0; i < 2; i++){
			this.level.addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), (random().nextDouble() - 0.5D) * 2.0D, -random().nextDouble(), (random().nextDouble() - 0.5D) * 2.0D);
		}
	}
}