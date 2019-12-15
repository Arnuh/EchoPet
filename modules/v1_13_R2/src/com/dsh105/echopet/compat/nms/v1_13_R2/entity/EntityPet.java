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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dsh105.echopet.compat.nms.v1_13_R2.entity;

import java.lang.reflect.Field;
import java.util.Random;
import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.ai.PetGoalSelector;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.event.PetAttackEvent;
import com.dsh105.echopet.compat.api.event.PetRideJumpEvent;
import com.dsh105.echopet.compat.api.event.PetRideMoveEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Logger;
import com.dsh105.echopet.compat.api.util.Perm;
import com.dsh105.echopet.compat.api.util.menu.PetMenu;
import com.dsh105.echopet.compat.nms.v1_13_R2.NMSEntityUtil;
import com.dsh105.echopet.compat.nms.v1_13_R2.entity.ai.PetGoalFloat;
import com.dsh105.echopet.compat.nms.v1_13_R2.entity.ai.PetGoalFollowOwner;
import com.dsh105.echopet.compat.nms.v1_13_R2.entity.ai.PetGoalLookAtPlayer;
import net.minecraft.server.v1_13_R2.Block;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityTameableAnimal;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.EnumInteractionResult;
import net.minecraft.server.v1_13_R2.IAnimal;
import net.minecraft.server.v1_13_R2.IRegistry;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.SoundEffect;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class EntityPet extends EntityCreature implements IAnimal, IEntityPet{
	
	protected IPet pet;
	public PetGoalSelector petGoalSelector;
	protected Field FIELD_JUMP = null;
	protected double jumpHeight;
	protected float rideSpeed;
	public EntityLiving goalTarget = null;
	public boolean shouldVanish;
	
	public EntityPet(EntityTypes<? extends Entity> type, World world){
		super(type, world);
	}
	
	public EntityPet(EntityTypes<? extends Entity> type, World world, IPet pet){
		super(type, world);
		this.pet = pet;
		this.initiateEntityPet();
	}
	
	private void initiateEntityPet(){
		this.resetEntitySize();
		this.fireProof = true;
		if(this.FIELD_JUMP == null){
			try{
				this.FIELD_JUMP = EntityLiving.class.getDeclaredField("bg");// Usually right below lastDamage float. Has 3 floats after it. EntityLiving
				this.FIELD_JUMP.setAccessible(true);
			}catch(NoSuchFieldException e){
				e.printStackTrace();
			}
		}
		// this.getBukkitEntity().setMaxHealth(pet.getPetType().getMaxHealth());
		// this.setHealth((float) pet.getPetType().getMaxHealth());
		this.jumpHeight = EchoPet.getOptions().getRideJumpHeight(this.getPet().getPetType());
		this.rideSpeed = EchoPet.getOptions().getRideSpeed(this.getPet().getPetType());
		this.setPathfinding();
	}
	
	public PetType getEntityPetType(){
		EntityPetType entityPetType = this.getClass().getAnnotation(EntityPetType.class);
		if(entityPetType != null){
			return entityPetType.petType();
		}
		return null;
	}
	
	public void resizeBoundingBox(boolean flag){
		EntitySize es = this.getClass().getAnnotation(EntitySize.class);
		if(es != null){
			this.setSize(flag ? (es.width() / 2) : es.width(), flag ? (es.height() / 2) : es.height());
		}
	}
	
	public void resetEntitySize(){
		EntitySize es = this.getClass().getAnnotation(EntitySize.class);
		if(es != null){
			this.setSize(es.width(), es.height());
		}
	}
	
	public void setEntitySize(float width, float height){
		this.setSize(width, height);
	}
	
	public boolean isPersistent(){
		return true;
	}
	
	public IPet getPet(){
		return this.pet;
	}
	
	public Player getPlayerOwner(){
		return pet.getOwner();
	}
	
	public Location getLocation(){
		return this.pet.getLocation();
	}
	
	public void setVelocity(Vector vel){
		this.motX = vel.getX();
		this.motY = vel.getY();
		this.motZ = vel.getZ();
		this.velocityChanged = true;
	}
	
	public Random random(){
		return this.random;
	}
	
	public PetGoalSelector getPetGoalSelector(){
		return petGoalSelector;
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public void setShouldVanish(boolean flag){
		this.shouldVanish = flag;
	}
	
	public void setTarget(LivingEntity livingEntity){
		this.setGoalTarget(((CraftLivingEntity) livingEntity).getHandle());
	}
	
	public LivingEntity getTarget(){
		return (LivingEntity) this.getGoalTarget().getBukkitEntity();
	}
	
	public void setOwnerShoulderEntityLeft(){// Spigot has the worst naming for this
		releaseLeftShoulderEntity();
		NBTTagCompound nbt = new NBTTagCompound();// figure out why entities that aren't parrots don't show.
		nbt.setString("id", "minecraft:parrot");// getSaveID();
		nbt.setBoolean("echopet", true);
		save(nbt);
		((CraftPlayer) this.getPlayerOwner()).getHandle().setShoulderEntityLeft(nbt);
	}
	
	public void setOwnerShoulderEntityRight(){
		releaseRightShoulderEntity();
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("id", "minecraft:parrot");// getSaveID();
		nbt.setBoolean("echopet", true);
		save(nbt);
		((CraftPlayer) this.getPlayerOwner()).getHandle().setShoulderEntityRight(nbt);
	}
	
	private void releaseLeftShoulderEntity(){
		EntityPlayer player = ((CraftPlayer) this.getPlayerOwner()).getHandle();
		NBTTagCompound nbt = player.getShoulderEntityLeft();
		if(nbt != null && !nbt.isEmpty()){
			if(!nbt.hasKey("echopet")) spawnEntityFromShoulder(nbt);
			player.setShoulderEntityLeft(new NBTTagCompound());
		}
	}
	
	private void releaseRightShoulderEntity(){
		EntityPlayer player = ((CraftPlayer) this.getPlayerOwner()).getHandle();
		NBTTagCompound nbt = player.getShoulderEntityRight();
		if(nbt != null){
			if(!nbt.hasKey("echopet")) spawnEntityFromShoulder(nbt);
			player.setShoulderEntityRight(null);
		}
	}
	
	private boolean spawnEntityFromShoulder(@Nullable NBTTagCompound nbttagcompound){// copied from EntityHuman
		if((!this.world.isClientSide) && (!nbttagcompound.isEmpty())){
			Entity entity = EntityTypes.a(nbttagcompound, this.world);
			if((entity instanceof EntityTameableAnimal)){
				((EntityTameableAnimal) entity).setOwnerUUID(this.uniqueID);
			}
			entity.setPosition(this.locX, this.locY + 0.699999988079071D, this.locZ);
			return this.world.addEntity(entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SHOULDER_ENTITY);
		}
		return true;
	}
	
	public boolean attack(Entity entity, float damage){
		return this.attack(entity, DamageSource.mobAttack(this), damage);
	}
	
	public boolean attack(Entity entity, DamageSource damageSource, float damage){
		PetAttackEvent attackEvent = new PetAttackEvent(this.getPet(), entity.getBukkitEntity(), damage);
		EchoPet.getPlugin().getServer().getPluginManager().callEvent(attackEvent);
		if(!attackEvent.isCancelled()){
			if(entity instanceof EntityPlayer){
				if(!(EchoPet.getConfig().getBoolean("canAttackPlayers", false))){
					return false;
				}
			}
			return entity.damageEntity(damageSource, (float) attackEvent.getDamage());
		}
		return false;
	}
	
	public void setPathfinding(){
		try{
			NMSEntityUtil.clearGoals(this);
			petGoalSelector = new PetGoalSelector();
			petGoalSelector.addGoal(new PetGoalFloat(this), 0);
			petGoalSelector.addGoal(new PetGoalFollowOwner(this, this.getSizeCategory().getStartWalk(getPet().getPetType()), this.getSizeCategory().getStopWalk(getPet().getPetType()), this.getSizeCategory().getTeleport(getPet().getPetType())), 1);
			petGoalSelector.addGoal(new PetGoalLookAtPlayer(this, EntityHuman.class), 2);
		}catch(Exception e){
			e.printStackTrace();
			Logger.log(Logger.LogLevel.WARNING, "Could not add PetGoals to Pet AI.", e, true);
		}
	}
	
	public CraftCreature getBukkitEntity(){
		return (CraftCreature) super.getBukkitEntity();
	}
	
	// well then...it's now 'final'
	/*
	// Overriden from EntityInsentient - Most importantly overrides pathfinding selectors
	
	protected final void doTick() {
	    super.doTick();
	    ++this.ticksFarFromPlayer;
	
	    this.D();
	
	    this.getEntitySenses().a();
	
	    // If this ever happens...
	    if (this.petGoalSelector == null) {
	        this.remove(false);
	        return;
	    }
	    this.petGoalSelector.updateGoals();
	
	    this.navigation.k();
	
	    this.E();
	
	    this.getControllerMove().c();
	
	    this.getControllerLook().a();
	
	    this.getControllerJump().b();
	}
	*/
	public boolean onInteract(Player p){
		if(p.getUniqueId().equals(getPlayerOwner().getUniqueId())){
			// if (IdentUtil.areIdentical(p, getPlayerOwner())) {
			if(EchoPet.getConfig().getBoolean("pets." + getPet().getPetType().getConfigKeyName() + ".interactMenu", true) && Perm.BASE_MENU.hasPerm(this.getPlayerOwner(), false, false)){
				new PetMenu(getPet()).open(false);
			}
			return true;
		}
		return false;
	}
	
	public EnumInteractionResult a(EntityHuman human, Vec3D vec3d, ItemStack itemstack, EnumHand enumhand){
		return onInteract((Player) human.getBukkitEntity()) ? EnumInteractionResult.SUCCESS : EnumInteractionResult.FAIL;
	}
	
	public void setPositionRotation(double d0, double d1, double d2, float f, float f1){
		super.setPositionRotation(d0, d1, d2, f, f1);
	}
	
	public void setLocation(Location l){
		this.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		this.world = ((CraftWorld) l.getWorld()).getHandle();
	}
	
	public void teleport(Location l){
		this.getPet().getCraftPet().teleport(l);
	}
	
	public void remove(boolean makeSound){
		if(getBukkitEntity() != null){
			getBukkitEntity().leaveVehicle();
			getBukkitEntity().remove();
		}
		if(makeSound){
			SoundEffect sound = getSoundFromString(getDeathSound());
			if(sound != null){
				a(sound, 1.0F, 1.0F);// was makeSound in 1.8
				/*
				  public void a(SoundEffect soundeffect, float f, float f1)
				  {
				  if (!ad()) {
				  this.world.a(null, this.locX, this.locY, this.locZ, soundeffect, bz(), f, f1);
				  }
				  }
				 */
			}
		}
	}
	
	public void onLive(){
		if(this.pet == null){
			this.remove(false);
			return;
		}
		if(this.getPlayerOwner() == null || !this.getPlayerOwner().isOnline()){
			EchoPet.getManager().removePet(this.getPet(), true);
			return;
		}
		if(pet.isOwnerRiding() && this.passengers.size() == 0 && !pet.isOwnerInMountingProcess()){
			pet.ownerRidePet(false);
		}
		if(((CraftPlayer) this.getPlayerOwner()).getHandle().isInvisible() != this.isInvisible() && !this.shouldVanish){
			this.setInvisible(!this.isInvisible());
		}
		if(((CraftPlayer) this.getPlayerOwner()).getHandle().isSneaking() != this.isSneaking()){
			this.setSneaking(!this.isSneaking());
		}
		if(((CraftPlayer) this.getPlayerOwner()).getHandle().isSprinting() != this.isSprinting()){
			this.setSprinting(!this.isSprinting());
		}
		if(this.getPet().isHat()){
			// this.lastYaw = this.yaw = (this.getPet().getPetType() == PetType.ENDERDRAGON ? this.getPlayerOwner().getLocation().getYaw() - 180 : this.getPlayerOwner().getLocation().getYaw());
			this.lastYaw = this.yaw = this.getPlayerOwner().getLocation().getYaw();
		}
		if(this.getPlayerOwner().isFlying() && EchoPet.getOptions().canFly(this.getPet().getPetType())){
			// if(this.getEntityPetType() == PetType.VEX && !((IVexPet) this.getPet()).isPowered()) return;
			Location petLoc = this.getLocation();
			Location ownerLoc = this.getPlayerOwner().getLocation();
			Vector v = ownerLoc.toVector().subtract(petLoc.toVector());
			double x = v.getX();
			double y = v.getY();
			double z = v.getZ();
			Vector vo = this.getPlayerOwner().getLocation().getDirection();
			if(vo.getX() > 0){
				x -= 1.5;
			}else if(vo.getX() < 0){
				x += 1.5;
			}
			if(vo.getZ() > 0){
				z -= 1.5;
			}else if(vo.getZ() < 0){
				z += 1.5;
			}
			this.setVelocity(new Vector(x, y, z).normalize().multiply(0.3F));
		}
	}
	
	// EntityInsentient
	public void a(float sideMot, float forwMot, float upMot){// ITS IN ENTITY LIVING
		// bF() is passenger shit. Minecraft changed it from 1 passenger to a list
		if(passengers.isEmpty()){// search for passengers.isEmpty() in Entity
			this.Q = 0.5F;// Above noclip
			this.aU = 0.02F;// above killer in entity living
			super.a(sideMot, forwMot, upMot);
			return;
		}
		Entity passenger = passengers.get(0);
		if(passenger == null || !(passenger instanceof EntityHuman) || (passenger instanceof EntityHuman && ((EntityHuman) passenger).getBukkitEntity() != this.getPlayerOwner().getPlayer())){
			this.Q = 0.5F;
			this.aU = 0.02F;
			super.a(sideMot, forwMot, upMot);
			return;
		}
		this.yaw = passenger.yaw;
		this.lastYaw = this.yaw;
		this.pitch = passenger.pitch * 0.5F;
		this.setYawPitch(this.yaw, this.pitch);
		// aP look for 009999999776482582D in EntityLiving.
		// aR look for tickPotionEffects(). Middle one
		this.aR = this.aP = this.yaw;
		this.P = 1.0F;
		sideMot = ((EntityLiving) passenger).bh * 0.5F;// 1 below lastDamage in EntityLiving
		forwMot = ((EntityLiving) passenger).bi;// After ^
		upMot = ((EntityLiving) passenger).bj;
		if(forwMot <= 0.0F){// ?
			forwMot *= 0.25F;
		}
		sideMot *= 0.75F;
		PetRideMoveEvent moveEvent = new PetRideMoveEvent(this.getPet(), forwMot, sideMot);
		EchoPet.getPlugin().getServer().getPluginManager().callEvent(moveEvent);
		if(moveEvent.isCancelled()) return;
		/*
		 * Search for 'getBoolean("NoAI")'. in EntityInsentient few methods down
		  public void k(float f) {
		    super.k(f);
		    n(f);
		  }
		 */
		
		this.o(this.rideSpeed);// before "looting" methodProfiler
		super.a(sideMot, forwMot, upMot);
		PetType pt = this.getPet().getPetType();
		if(FIELD_JUMP != null && !passengers.isEmpty()){
			if(EchoPet.getOptions().canFly(pt)){
				// if(this.getEntityPetType() == PetType.VEX && !((IVexPet) this.getPet()).isPowered()) return;
				try{
					if(((Player) (passenger.getBukkitEntity())).isFlying()){
						((Player) (passenger.getBukkitEntity())).setFlying(false);
					}
					if(FIELD_JUMP.getBoolean(passenger)){
						PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
						EchoPet.getPlugin().getServer().getPluginManager().callEvent(rideEvent);
						if(!rideEvent.isCancelled()){
							this.motY = 0.5F;
						}
					}
				}catch(IllegalArgumentException e){
					Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Flying Motion for " + this.getPlayerOwner().getName() + "'s Pet.", e, true);
				}catch(IllegalAccessException e){
					Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Flying Motion for " + this.getPlayerOwner().getName() + "'s Pet.", e, true);
				}catch(IllegalStateException e){
					Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Flying Motion for " + this.getPlayerOwner().getName() + "'s Pet.", e, true);
				}
			}else if(this.onGround){
				try{
					if(FIELD_JUMP.getBoolean(passenger)){
						PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
						EchoPet.getPlugin().getServer().getPluginManager().callEvent(rideEvent);
						if(!rideEvent.isCancelled()){
							this.motY = rideEvent.getJumpHeight();
							doJumpAnimation();
						}
					}
				}catch(IllegalArgumentException e){
					Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Jumping Motion for " + this.getPlayerOwner().getName() + "'s Pet.", e, true);
				}catch(IllegalAccessException e){
					Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Jumping Motion for " + this.getPlayerOwner().getName() + "'s Pet.", e, true);
				}catch(IllegalStateException e){
					Logger.log(Logger.LogLevel.WARNING, "Failed to initiate Pet Jumping Motion for " + this.getPlayerOwner().getName() + "'s Pet.", e, true);
				}
			}
		}
	}
	
	protected SoundEffect F(){
		return getSoundFromString(getIdleSound());
	}
	
	protected SoundEffect d(DamageSource damageSource){
		return getSoundFromString(getHurtSound());
	}
	
	protected SoundEffect cd(){
		return getSoundFromString(getDeathSound());
	}
	
	protected SoundEffect dA(){// EntityRabbit has this, but it goes to jump?
		return getSoundFromString(getStepSound());
	}
	
	protected void a(BlockPosition blockposition, Block block){
		makeStepSound(blockposition, block);
	}
	
	protected void makeStepSound(BlockPosition blockposition, Block block){// Allows the ability to override and change the step sound
		makeSound(getStepSound(), 0.15F, 1.0F);
	}
	
	public void makeSound(String soundEffect, float f, float f1){
		SoundEffect se = getSoundFromString(soundEffect);
		if(se != null) a(se, f, f1);
		// Minecraft doesn't actually do a null check in the method.. we have to do one for them.
		// But minecraft does do a null check on entity SoundEffects(ambient, hurt, death)
	}
	
	public SoundEffect getSoundFromString(String soundName){
		return soundName != null ? IRegistry.SOUND_EVENT.get(new MinecraftKey(soundName)) : null;
		// mojang made this method private
		// return soundName != null ? SoundEffect.a.get(new MinecraftKey(soundName)) : null;
	}
	
	protected String getIdleSound(){
		return "entity." + pet.getPetType().getMinecraftName() + ".ambient";
	}
	
	protected String getHurtSound(){
		return "entity." + pet.getPetType().getMinecraftName() + ".hurt";
	}
	
	protected String getDeathSound(){
		return "entity." + pet.getPetType().getMinecraftName() + ".death";
	}
	
	protected String getStepSound(){
		return "entity." + pet.getPetType().getMinecraftName() + ".step";
	}
	
	public abstract SizeCategory getSizeCategory();
	
	// Entity
	public void W(){// Search for "entityBaseTick". The method its in.
		super.W();
		onLive();
		if(this.petGoalSelector == null){
			this.remove(false);
			return;
		}
		if(!isPassenger() || getPet().getRider() == null) this.petGoalSelector.updateGoals();
	}
	
	protected void x_(){// Registers all the values into datawatcher
		super.x_();
		initDatawatcher();
		// We don't need datawatcher stuff from EntityCreature, EntityInsentinent, or EntityLiving.
	}
	
	protected void initDatawatcher(){}
	
	protected void doJumpAnimation(){}
	
	@Override
	public boolean startRiding(Entity entity){
		return false;
	}
	
	
	public void a(NBTTagCompound nbttagcompound){
		// Do nothing with NBT
		// Pets should not be stored to world save files
		/*super.a(nbttagcompound);
		String owner = nbttagcompound.getString("EchoPet_OwnerName");
		PetType pt = this.getEntityPetType();
		if (pt != null) {
		    this.pet = pt.getNewPetInstance(owner, this);
		    if (this.pet != null) {
		        EchoPet.getManager().loadRiderFromFile(this.getPet());
		        this.initiateEntityPet();
		    }
		}*/
	}
	
	public void b(NBTTagCompound nbttagcompound){
		// Do nothing with NBT
		// Pets should not be stored to world save files
	}
	
	public boolean c(NBTTagCompound nbttagcompound){// Calls e
		// Do nothing with NBT
		// Pets should not be stored to world save files
		return false;
	}
	
	public boolean d(NBTTagCompound nbttagcompound){// Calls e
		// Do nothing with NBT
		// Pets should not be stored to world save files
		return false;
	}
	
	public NBTTagCompound e(NBTTagCompound nbttagcompound){// Saving
		// Do nothing with NBT
		// Pets should not be stored to world save files
		return nbttagcompound;
	}
	
	public void f(NBTTagCompound nbttagcompound){// Loading
		//
	}
}
