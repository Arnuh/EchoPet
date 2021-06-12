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
 *
 *  * This file is part of EchoPet.
 *  * EchoPet is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  * EchoPet is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  * You should have received a copy of the GNU General Public License
 *  * along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.dsh105.echopet.compat.nms.v1_17_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PandaGene;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPandaPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityAgeablePet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@EntitySize(width = 1.3F, height = 1.25F)
@EntityPetType(petType = PetType.PANDA)
public class EntityPandaPet extends EntityAgeablePet implements IEntityPandaPet{
	
	private static final EntityDataAccessor<Integer> bA = SynchedEntityData.defineId(EntityPandaPet.class, EntityDataSerializers.INT);//Doesn't look like something we care about
	private static final EntityDataAccessor<Integer> bB = SynchedEntityData.defineId(EntityPandaPet.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> bC = SynchedEntityData.defineId(EntityPandaPet.class, EntityDataSerializers.INT);//clears mainhand if 100?
	private static final EntityDataAccessor<Byte> MainGene = SynchedEntityData.defineId(EntityPandaPet.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> HiddenGene = SynchedEntityData.defineId(EntityPandaPet.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> Flag = SynchedEntityData.defineId(EntityPandaPet.class, EntityDataSerializers.BYTE);
	//Unknown1 is some green particles
	private static final int Unknown1 = 0x2, Roll = 0x4, Sit = 0x8, LayOnBack = 0x10;
	private static int testStage = 1;
	
	public EntityPandaPet(Level world){
		super(EntityType.PANDA, world);
	}
	
	public EntityPandaPet(Level world, IPet pet){
		super(EntityType.PANDA, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(bA, 0);
		this.entityData.define(bB, 0);
		this.entityData.define(MainGene, (byte) 0);
		this.entityData.define(HiddenGene, (byte) 0);
		this.entityData.define(Flag, (byte) 0);
		this.entityData.define(bC, 0);
	}
	
	@Override
	public void setMainGene(PandaGene gene){
		entityData.set(MainGene, (byte) gene.ordinal());
	}
	
	@Override
	public void setHiddenGene(PandaGene gene){
		entityData.set(HiddenGene, (byte) gene.ordinal());
	}
	
	@Override
	public void setRolling(boolean roll){
		if(roll) addFlag(Roll);
		else removeFlag(Roll);
	}
	
	@Override
	public void setSitting(boolean sit){
		if(sit) addFlag(Sit);
		else removeFlag(Sit);
	}
	
	@Override
	public void setLayingDown(boolean layingDown){
		if(layingDown) addFlag(LayOnBack);
		else removeFlag(LayOnBack);
	}
	
	private void addFlag(int flag){
		entityData.set(Flag, (byte) (getFlag() | flag));
	}
	
	private void removeFlag(int flag){
		entityData.set(Flag, (byte) (getFlag() & ~flag));
	}
	
	public int getFlag(){
		return entityData.get(Flag);
	}
}