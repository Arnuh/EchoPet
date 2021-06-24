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

package com.dsh105.echopet.compat.nms.v1_16_R3.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityBatPet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.EntityPet;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.MathHelper;
import net.minecraft.server.v1_16_R3.World;

@EntitySize(width = 0.5F, height = 0.9F)
@EntityPetType(petType = PetType.BAT)
public class EntityBatPet extends EntityPet implements IEntityBatPet{
	
	private static final DataWatcherObject<Byte> a = DataWatcher.a(EntityBatPet.class, DataWatcherRegistry.a);
	
	public EntityBatPet(World world){
		super(EntityTypes.BAT, world);
	}
	
	public EntityBatPet(World world, IPet pet){
		super(EntityTypes.BAT, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(a, (byte) 0);
	}
	
	@Override
	protected String getIdleSound(){
		if((!isStartled()) && (this.random.nextInt(4) != 0)){
			return null;
		}
		return "entity.bat.ambient";
	}
	
	@Override
	public void onLive(){
		super.onLive();
		if(this.isStartled()){
			setMot(0, 0, 0);
			setPositionRaw(locX(), MathHelper.floor(locY()) + 1.0 - this.getHeight(), locZ());
		}else{
			setMot(getMot().x, getMot().y * 0.6000000238418579D, getMot().z);
		}
	}
	
	public boolean isStartled(){
		return (this.datawatcher.get(a) & 0x1) != 0;
	}
	
	public void setResting(boolean flag){
		int i = this.datawatcher.get(a);
		if(flag){
			this.datawatcher.set(a, (byte) (i | 0x1));
		}else{
			this.datawatcher.set(a, (byte) (i & ~0x1));
		}
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
	
	@Override
	public void setWandering(boolean flag){
		
	}
}
