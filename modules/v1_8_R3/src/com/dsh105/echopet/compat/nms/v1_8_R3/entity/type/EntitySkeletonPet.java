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

package com.dsh105.echopet.compat.nms.v1_8_R3.entity.type;

import org.bukkit.scheduler.BukkitRunnable;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntitySkeletonPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISkeletonPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.nms.v1_8_R3.entity.EntityPet;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.World;

@EntitySize(width = 0.6F, height = 1.9F)
@EntityPetType(petType = PetType.SKELETON)
public class EntitySkeletonPet extends EntityPet implements IEntitySkeletonPet {

    public EntitySkeletonPet(World world) {
        super(world);
    }

    public EntitySkeletonPet(World world, final IPet pet) {
        super(world, pet);
        new BukkitRunnable() {
            @Override
            public void run() {
				if(((ISkeletonPet) pet).getSkeletonType() == SkeletonType.WITHER){
                    setEquipment(0, new ItemStack(Items.STONE_SWORD));
                } else {
                    setEquipment(0, new ItemStack(Items.BOW));
                }
            }
        }.runTaskLater(EchoPet.getPlugin(), 5L);
    }

    @Override
	public void setSkeletonType(SkeletonType type){
		this.datawatcher.watch(13, (byte) type.ordinal());
		if(type == SkeletonType.WITHER){
            setEquipment(0, new ItemStack(Items.STONE_SWORD));
        } else {
            setEquipment(0, new ItemStack(Items.BOW));
        }
    }

    public int getSkeletonType() {
        return this.datawatcher.getByte(13);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
		this.datawatcher.a(13, (byte) 0);
    }

    @Override
    protected String getIdleSound() {
        return "mob.skeleton.say";
    }

    @Override
    protected String getStepSound() {
		return "mob.skeleton.step";
    }

    @Override
    protected String getDeathSound() {
        return "mob.skeleton.death";
    }

    @Override
    public SizeCategory getSizeCategory() {
        if (this.getSkeletonType() == 1) {
            return SizeCategory.LARGE;
        } else {
            return SizeCategory.REGULAR;
        }
    }
}
