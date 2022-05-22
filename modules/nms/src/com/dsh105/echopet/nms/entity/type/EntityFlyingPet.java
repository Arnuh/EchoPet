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

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityFishPet;
import com.dsh105.echopet.nms.entity.EntityPet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class EntityFlyingPet extends EntityPet implements IEntityFishPet{
	
	protected boolean useFlyTravel = false;
	
	public EntityFlyingPet(EntityType<? extends Mob> type, Level world){
		super(type, world);
	}
	
	public EntityFlyingPet(EntityType<? extends Mob> type, Level world, IPet pet){
		super(type, world, pet);
	}
	
	@Override
	public void travel(Vec3 var0){
		if(isVehicle() || !useFlyTravel){
			super.travel(var0);
			return;
		}
		if(this.isInWater()){
			this.moveRelative(0.02F, var0);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
		}else if(this.isInLava()){
			this.moveRelative(0.02F, var0);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
		}else{
			float var1 = 0.91F;
			if(this.onGround){
				var1 = this.level.getBlockState(new BlockPos(this.getX(), this.getY() - 1.0, this.getZ())).getBlock().getFriction() * 0.91F;
			}
			
			float var2 = 0.16277137F / (var1 * var1 * var1);
			var1 = 0.91F;
			if(this.onGround){
				var1 = this.level.getBlockState(new BlockPos(this.getX(), this.getY() - 1.0, this.getZ())).getBlock().getFriction() * 0.91F;
			}
			
			this.moveRelative(this.onGround ? 0.1F * var2 : 0.02F, var0);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(var1));
		}
		
		this.calculateEntityAnimation(this, false);
	}
}
