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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
		// 1.18.2
		return targetPosition.closerToCenterThan(position, range);
		// 1.18.1 and below
		// return targetPosition.closerThan(position, range);
	}
	
	public static BlockPos getBlockPosBelow(BlockPos pos){
		// 1.18.x
		return pos.below();
		// 1.17.1
		// return pos.down();
	}
	
	public static BlockPos getBlockPosAbove(BlockPos pos){
		// 1.18.x
		return pos.above();
		// 1.17.1
		// return pos.up();
	}
	
	public static void setItemSlot(Mob mob, EquipmentSlot slot, ItemStack itemStack, boolean silent){
		// 1.18.x
		mob.setItemSlot(slot, itemStack, silent);
		// 1.17.1 / 1.18.x
		// mob.setItemSlot(slot, itemStack);
	}
	
	public static boolean addEntity(Level level, Entity entity, CreatureSpawnEvent.SpawnReason spawnReason){
		// 1.18.x
		return level.addFreshEntity(entity, spawnReason);
		// 1.17.1
		// return level.addEntity(entity, spawnReason);
	}
	
	public static void entityShake(LivingEntity entity){
		// pre 1.19
		// entity.gameEvent(GameEvent.WOLF_SHAKING);
		// 1.19
		entity.gameEvent(GameEvent.ENTITY_SHAKE);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getRegistry(RegistryType registryType, ResourceLocation resourceLocation){
		return switch(registryType){
			// 1.19.2 and below
			// case Attribute -> (T) Registry.ATTRIBUTE.get(resourceLocation);
			// case Sound_Event -> (T) Registry.SOUND_EVENT.get(resourceLocation);
			// 1.19.3
			case Attribute -> (T) BuiltInRegistries.ATTRIBUTE.get(resourceLocation);
			case Sound_Event -> (T) BuiltInRegistries.SOUND_EVENT.get(resourceLocation);
		};
	}
}
