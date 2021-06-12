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
package com.dsh105.echopet.compat.nms.v1_17_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWolfPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IWolfPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityTameablePet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.royawesome.jlibnoise.MathHelper;
import org.bukkit.DyeColor;

@EntitySize(width = 0.6F, height = 0.8F)
@EntityPetType(petType = PetType.WOLF)
public class EntityWolfPet extends EntityTameablePet implements IEntityWolfPet{
	
	private static final EntityDataAccessor<Boolean> DATA_INTERESTED_ID = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.INT);
	private boolean wet;
	private boolean shaking;
	private float shakeCount;
	
	public EntityWolfPet(Level world){
		super(EntityType.WOLF, world);
	}
	
	public EntityWolfPet(Level world, IPet pet){
		super(EntityType.WOLF, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_INTERESTED_ID, false);
		this.entityData.define(DATA_COLLAR_COLOR, net.minecraft.world.item.DyeColor.RED.getId());
		this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
	}
	
	@Override
	public void setAngry(boolean angry){
		if(isTamed() && angry){
			setTamed(false);
		}
		if(angry) addFlag(Angry);
		else removeFlag(Angry);
	}
	
	@Override
	public void setTamed(boolean tamed){
		if(isAngry() && tamed){
			setAngry(false);
		}
		super.setTamed(tamed);
	}
	
	public boolean isAngry(){
		return (getFlag() & Angry) != 0;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setCollarColor(DyeColor dc){
		if(((IWolfPet) pet).isTamed()){
			this.entityData.set(DATA_COLLAR_COLOR, net.minecraft.world.item.DyeColor.byId(dc.getWoolData()).getId());
		}
	}
	
	@Override
	public void onLive(){
		super.onLive();
		if(this.isInWater()){
			this.wet = true;
			this.shaking = false;
			this.shakeCount = 0.0F;
		}else if((this.wet || this.shaking) && this.shaking){
			if(this.shakeCount == 0.0F){
				// After sounds
				makeSound("entity.wolf.shake", getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);// just search for '0.2F + 1.0F'
			}
			this.shakeCount += 0.05F;
			if(this.shakeCount - 0.05F >= 2.0F){
				this.wet = false;
				this.shaking = false;
				this.shakeCount = 0.0F;
			}
			if(this.shakeCount > 0.4F){
				float f = (float) this.getBoundingBox().minY;
				int i = (int) (MathHelper.sin((this.shakeCount - 0.4F) * 3.1415927F) * 7.0F);
				Vec3 mot = getDeltaMovement();
				for(int j = 0; j < i; ++j){
					float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
					float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getBbWidth() * 0.5F;
					this.level.addParticle(ParticleTypes.SPLASH, getX() + (double) f1, f + 0.8F, this.getZ() + (double) f2, mot.x, mot.y, mot.z);
				}
			}
		}
	}
	
	@Override
	protected String getAmbientSoundString(){
		return this.random.nextInt(3) == 0 ? "entity.wolf.pant" : (isTamed() && getHealth() < 10.0F) ? "entity.wolf.whine" : isAngry() ? "entity.wolf.growl" : "entity.wolf.ambient";
	}
}