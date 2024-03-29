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

import java.util.List;
import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.nms.IEntityAgeablePet;
import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityPetHandle;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IAxolotlPet;
import com.dsh105.echopet.nms.entity.EntityPetGiveMeAccess;
import com.dsh105.echopet.nms.entity.INMSEntityPetHandle;
import com.dsh105.echopet.nms.entity.base.EntityAxolotlPetHandle;
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
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.entity.LivingEntity;

@EntityPetType(petType = PetType.AXOLOTL)
public class EntityAxolotlPet extends Axolotl implements IEntityAgeablePet, EntityPetGiveMeAccess{
	
	private static final List<? extends SensorType<? extends Sensor<? super Axolotl>>> SENSOR_TYPES = ImmutableList.of();
	
	protected IAxolotlPet pet;
	private final INMSEntityPetHandle petHandle;
	
	public EntityAxolotlPet(Level world, IAxolotlPet pet){
		super(EntityType.AXOLOTL, world);
		this.pet = pet;
		this.petHandle = new EntityAxolotlPetHandle(this);
	}
	
	// Prevent from making random noises outside of water?
	@Override
	protected void handleAirSupply(int i){}
	
	@Override
	public @Nullable AgeableMob getBreedOffspring(ServerLevel worldserver, AgeableMob entityageable){
		return null;
	}
	
	@Override
	protected void customServerAiStep(){}
	
	@Override
	protected Brain.Provider<Axolotl> brainProvider(){
		return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
	}
	
	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic){
		return this.brainProvider().makeBrain(dynamic);
	}
	
	@Override
	public void applySupportingEffects(net.minecraft.world.entity.player.Player entityhuman){}
	
	@Override
	public LivingEntity getEntity(){
		return (LivingEntity) getBukkitEntity();
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
