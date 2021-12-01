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

package com.dsh105.echopet.compat.nms.v1_18_R1.entity.type;

import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.ai.IPetGoalSelector;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVillagerAbstractPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVillagerDataHolder;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVillagerPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IVillagerPet;
import com.dsh105.echopet.compat.nms.v1_18_R1.entity.EntityPetBase;
import com.dsh105.echopet.compat.nms.v1_18_R1.entity.EntityPetHandle;
import com.dsh105.echopet.compat.nms.v1_18_R1.entity.IEntityPetBase;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.VILLAGER)
public class EntityVillagerPet extends Villager implements IEntityPet, EntityPetHandle, IEntityVillagerAbstractPet, IEntityVillagerPet, IEntityVillagerDataHolder{
	
	protected IVillagerPet pet;
	private final IEntityPetBase petBase;
	private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, MemoryModuleType.MEETING_POINT, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.WALK_TARGET, new MemoryModuleType[]{MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN, MemoryModuleType.LAST_WORKED_AT_POI, MemoryModuleType.GOLEM_DETECTED_RECENTLY});
	
	public EntityVillagerPet(Level world, IVillagerPet pet){
		super(EntityType.VILLAGER, world);
		this.pet = pet;
		this.petBase = new EntityPetBase(this);
	}
	
	@Override
	public void setProfession(int i){
		try{
			setVillagerData(getVillagerData().setProfession((VillagerProfession) VillagerProfession.class.getFields()[i].get(null)));
		}catch(Exception ignored){
		}
	}
	
	@Override
	public void setType(int type){
		try{
			setVillagerData(getVillagerData().setType((VillagerType) VillagerType.class.getFields()[type].get(null)));
		}catch(Exception ignored){
		}
	}
	
	@Override
	public void setLevel(int level){
		setVillagerData(getVillagerData().setLevel(level));
	}
	
	@Override
	public @Nullable
	Villager getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob){
		return null;
	}
	
	@Override
	protected void customServerAiStep(){}
	
	@Override
	protected Brain.Provider<Villager> brainProvider(){
		// Other Villagers depend on our villager having memories and don't properly null check.
		return Brain.provider(MEMORY_TYPES, ImmutableList.of());
	}
	
	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic){
		return this.brainProvider().makeBrain(dynamic);
	}
	
	@Override
	public void refreshBrain(ServerLevel worldserver){
		//
	}
	
	@Override
	public void thunderHit(ServerLevel worldserver, LightningBolt entitylightning){
		//
	}
	
	// Below I'm not sure are needed but just incase.
	@Override
	public boolean wantsToPickUp(ItemStack itemstack){
		return false;
	}
	
	@Override
	public boolean wantsToSpawnGolem(long i){
		return false;
	}
	
	@Override
	protected void updateTrades(){
		//
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