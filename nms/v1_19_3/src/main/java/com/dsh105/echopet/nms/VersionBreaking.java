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
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Optional;

public class VersionBreaking{
	
	public static boolean closerToCenterThan(BlockPos targetPosition, Position position, double range){
		return targetPosition.closerToCenterThan(position, range);
	}
	
	public static BlockPos getBlockPosBelow(BlockPos pos){
		return pos.below();
	}
	
	public static BlockPos getBlockPosAbove(BlockPos pos){
		return pos.above();
	}
	
	public static void setItemSlot(Mob mob, EquipmentSlot slot, ItemStack itemStack, boolean silent){
		mob.setItemSlot(slot, itemStack, silent);
	}
	
	public static boolean addEntity(Level level, Entity entity, CreatureSpawnEvent.SpawnReason spawnReason){
		return level.addFreshEntity(entity, spawnReason);
	}
	
	public static void entityShake(LivingEntity entity){
		entity.gameEvent(GameEvent.ENTITY_SHAKE);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Registry<T> getRegistry(RegistryType registryType){
		return switch(registryType){
			case Attribute -> (Registry<T>) BuiltInRegistries.ATTRIBUTE;
			case Sound_Event -> (Registry<T>) BuiltInRegistries.SOUND_EVENT;
			case Villager_Profession -> (Registry<T>) BuiltInRegistries.VILLAGER_PROFESSION;
			case Villager_Type -> (Registry<T>) BuiltInRegistries.VILLAGER_TYPE;
		};
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getRegistry(RegistryType registryType, ResourceLocation resourceLocation){
		return (T) getRegistry(registryType).get(resourceLocation);
	}
	
	public static final EntityDataSerializer<Optional<BlockState>> OPTIONAL_BLOCK_STATE = EntityDataSerializers.BLOCK_STATE;
	
	public static void setMaxUpStep(Entity entity, float maxStepUp){
		entity.maxUpStep = maxStepUp;
	}
	
	public static void setFlyingSpeed(LivingEntity entity, float flyingSpeed){
		entity.flyingSpeed = flyingSpeed;
	}
	
	public static BlockPos blockPos(double x, double y, double z){
		return new BlockPos(x, y, z);
	}
	
	public static void calculateEntityAnimation(LivingEntity entity, boolean flutter){
		entity.calculateEntityAnimation(entity, flutter);
	}
	
	public static DamageSource getDamageSource(Entity entity, DamageSourceType damageSourceType){
		return switch(damageSourceType){
			case FLY_INTO_WALL -> DamageSource.FLY_INTO_WALL;
		};
	}
	
	public static boolean onGround(Entity entity){
		return entity.isOnGround();
	}
	
	public static Level level(Entity entity){
		return entity.level;
	}
	
	public static void setLevel(Entity entity, Level level){
		entity.level = level;
	}
}
