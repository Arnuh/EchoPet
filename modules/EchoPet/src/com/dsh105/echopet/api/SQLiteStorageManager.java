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

package com.dsh105.echopet.api;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.ISQLStorageManager;
import com.dsh105.echopet.compat.api.plugin.PetStorage;
import com.dsh105.echopet.compat.api.plugin.SavedType;
import com.dsh105.echopet.compat.api.plugin.action.ActionChain;
import com.dsh105.echopet.compat.api.plugin.action.AsyncBukkitAction;
import com.dsh105.echopet.compat.api.plugin.action.SyncBukkitAction;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class SQLiteStorageManager implements ISQLStorageManager{
	
	protected final Plugin plugin;
	protected final String tablePrefix;
	private File dbFile;
	private Connection connection;
	
	public SQLiteStorageManager(Plugin plugin){
		this.plugin = plugin;
		this.tablePrefix = EchoPet.getPlugin().getMainConfig().getString("sql.prefix", "echopet");
	}
	
	@Override
	public boolean init(){
		dbFile = new File(plugin.getDataFolder(), "sqlite.db");
		if(!dbFile.exists()){
			try{
				if(!dbFile.createNewFile()){
					return false;
				}
			}catch(IOException e){
				plugin.getLogger().log(Level.SEVERE, "Failed to create sqlite db file", e);
				return false;
			}
		}
		try{
			Connection con = getConnection();
			try(PreparedStatement ps = con.prepareStatement("""
				CREATE TABLE IF NOT EXISTS `%s_pets` (
					`uuid` CHAR(36) NOT NULL,
					`saved_type` TINYINT NOT NULL DEFAULT 0,
					`type` VARCHAR(255) NOT NULL,
					`name` VARCHAR(255) NOT NULL,
					`data` TEXT NOT NULL,
					`rider_type` VARCHAR(255) NULL DEFAULT NULL,
					`rider_name` VARCHAR(255) NULL DEFAULT NULL,
					`rider_data` TEXT NULL DEFAULT NULL,
					PRIMARY KEY (`uuid`, `saved_type`)
				);""".formatted(tablePrefix))){
				ps.executeUpdate();
			}
		}catch(SQLException ex){
			plugin.getLogger().log(Level.SEVERE, "Failed to connect to SQLite", ex);
			return false;
		}
		return true;
	}
	
	@Override
	public void shutdown(){
		AsyncBukkitAction.execute(plugin, ()->{
			try{
				connection.close();
				connection = null;
			}catch(SQLException e){
				plugin.getLogger().log(Level.SEVERE, "Failed to close database connection", e);
			}
			return true;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error shutting down SQLite", ex));
	}
	
	@Override
	public ActionChain<IPet> save(Player player, IPet pet, SavedType savedType){
		if(pet.isRider()){
			return new SyncBukkitAction<IPet>(plugin)::setAction;
		}
		final IPet rider = pet.getRider();
		return AsyncBukkitAction.execute(plugin, ()->{
			try{
				Connection con = getConnection();
				try(PreparedStatement ps = con.prepareStatement("""
					INSERT INTO %s_pets (uuid, saved_type, type, name, data, rider_type, rider_name, rider_data)
					VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT(uuid, saved_type) DO UPDATE SET
					saved_type = excluded.saved_type,
					type = excluded.type, name = excluded.name, data = excluded.data,
					rider_type = excluded.rider_type, rider_name = excluded.rider_name, rider_data = excluded.rider_data;
					""".formatted(tablePrefix))){
					prepareSave(ps, player, savedType, pet, rider);
					ps.executeUpdate();
				}
			}catch(Exception ex){
				plugin.getLogger().log(Level.SEVERE, "Error saving pet data for " + player.getUniqueId(), ex);
			}
			return pet;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error saving pet data for " + player.getUniqueId(), ex));
	}
	
	@Override
	public ActionChain<Boolean> save(Player player, PetStorage pet, PetStorage rider, SavedType savedType){
		return AsyncBukkitAction.execute(plugin, ()->{
			try{
				Connection con = getConnection();
				try(PreparedStatement ps = con.prepareStatement("""
					INSERT INTO %s_pets (uuid, saved_type, type, name, data, rider_type, rider_name, rider_data)
					VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT(uuid, saved_type) DO UPDATE SET
					saved_type = excluded.saved_type,
					type = excluded.type, name = excluded.name, data = excluded.data,
					rider_type = excluded.rider_type, rider_name = excluded.rider_name, rider_data = excluded.rider_data;
					""".formatted(tablePrefix))){
					prepareSave(ps, player, savedType, pet, rider);
					ps.executeUpdate();
					return true;
				}
			}catch(Exception ex){
				plugin.getLogger().log(Level.SEVERE, "Error saving pet data for " + player.getUniqueId(), ex);
			}
			return false;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error saving pet data for " + player.getUniqueId(), ex));
	}
	
	@Override
	public ActionChain<PetStorage> load(Player player, SavedType savedType){
		return AsyncBukkitAction.execute(plugin, ()->{
			try{
				Connection con = getConnection();
				try(PreparedStatement ps = con.prepareStatement("""
					SELECT type, name, data, rider_type, rider_name, rider_data
					FROM %s_pets WHERE uuid = ? ORDER BY saved_type DESC;""".formatted(tablePrefix))){
					ps.setString(1, player.getUniqueId().toString());
					try(ResultSet rs = ps.executeQuery()){
						if(rs.next()){
							return create(rs);
						}
					}
				}
			}catch(Exception ex){
				plugin.getLogger().log(Level.SEVERE, "Error loading pet data for " + player.getUniqueId(), ex);
			}
			return null;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error loading pet data for " + player.getUniqueId(), ex));
	}
	
	@Override
	public ActionChain<Boolean> remove(Player player, SavedType savedType){
		return AsyncBukkitAction.execute(plugin, ()->{
			try{
				Connection con = getConnection();
				try(PreparedStatement ps = con.prepareStatement("DELETE FROM %s_pets WHERE uuid = ?;".formatted(tablePrefix))){
					ps.setString(1, player.getUniqueId().toString());
					ps.executeUpdate();
				}
				return true;
			}catch(SQLException ex){
				plugin.getLogger().log(Level.SEVERE, "Failed to delete Pet data for " + player.getUniqueId(), ex);
			}
			return false;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error deleting pet data for " + player.getUniqueId(), ex));
	}
	
	@Override
	public ActionChain<Boolean> removeAll(){
		return AsyncBukkitAction.execute(plugin, ()->{
			try{
				Connection con = getConnection();
				try(PreparedStatement ps = con.prepareStatement("DELETE FROM %s_pets;".formatted(tablePrefix))){
					ps.executeUpdate();
				}
				return true;
			}catch(SQLException ex){
				plugin.getLogger().log(Level.SEVERE, "Failed to delete Pet data", ex);
			}
			return false;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error deleting pet data", ex));
	}
	
	@Override
	public Connection getConnection() throws SQLException{
		if(connection == null || connection.isClosed()){
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
		}
		return connection;
	}
}
