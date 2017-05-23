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
package com.dsh105.echopet.compat.nms.v1_10_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGhastPet;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityPet;

import net.minecraft.server.v1_10_R1.AxisAlignedBB;
import net.minecraft.server.v1_10_R1.ControllerMove;
import net.minecraft.server.v1_10_R1.MathHelper;
import net.minecraft.server.v1_10_R1.World;

@EntitySize(width = 4.0F, height = 4.0F)
@EntityPetType(petType = PetType.GHAST)
public class EntityGhastPet extends EntityPet implements IEntityGhastPet{

	public EntityGhastPet(World world){
		super(world);
		this.moveController = new ControllerGhast(this);
	}

	public EntityGhastPet(World world, IPet pet){
		super(world, pet);
		this.moveController = new ControllerGhast(this);
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.OVERSIZE;
	}

	static class ControllerGhast extends ControllerMove{

		private final EntityPet i;
		private int j;

		public ControllerGhast(EntityPet entityghast){
			super(entityghast);
			this.i = entityghast;
		}

		public void c(){
			if(this.h == ControllerMove.Operation.MOVE_TO){
				double d0 = this.b - this.i.locX;
				double d1 = this.c - this.i.locY;
				double d2 = this.d - this.i.locZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				if(this.j-- <= 0){
					this.j += this.i.getRandom().nextInt(5) + 2;
					d3 = MathHelper.sqrt(d3);
					if(b(this.b, this.c, this.d, d3)){
						this.i.motX += d0 / d3 * 0.1D;
						this.i.motY += d1 / d3 * 0.1D;
						this.i.motZ += d2 / d3 * 0.1D;
					}else{
						this.h = ControllerMove.Operation.WAIT;
					}
				}
			}
		}

		private boolean b(double d0, double d1, double d2, double d3){
			double d4 = (d0 - this.i.locX) / d3;
			double d5 = (d1 - this.i.locY) / d3;
			double d6 = (d2 - this.i.locZ) / d3;
			AxisAlignedBB axisalignedbb = this.i.getBoundingBox();
			for(int i = 1; i < d3; i++){
				axisalignedbb = axisalignedbb.c(d4, d5, d6);
				if(!this.i.world.getCubes(this.i, axisalignedbb).isEmpty()){ return false; }
			}
			return true;
		}
	}
}
