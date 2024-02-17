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

package com.dsh105.echopet.compat.api.plugin.action;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AsyncBukkitAction<T> extends SyncBukkitAction<T>{
	
	private static final BukkitScheduler BUKKIT_SCHEDULER = Bukkit.getScheduler();
	
	protected AsyncBukkitAction(Plugin plugin){
		super(plugin);
	}
	
	@Override
	protected ActionChain<T> execute(Supplier<T> supplier, Consumer<Throwable> errorHandler){
		if(!getPlugin().isEnabled()){
			// Result doesn't matter if server is down
			CompletableFuture.runAsync(supplier::get);
			return this::setAction;
		}
		BUKKIT_SCHEDULER.runTaskAsynchronously(getPlugin(), ()->{
			try{
				T value = supplier.get();
				BUKKIT_SCHEDULER.runTask(getPlugin(), ()->{
					if(getNextAction() != null){
						getNextAction().act(value);
					}
				});
			}catch(Exception ex){
				errorHandler.accept(ex);
			}
		});
		return this::setAction;
	}
	
	public static <T> ActionChain<T> execute(Plugin plugin, Supplier<T> supplier, Consumer<Throwable> errorHandler){
		return new AsyncBukkitAction<T>(plugin).execute(supplier, errorHandler);
	}
}
