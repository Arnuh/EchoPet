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

package com.dsh105.echopet.compat.nms.v1_18_R2;

import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.event.PetPreSpawnEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ISpawnUtil;
import com.dsh105.echopet.compat.api.util.Lang;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SpawnUtil implements ISpawnUtil{
	
	public static final NamespacedKey parrotKey = new NamespacedKey(EchoPet.getPlugin(), "parrot");
	
	@Override
	public IEntityPet spawn(IPet pet, Player owner){
		Location l = owner.getLocation();
		// if(EchoPet.getPlugin().getVanishProvider().isVanished(owner)) return null;
		PetPreSpawnEvent spawnEvent = new PetPreSpawnEvent(pet, l, Lang.PET_PRE_SPAWN_CANCELLED.toString());
		EchoPet.getPlugin().getServer().getPluginManager().callEvent(spawnEvent);
		if(spawnEvent.isCancelled()){
			if(spawnEvent.getCancellationMessage() != null){
				Lang.sendTo(owner, spawnEvent.getCancellationMessage());
			}
			EchoPet.getManager().removePet(pet, true);
			return null;
		}
		l = spawnEvent.getSpawnLocation();
		Level mcWorld = ((CraftWorld) l.getWorld()).getHandle();
		EchoPet.getPetRegistry().enablePet(pet.getPetType());
		IEntityPet entityPet = pet.getPetType().getNewEntityPetInstance(mcWorld, pet);
		entityPet.setLocation(new Location(mcWorld.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch()));
		if(!l.getChunk().isLoaded()){
			l.getChunk().load();
		}
		if(!mcWorld.addFreshEntity(((CraftLivingEntity) entityPet.getEntity()).getHandle(), CreatureSpawnEvent.SpawnReason.CUSTOM)){
			owner.sendMessage(EchoPet.getPrefix() + ChatColor.YELLOW + "Failed to spawn pet entity.");
			EchoPet.getManager().removePet(pet, true);
		}else{
			owner.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, l, 1);
		}
		EchoPet.getPetRegistry().disablePet(pet.getPetType());
		return entityPet;
	}
	
	@Override
	// This is kind of a dumb way to do this.. But I'm too lazy to fix my reflection
	public org.bukkit.inventory.ItemStack getSpawnEgg(org.bukkit.inventory.ItemStack i, String entityTag){
		ItemStack is = CraftItemStack.asNMSCopy(i);
		CompoundTag nbt = is.getTag();
		if(nbt == null) nbt = new CompoundTag();
		if(!nbt.contains("EntityTag")) nbt.put("EntityTag", new CompoundTag());
		if(!entityTag.startsWith("minecraft:")) entityTag = "minecraft:" + entityTag;
		nbt.getCompound("EntityTag").putString("id", entityTag);
		return CraftItemStack.asCraftMirror(is);
	}
}