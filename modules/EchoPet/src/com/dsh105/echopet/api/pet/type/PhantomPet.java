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
import com.dsh105.echopet.api.pet.LivingPet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPhantomPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPhantomPet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@EntityPetType(petType = PetType.PHANTOM)
public class PhantomPet extends LivingPet implements IPhantomPet{
	
	protected int size;
	protected boolean wandering;
	
	public PhantomPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setSize(int size){
		((IEntityPhantomPet) getEntityPet()).setSize(size);
		this.size = size;
	}
	
	@Override
	public int getSize(){
		return size;
	}
	
	@Override
	public void setWandering(boolean flag){
		((IEntityPhantomPet) getEntityPet()).setWandering(flag);
		this.wandering = flag;
	}
	
	@Override
	public boolean isWandering(){
		return this.wandering;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		super.generatePetInfo(info);
		info.add(ChatColor.GOLD + " - Wandering: " + ChatColor.YELLOW + isWandering());
	}
}
