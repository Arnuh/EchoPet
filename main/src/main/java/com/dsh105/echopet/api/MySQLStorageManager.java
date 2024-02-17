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

import com.dsh105.echopet.compat.api.config.YAMLConfig;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.ISQLStorageManager;
import com.dsh105.echopet.compat.api.plugin.PetStorage;
import com.dsh105.echopet.compat.api.plugin.SavedType;
import com.dsh105.echopet.compat.api.plugin.action.ActionChain;
import com.dsh105.echopet.compat.api.plugin.action.AsyncBukkitAction;
import com.dsh105.echopet.compat.api.plugin.action.SyncBukkitAction;
import com.dsh105.echopet.compat.api.util.SQLMigrationHandler;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

public class MySQLStorageManager implements ISQLStorageManager{
	
	protected final Plugin plugin;
	protected final String tablePrefix;
	
	private DataSource dataSource;
	
	public MySQLStorageManager(Plugin plugin){
		this.plugin = plugin;
		this.tablePrefix = EchoPet.getPlugin().getMainConfig().getString("sql.prefix", "com/dsh105/echopet");
	}
	
	@Override
	public boolean init(){
		YAMLConfig mainConfig = EchoPet.getPlugin().getMainConfig();
		String host = mainConfig.getString("sql.host", "localhost");
		int port = mainConfig.getInt("sql.port", 3306);
		String db = mainConfig.getString("sql.database", "com/dsh105/echopet");
		String user = mainConfig.getString("sql.username", "root");
		String pass = mainConfig.getString("sql.password", "");
		File propertiesFile = new File(plugin.getDataFolder(), "hikaricp.properties");
		HikariConfig config;
		if(propertiesFile.exists()){
			config = new HikariConfig(propertiesFile.getAbsolutePath());
		}else{
			config = new HikariConfig();
		}
		
		// Check if any values are already set to prevent overriding them.
		if(config.getJdbcUrl() == null) config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + db);
		if(config.getUsername() == null) config.setUsername(user);
		if(config.getPassword() == null) config.setPassword(pass);
		
		Properties properties = config.getDataSourceProperties();
		if(!properties.containsKey("rewriteBatchedStatements")){
			config.addDataSourceProperty("rewriteBatchedStatements", "true");
		}
		if(!properties.containsKey("cachePrepStmts")){
			config.addDataSourceProperty("cachePrepStmts", "true");
		}
		if(!properties.containsKey("useServerPrepStmts")){
			config.addDataSourceProperty("useServerPrepStmts", "true");
		}
		// config.setConnectionTestQuery("SELECT 1");
		try{
			dataSource = new HikariDataSource(config);
			try(Connection con = getConnection()){
				try(PreparedStatement ps = con.prepareStatement("""
					CREATE TABLE IF NOT EXISTS `%s_pets` (
						`uuid` CHAR(36) NOT NULL,
						`saved_type` TINYINT NOT NULL DEFAULT 0,
						`type` VARCHAR(255) NOT NULL,
						`name` VARCHAR(255) NOT NULL,
						`data` LONGTEXT NOT NULL,
						`rider_type` VARCHAR(255) NULL DEFAULT NULL,
						`rider_name` VARCHAR(255) NULL DEFAULT NULL,
						`rider_data` LONGTEXT NULL DEFAULT NULL,
						PRIMARY KEY (`uuid`, `saved_type`)
					);""".formatted(tablePrefix))){
					ps.executeUpdate();
					SQLMigrationHandler.handle(plugin, con, tablePrefix);
				}
			}
		}catch(SQLException ex){
			plugin.getLogger().log(Level.SEVERE, "Failed to connect to MySQL! [MySQL DataBase: " + db + "].", ex);
			return false;
		}
		return true;
	}
	
	@Override
	public void shutdown(){
		if(dataSource instanceof Closeable closeable){
			try{
				closeable.close();
			}catch(IOException e){
				plugin.getLogger().log(Level.SEVERE, "Failed to close database connection", e);
			}
		}
	}
	
	@Override
	public Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
	
	@Override
	public ActionChain<IPet> save(Player player, IPet pet, SavedType savedType){
		if(pet.isRider()){
			return new SyncBukkitAction<IPet>(plugin)::setAction;
		}
		final IPet rider = pet.getRider();
		return AsyncBukkitAction.execute(plugin, ()->{
			try(Connection con = getConnection()){
				try(PreparedStatement ps = con.prepareStatement("""
					INSERT INTO %s_pets (uuid, saved_type, type, name, data, rider_type, rider_name, rider_data)
					VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE
					saved_type = VALUES(saved_type),
					type = VALUES(type), name = VALUES(name), data = VALUES(data),
					rider_type = VALUES(rider_type), rider_name = VALUES(rider_name), rider_data = VALUES(rider_data);
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
			try(Connection con = getConnection()){
				try(PreparedStatement ps = con.prepareStatement("""
					INSERT INTO %s_pets (uuid, saved_type, type, name, data, rider_type, rider_name, rider_data)
					VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE
					saved_type = VALUES(saved_type),
					type = VALUES(type), name = VALUES(name), data = VALUES(data),
					rider_type = VALUES(rider_type), rider_name = VALUES(rider_name), rider_data = VALUES(rider_data);
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
			try(Connection con = getConnection()){
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
			try(Connection con = getConnection()){
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
			try(Connection con = getConnection()){
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
}
