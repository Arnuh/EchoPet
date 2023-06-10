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

package com.dsh105.echopet.nms.entity.handle;

import com.dsh105.echopet.compat.api.entity.nms.IEntityAnimalPet;
import com.dsh105.echopet.compat.api.entity.type.nms.handle.IEntitySnifferPetHandle;
import com.dsh105.echopet.compat.api.entity.type.pet.ISnifferPet;
import net.minecraft.world.entity.animal.sniffer.Sniffer;

public class EntitySnifferPetHandle extends EntityAnimalPetHandle implements IEntitySnifferPetHandle{
	
	private static final Sniffer.State[] STATES = Sniffer.State.values();
	
	public EntitySnifferPetHandle(IEntityAnimalPet entityPet){
		super(entityPet);
	}
	
	@Override
	public Sniffer get(){
		return (Sniffer) getEntity();
	}
	
	@Override
	public void transitionTo(ISnifferPet.State state){
		get().transitionTo(STATES[state.ordinal()]);
	}
}
