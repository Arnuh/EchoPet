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

package com.dsh105.echopet.compat.api.util;

import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;

public class WorldUtil{
	
	public static boolean allowPets(Location location){
		boolean allowWorld = EchoPet.getPlugin().getMainConfig().getBoolean("worlds." + location.getWorld().getName(), EchoPet.getPlugin().getMainConfig().getBoolean("worlds.enableByDefault", true));
		return allowWorld && allowRegion(location);
	}
	
	public static boolean allowRegion(Location location){
		if(!EchoPet.getPlugin().getWorldGuardProvider().isHooked()){
			return true;
		}
		
		if(location.getWorld() == null){
			return true;// ?
		}
		
		WorldGuardPlugin wg = EchoPet.getPlugin().getWorldGuardProvider().getDependency();// is this even used in worldguard anymore? Keeping it because it's how we hook
		if(wg == null){
			return true;
		}
		
		WorldGuardPlatform platform = WorldGuard.getInstance().getPlatform();
		RegionContainer container = platform.getRegionContainer();
		RegionManager regionManager = container.get(BukkitAdapter.adapt(location.getWorld()));
		if(regionManager == null){
			return true;
		}
		ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(location));
		
		if(set.size() > 0){
			ConfigurationSection cs = EchoPet.getPlugin().getMainConfig().getConfigurationSection("worldguard.regions");
			Set<String> regions = cs.getKeys(false);
			
			for(ProtectedRegion region : set){
				// if(key.equalsIgnoreCase("allowByDefault") || key.equalsIgnoreCase("regionEnterCheck")) continue;
				if(!regions.contains(region.getId())){
					continue;
				}
				return EchoPet.getPlugin().getMainConfig().getBoolean("worldguard.regions." + region.getId(), true);
			}
		}
		return EchoPet.getPlugin().getMainConfig().getBoolean("worldguard.regions.allowByDefault", true);
	}
}