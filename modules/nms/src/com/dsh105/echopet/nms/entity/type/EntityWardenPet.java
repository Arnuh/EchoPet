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

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.ai.IPetGoalSelector;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.nms.IEntityLivingPet;
import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityPetHandle;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IWardenPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.nms.entity.EntityPetGiveMeAccess;
import com.dsh105.echopet.nms.entity.INMSLivingEntityPetHandle;
import com.dsh105.echopet.nms.entity.ai.brain.PetWardenAi;
import com.dsh105.echopet.nms.entity.ai.brain.sensing.CustomSensorType;
import com.dsh105.echopet.nms.entity.base.EntityWardenPetHandle;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.warden.AngerManagement;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.WARDEN)
public class EntityWardenPet extends Warden implements IEntityLivingPet, EntityPetGiveMeAccess{
	
	protected static final List<SensorType<? extends Sensor<? super Warden>>> SENSOR_TYPES = List.of(CustomSensorType.NEAREST_PLAYERS, SensorType.WARDEN_ENTITY_SENSOR);
	protected static final List<MemoryModuleType<?>> MEMORY_TYPES = List.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.ROAR_TARGET, MemoryModuleType.DISTURBANCE_LOCATION, MemoryModuleType.RECENT_PROJECTILE, MemoryModuleType.IS_SNIFFING, MemoryModuleType.IS_EMERGING, MemoryModuleType.ROAR_SOUND_DELAY, MemoryModuleType.DIG_COOLDOWN, MemoryModuleType.ROAR_SOUND_COOLDOWN, MemoryModuleType.SNIFF_COOLDOWN, MemoryModuleType.TOUCH_COOLDOWN, MemoryModuleType.VIBRATION_COOLDOWN, MemoryModuleType.SONIC_BOOM_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_DELAY);
	protected static final ImmutableList<SensorType<? extends Sensor<? super Warden>>> EMPTY_SENSOR_TYPES = ImmutableList.of();
	protected IWardenPet pet;
	private final INMSLivingEntityPetHandle petHandle;
	
	// how do I init this without static or constructor
	private static boolean thisIsDumb = false;
	private static boolean usesBrain = false;
	
	private final CustomAngerManagement customAngerManagement;
	
	private static Field angerManagementField;
	
	static{
		try{
			for(Field field : Warden.class.getDeclaredFields()){
				if(field.getType().equals(AngerManagement.class)){
					angerManagementField = field;
					angerManagementField.setAccessible(true);
					break;
				}
			}
		}catch(Exception ignored){
		}
	}
	
	public EntityWardenPet(Level world, IWardenPet pet){
		super(EntityType.WARDEN, world);
		this.pet = pet;
		this.petHandle = new EntityWardenPetHandle(this);
		this.customAngerManagement = new CustomAngerManagement(this::canTargetEntity, Collections.emptyList());
		try{
			angerManagementField.set(this, customAngerManagement);
		}catch(Exception ex){
			EchoPet.getPlugin().getLogger().log(java.util.logging.Level.SEVERE, "", ex);
		}
	}
	
	protected Brain.Provider<EntityWardenPet> petBrainProvider(){
		return Brain.provider(MEMORY_TYPES, usesBrain() ? SENSOR_TYPES : EMPTY_SENSOR_TYPES);
	}
	
	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic){
		if(usesBrain()){
			return PetWardenAi.makeBrain(this, petBrainProvider().makeBrain(dynamic));
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
		ServerLevel serverLevel = (ServerLevel) this.level;
		this.level.getProfiler().push("wardenBrain");
		this.getBrain().tick(serverLevel, this);
		this.level.getProfiler().pop();
		/*if ((tickCount + getId()) % 120 == 0) {
			applyDarknessAround(serverLevel, position(), this, 20);
		}*/
		/*if (tickCount % 20 == 0) {
			getAngerManagement().tick(serverLevel, this::canTargetEntity);
			syncClientAngerLevel();
		}*/
		this.level.getProfiler().push("wardenActivityUpdate");
		PetWardenAi.updateActivity(this);
		this.level.getProfiler().pop();
	}
	
	public void setAnger(int anger){
		customAngerManagement.setAnger(anger);
	}
	
	@Override
	public boolean canTargetEntity(@Nullable Entity entity){
		return false;// hmm
	}
	
	// Pet handling
	
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
		return usesBrain = IWardenPet.BRAIN_ENABLED.get(PetType.WARDEN);
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
	
	public static class CustomAngerManagement extends AngerManagement{
		
		private int anger;
		
		public CustomAngerManagement(Predicate<Entity> filter, List<Pair<UUID, Integer>> angerByUuid){
			super(filter, angerByUuid);
		}
		
		@Override
		public void tick(ServerLevel level, Predicate<Entity> var1){
		
		}
		
		public void setAnger(int anger){
			this.anger = Math.min(150, anger);
		}
		
		@Override
		public int increaseAnger(Entity entity, int inc){
			anger = Math.min(150, anger + inc);
			return anger;
		}
		
		@Override
		public void clearAnger(Entity entity){
			anger = 0;
		}
		
		@Override
		public int getActiveAnger(@Nullable Entity entity){
			return anger;
		}
		
		@Override
		public Optional<net.minecraft.world.entity.LivingEntity> getActiveEntity(){
			return Optional.empty(); // Return pet owner?
		}
	}
}
