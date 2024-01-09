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
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * Coordinates migration between table schemas.
 */
public class SQLMigrationHandler{
	
	private static final List<MigrationStrategy> tableMigrationStrategies = new LinkedList<>();
	
	static{
		tableMigrationStrategies.add(con->{
			String prefix = EchoPet.getPlugin().getMainConfig().getString("sql.prefix", "echopet");
			try(PreparedStatement ps = con.prepareStatement("""
				ALTER TABLE `%s_pets`
				CHANGE COLUMN `data` `data` LONGTEXT NOT NULL AFTER `name`,
				CHANGE COLUMN `rider_data` `rider_data` LONGTEXT NULL DEFAULT NULL AFTER `rider_name`;
				""".formatted(prefix))){
				ps.executeUpdate();
			}
		});
	}
	
	public static void handle(Plugin plugin, Connection con, String prefix){
		try{
			int currentVersion = tableMigrationStrategies.size();
			try(PreparedStatement ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS `%s_version` (`id` INT NOT NULL DEFAULT 1, `version` INT NOT NULL, PRIMARY KEY (`id`))".formatted(prefix))){
				ps.executeUpdate();
			}
			try(PreparedStatement ps = con.prepareStatement("SELECT `version` FROM `%s_version`".formatted(prefix))){
				try(ResultSet rs = ps.executeQuery()){
					if(rs.next()){
						currentVersion = rs.getInt("version");
					}else{
						// This will throw an exception if it doesn't exist which means we are higher than version 0.
						try(PreparedStatement ps2 = con.prepareStatement("SELECT OwnerName FROM `EchoPet_version3` LIMIT 1")){
							try(ResultSet rs2 = ps2.executeQuery()){
								try(PreparedStatement ps3 = con.prepareStatement("""
									INSERT INTO %s_pets(uuid, type, name, data, rider_type, rider_name, rider_data)
									SELECT OwnerName, PetType, PetName, PetData, RiderPetType, RiderPetName, RiderPetData
									FROM EchoPet_version3;
									""".formatted(prefix))){
									ps3.executeUpdate();
								}
								rs2.next();
							}
						}catch(SQLException ignore){
						}
					}
				}
			}
			plugin.getLogger().info("SQL version: " + currentVersion);
			int version = currentVersion;
			try{
				for(; version < tableMigrationStrategies.size(); version++){
					plugin.getLogger().info("Upgrading sql to version %d.".formatted(version + 1));
					tableMigrationStrategies.get(version).upgrade(con);
				}
			}catch(SQLException ex){
				plugin.getLogger().log(Level.SEVERE, "Failed to upgrade sql to version %d!".formatted(version + 1), ex);
			}
			try(PreparedStatement ps = con.prepareStatement("INSERT INTO `%s_version` (`id`, `version`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `version` = VALUES(`version`)".formatted(prefix))){
				ps.setInt(1, 1);
				ps.setInt(2, version);
				ps.executeUpdate();
			}
		}catch(SQLException ex){
			plugin.getLogger().log(Level.SEVERE, "Failed to handle migration of SQL.", ex);
		}
	}
	
	/**
	 * Represents a table schema transition
	 */
	interface MigrationStrategy{
		
		/**
		 * Performs the upgrade
		 *
		 * @param con The connection that will be used when applying the upgrade
		 */
		void upgrade(Connection con) throws SQLException;
	}
}
