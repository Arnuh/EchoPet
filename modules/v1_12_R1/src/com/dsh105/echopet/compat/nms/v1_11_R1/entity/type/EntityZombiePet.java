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
package com.dsh105.echopet.compat.nms.v1_11_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityZombiePet;
import com.dsh105.echopet.compat.api.entity.type.pet.IZombiePet;
import com.dsh105.echopet.compat.nms.v1_11_R1.entity.EntityPet;

import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.ZOMBIE)
public class EntityZombiePet extends EntityPet implements IEntityZombiePet{

	private static final DataWatcherObject<Boolean> BABY = DataWatcher.a(EntityZombiePet.class, DataWatcherRegistry.h);
	private static final DataWatcherObject<Integer> bx = DataWatcher.a(EntityZombiePet.class, DataWatcherRegistry.b);// gets registered and that is it.
	private static final DataWatcherObject<Boolean> by = DataWatcher.a(EntityZombiePet.class, DataWatcherRegistry.h);// ? has a setter but no getter

	public EntityZombiePet(World world){
		super(world);
	}

	public EntityZombiePet(World world, IPet pet){
		super(world, pet);
		// TODO: Broken
		/*new BukkitRunnable() {
		    @Override
		    public void run() {
				setEquipment(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
		    }
		}.runTaskLater(EchoPet.getPlugin(), 5L);*/
	}

	@Override
	public void setBaby(boolean flag){
		getDataWatcher().set(BABY, Boolean.valueOf(flag));
	}

	@Override
	public void setVillagerProfession(Profession profession){
		//
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		getDataWatcher().register(BABY, false);
		getDataWatcher().register(bx, 0);
		getDataWatcher().register(by, false);
	}

	public boolean isVillager(){
		if(this instanceof EntityGiantPet) return false;
		else return(((IZombiePet) pet).getVillagerProfession().ordinal() >= 1 && ((IZombiePet) pet).getVillagerProfession().ordinal() <= 5);
	}

	@Override
	public boolean isBaby(){
		return ((Boolean) getDataWatcher().get(BABY)).booleanValue();
	}

	@Override
	public SizeCategory getSizeCategory(){
		if(this.isBaby()){
			return SizeCategory.TINY;
		}else{
			return SizeCategory.REGULAR;
		}
	}
}
