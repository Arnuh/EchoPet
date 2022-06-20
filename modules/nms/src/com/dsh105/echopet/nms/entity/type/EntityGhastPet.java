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
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGhastPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@EntitySize(width = 4.0F, height = 4.0F)
@EntityPetType(petType = PetType.GHAST)
public class EntityGhastPet extends EntityFlyingPet implements IEntityGhastPet{
	
	private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(EntityGhastPet.class, EntityDataSerializers.BOOLEAN);
	
	public EntityGhastPet(Level world){
		super(EntityType.GHAST, world);
		this.moveControl = new ControllerGhast(this);
	}
	
	public EntityGhastPet(Level world, IPet pet){
		super(EntityType.GHAST, world, pet);
		this.moveControl = new ControllerGhast(this);
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.OVERSIZE;
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_IS_CHARGING, false);
	}
	
	static class ControllerGhast extends MoveControl{
		
		private final EntityGhastPet ghast;
		private int floatDuration;
		
		public ControllerGhast(EntityGhastPet entityghast){
			super(entityghast);
			this.ghast = entityghast;
		}
		
		@Override
		public void tick(){
			if(this.operation == Operation.MOVE_TO && this.floatDuration-- <= 0){
				this.floatDuration += ghast.random().nextInt(5) + 2;
				Vec3 vec3d = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
				double d0 = vec3d.length();
				vec3d = vec3d.normalize();
				if(this.canReach(vec3d, Mth.ceil(d0))){
					this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(vec3d.scale(0.1D)));
				}else{
					this.operation = Operation.WAIT;
				}
			}
		}
		
		private boolean canReach(Vec3 vec3d, int i){
			AABB axisalignedbb = this.ghast.getBoundingBox();
			
			for(int j = 1; j < i; ++j){
				axisalignedbb = axisalignedbb.move(vec3d);
				if(!this.ghast.level.noCollision(this.ghast, axisalignedbb)){
					return false;
				}
			}
			
			return true;
		}
	}
}
