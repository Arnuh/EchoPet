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

package com.dsh105.echopet.compat.api.registration;

import java.lang.reflect.Constructor;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.nms.IEntityPet;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.util.StringUtil;
import org.bukkit.entity.Player;

public class PetRegistrationEntry{
	
	private final String name;
	private final Class<? extends IPet> petClass;
	private final Class<? extends IEntityPet> entityClass;
	
	private final Constructor<? extends IPet> petConstructor;
	private final Constructor<? extends IEntityPet> entityPetConstructor;
	
	public PetRegistrationEntry(String name, Class<? extends IPet> petClass, Class<? extends IEntityPet> entityClass){
		if(entityClass == null) throw new PetRegistrationException("Invalid Entity Class. Pet type is not supported by this server version.");
		if(petClass == null) throw new PetRegistrationException("Invalid Pet Class. Pet type is not supported by this server version.");
		
		this.name = name;
		this.entityClass = entityClass;
		this.petClass = petClass;
		
		try{
			this.petConstructor = lookupPetConstructor();
			this.entityPetConstructor = lookupEntityPetConstructor();
		}catch(NoSuchMethodException e){
			throw new PetRegistrationException("Failed to create pet constructors!", e);
		}
	}
	
	protected Constructor<? extends IPet> lookupPetConstructor() throws NoSuchMethodException{
		return this.petClass.getConstructor(Player.class);
	}
	
	@SuppressWarnings("unchecked")
	protected Constructor<? extends IEntityPet> lookupEntityPetConstructor() throws NoSuchMethodException{
		for(Constructor<?> con : this.entityClass.getConstructors()){
			if(con.getParameterCount() != 2) continue;
			Class<?>[] parameterTypes = con.getParameterTypes();
			if(!IPet.class.isAssignableFrom(parameterTypes[1])) continue;
			return (Constructor<? extends IEntityPet>) con;
		}
		throw new NoSuchMethodException();
	}
	
	public String getName(){
		return name;
	}
	
	public Class<? extends IPet> getPetClass(){
		return petClass;
	}
	
	public Class<? extends IEntityPet> getEntityClass(){
		return entityClass;
	}
	
	public Constructor<? extends IPet> getPetConstructor(){
		return petConstructor;
	}
	
	public IPet createFor(Player owner){
		Throwable throwable;
		try{
			return this.petConstructor.newInstance(owner);
		}catch(Exception e){
			throwable = e;
		}
		throw new IllegalStateException("Failed to create pet object for " + owner.getName(), throwable);
	}
	
	public IEntityPet createEntityPet(Object nmsWorld, IPet pet){
		Throwable throwable;
		try{
			return this.entityPetConstructor.newInstance(nmsWorld, pet);
		}catch(Exception e){
			throwable = e;
		}
		throw new IllegalStateException("Failed to create EntityPet object for " + pet.getOwner().getName(), throwable);
	}
	
	public static PetRegistrationEntry create(PetType petType){
		return new PetRegistrationEntry(StringUtil.capitalise(petType.toString().toLowerCase().replace("_", " ")).replace(" ", "") + "-Pet", petType.getPetClass(), petType.getEntityClass());
	}
}