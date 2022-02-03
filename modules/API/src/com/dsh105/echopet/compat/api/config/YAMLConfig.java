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

package com.dsh105.echopet.compat.api.config;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;

public class YAMLConfig{
	
	private static final Field yamlOptionsField;
	
	static{
		// Spigot API doesn't expose this so fuck them.
		yamlOptionsField = Stream.of("yamlOptions", "yamlDumperOptions")
			.map(YAMLConfig::getYamlConfigurationField)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.findFirst()
			.orElseThrow(()->new IllegalStateException("Cannot find yaml options."));
		yamlOptionsField.setAccessible(true);
	}
	
	private static Optional<Field> getYamlConfigurationField(String field){
		try{
			return Optional.of(YamlConfiguration.class.getDeclaredField(field));
		}catch(Exception ex){
			return Optional.empty();
		}
	}
	
	private int comments;
	private final YAMLConfigManager manager;
	
	private final File file;
	private YamlConfiguration config;
	private final JavaPlugin plugin;
	
	public YAMLConfig(InputStream configStream, File configFile, int comments, JavaPlugin plugin){
		this.comments = comments;
		this.manager = new YAMLConfigManager(plugin);
		
		this.file = configFile;
		try{
			this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(configStream, StandardCharsets.UTF_8));
		}catch(NoSuchMethodError e){
			this.config = YamlConfiguration.loadConfiguration(file);
		}
		this.plugin = plugin;
	}
	
	public FileConfiguration config(){
		return config;
	}
	
	public Object get(String path){
		return this.config.get(path);
	}
	
	public Object get(String path, Object def){
		return this.config.get(path, def);
	}
	
	public String getString(String path){
		return this.config.getString(path);
	}
	
	public String getString(String path, String def){
		return this.config.getString(path, def);
	}
	
	public int getInt(String path){
		return this.config.getInt(path);
	}
	
	public int getInt(String path, int def){
		return this.config.getInt(path, def);
	}
	
	public boolean getBoolean(String path){
		return this.config.getBoolean(path);
	}
	
	public boolean getBoolean(String path, boolean def){
		return this.config.getBoolean(path, def);
	}
	
	public void createSection(String path){
		this.config.createSection(path);
	}
	
	public ConfigurationSection getConfigurationSection(String path){
		return this.config.getConfigurationSection(path);
	}
	
	public double getDouble(String path){
		return this.config.getDouble(path);
	}
	
	public double getDouble(String path, double def){
		return this.config.getDouble(path, def);
	}
	
	public List<?> getList(String path){
		return this.config.getList(path);
	}
	
	public List<?> getList(String path, List<?> def){
		return this.config.getList(path, def);
	}
	
	public List<String> getStringList(String path){
		return this.config.getStringList(path);
	}
	
	public boolean contains(String path){
		return this.config.contains(path);
	}
	
	public void removeKey(String path){
		this.config.set(path, null);
	}
	
	public void set(String path, Object value){
		this.config.set(path, value);
	}
	
	public void set(String path, Object value, String... comments){
		for(String comment : comments){
			if(!this.config.contains(path)){
				this.config.set(manager.getPluginName() + "_COMMENT_" + this.comments, " " + comment);
				this.comments++;
			}
		}
		this.config.set(path, value);
	}
	
	public void setHeader(String[] header){
		manager.setHeader(this.file, header);
		this.comments = header.length + 2;
		this.reloadConfig();
	}
	
	public void setScalarStyle(DumperOptions.ScalarStyle style) throws IllegalAccessException, UnsupportedOperationException{
		if(yamlOptionsField == null) throw new UnsupportedOperationException();
		DumperOptions options = (DumperOptions) yamlOptionsField.get(config);
		options.setDefaultScalarStyle(style);
	}
	
	public void reloadConfig(){
		InputStream configStream = manager.getConfigContent(file);
		try(Reader reader = new InputStreamReader(configStream, StandardCharsets.UTF_8)){
			this.config = YamlConfiguration.loadConfiguration(reader);
		}catch(Exception ignore){
			this.config = YamlConfiguration.loadConfiguration(file);
		}
	}
	
	public void saveConfig(){
		String config = this.config.saveToString();
		manager.saveConfig(config, this.file);
		this.reloadConfig();
	}
	
	public Set<String> getKeys(boolean deep){
		return this.config.getKeys(deep);
	}
}