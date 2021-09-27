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

import java.util.List;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetDataCategory;
import com.dsh105.echopet.compat.api.event.PetMenuOpenEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.MenuUtil;
import com.dsh105.echopet.compat.api.util.Perm;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class PetMenu{
	
	private final Inventory inv;
	private final IPet pet;
	
	public PetMenu(IPet pet){
		this.pet = pet;
		List<Object> options = MenuUtil.createOptionList(pet);
		this.inv = Bukkit.createInventory(pet.getOwner(), round(options.size(), 9), "EchoPet DataMenu");
		int index = 0;
		for(Object obj : options){
			if(obj instanceof PetData){
				PetData data = (PetData) obj;
				if(data.isCompatible()){
					if(Perm.hasDataPerm(pet.getOwner(), false, pet.getPetType(), data, false)){
						inv.setItem(index++, data.toItem(pet));
					}
				}
			}else if(obj instanceof PetDataCategory){
				PetDataCategory category = (PetDataCategory) obj;
				ItemStack item = category.getItem();
				if(item == null) continue;
				inv.setItem(index++, item);
			}
		}
		this.inv.setItem(inv.getSize() - 1, MenuUtil.CLOSE);
	}
	
	private int round(int num, int multiple){
		return multiple * (int) Math.ceil((float) num / (float) multiple);
	}
	
	public void open(boolean sendMessage){
		PetMenuOpenEvent menuEvent = new PetMenuOpenEvent(this.pet.getOwner(), PetMenuOpenEvent.MenuType.MAIN);
		EchoPet.getPlugin().getServer().getPluginManager().callEvent(menuEvent);
		if(menuEvent.isCancelled()){
			return;
		}
		InventoryView view = this.pet.getOwner().openInventory(this.inv);
		pet.setInventoryView(view);
		if(sendMessage){
			Lang.sendTo(this.pet.getOwner(), Lang.OPEN_MENU.toString().replace("%type%", StringUtil.capitalise(this.pet.getPetType().toString().replace("_", " "))));
		}
	}
}
