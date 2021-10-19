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

package com.dsh105.echopet.hook;

import com.dsh105.echopet.EchoPetPlugin;
import com.dsh105.echopet.compat.api.plugin.IEchoPetPlugin;
import com.dsh105.echopet.compat.api.plugin.hook.PluginDependencyProvider;
import me.clip.placeholderapi.PlaceholderAPIPlugin;

public class PlaceHolderAPIProvider extends PluginDependencyProvider<IEchoPetPlugin, PlaceholderAPIPlugin>{
	
	private EchoPetExpansion expansion;
	
	public PlaceHolderAPIProvider(EchoPetPlugin myPluginInstance){
		super(myPluginInstance, "PlaceholderAPI");
	}
	
	@Override
	public void onHook(){
		// Min version we support?
		if(expansion == null){
			expansion = new EchoPetExpansion(getHandlingPlugin());
		}
		expansion.register();
	}
	
	@Override
	public void onUnhook(){
		if(expansion != null && expansion.isRegistered()){
			expansion.unregister();
		}
	}
}
