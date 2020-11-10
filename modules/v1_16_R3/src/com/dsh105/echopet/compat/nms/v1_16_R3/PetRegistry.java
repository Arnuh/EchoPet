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
 *  along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

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
package com.dsh105.echopet.compat.nms.v1_16_R3;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.registration.IPetRegistry;
import com.dsh105.echopet.compat.api.registration.PetRegistrationEntry;
import com.dsh105.echopet.compat.api.registration.PetRegistrationException;
import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;

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
		for(PetType petType : PetType.values){
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
		registrationEntries.clear();
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
	public void enablePet(PetType petType){
		/*if(petType.isCompatible()){
			EntityTypes<? extends Entity> entity = EntityTypes.a(petType.getMinecraftName()).orElse(null);
			if(entity == null){
				Bukkit.getLogger().warning("Failed to find entity for " + petType.getMinecraftName());
				return;
			}
			EnumCreatureType type = entity.e();
			a<Entity> entitytypes_a = EntityTypes.a.a(new EntityTypes.b(){
				
				@Override
				public Entity create(EntityTypes type, World world){
					return type.a(world);
				}
			}, type);
			IRegistry.a((IRegistry) IRegistry.ENTITY_TYPE, petType.getMinecraftName(), entitytypes_a.a(petType.getMinecraftName()));
		}*/
	}
	
	@Override
	public void disablePet(PetType petType){
		/*try{
			if(petType.isCompatible()){
				Object val = EntityTypes.class.getField(petType.getMinecraftName().toUpperCase()).get(null);
				IRegistry.a((IRegistry) IRegistry.ENTITY_TYPE, petType.getMinecraftName(), val);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}*/
	}
}