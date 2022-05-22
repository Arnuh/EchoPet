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

import java.util.EnumSet;
import com.dsh105.echopet.compat.api.ai.PetGoal;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPhantomPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPhantomPet;
import com.dsh105.echopet.nms.entity.ai.BiMoveControl;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;

@EntitySize(width = 0.9F, height = 0.5F)
@EntityPetType(petType = PetType.PHANTOM)
public class EntityPhantomPet extends EntityFlyingPet implements IEntityPhantomPet{
	
	// Changes size and damage
	private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(EntityPhantomPet.class, EntityDataSerializers.INT);
	private Vec3 moveTargetPoint = Vec3.ZERO;
	private BlockPos anchorPoint = BlockPos.ZERO;
	private AttackPhase attackPhase = AttackPhase.CIRCLE;
	private MoveControl originalMoveControl, flyMoveControl;
	private LookControl originalLookControl, flyLookControl;
	
	public EntityPhantomPet(Level world){
		super(EntityType.PHANTOM, world);
	}
	
	public EntityPhantomPet(Level world, IPet pet){
		super(EntityType.PHANTOM, world, pet);
		originalMoveControl = moveControl;
		originalLookControl = lookControl;
		flyMoveControl = new BiMoveControl(this, new PhantomMoveControl(this), originalMoveControl, Entity::isVehicle);
		flyLookControl = new PhantomLookControl(this);
	}
	
	@Override
	public void setPathfinding(){
		super.setPathfinding();
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(ID_SIZE, 0);
	}
	
	public void setPhantomSize(int i){
		this.entityData.set(ID_SIZE, Mth.clamp(i, 0, 64));
	}
	
	public int getPhantomSize(){
		return this.entityData.get(ID_SIZE);
	}
	
	@Override
	protected BodyRotationControl createBodyControl(){
		return new PhantomBodyRotationControl(this);
	}
	
	@Override
	public void setWandering(boolean flag){
		if(flag){
			useFlyTravel = true;
			petGoalSelector.removeAllGoals();
			petGoalSelector.addGoal(1, new PhantomAttackStrategyGoal());
			petGoalSelector.addGoal(3, new PhantomCircleAroundAnchorGoal());
			moveControl = flyMoveControl;
			lookControl = flyLookControl;
		}else{
			useFlyTravel = false;
			setPathfindingGoals();
			moveControl = originalMoveControl;
			lookControl = originalLookControl;
		}
	}
	
	private class PhantomBodyRotationControl extends BodyRotationControl{
		
		public PhantomBodyRotationControl(Mob mob){
			super(mob);
		}
		
		@Override
		public void clientTick(){
			yHeadRot = yBodyRot;
			yBodyRot = getYRot();
		}
	}
	
	private enum AttackPhase{
		CIRCLE,
		SWOOP
	}
	
	private class PhantomAttackStrategyGoal extends PetGoal{
		
		private final int sweepTickDelayStart, sweepTickNextMin, sweepTickNextRand;
		private final int minHeightOffset, randHeightOffset;
		
		private int nextSweepTick;
		
		PhantomAttackStrategyGoal(){
			setFlags(EnumSet.of(PetGoal.Flag.MOVE));
			this.sweepTickDelayStart = IPhantomPet.SWEEP_NEXT_START_DELAY.getConfigValue(PetType.PHANTOM);
			this.sweepTickNextMin = IPhantomPet.SWEEP_NEXT_MIN_DELAY.getConfigValue(PetType.PHANTOM);
			this.sweepTickNextRand = IPhantomPet.SWEEP_NEXT_RAND_DELAY.getConfigValue(PetType.PHANTOM);
			this.minHeightOffset = IPhantomPet.SWEEP_MIN_HEIGHT_OFFSET.getConfigValue(PetType.PHANTOM);
			this.randHeightOffset = IPhantomPet.SWEEP_RAND_HEIGHT_OFFSET.getConfigValue(PetType.PHANTOM);
		}
		
		@Override
		public boolean canUse(){
			return true;
		}
		
		@Override
		public void start(){
			this.nextSweepTick = this.adjustedTickDelay(sweepTickDelayStart);
			attackPhase = AttackPhase.CIRCLE;
			this.setAnchorAboveTarget();
		}
		
		@Override
		public void stop(){
			anchorPoint = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, anchorPoint).above(minHeightOffset + random.nextInt(randHeightOffset));
		}
		
		@Override
		public void tick(){
			if(attackPhase == AttackPhase.CIRCLE){
				--this.nextSweepTick;
				if(this.nextSweepTick <= 0){
					// attackPhase = AttackPhase.SWOOP;
					this.setAnchorAboveTarget();
					this.nextSweepTick = this.adjustedTickDelay((sweepTickNextMin + random.nextInt(sweepTickNextRand)) * 20);
					// playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + random.nextFloat() * 0.1F);
				}
			}
			
		}
		
