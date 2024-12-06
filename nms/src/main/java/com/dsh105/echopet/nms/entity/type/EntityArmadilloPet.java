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
import com.dsh105.echopet.compat.api.entity.type.pet.IArmadilloPet;
import com.dsh105.echopet.nms.VersionBreaking;
import com.dsh105.echopet.nms.entity.EntityPetGiveMeAccess;
import com.dsh105.echopet.nms.entity.INMSEntityPetHandle;
import com.dsh105.echopet.nms.entity.ai.brain.PetArmadilloAi;
import com.dsh105.echopet.nms.entity.handle.EntityArmadilloPetHandle;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.LivingEntity;

@EntityPetType(petType = PetType.ARMADILLO)
public class EntityArmadilloPet extends Armadillo implements IEntityAnimalPet, EntityPetGiveMeAccess{
	
	protected final IArmadilloPet pet;
	private final INMSEntityPetHandle petHandle;
	
	public EntityArmadilloPet(Level world, IArmadilloPet pet){
		super(EntityType.ARMADILLO, world);
		this.pet = pet;
		this.petHandle = new EntityArmadilloPetHandle(this);
	}
	
	protected Brain.Provider<EntityArmadilloPet> petBrainProvider(){
		return PetArmadilloAi.brainProvider();
	}
	
	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic){
		if(usesBrain()){
			return PetArmadilloAi.makeBrain(petBrainProvider().makeBrain(dynamic));
		}else{
			return petBrainProvider().makeBrain(dynamic);
		}
	}
	
	@Override
	protected void customServerAiStep(ServerLevel world){
		if(!usesBrain()){
			return;
		}
		if(isVehicle()){
			return;
		}
		ProfilerFiller profilerFiller = Profiler.get();
		
		profilerFiller.push("armadilloBrain");
		((Brain<EntityArmadilloPet>) this.brain).tick(world, this); // CraftBukkit - decompile error
		profilerFiller.pop();
		profilerFiller.push("armadilloActivityUpdate");
		PetArmadilloAi.updateActivity(this);
		profilerFiller.pop();
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
	public float getMaxUpStep(){
		return maxUpStep();
	}
	
	@Override
	public void setLocation(Location location){
		this.absMoveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		setLevel(((CraftWorld) location.getWorld()).getHandle());
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
	
	@Override
	public boolean shouldBeSaved(){
		return false;
	}
}
