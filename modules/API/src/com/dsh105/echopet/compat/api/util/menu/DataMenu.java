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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.event.PetMenuOpenEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Version;
import com.dsh105.echopet.compat.api.util.VersionCheckType;

public class DataMenu {

    Inventory inv;
    private IPet pet;

    public DataMenu(MenuItem mi, IPet pet) {
        this.pet = pet;
		int size = round(countItems(mi.getMenuType()), 9);
		this.inv = Bukkit.createInventory(pet.getOwner(), size, "EchoPet DataMenu - " + StringUtil.capitalise(mi.toString().replace("_", " ")));
		this.setItems(mi.getMenuType(), size);
    }

    public void open(boolean sendMessage) {
        PetMenuOpenEvent menuEvent = new PetMenuOpenEvent(this.pet.getOwner(), PetMenuOpenEvent.MenuType.DATA);
        EchoPet.getPlugin().getServer().getPluginManager().callEvent(menuEvent);
        if (menuEvent.isCancelled()) {
            return;
        }
        Player p = this.pet.getOwner();
        if (p != null) {
			InventoryView view = p.openInventory(this.inv);
			pet.setInventoryView(view);
        }
    }

	private int countItems(DataMenuType type){
		int i = 0;
		for(DataMenuItem mi : DataMenuItem.values()){
			if(mi.getTypes().contains(type) && type.isValid()){
				if(mi.getDataLink().isCompatible()){
					i++;
				}
			}
		}
		return i + 1;// back
	}

	private int round(int num, int multiple){
		return multiple * (int) Math.ceil((float) num / (float) multiple);
	}

    public void setItems(DataMenuType type, int size) {
        int i = 0;
        for (DataMenuItem mi : DataMenuItem.values()) {
			if(mi.getTypes().contains(type) && type.isValid()){
				if(mi.getDataLink().isCompatible()){
					this.inv.setItem(i, mi.getItem());
					i++;
				}
            }
        }
        this.inv.setItem((size - 1), DataMenuItem.BACK.getItem());
    }

	public enum DataMenuType{
        BOOLEAN,
        CAT_TYPE,
        COLOR,
		PROFESSION,
		ZOMBIE_PROFESSION,
        SIZE,
        OTHER,
		HORSE_TYPE,
		HORSE_VARIANT,
        HORSE_MARKING,
		HORSE_ARMOUR,
		RABBIT_TYPE,
		SKELETON_TYPE,
		LLAMA_VARIANT(VersionCheckType.COMPATIBLE, new Version("1.11-R1")),
		LLAMA_COLOR(VersionCheckType.COMPATIBLE, new Version("1.11-R1")),
		PARROT_VARIANT;

		private Version version;
		private VersionCheckType checkType;

		private DataMenuType(){
			version = new Version();
			checkType = VersionCheckType.COMPATIBLE;
		}

		private DataMenuType(VersionCheckType checkType, Version version){
			this.version = version;
			this.checkType = checkType;
		}

		public boolean isValid(){
			switch (checkType){
				case COMPATIBLE:
					return version.isCompatible(new Version());
				case SUPPORTED:
					return version.isSupported(new Version());
				default:
					return version.isIdentical(new Version());
			}
		}
    }
}