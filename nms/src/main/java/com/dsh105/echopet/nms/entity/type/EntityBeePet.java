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
import com.dsh105.echopet.compat.api.entity.nms.IEntityAgeablePet;
import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityPetHandle;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IBeePet;
import com.dsh105.echopet.nms.VersionBreaking;
import com.dsh105.echopet.nms.entity.EntityPetGiveMeAccess;
import com.dsh105.echopet.nms.entity.INMSEntityPetHandle;
import com.dsh105.echopet.nms.entity.ai.BiMoveControl;
import com.dsh105.echopet.nms.entity.handle.EntityBeePetHandle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;

@EntityPetType(petType = PetType.BEE)
public class EntityBeePet extends Bee implements IEntityAgeablePet, EntityPetGiveMeAccess{
	
	protected IBeePet pet;
	private final INMSEntityPetHandle petHandle;
	
	public EntityBeePet(Level world, IBeePet pet){
		super(EntityType.BEE, world);
		this.pet = pet;
		this.petHandle = new EntityBeePetHandle(this);
		this.moveControl = new BiMoveControl(this, new FlyingMoveControl(this, 20, true), new MoveControl(this), Entity::isVehicle);
		this.navigation = createNavigation(world);
	}
	
	@Override
	protected PathNavigation createNavigation(Level level){
		FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
		nav.setCanOpenDoors(false);
		nav.setCanFloat(true);
		nav.setCanPassDoors(true);
		return nav;
	}
	
	@Override
	public void updatePersistentAnger(ServerLevel worldserver, boolean flag){
		//
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