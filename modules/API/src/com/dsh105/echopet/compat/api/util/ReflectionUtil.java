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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.reflection.utility.CommonReflection;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;

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
		
		if(matcher.matches()){
			return MINECRAFT_VERSION = matcher.group(1);
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
		String packageName = craftServerClass.getCanonicalName();// org.bukkit.craftbukkit.v1_17_R1
		Matcher matcher = PACKAGE_VERSION_MATCHER.matcher(packageName);
		
		if(matcher.matches()){
			return CRAFTBUKKIT_PACKAGE_VERSION = matcher.group(1);
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
	public static Class<?> getVersionedClass(String classPath) throws ClassNotFoundException, NoClassDefFoundError{
		return Class.forName(COMPAT_NMS_PATH + "." + classPath);
	}
	
	@Deprecated
	public static int MC_VERSION_NUMERIC = getMinecraftVersion().length() == 0 ? 0 : Integer.valueOf(getMinecraftVersion().replaceAll("[^0-9]", ""));
	@Deprecated
	public static int BUKKIT_VERSION_NUMERIC = getBukkitVersion().length() == 0 ? 0 : Integer.valueOf(getBukkitVersion().replaceAll("[^0-9]", ""));
	
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
	
	public static Object getEntityHandle(Entity entity){
		return invokeMethod(getMethod(getCBCClass("entity.CraftEntity"), "getHandle"), entity);
	}
	
	// Thanks ProtocolLib <3
	@Deprecated
	public static String getBukkitVersion(){
		try{
			Pattern versionPattern = Pattern.compile(".*\\(.*MC.\\s*([a-zA-z0-9\\-\\.]+)\\s*\\)");
			Matcher version = versionPattern.matcher(Bukkit.getServer().getVersion());
			if(version.matches() && version.group(1) != null){
				return version.group(1);
			}else{
				return "";
			}
		}catch(NullPointerException ex){
			return "";
		}
	}
	
	@Deprecated
	public static boolean isServerMCPC(){
		return Bukkit.getVersion().contains("MCPC-Plus");
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
	
	@Deprecated
	public static Class<?> getNMSClass(String className){
		return CommonReflection.getMinecraftClass(className);
	}
	
	@Deprecated
	public static Class<?> getCBCClass(String className){
		return CommonReflection.getCraftBukkitClass(className);
	}
	
	/**
	 * Field stuff
	 */
	@Deprecated
	public static Field getField(Class<?> clazz, String fieldName){
		try{
			Field field = clazz.getDeclaredField(fieldName);
			
			if(!field.isAccessible()){
				field.setAccessible(true);
			}
			
			return field;
		}catch(NoSuchFieldException e){
			EchoPet.LOG.warning("No such field: " + fieldName + "!");
			e.printStackTrace();
			return null;
		}
	}
	
	@Deprecated
	public static <T> T getField(Class<?> clazz, String fieldName, Object instance){
		try{
			return (T) getField(clazz, fieldName).get(instance);
		}catch(IllegalAccessException e){
			EchoPet.LOG.warning("Failed to access field: " + fieldName + "!");
			e.printStackTrace();
			return null;
		}
	}
	
	@Deprecated
	public static void setField(Class<?> clazz, String fieldName, Object instance, Object value){
		try{
			getField(clazz, fieldName).set(instance, value);
		}catch(IllegalAccessException e){
			EchoPet.LOG.warning("Could not set new field value for: " + fieldName);
			e.printStackTrace();
		}
	}
	
	@Deprecated
	public static <T> T getField(Field field, Object instance){
		try{
			return (T) field.get(instance);
		}catch(IllegalAccessException e){
			EchoPet.LOG.warning("Failed to retrieve field: " + field.getName());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Method stuff
	 */
	@Deprecated
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params){
		try{
			return clazz.getDeclaredMethod(methodName, params);
		}catch(NoSuchMethodException e){
			EchoPet.LOG.warning("No such method: " + methodName + "!");
			e.printStackTrace();
			return null;
		}
	}
	
	@Deprecated
	public static <T> T invokeMethod(Method method, Object instance, Object... args){
		try{
			return (T) method.invoke(instance, args);
		}catch(IllegalAccessException e){
			EchoPet.LOG.warning("Failed to access method: " + method.getName() + "!");
			return null;
		}catch(InvocationTargetException e){
			EchoPet.LOG.warning("Failed to invoke method: " + method.getName() + "!");
			e.printStackTrace();
			return null;
		}
	}
}
