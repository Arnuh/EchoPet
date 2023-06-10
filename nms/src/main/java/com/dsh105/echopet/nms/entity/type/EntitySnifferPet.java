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

package com.dsh105.echopet.nms.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.nms.IEntityAnimalPet;
import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityPetHandle;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISnifferPet;
import com.dsh105.echopet.nms.VersionBreaking;
import com.dsh105.echopet.nms.entity.EntityPetGiveMeAccess;
import com.dsh105.echopet.nms.entity.INMSEntityPetHandle;
import com.dsh105.echopet.nms.entity.ai.brain.PetSnifferAi;
import com.dsh105.echopet.nms.entity.handle.EntitySnifferPetHandle;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;

@EntityPetType(petType = PetType.SNIFFER)
public class EntitySnifferPet extends Sniffer implements IEntityAnimalPet, EntityPetGiveMeAccess{
	
	protected ISnifferPet pet;
	private final INMSEntityPetHandle petHandle;
	// how do I init this without static or constructor
	private static boolean thisIsDumb = false;
	private static boolean usesBrain = false;
	
	public EntitySnifferPet(Level world, ISnifferPet pet){
		super(EntityType.SNIFFER, world);
		this.pet = pet;
		this.petHandle = new EntitySnifferPetHandle(this);
	}
	
	protected Brain.Provider<EntitySnifferPet> petBrainProvider(){
		return Brain.provider(PetSnifferAi.MEMORY_TYPES, usesBrain() ? PetSnifferAi.SENSOR_TYPES : PetSnifferAi.EMPTY_SENSOR_TYPES);
	}
	
	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic){
		if(usesBrain()){
			return null;// PetWardenAi.makeBrain(this, petBrainProvider().makeBrain(dynamic));
		}else{
			return petBrainProvider().makeBrain(dynamic);
		}
	}
	
	@Override
	protected void customServerAiStep(){
		if(!usesBrain()){
			return;
		}
		if(isVehicle()){
			return;
		}
		ServerLevel serverLevel = (ServerLevel) VersionBreaking.level(this);
		serverLevel.getProfiler().push("snifferBrain");
		getBrain().tick(serverLevel, this);
		serverLevel.getProfiler().popPush("snifferActivityUpdate");
		PetSnifferAi.updateActivity(this);
		serverLevel.getProfiler().pop();
	}
	
	@Override
	public IPet getPet(){
		return pet;
	}
	
	@Override
	public IEntityPetHandle getHandle(){
		return petHandle;
	}
	
	@Override
	public boolean usesBrain(){
		if(thisIsDumb) return usesBrain;
		thisIsDumb = true;
		return usesBrain = ISnifferPet.BRAIN_ENABLED.get(PetType.SNIFFER);
	}
	
	@Override
	public org.bukkit.entity.Entity getEntity(){
		return getBukkitEntity();
	}
	
	@Override
	public boolean isDead(){
		return dead;
	}
	
	@Override
	public void setLocation(Location location){
		this.absMoveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		VersionBreaking.setLevel(this, ((CraftWorld) location.getWorld()).getHandle());
	}
	
	@Override
	public SoundEvent publicDeathSound(){
		return getDeathSound();
	}
	
	@Override
	public boolean isPersistenceRequired(){
		return true;
	}
	
	@Override
	public void tick(){
		super.tick();
		petHandle.tick();
	}
	
	@Override
	public void travel(Vec3 vec3d){
		Vec3 result = petHandle.travel(vec3d);
		if(result == null){
			VersionBreaking.setFlyingSpeed(this, 0.02F);
			super.travel(vec3d);
			return;
		}
		setSpeed(petHandle.getSpeed());
		super.travel(result);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound){}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbttagcompound){}
}
