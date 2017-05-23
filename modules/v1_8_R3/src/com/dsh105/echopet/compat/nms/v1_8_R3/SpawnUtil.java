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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.nms.v1_8_R3;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import com.dsh105.commodus.particle.Particle;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.event.PetPreSpawnEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ISpawnUtil;
import com.dsh105.echopet.compat.nms.v1_8_R3.entity.EntityPet;

import net.minecraft.server.v1_8_R3.World;

public class SpawnUtil implements ISpawnUtil {

	public IEntityPet spawn(IPet pet, Player owner){
		Location l = owner.getLocation();
		if(EchoPet.getPlugin().getVanishProvider().isVanished(owner)) return null;
		PetPreSpawnEvent spawnEvent = new PetPreSpawnEvent(pet, l);
		EchoPet.getPlugin().getServer().getPluginManager().callEvent(spawnEvent);
		if(spawnEvent.isCancelled()){
			owner.sendMessage(EchoPet.getPrefix() + ChatColor.YELLOW + "Pet spawn was cancelled externally.");
			EchoPet.getManager().removePet(pet, true);
			return null;
		}
		l = spawnEvent.getSpawnLocation();
		World mcWorld = ((CraftWorld) l.getWorld()).getHandle();
		EchoPet.getPetRegistry().enablePets();
		EntityPet entityPet = (EntityPet) pet.getPetType().getNewEntityPetInstance(mcWorld, pet);
		entityPet.setLocation(new Location(mcWorld.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch()));
		if(!l.getChunk().isLoaded()){
			l.getChunk().load();
		}
		if(!mcWorld.addEntity(entityPet, CreatureSpawnEvent.SpawnReason.CUSTOM)){
			owner.sendMessage(EchoPet.getPrefix() + ChatColor.YELLOW + "Failed to spawn pet entity.");
			EchoPet.getManager().removePet(pet, true);
		}else{
			Particle.MAGIC_RUNES.builder().at(l).show();
		}
		EchoPet.getPetRegistry().disablePets();
		return entityPet;
	}

	@SuppressWarnings("deprecation")
	@Override
	// This is kind of a dumb way to do this.. But I'm too lazy to fix my reflection
	public org.bukkit.inventory.ItemStack getSpawnEgg(org.bukkit.inventory.ItemStack i, String entityTag){
		EntityType type = null;
		try{
			type = EntityType.valueOf(entityTag.toUpperCase());
		}catch(Exception ex){
			switch (entityTag){
				case "mooshroom":
					type = EntityType.MUSHROOM_COW;
					break;
				case "zombie_pigman":
					type = EntityType.PIG_ZOMBIE;
					break;
				default:
					type = EntityType.BAT;
					System.out.println("Bad tag: " + entityTag);
					break;
			}
		}
		return new ItemStack(i.getType(), i.getAmount(), type.getTypeId());
	}

	public void setPassenger(int pos, org.bukkit.entity.LivingEntity entity, org.bukkit.entity.LivingEntity passenger){
		((CraftLivingEntity) entity).getHandle().passenger = ((CraftLivingEntity) passenger).getHandle();
	}

	public void removePassenger(org.bukkit.entity.LivingEntity entity){
		((CraftLivingEntity) entity).getHandle().passenger = null;
	}
}
