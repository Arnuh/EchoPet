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

package com.dsh105.echopet;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import javax.sql.DataSource;
import com.codingforcookies.robert.core.Robert;
import com.dsh105.echopet.api.FileDataManager;
import com.dsh105.echopet.api.PetManager;
import com.dsh105.echopet.api.SQLDataManager;
import com.dsh105.echopet.api.updater.JenkinsUpdater;
import com.dsh105.echopet.commands.CommandComplete;
import com.dsh105.echopet.commands.PetAdminCommand;
import com.dsh105.echopet.commands.PetCommand;
import com.dsh105.echopet.commands.util.CommandManager;
import com.dsh105.echopet.commands.util.DynamicPluginCommand;
import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.config.YAMLConfig;
import com.dsh105.echopet.compat.api.config.YAMLConfigManager;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.IDataManager;
import com.dsh105.echopet.compat.api.plugin.IEchoPetPlugin;
import com.dsh105.echopet.compat.api.plugin.IPetManager;
import com.dsh105.echopet.compat.api.plugin.uuid.UUIDMigration;
import com.dsh105.echopet.compat.api.reflection.SafeConstructor;
import com.dsh105.echopet.compat.api.registration.IPetRegistry;
import com.dsh105.echopet.compat.api.util.ISpawnUtil;
import com.dsh105.echopet.compat.api.util.IUpdater;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.Logger;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.dsh105.echopet.compat.api.util.SQLMigrationHandler;
import com.dsh105.echopet.compat.api.util.VersionIncompatibleCommand;
import com.dsh105.echopet.hook.PlaceHolderAPIProvider;
import com.dsh105.echopet.hook.WorldGuardProvider;
import com.dsh105.echopet.listeners.MenuListener;
import com.dsh105.echopet.listeners.PetEntityListener;
import com.dsh105.echopet.listeners.PetOwnerListener;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;

public class EchoPetPlugin extends JavaPlugin implements IEchoPetPlugin{
	
	private static ISpawnUtil SPAWN_UTIL;
	private static PetManager MANAGER;
	private static ConfigOptions OPTIONS;
	
	private IDataManager dataManager;
	private IPetRegistry petRegistry;
	
	private CommandManager COMMAND_MANAGER;
	private YAMLConfigManager configManager;
	private YAMLConfig petConfig;
	private YAMLConfig mainConfig;
	private YAMLConfig langConfig;
	private DataSource dataSource;
	
	// private VanishProvider vanishProvider;
	private WorldGuardProvider worldGuardProvider;
	
	public String prefix = "" + ChatColor.DARK_RED + "[" + ChatColor.RED + "EchoPet" + ChatColor.DARK_RED + "] " + ChatColor.RESET;
	
	public String cmdString = "pet";
	public String adminCmdString = "petadmin";
	
	// Update data
	private IUpdater updater;
	
