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

package com.dsh105.echopet.nms.entity;

import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityPetHandle;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;

public interface INMSEntityPetHandle extends IEntityPetHandle{
	
	CraftPlayer getCraftOwner();
	
	ServerPlayer getNMSOwner();
	
	Vec3 travel(Vec3 vec3d);
	
	void setDefaultGoals(Mob mob);
}
