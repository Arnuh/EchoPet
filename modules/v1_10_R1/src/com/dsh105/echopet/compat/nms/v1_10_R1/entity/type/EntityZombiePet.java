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
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityZombiePet;
import com.dsh105.echopet.compat.api.entity.type.pet.IZombiePet;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityPet;

import net.minecraft.server.v1_10_R1.*;

@EntitySize(width = 0.6F, height = 1.8F)
@EntityPetType(petType = PetType.ZOMBIE)
public class EntityZombiePet extends EntityPet implements IEntityZombiePet{

	private static final DataWatcherObject<Boolean> BABY = DataWatcher.a(EntityZombiePet.class, DataWatcherRegistry.h);
	private static final DataWatcherObject<Integer> VILLAGER_PROFESSION = DataWatcher.a(EntityZombiePet.class, DataWatcherRegistry.b);// Villager profession to convert to
	private static final DataWatcherObject<Boolean> CONVERTING = DataWatcher.a(EntityZombiePet.class, DataWatcherRegistry.h);// if converting from zombie villager > villager
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
		getDataWatcher().set(VILLAGER_PROFESSION, profession.ordinal());
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		getDataWatcher().register(BABY, Boolean.valueOf(false));
		getDataWatcher().register(VILLAGER_PROFESSION, Integer.valueOf(0));
		getDataWatcher().register(CONVERTING, Boolean.valueOf(false));
		getDataWatcher().register(by, Boolean.valueOf(false));
	}

	public boolean isVillager(){
		if(this instanceof EntityGiantPet) return false;
		else return(((IZombiePet) pet).getVillagerProfession().ordinal() >= 1 && ((IZombiePet) pet).getVillagerProfession().ordinal() <= 5);
	}

	@Override
	protected String getIdleSound(){
		return isVillager() ? "entity.zombie_villager.ambient" : "entity.zombie.ambient";
	}

	protected void makeStepSound(BlockPosition blockposition, Block block){
		makeSound(isVillager() ? "entity.zombie_villager.step" : "entity.zombie.step", 0.15F, 1.0F);
	}

	@Override
	protected String getDeathSound(){
		return isVillager() ? "entity.zombie_villager.death" : "entity.zombie.death";
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
