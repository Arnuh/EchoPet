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
package com.dsh105.echopet.api.pet.type;

import java.util.List;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.Profession;
import com.dsh105.echopet.compat.api.entity.VillagerLevel;
import com.dsh105.echopet.compat.api.entity.VillagerType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityVillagerDataHolder;
import com.dsh105.echopet.compat.api.entity.type.pet.IVillagerDataHolder;
import com.dsh105.echopet.compat.api.entity.type.pet.IZombieVillagerPet;
import com.dsh105.echopet.compat.api.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


@EntityPetType(petType = PetType.ZOMBIEVILLAGER)
public class ZombieVillagerPet extends ZombiePet implements IZombieVillagerPet, IVillagerDataHolder{
	
	private VillagerType type = VillagerType.PLAINS;
	private Profession profession = Profession.NONE;
	private VillagerLevel level = VillagerLevel.NOVICE;
	
	public ZombieVillagerPet(Player owner){
		super(owner);
	}
	
	@Override
	public Profession getProfession(){
		return profession;
	}
	
	@Override
	public void setProfession(Profession prof){
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
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		String prof = StringUtil.capitalise(getProfession().toString().replace("_", " "));
		info.add(ChatColor.GOLD + " - Profession: " + ChatColor.YELLOW + prof);
		prof = StringUtil.capitalise(getType().toString().replace("_", " "));
		info.add(ChatColor.GOLD + " - Type: " + ChatColor.YELLOW + prof);
		prof = StringUtil.capitalise(getLevel().toString().replace("_", " "));
		info.add(ChatColor.GOLD + " - Level: " + ChatColor.YELLOW + prof);
	}
}
