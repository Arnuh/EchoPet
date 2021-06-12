/*
 * This file is part of EchoPet.
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.nms.v1_17_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityRabbitPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityAgeablePet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.ai.PetGoalFollowOwner;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.bukkit.entity.Rabbit;

@EntitySize(width = 0.6F, height = 0.7F)
@EntityPetType(petType = PetType.RABBIT)
public class EntityRabbitPet extends EntityAgeablePet implements IEntityRabbitPet{
	
	private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(EntityRabbitPet.class, EntityDataSerializers.INT);
	private int jumpTicks;
	private int jumpDuration;
	private boolean wasOnGround;
	private int jumpDelayTicks;
	
	public EntityRabbitPet(Level world){
		super(EntityType.RABBIT, world);
		this.jumpControl = new ControllerJumpRabbit(this);
	}
	
	public EntityRabbitPet(Level world, IPet pet){
		super(EntityType.RABBIT, world, pet);
		this.jumpControl = new ControllerJumpRabbit(this);
	}
	
	@Override
	public Rabbit.Type getRabbitType(){
		return TypeMapping.fromMagic(this.entityData.get(TYPE));
	}
	
	@Override
	public void setRabbitType(Rabbit.Type type){
		this.entityData.set(TYPE, TypeMapping.toMagic(type));
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(TYPE, Integer.valueOf(0));
	}
	
	public void setJumping(boolean flag){
		super.setJumping(flag);
		if(flag){
			this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
		}
	}
	
	public void startJumping(){
		this.setJumping(true);
		this.jumpDuration = 10;
		this.jumpTicks = 0;
	}
	
	@Override
	public void customServerAiStep(){
		super.customServerAiStep();
		if(this.jumpDelayTicks > 0){
			this.jumpDelayTicks -= 1;
		}
		if(this.onGround){
			if(!this.wasOnGround){
				this.setJumping(false);
				this.checkLandingDelay();
			}
			ControllerJumpRabbit jumpController = (ControllerJumpRabbit) getJumpControl();
			if(!jumpController.wantJump()){
				if(this.moveControl.hasWanted() && this.jumpDelayTicks == 0){
					Path pathentity = ((PetGoalFollowOwner) petGoalSelector.getGoal("FollowOwner")).getNavigation().getPath();// Gets path towards the player.
					// if(pathentity != null && pathentity.e() < pathentity.d()){
					// Vec3D vec3d = pathentity.a(this);
					// a(vec3d.x, vec3d.z);
					// dl();
					// }
					Vec3 vec3d = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
					if(pathentity != null && !pathentity.isDone()){
						vec3d = pathentity.getNextEntityPos(this);
					}
					this.facePoint(vec3d.x, vec3d.z);
					this.startJumping();
				}
			}else if(!jumpController.canJump()){
				this.enableJumpControl();
			}
		}
		this.wasOnGround = this.onGround;
	}
	
	private void facePoint(double d0, double d1){
		this.setYRot((float) (Mth.atan2(d1 - this.getZ(), d0 - this.getX()) * 57.2957763671875D) - 90.0F);
	}
	
	private void enableJumpControl(){
		((ControllerJumpRabbit) this.jumpControl).setCanJump(true);
	}
	
	private void disableJumpControl(){
		((ControllerJumpRabbit) this.jumpControl).setCanJump(false);
	}
	
	private void setLandingDelay(){
		if(this.moveControl.getSpeedModifier() < 2.2D){
			this.jumpDelayTicks = 10;
		}else{
			this.jumpDelayTicks = 1;
		}
	}
	
	private void checkLandingDelay(){
		this.setLandingDelay();
		this.disableJumpControl();
	}
	
	public void aiStep(){
		super.aiStep();
		if(this.jumpTicks != this.jumpDuration){
			++this.jumpTicks;
		}else if(this.jumpDuration != 0){
			this.jumpTicks = 0;
			this.jumpDuration = 0;
			this.setJumping(false);
		}
	}
	
	protected SoundEvent getJumpSound(){
		return SoundEvents.RABBIT_JUMP;
	}
	
	public class ControllerJumpRabbit extends JumpControl{// Copied from EntityRabbit
		
		private final EntityRabbitPet rabbit;
		private boolean canJump;
		
		public ControllerJumpRabbit(EntityRabbitPet entityrabbit){
			super(entityrabbit);
			this.rabbit = entityrabbit;
		}
		
		public boolean wantJump(){
			return this.jump;
		}
		
		public boolean canJump(){
			return this.canJump;
		}
		
		public void setCanJump(boolean flag){
			this.canJump = flag;
		}
		
		public void tick(){
			if(this.jump){
				this.rabbit.startJumping();
				this.jump = false;
			}
			
		}
	}
	
	static class TypeMapping{
		
		private static final int[] NMS_TYPES = new int[Rabbit.Type.values().length];
		private static final Rabbit.Type[] INVERSE = new Rabbit.Type[Rabbit.Type.values().length];
		
		static{
			set(Rabbit.Type.BROWN, 0);
			set(Rabbit.Type.WHITE, 1);
			set(Rabbit.Type.BLACK, 2);
			set(Rabbit.Type.BLACK_AND_WHITE, 3);
			set(Rabbit.Type.GOLD, 4);
			set(Rabbit.Type.SALT_AND_PEPPER, 5);
			set(Rabbit.Type.THE_KILLER_BUNNY, 99);
		}
		
		private static void set(Rabbit.Type type, int magicValue){
			NMS_TYPES[type.ordinal()] = magicValue;
			if(magicValue < INVERSE.length){
				INVERSE[magicValue] = type;
			}
		}
		
		protected static Rabbit.Type fromMagic(int magicValue){
			if(magicValue < INVERSE.length){
				return INVERSE[magicValue];
			}else if(magicValue == 99){
				return Rabbit.Type.THE_KILLER_BUNNY;
			}
			// a default
			return Rabbit.Type.BROWN;
		}
		
		protected static int toMagic(Rabbit.Type type){
			return NMS_TYPES[type.ordinal()];
		}
	}
}
