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

package com.dsh105.echopet.nms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class VersionBreaking{
	
	public static boolean closerToCenterThan(BlockPos targetPosition, Position position, double range){
		return targetPosition.closerThan(position, range);
	}
	
	public static BlockPos getBlockPosBelow(BlockPos pos){
		return pos.down();
	}
	
	public static BlockPos getBlockPosAbove(BlockPos pos){
		return pos.up();
	}
	
	public static void setItemSlot(Mob mob, EquipmentSlot slot, ItemStack itemStack, boolean silent){
		mob.setItemSlot(slot, itemStack);
	}
	
	public static boolean addEntity(Level level, Entity entity, CreatureSpawnEvent.SpawnReason spawnReason){
		return level.addEntity(entity, spawnReason);
	}
	
	public static void entityShake(LivingEntity entity){
		entity.gameEvent(GameEvent.WOLF_SHAKING);
	}
}
