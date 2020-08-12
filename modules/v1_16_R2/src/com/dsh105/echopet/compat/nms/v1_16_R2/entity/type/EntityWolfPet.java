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
package com.dsh105.echopet.compat.nms.v1_16_R2.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityWolfPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IWolfPet;
import com.dsh105.echopet.compat.nms.v1_16_R2.entity.EntityTameablePet;
import net.minecraft.server.v1_16_R2.DataWatcher;
import net.minecraft.server.v1_16_R2.DataWatcherObject;
import net.minecraft.server.v1_16_R2.DataWatcherRegistry;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.EnumColor;
import net.minecraft.server.v1_16_R2.MathHelper;
import net.minecraft.server.v1_16_R2.Particles;
import net.minecraft.server.v1_16_R2.Vec3D;
import net.minecraft.server.v1_16_R2.World;
import org.bukkit.DyeColor;

@EntitySize(width = 0.6F, height = 0.8F)
@EntityPetType(petType = PetType.WOLF)
public class EntityWolfPet extends EntityTameablePet implements IEntityWolfPet{
	
	private static final DataWatcherObject<Boolean> bA = DataWatcher.a(EntityWolfPet.class, DataWatcherRegistry.i);// ??
	private static final DataWatcherObject<Integer> COLLAR_COLOR = DataWatcher.a(EntityWolfPet.class, DataWatcherRegistry.b);
	private boolean wet;
	private boolean shaking;
	private float shakeCount;
	
	public EntityWolfPet(World world){
		super(EntityTypes.WOLF, world);
	}
	
	public EntityWolfPet(World world, IPet pet){
		super(EntityTypes.WOLF, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(bA, false);
		this.datawatcher.register(COLLAR_COLOR, EnumColor.RED.getColorIndex());
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
			this.datawatcher.set(COLLAR_COLOR, EnumColor.fromColorIndex(dc.getWoolData()).getColorIndex());
		}
	}
	
	@Override
	public void onLive(){
		super.onLive();
		if(this.inWater){
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
				Vec3D mot = getMot();
				for(int j = 0; j < i; ++j){
					float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
					float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.getWidth() * 0.5F;
					this.world.addParticle(Particles.SPLASH, locX() + (double) f1, f + 0.8F, this.locZ() + (double) f2, mot.x, mot.y, mot.z);
				}
			}
		}
	}
	
	@Override
	protected String getIdleSound(){
		return this.random.nextInt(3) == 0 ? "entity.wolf.pant" : (isTamed() && getHealth() < 10.0F) ? "entity.wolf.whine" : isAngry() ? "entity.wolf.growl" : "entity.wolf.ambient";
	}
}