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

package com.dsh105.echopet.compat.api.entity.nms.handle;

import com.dsh105.echopet.compat.api.ai.IPetGoalSelector;
import com.dsh105.echopet.compat.api.entity.nms.IEntityPet;
import org.bukkit.entity.Pose;


/**
 * Represents a handle for a pet entity.<br>
 * A pet handle allows control over the NMS EntityData and Goals via EchoPet API friendly methods.<br>
 * <br>
 * While all NMS Pets are represented by an IEntityPet, IEntityPet only offers basic information that every NMS Pet Entity needs.<br>
 *
 * @see IEntityPet
 */
public interface IEntityPetHandle{
	
	IEntityPet getEntityPet();
	
	IPetGoalSelector getPetGoalSelector();
	
	void remove(boolean makeSound);
	
	void tick();
	
	float getSpeed();
	
	void setPose(Pose pose);
}
