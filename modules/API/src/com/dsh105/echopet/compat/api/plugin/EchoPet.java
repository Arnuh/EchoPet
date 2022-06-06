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

import java.util.logging.Logger;
import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.config.YAMLConfig;
import com.dsh105.echopet.compat.api.registration.IPetRegistry;

public final class EchoPet{
	
	private static IEchoPetPlugin PLUGIN;
	public static Logger LOG;
	
	public static void setPlugin(IEchoPetPlugin plugin){
		if(PLUGIN != null){
			return;
		}
		PLUGIN = plugin;
		LOG = plugin.getLogger();
	}
	
	public static IEchoPetPlugin getPlugin(){
		return PLUGIN;
	}
	
	public static String getPrefix(){
		return PLUGIN.getPrefix();
	}
	
	public static IPetManager getManager(){
		return PLUGIN.getPetManager();
	}
	
	public static IStorageManager getDataManager(){
		return PLUGIN.getStorageManager();
	}
	
	public static IPetRegistry getPetRegistry(){
		return PLUGIN.getPetRegistry();
	}
	
	public static ConfigOptions getOptions(){
		return PLUGIN.getOptions();
	}
	
	public static YAMLConfig getConfig(){
		return getConfig(ConfigType.MAIN);
	}
	
	public static YAMLConfig getConfig(ConfigType type){
		return switch(type){
			case DATA -> PLUGIN.getPetConfig();
			case LANG -> PLUGIN.getLangConfig();
			case PET_CATEGORY -> PLUGIN.getPetCategoryConfig();
			default -> PLUGIN.getMainConfig();
		};
	}
	
	public enum ConfigType{
		MAIN,
		DATA,
		LANG,
		PET_CATEGORY
	}
}