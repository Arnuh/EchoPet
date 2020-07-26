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

import java.util.Map;
import java.util.logging.Level;
import com.codingforcookies.robert.core.GUI;
import com.codingforcookies.robert.slot.ISlotAction;
import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class SelectorMenu extends GUI{
	
	public SelectorMenu(Player player, final int page){
		super(ConfigOptions.instance.getConfig().getString("petSelector.menu.title", "Pets Page: ") + (page + 1));
		type(6);
		final Map<Integer, Map<Integer, SelectorIcon>> layout = SelectorLayout.getLoadedLayout();
		final Map<Integer, SelectorIcon> pageItems = layout.get(page);
		if(pageItems == null){
			EchoPet.getPlugin().getLogger().log(Level.SEVERE, "No Config data for page: " + page + ". Please regenerate the config.yml");
			return;
		}
		for(final int slot : pageItems.keySet()){
			SelectorIcon icon = pageItems.get(slot);
			ItemStack iconIS = icon.getIcon(player);
			if(icon.getName().equals(SelectorItem.BACK.getName().replace(ChatColor.COLOR_CHAR, '&')) && page == 0){
				iconIS.setType(Material.MAGMA_CREAM);
			}else if(icon.getName().equals(SelectorItem.NEXT.getName().replace(ChatColor.COLOR_CHAR, '&')) && page == layout.size() - 1){
				iconIS.setType(Material.MAGMA_CREAM);
			}
			this.slot(slot, iconIS, new ISlotAction(){
				
				private final SelectorIcon icon = pageItems.get(slot);
				
				public void doAction(GUI gui, final Player p, ClickType type){
					if(icon.getPage() == page){
						if(icon.getName().equals(SelectorItem.BACK.getName().replace(ChatColor.COLOR_CHAR, '&'))){
							if(page > 0){
								new SelectorMenu(p, page - 1).open(p);
							}
						}else if(icon.getName().equals(SelectorItem.NEXT.getName().replace(ChatColor.COLOR_CHAR, '&'))){
							if(page < layout.size() - 1){
								new SelectorMenu(p, page + 1).open(p);
							}
						}else{
							p.closeInventory();
							if(icon.getCommand().equalsIgnoreCase(EchoPet.getPlugin().getCommandString() + " menu")){
								new BukkitRunnable(){
									
									public void run(){
										p.performCommand(icon.getCommand());
									}
								}.runTaskLater(EchoPet.getPlugin(), 5L);
							}else{
								p.performCommand(icon.getCommand());
							}
						}
					}
				}
			});
		}
	}
}