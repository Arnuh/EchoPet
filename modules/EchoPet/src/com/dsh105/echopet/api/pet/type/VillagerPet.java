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

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVillagerDataHolder;
import com.dsh105.echopet.compat.api.entity.type.pet.IVillagerDataHolder;
import com.dsh105.echopet.compat.api.entity.type.pet.IVillagerPet;

@EntityPetType(petType = PetType.VILLAGER)
public class VillagerPet extends VillagerAbstractPet implements IVillagerPet, IVillagerDataHolder{

	private VillagerType type = VillagerType.PLAINS;
	private Profession profession = Profession.NONE;
	private VillagerLevel level = VillagerLevel.NOVICE;

    public VillagerPet(Player owner) {
        super(owner);
    }

    @Override
    public Profession getProfession() {
        return profession;
    }

    @Override
    public void setProfession(Profession prof) {
		((IEntityVillagerDataHolder) getEntityPet()).setProfession(prof.ordinal());
        this.profession = prof;
    }

	@Override
	public VillagerType getType(){
		return type;
	}

	@Override
	public void setType(VillagerType type){
		((IEntityVillagerDataHolder) getEntityPet()).setType(type.ordinal());
		this.type = type;
	}

	@Override
	public VillagerLevel getLevel(){
		return level;
	}

	@Override
	public void setLevel(VillagerLevel level){
		((IEntityVillagerDataHolder) getEntityPet()).setLevel(level.ordinal());
		this.level = level;
	}
}
