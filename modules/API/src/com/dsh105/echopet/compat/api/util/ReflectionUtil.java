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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.Bukkit;
import org.bukkit.Server;

@SuppressWarnings("unchecked")
public class ReflectionUtil{
	
	private static final Pattern MINECRAFT_VERSION_MATCHER = Pattern.compile("\\(.*MC.\\s*([a-zA-z0-9\\-\\.]+)\\s*\\)");
	private static final Pattern PACKAGE_VERSION_MATCHER = Pattern.compile("(\\d+_\\d+_\\w*\\d+)"); // Should \w not be just an R?
	
	/**
	 * com.dsh105.echopet.compat.nms.v1_19_1
	 */
	public static String COMPAT_NMS_PATH = prepareCompatNmsPath();
	private static String MINECRAFT_VERSION = null;
	private static String CRAFTBUKKIT_PACKAGE_VERSION = null;
	
	/**
	 * @return Version formatted like "1.19.1"
	 */
	public static String getMinecraftVersion(){
		// Bukkit.getVersion returns a string like "git-Paper-86 (MC: 1.19.1)"
		// Bukkit.getBukkitVersion returns a string like 1.19.1-R0.1-SNAPSHOT
		// Need to just assume these are updated properly.
		if(MINECRAFT_VERSION != null){
			return MINECRAFT_VERSION;
		}
		String version = Bukkit.getVersion();
		Matcher matcher = MINECRAFT_VERSION_MATCHER.matcher(version);
		
		if(matcher.find()){
			version = matcher.group();
			version = version.substring(version.indexOf(": ") + 2, version.lastIndexOf(")"));
			return MINECRAFT_VERSION = version;
		}
		
		version = Bukkit.getBukkitVersion();
		version = version.substring(0, version.indexOf("-"));
		return MINECRAFT_VERSION = version;
	}
	
	/**
	 *
	 * @return Version formatted like "1_19_R1"
	 */
	public static String getCraftBukkitPackageVersion(){
		if(CRAFTBUKKIT_PACKAGE_VERSION != null){
			return CRAFTBUKKIT_PACKAGE_VERSION;
		}
		Server bukkitServer = Bukkit.getServer();
		Class<?> craftServerClass = bukkitServer.getClass();
		String packageName = craftServerClass.getCanonicalName();// org.bukkit.craftbukkit.v1_19_R1.CraftServer
		Matcher matcher = PACKAGE_VERSION_MATCHER.matcher(packageName);
		if(matcher.find()){
			return CRAFTBUKKIT_PACKAGE_VERSION = matcher.group(); // 1_19_R1
		}else{
			return null;
		}
	}
	
	private static String prepareCompatNmsPath(){
		return "com.dsh105.echopet.compat.nms.v" + getMinecraftVersion().replace(".", "_");
	}
	
	/**
	 * com.dsh105.echopet.compat.nms.v1_19_1.classPath
	 */
	public static <T> Class<T> getVersionedClass(Class<T> clazz, String classPath) throws ClassNotFoundException, NoClassDefFoundError{
		return (Class<T>) Class.forName(COMPAT_NMS_PATH + "." + classPath);
	}
	
	@Deprecated
	public static int MC_VERSION_NUMERIC = getMinecraftVersion().length() == 0 ? 0 : Integer.valueOf(getMinecraftVersion().replaceAll("[^0-9]", ""));
	
	@Deprecated
	public static boolean isSpigot1dot8(){
		try{
			// not ideal, but it's the only class needed
			Class.forName("org.spigotmc.ProtocolData");
			return true;
		}catch(ClassNotFoundException e){
			return false;
		}
	}
	
	@Deprecated
	public static String getNMSPackageName(){
		return "net.minecraft.server." + getMinecraftVersion();
	}
	
	@Deprecated
	public static String getCBCPackageName(){
		return "org.bukkit.craftbukkit." + getMinecraftVersion();
	}
	
	/**
	 * Class stuff
	 */
	
	public static <T> Class<T> getClass(Class<T> clazz, String name) throws ClassNotFoundException{
		return (Class<T>) Class.forName(name);
	}
	
	@SuppressWarnings("rawtypes")
	@Deprecated
	public static Class getClass(String name){
		try{
			return Class.forName(name);
		}catch(ClassNotFoundException e){
			EchoPet.LOG.warning("Could not find class: " + name + "!");
			e.printStackTrace();
			return null;
		}
	}
}
