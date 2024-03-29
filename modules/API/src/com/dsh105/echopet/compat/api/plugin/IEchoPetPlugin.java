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

package com.dsh105.echopet.compat.api.plugin;

import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.config.YAMLConfig;
import com.dsh105.echopet.compat.api.plugin.hook.IWorldGuardProvider;
import com.dsh105.echopet.compat.api.registration.IPetRegistry;
import com.dsh105.echopet.compat.api.util.ICraftBukkitUtil;
import com.dsh105.echopet.compat.api.util.ISpawnUtil;
import com.dsh105.echopet.compat.api.util.IUpdater;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public interface IEchoPetPlugin extends Plugin{
	
	ISpawnUtil getSpawnUtil();
	
	String getPrefix();
	
	String getCommandString();
	
	String getAdminCommandString();
	
	IPetRegistry getPetRegistry();
	
	ICraftBukkitUtil getCraftBukkitUtil();
	
	IPetManager getPetManager();
	
	IStorageManager getStorageManager();
	
	// public IVanishProvider getVanishProvider();
	
	IWorldGuardProvider getWorldGuardProvider();
	
	YAMLConfig getPetConfig();
	
	YAMLConfig getMainConfig();
	
	YAMLConfig getLangConfig();
	
	YAMLConfig getPetCategoryConfig();
	
	ConfigOptions getOptions();
	
	IUpdater getUpdater();
	
	/**
	 * A {@link NamespacedKey} that can be used to identify a Pet from EchoPet.<br>
	 * Used by accessing the {@link org.bukkit.persistence.PersistentDataContainer} with type {@link PersistentDataType#BYTE}<br>
	 * <br>
	 * @return A {@link NamespacedKey} for EchoPet entity identification.
	 */
	NamespacedKey getPetNamespacedKey();
}