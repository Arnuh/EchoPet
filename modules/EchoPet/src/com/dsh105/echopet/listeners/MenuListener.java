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

package com.dsh105.echopet.listeners;

import java.util.logging.Level;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.data.PetData;
import com.dsh105.echopet.compat.api.entity.data.PetDataCategory;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ItemUtil;
import com.dsh105.echopet.compat.api.util.MenuUtil;
import com.dsh105.echopet.compat.api.util.StringUtil;
import com.dsh105.echopet.compat.api.util.menu.DataMenu;
import com.dsh105.echopet.compat.api.util.menu.PetMenu;
import com.dsh105.echopet.compat.api.util.menu.SelectorLayout;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener{
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event){
		if(!(event.getWhoClicked() instanceof Player player)){
			return;
		}
		
		if(event.getView().getTitle().contains("EchoPet DataMenu")){
			event.setCancelled(true);
			event.setResult(Event.Result.DENY);
		}
		
		Inventory inv = event.getInventory();
		String title = event.getView().getTitle();
		int slot = event.getRawSlot();
		
		if(slot < 0 || slot >= inv.getSize() || inv.getItem(slot) == null){
			return;
		}
		
		ItemStack currentlyInSlot = inv.getItem(slot);
		
		if(event.getSlotType() == InventoryType.SlotType.RESULT){
			try{
				for(int i = 1; i <= 4; i++){
					if(currentlyInSlot != null && inv.getItem(i) != null && inv.getItem(i).isSimilar(SelectorLayout.getSelectorItem())){
						player.updateInventory();
						break;
					}
				}
			}catch(Exception ignored){
				return;
			}
		}
		IPet currentPet = EchoPet.getManager().getPet(player);
		if(currentPet == null){
			return;
		}
		if(currentPet.getRider() != null){
			if(currentPet.getRider().getInventoryView() != null){
				if(currentPet.getRider().getInventoryView().equals(event.getView())){
					currentPet = currentPet.getRider();
				}
			}
		}
		final IPet pet = currentPet;
		if(currentlyInSlot != null){
			try{
				if(title.equals("EchoPet DataMenu")){
					if(currentlyInSlot.equals(MenuUtil.CLOSE)){
						player.closeInventory();
						return;
					}
					for(PetDataCategory category : pet.getPetType().getAllowedCategories()){
						if(ItemUtil.matches(currentlyInSlot, category.getItem())){
							new DataMenu(category, pet).open(false);
							return;
						}
					}
				}else if(title.startsWith("EchoPet DataMenu - ")){
					if(currentlyInSlot.equals(MenuUtil.BACK)){
						new PetMenu(pet).open(false);
						return;
					}
				}
				if(title.startsWith("EchoPet DataMenu")){
					PetDataCategory category = null;
					if(title.contains(" - ")){
						String[] split = title.split(" - ");
						for(PetDataCategory cat : pet.getPetType().getAllowedCategories()){
							if(split[split.length - 1].equals(StringUtil.capitalise(cat.toString().replace("_", " ")))){
								category = cat;
								break;
							}
						}
					}
					for(PetData<?> data : category != null ? category.getData() : pet.getPetType().getAllowedDataTypes()){// Pretty sure this is fine.
						if(data.attemptInteract(player, pet, category, currentlyInSlot)){
							break;
						}
					}
				}
			}catch(Exception e){
				EchoPet.LOG.log(Level.SEVERE, "Encountered severe error whilst handling InventoryClickEvent.", e);
			}
		}
	}
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent e){
		Player player = (Player) e.getPlayer();
		if(e.getView().getTitle().startsWith("EchoPet DataMenu")){
			IPet pet = EchoPet.getManager().getPet(player);
			if(pet == null) return;
			pet.setInventoryView(null);
			if(pet.getRider() != null){
				pet.getRider().setInventoryView(null);
			}
		}
	}
}