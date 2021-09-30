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

import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.particle.Trails;
import com.dsh105.echopet.compat.api.plugin.hook.IWorldGuardProvider;
import com.dsh105.echopet.compat.api.registration.IPetRegistry;
import com.dsh105.echopet.compat.api.util.ISpawnUtil;
import com.jolbox.bonecp.BoneCP;
import org.bukkit.plugin.Plugin;

public interface IEchoPetPlugin extends Plugin{
	
	ISpawnUtil getSpawnUtil();
	
	String getPrefix();
	
	String getCommandString();
	
	String getAdminCommandString();
	
	IPetRegistry getPetRegistry();
	
	IPetManager getPetManager();
	
	ISqlPetManager getSqlPetManager();
	
	BoneCP getDbPool();
	
	// public IVanishProvider getVanishProvider();
	
	IWorldGuardProvider getWorldGuardProvider();
	
	YAMLConfig getPetConfig();
	
	YAMLConfig getMainConfig();
	
	YAMLConfig getLangConfig();
	
	ConfigOptions getOptions();
	
	boolean isUpdateAvailable();
	
	String getUpdateName();
	
	long getUpdateSize();
	
	Trails getTrailManager();
}