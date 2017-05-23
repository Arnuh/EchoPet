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
package com.dsh105.echopet.compat.nms.v1_8_R3.entity;

import com.dsh105.echopet.compat.api.entity.IEntityTameablePet;
import com.dsh105.echopet.compat.api.entity.IPet;

import net.minecraft.server.v1_8_R3.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Mar 6, 2016
 */
// These are not actually tameable and only here to add missing datawatchers
public class EntityTameablePet extends EntityAgeablePet implements IEntityTameablePet{

	public EntityTameablePet(World world){
		super(world);
	}

	public EntityTameablePet(World world, IPet pet){
		super(world, pet);
	}

	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.a(16, (byte) 0);
		this.datawatcher.a(17, "");
	}

	// These are all useless
	public boolean isTamed(){
		return (this.datawatcher.getByte(16) & 0x4) != 0;
	}

	public void setTamed(boolean flag){
		byte b0 = this.datawatcher.getByte(16);

		if(flag){
			this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 4)));
		}else{
			this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -5)));
		}
	}

	public boolean isSitting(){
		return (this.datawatcher.getByte(16) & 0x1) != 0;
	}

	public void setSitting(boolean flag){
		byte b0 = this.datawatcher.getByte(16);
		if(flag){
			this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 0x1)));
		}else{
			this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & 0xFFFFFFFE)));
		}
	}
}
