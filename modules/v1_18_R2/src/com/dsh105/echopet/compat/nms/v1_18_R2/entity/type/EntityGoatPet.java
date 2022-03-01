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

package com.dsh105.echopet.compat.nms.v1_18_R2.entity.type;

import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.ai.IPetGoalSelector;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IEntityPetBase;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGoatPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IGoatPet;
import com.dsh105.echopet.compat.nms.v1_18_R2.entity.EntityPetGiveMeAccess;
import com.dsh105.echopet.compat.nms.v1_18_R2.entity.INMSEntityPetBase;
import com.dsh105.echopet.compat.nms.v1_18_R2.entity.base.EntityAgeablePetBase;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@EntitySize(width = 1.3F, height = 0.9F)
@EntityPetType(petType = PetType.GOAT)
public class EntityGoatPet extends Goat implements IEntityPet, EntityPetGiveMeAccess, IEntityGoatPet{
	
	protected IGoatPet pet;
	private final INMSEntityPetBase petBase;
	
	private static final ImmutableList<SensorType<? extends Sensor<? super Goat>>> SENSOR_TYPES = ImmutableList.of();
	
	public EntityGoatPet(Level world, IGoatPet pet){
		super(EntityType.GOAT, world);
		this.pet = pet;
		this.petBase = new EntityAgeablePetBase(this);
	}
	
	@Override
	public void setScreaming(boolean flag){
		setScreamingGoat(flag);
	}
	// TODO: Maybe can implement some of goats custom AI?
	// Look at GoatAi. stuff like looking at sinks, randomly running, support long jumping? Probably not ramming but idk
	
	@Override
	protected Brain.Provider<Goat> brainProvider(){
		// Use goat memory types because other goats will try to access it usually.
		// Empty sensors tho because we don't need that handling.
		return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
	}
	
	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic){
		// This info is put into a GoatAI which adds some other things like ramming, long jump, etc
		return brainProvider().makeBrain(dynamic);
	}
	
	@Override
	protected void customServerAiStep(){
		// disable brain / goat specific ai
	}
	
	@Override
	public @Nullable Goat getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob){
		return null;
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
	
	@Override
	public LivingEntity getEntity(){
		return (LivingEntity) getBukkitEntity();
	}
	
	@Override
	public void remove(boolean makeSound){
		petBase.remove(makeSound);
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
	public boolean onInteract(Player player){
		return petBase.onInteract(player);
	}
	
	@Override
	public IPet getPet(){
		return pet;
	}
	
	@Override
	public IEntityPetBase getHandle(){
		return petBase;
	}
	
	@Override
	public IPetGoalSelector getPetGoalSelector(){
		return petBase.getPetGoalSelector();
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
		petBase.tick();
	}
	
	@Override
	public void travel(Vec3 vec3d){
		Vec3 result = petBase.travel(vec3d);
		if(result == null){
			this.flyingSpeed = 0.02F;
			super.travel(vec3d);
			return;
		}
		setSpeed(petBase.getSpeed());
		super.travel(result);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound){}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbttagcompound){}
}
