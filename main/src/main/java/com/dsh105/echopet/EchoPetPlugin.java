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

import java.util.logging.Level;
import javax.annotation.Nonnull;
import com.codingforcookies.robert.core.Robert;
import com.dsh105.echopet.api.FileStorageManager;
import com.dsh105.echopet.api.MySQLStorageManager;
import com.dsh105.echopet.api.PetManager;
import com.dsh105.echopet.api.SQLiteStorageManager;
import com.dsh105.echopet.api.updater.JenkinsUpdater;
import com.dsh105.echopet.commands.CommandComplete;
import com.dsh105.echopet.commands.PetAdminCommand;
import com.dsh105.echopet.commands.PetCommand;
import com.dsh105.echopet.commands.util.CommandManager;
import com.dsh105.echopet.commands.util.DynamicPluginCommand;
import com.dsh105.echopet.compat.api.config.CategoryConfigOptions;
import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.config.YAMLConfig;
import com.dsh105.echopet.compat.api.config.YAMLConfigManager;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.IEchoPetPlugin;
import com.dsh105.echopet.compat.api.plugin.IPetManager;
import com.dsh105.echopet.compat.api.plugin.IStorageManager;
import com.dsh105.echopet.compat.api.registration.IPetRegistry;
import com.dsh105.echopet.compat.api.util.ICraftBukkitUtil;
import com.dsh105.echopet.compat.api.util.ISpawnUtil;
import com.dsh105.echopet.compat.api.util.IUpdater;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.dsh105.echopet.compat.api.util.VersionIncompatibleCommand;
import com.dsh105.echopet.hook.PlaceHolderAPIProvider;
import com.dsh105.echopet.hook.WorldGuardProvider;
import com.dsh105.echopet.listeners.MenuListener;
import com.dsh105.echopet.listeners.PetEntityListener;
import com.dsh105.echopet.listeners.PetOwnerListener;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;

public class EchoPetPlugin extends JavaPlugin implements IEchoPetPlugin{
	
	private static ISpawnUtil SPAWN_UTIL;
	private static PetManager MANAGER;
	private static ConfigOptions OPTIONS;
	
	private IStorageManager dataManager;
	private IPetRegistry petRegistry;
	private ICraftBukkitUtil craftBukkitUtil;
	private CommandManager COMMAND_MANAGER;
	private YAMLConfigManager configManager;
	private YAMLConfig petConfig, petCategoryConfig;
	private YAMLConfig mainConfig;
	private YAMLConfig langConfig;
	
	// private VanishProvider vanishProvider;
	private WorldGuardProvider worldGuardProvider;
	
	public String prefix = "" + ChatColor.DARK_RED + "[" + ChatColor.RED + "EchoPet" + ChatColor.DARK_RED + "] " + ChatColor.RESET;
	
	public String cmdString = "pet";
	public String adminCmdString = "petadmin";
	private IUpdater updater;
	public NamespacedKey petNamespacedKey;
	
