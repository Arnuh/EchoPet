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
package com.dsh105.echopet.compat.nms.v1_14_R1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.registration.IPetRegistry;
import com.dsh105.echopet.compat.api.registration.PetRegistrationEntry;
import com.dsh105.echopet.compat.api.registration.PetRegistrationException;
import com.google.common.base.Preconditions;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;

/**
 * Reversible registration of entities to Minecraft internals. Allows for temporary modification of internal mappings
 * so
 * that custom pet entities can be spawned.
 * <p/>
 * NOTE: This class is a modified version of the registry used in EchoPet v3.
 */
@SuppressWarnings("unchecked")
public class PetRegistry implements IPetRegistry{

	private final Map<PetType, PetRegistrationEntry> registrationEntries = new HashMap<>();


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
					EntityTypes<? extends Entity> oldClass = EntityTypes.a(petType.getMinecraftName());
					if(oldClass == null){
						System.out.println("Null oldClass for " + petType.getMinecraftName());
					}else 
					oldClasses.put(petType, new ImmutablePair<String, EntityTypes<? extends Entity>>(petType.getMinecraftName(), oldClass));
				}
				Function<? super World, ? extends Entity> func = t-> {
					try{
						return (Entity) entry.getEntityClass().getConstructor().newInstance(t);
					}catch(Exception ex){
						ex.printStackTrace();
						return null;
					}
				};
				EntityTypes.a.a((Class<? extends Entity>) entry.getEntityClass(), func);
			}
		}
	}

	Map<PetType, Pair<String, EntityTypes<? extends Entity>>> oldClasses = new HashMap<>();

	@Override
	public void disablePets(){
		try{
			// K = minecraftkey
			// V = class
			for(PetType petType : registrationEntries.keySet()){
				if(petType.isCompatible()){
					// PetRegistrationEntry entry = getRegistrationEntry(petType);
					Pair<String, EntityTypes<? extends Entity>> oldClass = oldClasses.get(petType);
					if(oldClass == null){
						continue;
					}
					Function<? super World, ? extends Entity> func = t-> {
						try{
							return oldClass.getValue().c().getConstructor().newInstance(t);
						}catch(Exception ex){
							ex.printStackTrace();
							return null;
						}
					};
					EntityTypes.a.a(oldClass.getValue().c(), func);
				}
			}
		}catch(SecurityException | IllegalArgumentException e){
			e.printStackTrace();
		}
	}

}