	@Override
	public void onEnable(){
		EchoPet.setPlugin(this);
		
		this.configManager = new YAMLConfigManager(this);
		COMMAND_MANAGER = new CommandManager(this);
		// Make sure that the plugin is running under the correct version to prevent errors
		
		try{
			Class.forName(ReflectionUtil.COMPAT_NMS_PATH + ".SpawnUtil");
		}catch(ClassNotFoundException e){
			EchoPet.LOG.log(ChatColor.RED + "EchoPet " + ChatColor.GOLD + this.getDescription().getVersion() + ChatColor.RED + " is not compatible with this version of Spigot");
			EchoPet.LOG.log(ChatColor.RED + "Initialisation failed. Please update the plugin.");
			
			DynamicPluginCommand cmd = new DynamicPluginCommand(this.cmdString, new String[0], "", "", new VersionIncompatibleCommand(this.cmdString, prefix, ChatColor.YELLOW + "EchoPet " + ChatColor.GOLD + this.getDescription().getVersion() + ChatColor.YELLOW + " is not compatible with this version of Spigot. Please update the plugin.", "echopet.pet", ChatColor.YELLOW + "You are not allowed to do that."), null, this);
			COMMAND_MANAGER.register(cmd);
			return;
		}
		
		this.petRegistry = new SafeConstructor<IPetRegistry>(ReflectionUtil.getVersionedClass("PetRegistry")).newInstance();
		
		SPAWN_UTIL = new SafeConstructor<ISpawnUtil>(ReflectionUtil.getVersionedClass("SpawnUtil")).newInstance();
		
		this.loadConfiguration();
		
		PluginManager manager = getServer().getPluginManager();
		
		MANAGER = new PetManager();
		
		if(OPTIONS.useSql()){
			if(!prepareSqlDatabase()){
				manager.disablePlugin(this);
				return;
			}
			dataManager = new SQLDataManager(this);
		}else{
			dataManager = new FileDataManager(this);
		}
		
		// Register custom commands
		// Command string based off the string defined in config.yml
		// By default, set to 'pet'
		// PetAdmin command draws from the original, with 'admin' on the end
		this.cmdString = OPTIONS.getCommandString();
		this.adminCmdString = OPTIONS.getCommandString() + "admin";
		DynamicPluginCommand petCmd = new DynamicPluginCommand(this.cmdString, new String[0], "Create and manage your own custom pets.", "Use /" + this.cmdString + " help to see the command list.", new PetCommand(this.cmdString), null, this);
		petCmd.setTabCompleter(new CommandComplete());
		COMMAND_MANAGER.register(petCmd);
		COMMAND_MANAGER.register(new DynamicPluginCommand(this.adminCmdString, new String[0], "Create and manage the pets of other players.", "Use /" + this.adminCmdString + " help to see the command list.", new PetAdminCommand(this.adminCmdString), null, this));
		
		Robert.enablePortable(this);
		// Register listeners
		manager.registerEvents(new MenuListener(), this);
		manager.registerEvents(new PetEntityListener(), this);
		manager.registerEvents(new PetOwnerListener(), this);
		
		this.worldGuardProvider = new WorldGuardProvider(this);
		new PlaceHolderAPIProvider(this);
		
		new Metrics(this, 12900);
		this.updater = new JenkinsUpdater(this);
	}
	
	@Override
	public void onDisable(){
		if(MANAGER != null){
			MANAGER.removeAllPets();
		}
		if(dataSource instanceof Closeable closeable){
			try{
				closeable.close();
			}catch(IOException e){
				getLogger().log(Level.SEVERE, "Failed to close database connection", e);
			}
		}
		
		// Unregister the commands
		this.COMMAND_MANAGER.unregister();
	}
	
	private void loadConfiguration(){
		String[] header = {"EchoPet By DSH105", "Updated by Borlea", "& NobleProductions <3", "---------------------", "Configuration for EchoPet 2", "See the EchoPet Wiki before editing this file"};
		try{
			mainConfig = this.configManager.getNewConfig("config.yml", header);
		}catch(Exception e){
			Logger.log(Logger.LogLevel.WARNING, "Configuration File [config.yml] generation failed.", e, true);
		}
		
		OPTIONS = new ConfigOptions(mainConfig);
		
		try{
			petConfig = this.configManager.getNewConfig("pets.yml");
			petConfig.setScalarStyle(DumperOptions.ScalarStyle.SINGLE_QUOTED);
			petConfig.reloadConfig();
		}catch(Exception e){
			Logger.log(Logger.LogLevel.WARNING, "Configuration File [pets.yml] generation failed.", e, true);
		}
		
		// Make sure to convert those UUIDs!
		if(ReflectionUtil.MC_VERSION_NUMERIC >= 172 && UUIDMigration.supportsUuid() && mainConfig.getBoolean("convertDataFileToUniqueId", true) && petConfig.getConfigurationSection("autosave") != null){
			EchoPet.LOG.info("Converting data files to UUID system...");
			UUIDMigration.migrateConfig(petConfig);
			mainConfig.set("convertDataFileToUniqueId", false);
			mainConfig.saveConfig();
		}
		
		String[] langHeader = {"EchoPet By DSH105", "Updated by Borlea", "& NobleProductions <3", "---------------------", "Language Configuration File"};
		try{
			langConfig = this.configManager.getNewConfig("language.yml", langHeader);
			try{
				for(Lang l : Lang.values){
					String[] desc = l.getDescription();
					langConfig.set(l.getPath(), langConfig.getString(l.getPath(), l.getConfigValue()), desc);
				}
			}catch(Exception e){
				Logger.log(Logger.LogLevel.WARNING, "Configuration File [language.yml] generation failed.", e, true);
			}finally{
				langConfig.saveConfig();
			}
		}catch(Exception e){
			Logger.log(Logger.LogLevel.WARNING, "Configuration File [language.yml] generation failed.", e, true);
		}
		
		if(Lang.PREFIX.getConfigValue().equals("&4[&cEchoPet&4]&r")){
			langConfig.set(Lang.PREFIX.getPath(), "&4[&cEchoPet&4]&r ", Lang.PREFIX.getDescription());
		}
		this.prefix = Lang.PREFIX.toString();
	}
	
