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

import com.dsh105.commodus.GeometryUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.entity.IEntityPacketPet;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.event.PetInteractEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.ItemUtil;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.Perm;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.dsh105.echopet.compat.api.util.WorldUtil;
import com.dsh105.echopet.compat.api.util.menu.SelectorLayout;
import com.dsh105.echopet.compat.api.util.menu.SelectorMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PetOwnerListener implements Listener{
	
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent event){
		for(ItemStack item : event.getInventory().getMatrix()){
			if(item == null) continue;
			if(ItemUtil.matches(item, SelectorLayout.getSelectorItem())){
				event.getInventory().setResult(new ItemStack(Material.AIR));
				break;
			}
		}
	}
	
	@EventHandler
	public void onCraftItem(CraftItemEvent event){// kinda not needed.
		for(ItemStack item : event.getInventory().getMatrix()){
			if(item == null) continue;
			if(ItemUtil.matches(item, SelectorLayout.getSelectorItem())){
				event.setResult(Event.Result.DENY);
				event.setCancelled(true);
				break;
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Player p = event.getPlayer();
		ItemStack itemStack = event.getItem();
		if(itemStack != null && itemStack.isSimilar(SelectorLayout.getSelectorItem())){
			new SelectorMenu(p, 0).open(p);
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
		Player p = event.getPlayer();
		Entity e = event.getRightClicked();
		int slot = p.getInventory().getHeldItemSlot();
		if(slot >= 0 && slot < 9){
			ItemStack itemInHand = p.getInventory().getContents()[slot];
			if(itemInHand != null && itemInHand.getType() != Material.AIR && itemInHand.isSimilar(SelectorLayout.getSelectorItem())){
				new SelectorMenu(p, 0).open(p);
				event.setCancelled(true);
				return;
			}
		}
		
		if(ReflectionUtil.getEntityHandle(e) instanceof IEntityPet){
			IPet pet = ((IEntityPet) ReflectionUtil.getEntityHandle(e)).getPet();
			event.setCancelled(true);
			PetInteractEvent iEvent = new PetInteractEvent(pet, p, PetInteractEvent.Action.RIGHT_CLICK, false);
			EchoPet.getPlugin().getServer().getPluginManager().callEvent(iEvent);
			if(!iEvent.isCancelled()){
				pet.getEntityPet().onInteract(p);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player p = (Player) event.getEntity();
			if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
				IPet pet = EchoPet.getManager().getPet(p);
				if(pet != null && pet.isOwnerRiding()){
					if(EchoPet.getOptions().canIgnoreFallDamage(pet.getPetType())){
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(final PlayerTeleportEvent event){
		final Player p = event.getPlayer();
		final IPet pi = EchoPet.getManager().getPet(p);
		for(IPet pet : EchoPet.getManager().getPets()){
			if(pet.getEntityPet() instanceof IEntityPacketPet && ((IEntityPacketPet) pet.getEntityPet()).hasInititiated()){
				if(GeometryUtil.getNearbyEntities(event.getTo(), 50).contains(pet)){
					((IEntityPacketPet) pet.getEntityPet()).updatePosition();
				}
			}
		}
		if(pi != null && pi.isSpawned()){
			if(!WorldUtil.allowPets(event.getTo())){
				Lang.sendTo(p, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(event.getTo().getWorld().getName())));
				pi.removePet(false, false);
			}else{
				pi.setAsHat(false);
				if(event.getCause() != TeleportCause.UNKNOWN){// This will probably cause issues.. I don't know why more causes don't exist.
					pi.ownerRidePet(false);
					pi.removePet(false, false);
					new BukkitRunnable(){
						
						@Override
						public void run(){
							if(pi != null){
								if(WorldUtil.allowPets(event.getTo())){
									pi.spawnPet(p, false);
								}
							}
						}
					}.runTaskLater(EchoPet.getPlugin(), 20L);// could be reduced
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player p = event.getPlayer();
		IPet pi = EchoPet.getManager().getPet(p);
		if(pi != null){
			EchoPet.getManager().saveFileData("autosave", pi);
			EchoPet.getSqlManager().saveToDatabase(pi, false);
			EchoPet.getManager().removePet(pi, true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDropItem(PlayerDropItemEvent event){
		if(event.getItemDrop().getItemStack().isSimilar(SelectorLayout.getSelectorItem()) && !(ConfigOptions.instance.getConfig().getBoolean("petSelector.allowDrop", true))){
			event.setCancelled(true);
			event.getPlayer().updateInventory();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event){
		final Player p = event.getPlayer();
		Inventory inv = p.getInventory();
		if(EchoPet.getPlugin().isUpdateAvailable() && p.hasPermission("echopet.update")){
			p.sendMessage(EchoPet.getPrefix() + ChatColor.GOLD + "An update is available: " + EchoPet.getPlugin().getUpdateName() + " (" + EchoPet.getPlugin().getUpdateSize() + " bytes).");
			p.sendMessage(EchoPet.getPrefix() + ChatColor.GOLD + "Type /ecupdate to update.");
		}
		
		for(ItemStack item : inv.getContents()){
			if(item != null && item.isSimilar(SelectorLayout.getSelectorItem())){
				inv.remove(item);
			}
		}
		
		if(EchoPet.getConfig().getBoolean("petSelector.clearInvOnJoin", false)){
			inv.clear();
		}
		if(EchoPet.getConfig().getBoolean("petSelector.giveOnJoin.enable", true) && ((EchoPet.getConfig().getBoolean("petSelector.giveOnJoin.usePerm", false) && p.hasPermission(EchoPet.getConfig().getString("petSelector.giveOnJoin.perm", "echopet.selector.join"))) || !(EchoPet.getConfig().getBoolean("petSelector.giveOnJoin.usePerm", false)))){
			int slot = (EchoPet.getConfig().getInt("petSelector.giveOnJoin.slot", 9)) - 1;
			ItemStack i = inv.getItem(slot);
			ItemStack selector = SelectorLayout.getSelectorItem();
			if(i != null){
				inv.clear(slot);
				inv.setItem(slot, selector);
				inv.addItem(i);
			}else{
				inv.setItem(slot, selector);
			}
		}
		
		// TODO MAKE THIS ASYNC, maybe? one day?
		// only load pets for players with permissions (otherwise RIP mysql users)
		for(PetType type : PetType.values()){
			boolean access = Perm.hasTypePerm(p, false, Perm.BASE_PETTYPE, true, type);
			
			// no perms? no fun!
			if(!access) continue;
			
			// delayed loading if the player has at least 1 perm
			final boolean sendMessage = EchoPet.getConfig().getBoolean("sendLoadMessage", true);
			new BukkitRunnable(){
				@Override
				public void run(){
					if(p != null && p.isOnline()){
						IPet pet = EchoPet.getManager().loadPets(p, true, sendMessage, false);
						if(pet != null){
							pet.spawnPet(p, false);
						}
					}
				}
			}.runTaskLater(EchoPet.getPlugin(), 20);
			
			// we're searching for at least 1 perm... (wouldn't want to load pet for every permission since player can have only 1 pet at the time)
			break;
		}
		
		for(IPet pet : EchoPet.getManager().getPets()){
			if(pet.getEntityPet() instanceof IEntityPacketPet && ((IEntityPacketPet) pet.getEntityPet()).hasInititiated()){
				if(GeometryUtil.getNearbyEntities(event.getPlayer().getLocation(), 50).contains(pet)){
					((IEntityPacketPet) pet.getEntityPet()).updatePosition();
				}
			}
		}
		
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player p = event.getEntity();
		IPet pet = EchoPet.getManager().getPet(p);
		if(pet != null) pet.removePet(false, false);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		final Player p = event.getPlayer();
		new BukkitRunnable(){
			
			@Override
			public void run(){
				IPet pet = EchoPet.getManager().getPet(p);
				if(pet != null) pet.spawnPet(p, false);
			}
			
		}.runTaskLater(EchoPet.getPlugin(), 20L);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onWorldChange(final PlayerChangedWorldEvent event){
		final Player p = event.getPlayer();
		final IPet pi = EchoPet.getManager().getPet(p);
		if(pi != null){
			pi.removePet(false, false);
			new BukkitRunnable(){
				@Override
				public void run(){
					if(pi != null){
						if(WorldUtil.allowPets(p.getLocation())){// this should be the new worlds location
							pi.spawnPet(p, false);
						}
					}
				}
				
			}.runTaskLater(EchoPet.getPlugin(), 20L);
		}
	}
}