	@Override
	public void onEnable(){
		EchoPet.setPlugin(this);
		
		this.configManager = new YAMLConfigManager(this);
		COMMAND_MANAGER = new CommandManager(this);
		// Make sure that the plugin is running under the correct version to prevent errors
		
		getLogger().info("Found MC Version %s".formatted(ReflectionUtil.getMinecraftVersion()));
		try{
			COMMAND_MANAGER.initialize();
			SPAWN_UTIL = ReflectionUtil.getVersionedClass(ISpawnUtil.class, "SpawnUtil").getConstructor().newInstance();
			petRegistry = ReflectionUtil.getVersionedClass(IPetRegistry.class, "PetRegistry").getConstructor().newInstance();
			craftBukkitUtil = ReflectionUtil.getVersionedClass(ICraftBukkitUtil.class, "CraftBukkitUtil").getConstructor().newInstance();
		}catch(Exception ex){
			getLogger().warning("EchoPet " + ChatColor.GOLD + this.getDescription()
				.getVersion() + ChatColor.RED + " is not compatible with this version of Minecraft");
			getLogger().warning("Initialisation failed. Please update the plugin.");
			
			DynamicPluginCommand cmd = new DynamicPluginCommand(this.cmdString, new String[0], "", "", new VersionIncompatibleCommand(this.cmdString, prefix, ChatColor.YELLOW + "EchoPet " + ChatColor.GOLD + this.getDescription()
				.getVersion() + ChatColor.YELLOW + " is not compatible with this version of Paper. Please update the plugin.", "echopet.pet", ChatColor.YELLOW + "You are not allowed to do that."), null, this);
			COMMAND_MANAGER.register(cmd);
			return;
		}
		
		this.loadConfiguration();
		
		PluginManager manager = getServer().getPluginManager();
		
		MANAGER = new PetManager();
		
		switch(OPTIONS.getStorageType()){
			case YAML -> this.dataManager = new FileStorageManager(this);
			case MySQL -> this.dataManager = new MySQLStorageManager(this);
			case SQLite -> this.dataManager = new SQLiteStorageManager(this);
		}
		if(!dataManager.init()){
			manager.disablePlugin(this);
			return;
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
		manager.registerEvents(new PetEntityListener(this), this);
		manager.registerEvents(new PetOwnerListener(this), this);
		
		this.worldGuardProvider = new WorldGuardProvider(this);
		new PlaceHolderAPIProvider(this);
		
		Metrics metrics = new Metrics(this, 12900);
		metrics.addCustomChart(new SimplePie("data_storage_type", ()->OPTIONS.getStorageType().name()));
		this.updater = new JenkinsUpdater(this);
		
		this.petNamespacedKey = new NamespacedKey(this, "pet");
	}
	
	@Override
	public void onDisable(){
		if(MANAGER != null){
			MANAGER.removeAllPets();
		}
		if(dataManager != null){
			dataManager.shutdown();
		}
		
		// Unregister the commands
		this.COMMAND_MANAGER.unregister();
	}
	
	private void loadConfiguration(){
		String[] header = {"EchoPet By DSH105",
			"Updated by Borlea",
			"& NobleProductions <3",
			"---------------------",
			"Configuration for EchoPet 2",
			"See the EchoPet Wiki before editing this file"};
		// Load categories first because PetData default config is dependent on category petdata being loaded.
		try{
			petCategoryConfig = configManager.getNewConfig("pet-categories.yml");
			petCategoryConfig.setScalarStyle(DumperOptions.ScalarStyle.SINGLE_QUOTED);
			new CategoryConfigOptions(petCategoryConfig);
		}catch(Exception ex){
			getLogger().log(Level.WARNING, "Configuration File [pets-categories.yml] generation failed.", ex);
		}
		try{
			mainConfig = this.configManager.getNewConfig("config.yml", header);
		}catch(Exception e){
			getLogger().log(Level.WARNING, "Configuration File [config.yml] generation failed.", e);
		}
		
		OPTIONS = new ConfigOptions(mainConfig);
		
		try{
			petConfig = this.configManager.getNewConfig("pets.yml");
			petConfig.setScalarStyle(DumperOptions.ScalarStyle.SINGLE_QUOTED);
			petConfig.reloadConfig();
		}catch(Exception e){
			getLogger().log(Level.WARNING, "Configuration File [pets.yml] generation failed.", e);
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
				getLogger().log(Level.WARNING, "Configuration File [language.yml] generation failed.", e);
			}finally{
				langConfig.saveConfig();
			}
		}catch(Exception e){
			getLogger().log(Level.WARNING, "Configuration File [language.yml] generation failed.", e);
		}
		
		if(Lang.PREFIX.getConfigValue().equals("&4[&cEchoPet&4]&r")){
			langConfig.set(Lang.PREFIX.getPath(), "&4[&cEchoPet&4]&r ", Lang.PREFIX.getDescription());
		}
		this.prefix = Lang.PREFIX.toString();
	}
	
	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, String commandLabel, String[] args){
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
		if(commandLabel.equalsIgnoreCase("com/dsh105/echopet")){
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
	public YAMLConfig getPetCategoryConfig(){
		return petCategoryConfig;
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
	public ICraftBukkitUtil getCraftBukkitUtil(){
		return craftBukkitUtil;
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
	public IStorageManager getStorageManager(){
		return dataManager;
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
	
	@Override
	public NamespacedKey getPetNamespacedKey(){
		return petNamespacedKey;
	}
}
