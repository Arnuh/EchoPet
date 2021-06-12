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
import net.minecraft.server.v1_17_R1.DataWatcher;
import net.minecraft.server.v1_17_R1.DataWatcherObject;
import net.minecraft.server.v1_17_R1.DataWatcherRegistry;
import net.minecraft.server.v1_17_R1.EntityTypes;
import net.minecraft.server.v1_17_R1.World;

/**
 * @author Arnah
 * @since Jul 03, 2019
 */
@EntitySize(width = 1.3F, height = 1.25F)
@EntityPetType(petType = PetType.PANDA)
public class EntityPandaPet extends EntityAgeablePet implements IEntityPandaPet{
	
	private static final DataWatcherObject<Integer> bA = DataWatcher.a(EntityPandaPet.class, DataWatcherRegistry.b);//Doesn't look like something we care about
	private static final DataWatcherObject<Integer> bB = DataWatcher.a(EntityPandaPet.class, DataWatcherRegistry.b);
	private static final DataWatcherObject<Integer> bC = DataWatcher.a(EntityPandaPet.class, DataWatcherRegistry.b);//clears mainhand if 100?
	private static final DataWatcherObject<Byte> MainGene = DataWatcher.a(EntityPandaPet.class, DataWatcherRegistry.a);
	private static final DataWatcherObject<Byte> HiddenGene = DataWatcher.a(EntityPandaPet.class, DataWatcherRegistry.a);
	private static final DataWatcherObject<Byte> Flag = DataWatcher.a(EntityPandaPet.class, DataWatcherRegistry.a);
	//Unknown1 is some green particles
	private static final int Unknown1 = 0x2, Roll = 0x4, Sit = 0x8, LayOnBack = 0x10;
	private static int testStage = 1;
	
	public EntityPandaPet(World world){
		super(EntityTypes.PANDA, world);
	}
	
	public EntityPandaPet(World world, IPet pet){
		super(EntityTypes.PANDA, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(bA, 0);
		this.datawatcher.register(bB, 0);
		this.datawatcher.register(MainGene, (byte) 0);
		this.datawatcher.register(HiddenGene, (byte) 0);
		this.datawatcher.register(Flag, (byte) 0);
		this.datawatcher.register(bC, 0);
	}
	
	@Override
	public void setMainGene(PandaGene gene){
		datawatcher.set(MainGene, (byte) gene.ordinal());
	}
	
	@Override
	public void setHiddenGene(PandaGene gene){
		datawatcher.set(HiddenGene, (byte) gene.ordinal());
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
		datawatcher.set(Flag, (byte) (getFlag() | flag));
	}
	
	private void removeFlag(int flag){
		datawatcher.set(Flag, (byte) (getFlag() & ~flag));
	}
	
	public int getFlag(){
		return datawatcher.get(Flag);
	}
}