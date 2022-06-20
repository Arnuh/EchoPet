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

package com.dsh105.echopet.compat.api.util.menu;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.data.PetData;
import com.dsh105.echopet.compat.api.entity.data.PetDataCategory;
import com.dsh105.echopet.compat.api.event.PetMenuOpenEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.MenuUtil;
import com.dsh105.echopet.compat.api.util.Perm;
import com.dsh105.echopet.compat.api.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class DataMenu{
	
	private Inventory inv;
	private IPet pet;
	
	public DataMenu(PetDataCategory category, IPet pet){
		this.pet = pet;
		int size = round(countItems(category), 9);
		this.inv = Bukkit.createInventory(pet.getOwner(), size, "EchoPet DataMenu - " + StringUtil.capitalise(category.toString().replace("_", " ")));
		this.setItems(category, size);
	}
	
	public void open(boolean sendMessage){
		PetMenuOpenEvent menuEvent = new PetMenuOpenEvent(this.pet.getOwner(), PetMenuOpenEvent.MenuType.DATA);
		EchoPet.getPlugin().getServer().getPluginManager().callEvent(menuEvent);
		if(menuEvent.isCancelled()){
			return;
		}
		Player p = this.pet.getOwner();
		if(p != null){
			InventoryView view = p.openInventory(this.inv);
			pet.setInventoryView(view);
		}
	}
	
	private int countItems(PetDataCategory category){
		int i = 0;
		for(PetData<?> data : category.getData()){
			if(!data.isCompatible()) continue;
			if(Perm.hasDataPerm(pet.getOwner(), false, pet.getPetType(), data, false)){
				i++;
			}
		}
		return i + 1;// back
	}
	
	private int round(int num, int multiple){
		return multiple * (int) Math.ceil((float) num / (float) multiple);
	}
	
	public void setItems(PetDataCategory category, int size){
		int i = 0;
		for(PetData<?> data : category.getData()){
			if(!data.isCompatible()) continue;
			if(data.getMaterial() == null) continue;
			if(Perm.hasDataPerm(pet.getOwner(), false, pet.getPetType(), data, false)){
				inv.setItem(i++, data.toItem(pet));
			}
		}
		this.inv.setItem((size - 1), MenuUtil.BACK);
	}
}