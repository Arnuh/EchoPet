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
import com.dsh105.echopet.compat.api.ai.IPetGoalSelector;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.nms.IEntityLivingPet;
import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityPetHandle;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IAllayPet;
import com.dsh105.echopet.nms.entity.EntityPetGiveMeAccess;
import com.dsh105.echopet.nms.entity.INMSLivingEntityPetHandle;
import com.dsh105.echopet.nms.entity.ai.BiMoveControl;
import com.dsh105.echopet.nms.entity.base.LivingEntityPetHandle;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.ALLAY)
public class EntityAllayPet extends Allay implements IEntityLivingPet, EntityPetGiveMeAccess{
	
	private static final List<? extends SensorType<? extends Sensor<? super Allay>>> SENSOR_TYPES = ImmutableList.of();
	
	protected IAllayPet pet;
	private final INMSLivingEntityPetHandle petHandle;
	
	public EntityAllayPet(Level world, IAllayPet pet){
		super(EntityType.ALLAY, world);
		this.pet = pet;
		this.petHandle = new LivingEntityPetHandle(this);
		setCanPickUpLoot(false);
		moveControl = new BiMoveControl(this, moveControl, new MoveControl(this), Entity::isVehicle);
	}
	
	// Make the entity dumb since it's a pet now.
	
	@Override
	protected Brain.Provider<Allay> brainProvider(){
		return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
	}
	
	@Override
	protected Brain<?> makeBrain(Dynamic<?> var0){
		return this.brainProvider().makeBrain(var0);
	}
	
	@Override
	protected void customServerAiStep(){}
	
	@Override
	public boolean canPickUpLoot(){
		return false;
	}
	
	@Override
	public boolean canTakeItem(ItemStack var0){
		return false;
	}
	
	@Override
	protected InteractionResult mobInteract(net.minecraft.world.entity.player.Player var0, InteractionHand var1){
		return InteractionResult.PASS;
	}
	
	@Override
	public boolean wantsToPickUp(ItemStack var0){
		return false;
	}
	
	@Override
	protected void dropEquipment(){}
	
	public boolean shouldListen(ServerLevel var0, GameEventListener var1, BlockPos var2, GameEvent var3, GameEvent.Context var4){
		return false;
	}
	
	public void onSignalReceive(ServerLevel var0, GameEventListener var1, BlockPos var2, GameEvent var3, @Nullable Entity var4, @Nullable Entity var5, float var6){}
	
	@Override
	public boolean canHoldItem(ItemStack itemstack){
		return false;
	}
	
	public ItemStack equipItemIfPossible(ItemStack itemstack, ItemEntity entityitem){
		return null;
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
			calculateEntityAnimation(this, false);
			return;
		}
		setSpeed(petHandle.getSpeed());
		petHandle.originalTravel(this, result);
		calculateEntityAnimation(this, false);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound){}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbttagcompound){}
}
