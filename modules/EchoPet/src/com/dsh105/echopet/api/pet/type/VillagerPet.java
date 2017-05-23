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

package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.Profession;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVillagerPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IVillagerPet;

@EntityPetType(petType = PetType.VILLAGER)
public class VillagerPet extends Pet implements IVillagerPet {

    boolean baby = false;
    Profession profession = Profession.FARMER;

    public VillagerPet(Player owner) {
        super(owner);
    }

    @Override
    public boolean isBaby() {
        return this.baby;
    }

    @Override
    public void setBaby(boolean flag) {
        ((IEntityVillagerPet) getEntityPet()).setBaby(flag);
        this.baby = flag;
    }

    @Override
    public Profession getProfession() {
        return profession;
    }

    @Override
    public int getProfessionId() {
		return profession.ordinal();
    }

    @Override
    public void setProfession(Profession prof) {
		((IEntityVillagerPet) getEntityPet()).setProfession(prof.ordinal());
        this.profession = prof;
    }

}
