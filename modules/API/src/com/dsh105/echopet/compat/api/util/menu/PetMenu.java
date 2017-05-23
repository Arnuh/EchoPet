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
package com.dsh105.echopet.compat.api.util.menu;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.event.PetMenuOpenEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.MenuUtil;

public class PetMenu{

	Inventory inv;
	private IPet pet;
	private ArrayList<MenuOption> options = new ArrayList<MenuOption>();

	public PetMenu(IPet pet){
		this.pet = pet;
		this.options = MenuUtil.createOptionList(pet);
		this.inv = Bukkit.createInventory(pet.getOwner(), round(options.size(), 9), "EchoPet DataMenu");
		int incompatible = 0;
		for(MenuOption o : this.options){
			MenuItem mi = o.item;
			if(mi.getMenuType().isSupported()){
				if(mi.isSupported()){
					if(mi.getMenuType() == DataMenu.DataMenuType.BOOLEAN){
						if(GeneralUtil.isEnumType(PetData.class, mi.toString())){
							PetData pd = PetData.valueOf(mi.toString());
							if(pd.isCompatible()){
								this.inv.setItem(o.position - incompatible, mi.getBoolean(!pet.getPetData().contains(pd)));
							}else incompatible++;
						}else{
							if(mi.toString().equals("HAT")){
								if(pet.isHat()){
									this.inv.setItem(o.position - incompatible, mi.getBoolean(false));
								}else{
									this.inv.setItem(o.position - incompatible, mi.getBoolean(true));
								}
							}
							if(mi.toString().equals("RIDE")){
								if(pet.isOwnerRiding()){
									this.inv.setItem(o.position - incompatible, mi.getBoolean(false));
								}else{
									this.inv.setItem(o.position - incompatible, mi.getBoolean(true));
								}
							}
						}
					}else{
						this.inv.setItem(o.position - incompatible, mi.getItem());
					}
				}else incompatible++;
			}else incompatible++;
		}
		this.inv.setItem(inv.getSize() - 1, DataMenuItem.CLOSE.getItem());
	}

	private int round(int num, int multiple){
		return multiple * (int) Math.ceil((float) num / (float) multiple);
	}

	public void open(boolean sendMessage){
		PetMenuOpenEvent menuEvent = new PetMenuOpenEvent(this.pet.getOwner(), PetMenuOpenEvent.MenuType.MAIN);
		EchoPet.getPlugin().getServer().getPluginManager().callEvent(menuEvent);
		if(menuEvent.isCancelled()){ return; }
		InventoryView view = this.pet.getOwner().openInventory(this.inv);
		pet.setInventoryView(view);
		if(sendMessage){
			Lang.sendTo(this.pet.getOwner(), Lang.OPEN_MENU.toString().replace("%type%", StringUtil.capitalise(this.pet.getPetType().toString().replace("_", " "))));
		}
	}
}
