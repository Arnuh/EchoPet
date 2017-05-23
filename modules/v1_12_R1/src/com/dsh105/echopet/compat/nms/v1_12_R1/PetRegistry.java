/*
 * This file is part of EchoPet.
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */
package com.dsh105.echopet.compat.nms.v1_12_R1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.registration.IPetRegistry;
import com.dsh105.echopet.compat.api.registration.PetRegistrationEntry;
import com.dsh105.echopet.compat.api.registration.PetRegistrationException;
import com.dsh105.echopet.compat.nms.v1_12_R1.entity.EntityPet;
import com.google.common.base.Preconditions;

import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.MinecraftKey;

/**
 * Reversible registration of entities to Minecraft internals. Allows for temporary modification of internal mappings
 * so
 * that custom pet entities can be spawned.
 * <p/>
 * NOTE: This class is a modified version of the registry used in EchoPet v3.
 */
@SuppressWarnings("unchecked")
public class PetRegistry implements IPetRegistry{

	private final Map<PetType, PetRegistrationEntry> registrationEntries = new HashMap<PetType, PetRegistrationEntry>();


	public PetRegistry(){

		for(PetType petType : PetType.values()){
			if(petType.isCompatible()){
				try{
					PetRegistrationEntry registrationEntry = PetRegistrationEntry.create(petType);
					registrationEntries.put(petType, registrationEntry);
				}catch(PetRegistrationException e){
					// not found = not compatible with this server version
				}
			}
		}
	}

	public PetRegistrationEntry getRegistrationEntry(PetType petType){
		return registrationEntries.get(petType);
	}

	public void shutdown(){
		//
	}

	public IPet spawn(PetType petType, final Player owner){
		Preconditions.checkNotNull(petType, "Pet type must not be null.");
		Preconditions.checkNotNull(owner, "Owner type must not be null.");
		final PetRegistrationEntry registrationEntry = getRegistrationEntry(petType);
		if(registrationEntry == null){
			// Pet type not registered
			return null;
		}
		return performRegistration(registrationEntry, new Callable<IPet>(){

			public IPet call() throws Exception{
				return registrationEntry.createFor(owner);
			}
		});
	}

	public <T> T performRegistration(PetRegistrationEntry registrationEntry, Callable<T> callable){
		try{
			return callable.call();
		}catch(Exception e){
			throw new PetRegistrationException(e);
		}
	}

	@Override
	public void enablePets(){
		for(PetType petType : registrationEntries.keySet()){
			if(petType.isCompatible()){
				PetRegistrationEntry entry = getRegistrationEntry(petType);
				if(!oldClasses.containsKey(petType)){
					Class<? extends Entity> oldClass = EntityTypes.b.get(new MinecraftKey(petType.getMinecraftName()));
					MinecraftKey key = EntityTypes.b.b(oldClass);
					oldClasses.put(petType, new ImmutablePair<MinecraftKey, Class<? extends Entity>>(key, oldClass));
				}
				EntityTypes.b.a(entry.getRegistrationId(), new MinecraftKey(entry.getName()), (Class<? extends EntityPet>) entry.getEntityClass());
				// registry.addToRegistry(registrationEntry.getRegistrationId(), new MinecraftKey(registrationEntry.getName()), (Class<? extends EntityPet>) registrationEntry.getEntityClass());
			}
		}
	}

	Map<PetType, Pair<MinecraftKey, Class<? extends Entity>>> oldClasses = new HashMap<>();

	@Override
	public void disablePets(){
		try{
			// K = minecraftkey
			// V = class
			for(PetType petType : registrationEntries.keySet()){
				if(petType.isCompatible()){
					PetRegistrationEntry entry = getRegistrationEntry(petType);
					Pair<MinecraftKey, Class<? extends Entity>> oldClass = oldClasses.get(petType);
					EntityTypes.b.a(entry.getRegistrationId(), oldClass.getKey(), oldClass.getValue());
				}
			}
		}catch(SecurityException | IllegalArgumentException e){
			e.printStackTrace();
		}
	}

}