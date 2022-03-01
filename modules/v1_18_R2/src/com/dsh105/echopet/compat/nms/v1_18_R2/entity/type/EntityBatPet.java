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

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityBatPet;
import com.dsh105.echopet.compat.nms.v1_18_R2.entity.EntityPet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;

@EntitySize(width = 0.5F, height = 0.9F)
@EntityPetType(petType = PetType.BAT)
public class EntityBatPet extends EntityPet implements IEntityBatPet{
	
	private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(EntityBatPet.class, EntityDataSerializers.BYTE);
	private static final int FLAG_RESTING = 0x1;
	
	private boolean wandering;
	private BlockPos targetPosition;
	private int flyRange = 7;
	
	public EntityBatPet(Level world){
		super(EntityType.BAT, world);
	}
	
	public EntityBatPet(Level world, IPet pet){
		super(EntityType.BAT, world, pet);
		double sizeModifier = getSizeCategory().getModifier();
		flyRange = (int) Math.max(1, Math.ceil(pet.getPetType().getStartFollowDistance() * sizeModifier));
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_ID_FLAGS, (byte) 0);
	}
	
	@Override
	public SoundEvent getAmbientSound(){
		return this.isResting() && this.random.nextInt(4) != 0 ? null : SoundEvents.BAT_AMBIENT;
	}
	
	@Override
	public void tick(){
		super.tick();
		if(this.isResting()){
			this.setDeltaMovement(Vec3.ZERO);
			this.setPosRaw(this.getX(), (double) Mth.floor(this.getY()) + 1.0D - (double) this.getBbHeight(), this.getZ());
		}else{
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
		}
	}
	
	@Override
	protected void customServerAiStep(){
		super.customServerAiStep();
		if(!wandering){
			return;
		}
		BlockPos blockposition = this.blockPosition();
		BlockPos blockposition1 = blockposition.above();
		if(isResting()){
			boolean flag = this.isSilent();
			if(this.level.getBlockState(blockposition1).isRedstoneConductor(this.level, blockposition)){
				if(this.random.nextInt(200) == 0){
					this.yHeadRot = (float) this.random.nextInt(360);
				}
			}else{
				this.setResting(false);
				if(!flag){
					this.level.levelEvent(null, 1025, blockposition, 0);
				}
			}
		}else{
			if(this.targetPosition != null && (!this.level.isEmptyBlock(this.targetPosition) || this.targetPosition.getY() <= this.level.getMinBuildHeight())){
				this.targetPosition = null;
			}
			
			Location ownerLoc = getOwner().getLocation();
			
			// closerThan squares it internally
			// I think it checking if its too close to the player is better.
			ServerPlayer owner = ((CraftPlayer) getOwner()).getHandle();
			if(this.targetPosition == null || this.random.nextInt(30) == 0 || this.targetPosition.closerToCenterThan(owner.position(), 2.0D)){
				// Use to be off mob x,y,z but he just tries to fly away constantly.
				this.targetPosition = new BlockPos(ownerLoc.getX() + random.nextInt(flyRange) - random.nextInt(flyRange), ownerLoc.getY() + random.nextInt(flyRange - 1) - 2.0D, ownerLoc.getZ() + this.random.nextInt(flyRange) - this.random.nextInt(flyRange));
			}
			
			double d0 = (double) this.targetPosition.getX() + 0.5D - this.getX();// Should these values be off the player loc
			double d1 = (double) this.targetPosition.getY() + 0.1D - this.getY();
			double d2 = (double) this.targetPosition.getZ() + 0.5D - this.getZ();
			Vec3 vec3d = this.getDeltaMovement();
			Vec3 vec3d1 = vec3d.add((Math.signum(d0) * 0.5D - vec3d.x) * 0.10000000149011612D, (Math.signum(d1) * 0.699999988079071D - vec3d.y) * 0.10000000149011612D, (Math.signum(d2) * 0.5D - vec3d.z) * 0.10000000149011612D);
			this.setDeltaMovement(vec3d1);
			float f = (float) (Mth.atan2(vec3d1.z, vec3d1.x) * 57.2957763671875D) - 90.0F;
			float f1 = Mth.wrapDegrees(f - this.getYRot());
			this.zza = 0.5F;
			this.setYRot(this.getYRot() + f1);
			if(this.random.nextInt(100) == 0 && this.level.getBlockState(blockposition1).isRedstoneConductor(this.level, blockposition1)){
				this.setResting(true);
			}
		}
	}
	
	public boolean isResting(){
		return (this.entityData.get(DATA_ID_FLAGS) & FLAG_RESTING) != 0;
	}
	
	public void setResting(boolean flag){
		int i = this.entityData.get(DATA_ID_FLAGS);
		if(flag){
			this.entityData.set(DATA_ID_FLAGS, (byte) (i | 0x1));
		}else{
			this.entityData.set(DATA_ID_FLAGS, (byte) (i & ~0x1));
		}
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
	
	@Override
	public void setWandering(boolean flag){
		this.wandering = flag;
	}
}
