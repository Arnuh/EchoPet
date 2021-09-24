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

package com.dsh105.echopet.compat.nms.v1_17_R1.entity;

import com.dsh105.echopet.compat.api.ai.IPetGoalSelector;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import net.minecraft.world.phys.Vec3;
import org.bukkit.entity.Player;

public interface IEntityPetBase{
	
	IEntityPet getEntityPet();
	
	IPetGoalSelector getPetGoalSelector();
	
	boolean onInteract(Player player);
	
	void remove(boolean makeSound);
	
	void tick();
	
	float getSpeed();
	
	Vec3 travel(Vec3 vec3d);
}