	private boolean prepareSqlDatabase(){
		String host = mainConfig.getString("sql.host", "localhost");
		int port = mainConfig.getInt("sql.port", 3306);
		String db = mainConfig.getString("sql.database", "echopet");
		String user = mainConfig.getString("sql.username", "root");
		String pass = mainConfig.getString("sql.password", "");
		String prefix = mainConfig.getString("sql.prefix", "echopet");
		File propertiesFile = new File(getDataFolder(), "hikaricp.properties");
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
						`data` BIGINT NOT NULL DEFAULT 0,
						`rider_type` VARCHAR(255) NULL DEFAULT NULL,
						`rider_name` VARCHAR(255) NULL DEFAULT NULL,
						`rider_data` BIGINT NULL DEFAULT NULL,
						PRIMARY KEY (`uuid`, `saved_type`)
					);""".formatted(prefix))){
					ps.executeUpdate();
					SQLMigrationHandler.handle(this, con, prefix);
				}
				return true;
			}
		}catch(SQLException ex){
			getLogger().log(Level.SEVERE, "Failed to connect to MySQL! [MySQL DataBase: " + db + "].", ex);
			return false;
		}
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, String commandLabel, String[] args){
		/*if(commandLabel.equalsIgnoreCase("ecupdate")){
			if(sender.hasPermission("echopet.update")){
				if(updater.isUpdateFound()){
					updater.update();
				}else{
					sender.sendMessage(this.prefix + ChatColor.GOLD + " An update is not available.");
				}
			}else{
				Lang.sendTo(sender, Lang.NO_PERMISSION.toString().replace("%perm%", "echopet.update"));
			}
			return true;
		}else */
		if(commandLabel.equalsIgnoreCase("echopet")){
			if(sender.hasPermission("echopet.petadmin")){
				PluginDescriptionFile pdFile = this.getDescription();
				sender.sendMessage(ChatColor.RED + "-------- EchoPet --------");
				sender.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.YELLOW + "DSH105, Borlea");
				sender.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.YELLOW + pdFile.getVersion());
				sender.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.YELLOW + pdFile.getWebsite());
				sender.sendMessage(ChatColor.GOLD + "Commands are registered at runtime to provide you with more dynamic control over the command labels.");
				sender.sendMessage(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Command Registration:");
				sender.sendMessage(ChatColor.GOLD + "Main: " + EchoPetPlugin.OPTIONS.getCommandString());
				sender.sendMessage(ChatColor.GOLD + "Admin: " + EchoPetPlugin.OPTIONS.getCommandString() + "admin");
			}else{
				Lang.sendTo(sender, Lang.NO_PERMISSION.toString().replace("%perm%", "echopet.petadmin"));
				return true;
			}
		}
		return false;
	}
	
	@Override
	public YAMLConfig getPetConfig(){
		return this.petConfig;
	}
	
	@Override
	public YAMLConfig getMainConfig(){
		return mainConfig;
	}
	
	@Override
	public YAMLConfig getLangConfig(){
		return langConfig;
	}
	
	@Override
	public ISpawnUtil getSpawnUtil(){
		return SPAWN_UTIL;
	}
	
	@Override
	public WorldGuardProvider getWorldGuardProvider(){
		return worldGuardProvider;
	}
	
	@Override
	public String getPrefix(){
		return prefix;
	}
	
	@Override
	public IPetRegistry getPetRegistry(){
		return this.petRegistry;
	}
	
	@Override
	public IPetManager getPetManager(){
		return MANAGER;
	}
	
	@Override
	public ConfigOptions getOptions(){
		return OPTIONS;
	}
	
	@Override
	public IDataManager getDataManager(){
		return dataManager;
	}
	
	@Override
	public Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
	
	@Override
	public String getCommandString(){
		return cmdString;
	}
	
	@Override
	public String getAdminCommandString(){
		return adminCmdString;
	}
	
	@Override
	public IUpdater getUpdater(){
		return updater;
	}
}
