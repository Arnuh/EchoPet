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

package com.dsh105.echopet.nms.entity.ai.brain.sensing;


import com.dsh105.echopet.compat.api.entity.nms.IEntityPet;
import com.dsh105.echopet.nms.entity.INMSEntityPetHandle;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.PlayerSensor;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PetPlayerSensor extends PlayerSensor{
	
	@Override
	protected void doTick(ServerLevel level, LivingEntity entity){
		
		List<Player> players = new ArrayList<>();
		if(entity instanceof IEntityPet entityPet && entityPet.getHandle() instanceof INMSEntityPetHandle handle){
			players.add(handle.getNMSOwner());
		}
		Brain<?> brain = entity.getBrain();
		brain.setMemory(MemoryModuleType.NEAREST_PLAYERS, players);
		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, players.isEmpty() ? null : players.get(0));
		brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, Optional.empty());
	}
}
