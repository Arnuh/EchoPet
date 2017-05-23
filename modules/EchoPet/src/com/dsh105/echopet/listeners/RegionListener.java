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
import org.bukkit.event.player.PlayerMoveEvent;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.event.PetRideMoveEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.WorldUtil;


public class RegionListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        IPet pet = EchoPet.getManager().getPet(p);
		if(pet != null && pet.isSpawned()){
            if (!WorldUtil.allowRegion(event.getTo())) {
				pet.removePet(true, true);
                Lang.sendTo(p, Lang.ENTER_PET_DISABLED_REGION.toString());
            }
        }
    }

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPetMove(PetRideMoveEvent event){
		IPet pet = event.getPet();
		if(pet != null && pet.isSpawned()){
			if(!WorldUtil.allowRegion(pet.getLocation())){
				pet.ownerRidePet(false);
				pet.removePet(true, true);
				Lang.sendTo(pet.getOwner(), Lang.ENTER_PET_DISABLED_REGION.toString());
			}
		}
	}
}