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

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;

import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.World;

public abstract class EntityAgeablePet extends EntityPet {

	protected int age, b, c;
    private boolean ageLocked = true;

    public EntityAgeablePet(World world) {
        super(world);
    }

    public EntityAgeablePet(World world, IPet pet) {
        super(world, pet);
    }

	public int getAge(){
		return this.world.isClientSide ? this.datawatcher.getByte(12) : this.age;
	}

	public void setAge(int i){
		int j = getAge();
		j += i * 20;
		if(j > 0){
			j = 0;
		}
		setAgeRaw(j);
	}

	public void setAgeRaw(int i){
		this.datawatcher.watch(12, (byte) MathHelper.clamp(i, -1, 1));
		this.age = i;
	}

    public boolean isAgeLocked() {
        return ageLocked;
    }

    public void setAgeLocked(boolean ageLocked) {
        this.ageLocked = ageLocked;
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
		this.datawatcher.a(12, (byte) 0);
    }

    @Override
	public void m(){
		super.m();
        if (!(this.world.isClientSide || this.ageLocked)) {
			int i = getAge();
			if(i < 0){
				setAgeRaw(++i);
			}else if(i > 0){
				setAgeRaw(--i);
            }
        }
    }

    public void setBaby(boolean flag) {
		this.datawatcher.watch(12, (byte) (flag ? -1 : 0));
    }

    @Override
	public boolean isBaby(){
		return getAge() < 0;
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.isBaby()) {
            return SizeCategory.TINY;
        } else {
            return SizeCategory.REGULAR;
        }
    }
}
