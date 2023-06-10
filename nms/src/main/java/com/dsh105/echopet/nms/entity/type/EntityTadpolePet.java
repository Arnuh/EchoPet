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
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.nms.IEntityFishPet;
import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityPetHandle;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ITadpolePet;
import com.dsh105.echopet.nms.VersionBreaking;
import com.dsh105.echopet.nms.entity.EntityPetGiveMeAccess;
import com.dsh105.echopet.nms.entity.INMSLivingEntityPetHandle;
import com.dsh105.echopet.nms.entity.ai.BiMoveControl;
import com.dsh105.echopet.nms.entity.ai.brain.sensing.CustomSensorType;
import com.dsh105.echopet.nms.entity.handle.LivingEntityPetHandle;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.TADPOLE)
public class EntityTadpolePet extends Tadpole implements IEntityFishPet, EntityPetGiveMeAccess{
	
	protected static final ImmutableList<SensorType<? extends Sensor<? super Tadpole>>> SENSOR_TYPES = ImmutableList.of(CustomSensorType.NEAREST_LIVING_ENTITIES, CustomSensorType.NEAREST_PLAYERS/*, SensorType.HURT_BY, SensorType.FROG_TEMPTATIONS*/);
	protected static final ImmutableList<SensorType<? extends Sensor<? super Tadpole>>> EMPTY_SENSOR_TYPES = ImmutableList.of();
	
	protected ITadpolePet pet;
	private final INMSLivingEntityPetHandle petHandle;
	
	// how do I init this without static or constructor
	private static boolean thisIsDumb = false;
	private static boolean usesBrain = false;
	
	public EntityTadpolePet(Level world, ITadpolePet pet){
		super(EntityType.TADPOLE, world);
		this.pet = pet;
		this.petHandle = new LivingEntityPetHandle(this);
		if(usesBrain()){
			moveControl = new BiMoveControl(this, moveControl, new MoveControl(this), Entity::isVehicle);
		}else{
			moveControl = new MoveControl(this);
			// Look control can probably be the same?
		}
	}
	
	protected Brain.Provider<EntityTadpolePet> petBrainProvider(){
		return Brain.provider(MEMORY_TYPES, usesBrain() ? SENSOR_TYPES : EMPTY_SENSOR_TYPES);
	}
	
	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic){
		if(usesBrain()){
			return null;// PetTadpoleAi.makeBrain(petBrainProvider().makeBrain(dynamic));
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
		var level = VersionBreaking.level(this);
		level.getProfiler().push("tadpoleBrain");
		this.getBrain().tick((ServerLevel) level, this);
		level.getProfiler().pop();
		level.getProfiler().push("tadpoleActivityUpdate");
		// PetTadpoleAi.updateActivity(this);
		level.getProfiler().pop();
	}
	
	@Override
	protected PathNavigation createNavigation(Level world){
		// Probably don't want the water navigation unless in water?
		return new GroundPathNavigation(this, world);
	}
	
	@Override
	public InteractionResult mobInteract(net.minecraft.world.entity.player.Player var0, InteractionHand var1){
		return InteractionResult.PASS;
	}
	
	@Override
	public void aiStep(){
		verticalCollision = false; // Disable flopping for now
		super.aiStep();
		age = 0;
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
		VersionBreaking.setLevel(this, ((CraftWorld) location.getWorld()).getHandle());
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
		return usesBrain = ITadpolePet.BRAIN_ENABLED.get(PetType.TADPOLE);
	}
	
	@Override
	public Player getPetOwner(){
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
