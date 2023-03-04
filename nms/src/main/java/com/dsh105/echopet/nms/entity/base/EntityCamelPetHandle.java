/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  EchoPet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.nms.entity.base;


import com.dsh105.echopet.compat.api.entity.nms.IEntityAgeablePet;
import net.minecraft.world.entity.animal.camel.Camel;
import org.bukkit.entity.Pose;

public class EntityCamelPetHandle extends EntityAgeablePetHandle{// Move to AbstractHorsePetHandle someday
	
	public EntityCamelPetHandle(IEntityAgeablePet entityPet){
		super(entityPet);
	}
	
	@Override
	public Camel get(){
		return (Camel) getEntity();
	}
	
	@Override
	public void setPose(Pose pose){
		// To play sound effects.
		if(Pose.SITTING.equals(pose)){
			get().sitDown();
		}else if(Pose.STANDING.equals(pose)){
			get().standUp();
		}
	}
}
