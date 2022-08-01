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

import com.dsh105.echopet.compat.api.ai.IPetGoalSelector;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.nms.IEntityLivingPet;
import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityPetHandle;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IFrogPet;
import com.dsh105.echopet.nms.entity.EntityPetGiveMeAccess;
import com.dsh105.echopet.nms.entity.INMSLivingEntityPetHandle;
import com.dsh105.echopet.nms.entity.ai.brain.PetFrogAi;
import com.dsh105.echopet.nms.entity.ai.brain.sensing.CustomSensorType;
import com.dsh105.echopet.nms.entity.base.EntityFrogPetHandle;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@EntitySize(width = 0.5F, height = 0.55F)
@EntityPetType(petType = PetType.FROG)
public class EntityFrogPet extends Frog implements IEntityLivingPet, EntityPetGiveMeAccess{
	
	protected static final ImmutableList<SensorType<? extends Sensor<? super Frog>>> SENSOR_TYPES = ImmutableList.of(CustomSensorType.NEAREST_LIVING_ENTITIES/*, SensorType.HURT_BY, SensorType.FROG_ATTACKABLES, SensorType.FROG_TEMPTATIONS*/, SensorType.IS_IN_WATER);
	protected static final ImmutableList<SensorType<? extends Sensor<? super Frog>>> EMPTY_SENSOR_TYPES = ImmutableList.of();
	protected IFrogPet pet;
	private final INMSLivingEntityPetHandle petHandle;
	
	// how do I init this without static or constructor
	private static boolean thisIsDumb = false;
	private static boolean usesBrain = false;
	
	public EntityFrogPet(Level world, IFrogPet pet){
		super(EntityType.FROG, world);
		this.pet = pet;
		this.petHandle = new EntityFrogPetHandle(this);
	}
	
	protected Brain.Provider<EntityFrogPet> petBrainProvider(){
		return Brain.provider(MEMORY_TYPES, usesBrain() ? SENSOR_TYPES : EMPTY_SENSOR_TYPES);
	}
	
	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic){
		if(usesBrain()){
			return PetFrogAi.makeBrain(petBrainProvider().makeBrain(dynamic));
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
		this.level.getProfiler().push("frogBrain");
		this.getBrain().tick((ServerLevel) this.level, this);
		this.level.getProfiler().pop();
		this.level.getProfiler().push("frogActivityUpdate");
		PetFrogAi.updateActivity(this);
		this.level.getProfiler().pop();
	}
	
	// Pet handling
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
	
	@Override
	public LivingEntity getEntity(){
		return (LivingEntity) getBukkitEntity();
	}
	
	@Override
	public void remove(boolean makeSound){
		petHandle.remove(makeSound);
	}
	
	@Override
	public boolean isDead(){
		return dead;
	}
	
	@Override
	public float getMaxUpStep(){
		return maxUpStep;
	}
	
	@Override
	public void setLocation(Location location){
		this.absMoveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		this.level = ((CraftWorld) location.getWorld()).getHandle();
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
	public IPetGoalSelector getPetGoalSelector(){
		return petHandle.getPetGoalSelector();
	}
	
	@Override
	public boolean usesBrain(){
		if(thisIsDumb) return usesBrain;
		thisIsDumb = true;
		return usesBrain = IFrogPet.BRAIN_ENABLED.get(PetType.FROG);
	}
	
	@Override
	public Player getOwner(){
		return pet.getOwner();
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
			this.flyingSpeed = 0.02F;
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
