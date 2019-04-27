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

package com.dsh105.echopet.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetDataCategory;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ItemUtil;
import com.dsh105.echopet.compat.api.util.Logger;
import com.dsh105.echopet.compat.api.util.MenuUtil;
import com.dsh105.echopet.compat.api.util.menu.DataMenu;
import com.dsh105.echopet.compat.api.util.menu.PetMenu;
import com.dsh105.echopet.compat.api.util.menu.SelectorLayout;

public class MenuListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event){
		if(!(event.getWhoClicked() instanceof Player)){
			return;
		}

		Player player = (Player) event.getWhoClicked();
		if(event.getView().getTitle().contains("EchoPet DataMenu")){
			event.setCancelled(true);
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
			}catch(Exception e){
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
					for(PetDataCategory category : PetDataCategory.values){
						if(ItemUtil.matches(currentlyInSlot, category.getItem())){
							new DataMenu(category, pet).open(false);
							return;
						}
					}
					/*for (MenuItem mi : MenuItem.values()) {
					    if (ItemUtil.matches(mi.getItem(), currentlyInSlot) || ItemUtil.matches(mi.getBoolean(false), currentlyInSlot) || ItemUtil.matches(mi.getBoolean(true), currentlyInSlot)) {
					        if (mi.getMenuType() == DataMenuType.BOOLEAN) {
					            if (GeneralUtil.isEnumType(PetData.class, mi.toString().toUpperCase())) {
					                PetData pd = PetData.valueOf(mi.toString());
					                if (Perm.hasDataPerm(player, true, pet.getPetType(), pd, false)) {
					                    if (pet.getPetData().contains(pd)) {
					                        EchoPet.getManager().setData(pet, pd, false);
					                        inv.setItem(slot, mi.getBoolean(true));
											// pet.getLocation().getWorld().spawnParticle(Particle.SMOKE_NORMAL, pet.getLocation(), 1);
											// Particle.RED_SMOKE.builder().at(pet.getLocation()).show();
					                    } else {
					                        EchoPet.getManager().setData(pet, pd, true);
					                        inv.setItem(slot, mi.getBoolean(false));
											// pet.getLocation().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, pet.getLocation(), 1);
											// Particle.SPARKLE.builder().at(pet.getLocation()).show();
					                    }
					                }
					            } else {
					                if (mi.toString().equals("HAT")) {
					                    if (Perm.hasTypePerm(player, true, Perm.BASE_HAT, false, pet.getPetType())) {
					                        if (!pet.isHat()) {
					                            pet.setAsHat(true);
					                            inv.setItem(slot, mi.getBoolean(false));
					                            Lang.sendTo(pet.getOwner(), Lang.HAT_PET_ON.toString());
					                        } else {
					                            pet.setAsHat(false);
					                            inv.setItem(slot, mi.getBoolean(true));
					                            Lang.sendTo(pet.getOwner(), Lang.HAT_PET_OFF.toString());
					                        }
					                    }
					                }
					                if (mi.toString().equals("RIDE")) {
					                    if (Perm.hasTypePerm(player, true, Perm.BASE_RIDE, false, pet.getPetType())) {
					                        if (!pet.isOwnerRiding()) {
					                            pet.ownerRidePet(true);
					                            inv.setItem(slot, mi.getBoolean(false));
					                            Lang.sendTo(pet.getOwner(), Lang.RIDE_PET_ON.toString());
					                        } else {
					                            pet.ownerRidePet(false);
					                            inv.setItem(slot, mi.getBoolean(true));
					                            Lang.sendTo(pet.getOwner(), Lang.RIDE_PET_OFF.toString());
					                        }
					                    }
					                }
					            }
					        } else {
								DataMenu dm = new DataMenu(mi, pet);
								dm.open(false);
					        }
					        break;
					    }
					}*/
				}else if(title.startsWith("EchoPet DataMenu - ")){
					if(currentlyInSlot.equals(MenuUtil.BACK)){
						new PetMenu(pet).open(false);
						return;
					}
					/*
					for (DataMenuItem dmi : DataMenuItem.values()) {
					    if (ItemUtil.matches(dmi.getItem(), currentlyInSlot)) {
					        PetData pd = dmi.getDataLink();
							if(pet.getPetType().isDataAllowed(pd)){
								if(pd.isCompatible() && Perm.hasDataPerm(player, true, pet.getPetType(), pd, false)){
									EchoPet.getManager().setData(pet, pd, true);
									break;
								}
							}
						}
					}*/
				}
				if(title.startsWith("EchoPet DataMenu")){
					for(PetData data : PetData.values){
						ItemStack item = data.toItem();
						if(item == null){// If no item = boolean toggle
							if(ItemUtil.matches(currentlyInSlot, MenuUtil.BOOLEAN_FALSE) || ItemUtil.matches(currentlyInSlot, MenuUtil.BOOLEAN_TRUE)){
								boolean newFlag = ItemUtil.matches(currentlyInSlot, MenuUtil.BOOLEAN_FALSE);
								if(data.getAction() != null){
									if(data.getAction().click(player, pet, data, newFlag)){
										EchoPet.getManager().setData(pet, data, newFlag);
										inv.setItem(slot, data.toItem(!newFlag));
									}
								}
							}
						}else{
							if(ItemUtil.matches(currentlyInSlot, item)){
								if(data.getAction() != null){
									boolean newFlag = !pet.getPetData().contains(data);
									if(data.getAction().click(player, pet, data, newFlag)){
										EchoPet.getManager().setData(pet, data, newFlag);
									}
								}
								break;
							}
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				Logger.log(Logger.LogLevel.SEVERE, "Encountered severe error whilst handling InventoryClickEvent.", e, true);
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
			if(pet.getRider() != null) pet.getRider().setInventoryView(null);
		}
	}
}