		private void setAnchorAboveTarget(){
			Location loc = getOwner().getLocation();
			BlockPos pos = new BlockPos(loc.getX(), loc.getY(), loc.getZ());
			anchorPoint = pos.above(minHeightOffset + random.nextInt(randHeightOffset));
		}
	}
	
	private abstract class PhantomMoveTargetGoal extends PetGoal{
		
		public PhantomMoveTargetGoal(){
			setFlags(EnumSet.of(PetGoal.Flag.MOVE));
		}
		
		protected boolean touchingTarget(){
			return moveTargetPoint.distanceToSqr(getX(), getY(), getZ()) < 4.0;
		}
	}
	
	private class PhantomCircleAroundAnchorGoal extends PhantomMoveTargetGoal{
		
		private float angle;
		private float distance;
		private float height;
		private float clockwise;
		//
		private final float minDistance, maxRandDistance;
		private final float maxDistance, maxDistanceReset;
		private final float minHeight, maxRandHeight;
		
		PhantomCircleAroundAnchorGoal(){
			super();
			this.minDistance = IPhantomPet.CIRCLE_MIN_DISTANCE.getConfigValue(PetType.PHANTOM).floatValue();
			this.maxRandDistance = IPhantomPet.CIRCLE_MAX_RAND_DISTANCE.getConfigValue(PetType.PHANTOM).floatValue();
			this.maxDistance = IPhantomPet.CIRCLE_MAX_DISTANCE.getConfigValue(PetType.PHANTOM).floatValue();
			this.maxDistanceReset = IPhantomPet.CIRCLE_MAX_DISTANCE_RESET.getConfigValue(PetType.PHANTOM).floatValue();
			this.minHeight = IPhantomPet.CIRCLE_MIN_HEIGHT.getConfigValue(PetType.PHANTOM).floatValue();
			this.maxRandHeight = IPhantomPet.CIRCLE_MAX_RAND_HEIGHT.getConfigValue(PetType.PHANTOM).floatValue();
		}
		
		@Override
		public boolean canUse(){
			return getTarget() == null || attackPhase == AttackPhase.CIRCLE;
		}
		
		@Override
		public void start(){
			this.distance = minDistance + random.nextFloat() * maxRandDistance;
			this.height = minHeight + random.nextFloat() * maxRandHeight;
			this.clockwise = random.nextBoolean() ? 1.0F : -1.0F;
			this.selectNext();
		}
		
		@Override
		public void tick(){
			if(random.nextInt(this.adjustedTickDelay(350)) == 0){
				this.height = minHeight + random.nextFloat() * maxRandHeight;
			}
			
			if(random.nextInt(this.adjustedTickDelay(250)) == 0){
				++this.distance;
				if(this.distance > maxDistance){
					this.distance = maxDistanceReset;
					this.clockwise = -this.clockwise;
				}
			}
			
			if(random.nextInt(this.adjustedTickDelay(450)) == 0){
				this.angle = random.nextFloat() * 2.0F * (float) Math.PI;
				this.selectNext();
			}
			
			if(this.touchingTarget()){
				this.selectNext();
			}
			
			if(moveTargetPoint.y < getY() && !level.isEmptyBlock(blockPosition().below(1))){
				this.height = Math.max(1.0F, this.height);
				this.selectNext();
			}
			
			if(moveTargetPoint.y > getY() && !level.isEmptyBlock(blockPosition().above(1))){
				this.height = Math.min(-1.0F, this.height);
				this.selectNext();
			}
		}
		
		private void selectNext(){
			if(BlockPos.ZERO.equals(anchorPoint)){
				anchorPoint = blockPosition();
			}
			
			this.angle += this.clockwise * 15.0F * (float) (Math.PI / 180.0);
			moveTargetPoint = Vec3.atLowerCornerOf(anchorPoint).add(this.distance * Mth.cos(this.angle), minHeight + this.height, this.distance * Mth.sin(this.angle));
		}
	}
	
	private class PhantomMoveControl extends MoveControl{
		
		private float speed = 0.1F;
		
		public PhantomMoveControl(Mob entityinsentient){
			super(entityinsentient);
		}
		
		@Override
		public void tick(){
			if(horizontalCollision){
				setYRot(getYRot() + 180.0F);
				this.speed = 0.1F;
			}
			
			double d0 = moveTargetPoint.x - getX();
			double d1 = moveTargetPoint.y - getY();
			double d2 = moveTargetPoint.z - getZ();
			double d3 = Math.sqrt(d0 * d0 + d2 * d2);
			if(Math.abs(d3) > 1.0E-5F){
				double d4 = 1.0 - Math.abs(d1 * 0.7F) / d3;
				d0 *= d4;
				d2 *= d4;
				d3 = Math.sqrt(d0 * d0 + d2 * d2);
				double d5 = Math.sqrt(d0 * d0 + d2 * d2 + d1 * d1);
				float f = getYRot();
				float f1 = (float) Mth.atan2(d2, d0);
				float f2 = Mth.wrapDegrees(getYRot() + 90.0F);
				float f3 = Mth.wrapDegrees(f1 * 180.0F / (float) Math.PI);
				setYRot(Mth.approachDegrees(f2, f3, 4.0F) - 90.0F);
				yBodyRot = getYRot();
				if(Mth.degreesDifferenceAbs(f, getYRot()) < 3.0F){
					this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
				}else{
					this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
				}
				
				float f4 = (float) (-(Mth.atan2(-d1, d3) * 180.0F / (float) Math.PI));
				setXRot(f4);
				float f5 = getYRot() + 90.0F;
				double d6 = (double) (this.speed * Mth.cos(f5 * (float) (Math.PI / 180.0))) * Math.abs(d0 / d5);
				double d7 = (double) (this.speed * Mth.sin(f5 * (float) (Math.PI / 180.0))) * Math.abs(d2 / d5);
				double d8 = (double) (this.speed * Mth.sin(f4 * (float) (Math.PI / 180.0))) * Math.abs(d1 / d5);
				Vec3 vec3d = getDeltaMovement();
				setDeltaMovement(vec3d.add(new Vec3(d6, d8, d7).subtract(vec3d).scale(0.2)));
			}
		}
	}
	
	private class PhantomLookControl extends LookControl{
		
		public PhantomLookControl(Mob mob){
			super(mob);
		}
		
		@Override
		public void tick(){
		}
	}
}
