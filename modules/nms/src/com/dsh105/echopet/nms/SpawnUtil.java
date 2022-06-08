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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.event.PetPreSpawnEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ISpawnUtil;
import com.dsh105.echopet.compat.api.util.Lang;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.meta.SkullMeta;

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
		if(!VersionBreaking.addEntity(mcWorld, ((CraftEntity) entityPet.getEntity()).getHandle(), CreatureSpawnEvent.SpawnReason.CUSTOM)){
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
	
	private final Map<String, Class<?>> entityLookup = new HashMap<>();
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(IPetType petType, String attributeKey){
		Attribute attribute = Registry.ATTRIBUTE.get(ResourceLocation.tryParse(attributeKey));
		if(attribute == null){
			return null;
		}
		return (T) getEntityClass(petType.getMinecraftName()).map(this::createAttributeSupplier)
			.map(supplier->supplier.getBaseValue(attribute))
			.orElse(null);
	}
	
	public Optional<Class<?>> getEntityClass(String typeName){
		return Optional.ofNullable(entityLookup.computeIfAbsent(typeName, s->{
			try{
				for(var field : EntityType.class.getFields()){
					if(field.getType() == EntityType.class){
						ParameterizedType listType = (ParameterizedType) field.getGenericType();
						Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
						if(EntityType.getKey((EntityType<?>) field.get(null)).getPath().equals(s)){
							return listClass;
						}
					}
				}
				return null;
			}catch(Exception ex){
				EchoPet.LOG.log(java.util.logging.Level.SEVERE, "Failed to get entity class for " + typeName, ex);
				return null;
			}
		}));
	}
	
	public AttributeSupplier createAttributeSupplier(Class<?> clazz){
		try{
			for(var method : clazz.getMethods()){
				if(method.getReturnType() == AttributeSupplier.Builder.class){
					return ((AttributeSupplier.Builder) method.invoke(null)).build();
				}
			}
		}catch(Exception ex){
			EchoPet.LOG.log(java.util.logging.Level.SEVERE, "Failed to create attribute supplier for " + clazz.getName(), ex);
		}
		return null;
	}
	
	private Field profileField = null;
	private static boolean commonsCodecBase64 = false;
	
	static{
		try{
			Class.forName("org.apache.commons.codec.binary.Base64");
			commonsCodecBase64 = true;
		}catch(Exception ignored){
		}
	}
	
	@Override
	public void setSkullTexture(SkullMeta meta, String data){
		try{
			if(profileField == null){
				profileField = meta.getClass().getDeclaredField("profile");
				profileField.setAccessible(true);
			}
			GameProfile profile = new GameProfile(UUID.randomUUID(), "EchoPet");
			if(!isBase64(data)){
				if(data.contains("textures") && data.contains("url")){// Assume it's the full json just not base64'd
					data = Base64.getEncoder().encodeToString(data.getBytes());
				}else if(data.contains("textures.minecraft.net")){// Assume its just the URL and we need to setup the json.
					data = Base64.getEncoder().encodeToString("{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}".formatted(data).getBytes());
				}
			}
			profile.getProperties().put("textures", new Property("textures", data));
			profileField.set(meta, profile);
		}catch(Exception ex){
			EchoPet.LOG.log(java.util.logging.Level.SEVERE, "Failed to set skull texture", ex);
		}
	}
	
	public static boolean isBase64(String data){
		if(!commonsCodecBase64) return false;
		return org.apache.commons.codec.binary.Base64.isBase64(data);
	}
}