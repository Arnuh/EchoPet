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

/**
 * Represents a server version that can be utilised as a comparison
 */
public class Version{
	
	private final String version;
	private final int[] numericVersion;
	private boolean snapshot;
	
	/**
	 * Constructs a new Version from the current server version running
	 */
	public Version(){
		this(ReflectionUtil.getMinecraftVersion());
	}
	
	/**
	 * Constructs a new Version from the given server version
	 *
	 * @param version server version e.g. 1.7.1
	 */
	public Version(String version){
		this.version = version;
		this.numericVersion = getNumericVersion(version);
	}
	
	/**
	 * Constructs a new Version from the given numeric server version
	 * <p>
	 * <strong>Not recommended for public API consumption</strong>
	 *
	 * @param numericVersion numeric server version e.g. 1.7.10 would be 1, 7, 10
	 */
	public Version(int[] numericVersion){
		this.numericVersion = numericVersion;
		String ver = "";
		for(int in = 0; in < numericVersion.length; in++){
			ver += in;
			if((in + 1) == numericVersion.length - 1) ver += "-";
			else ver += ".";
		}
		this.version = ver;
	}
	
	/**
	 * Returns an array of integers that represents the given server version
	 *
	 * @param serverVersion version to convert to a numeric array
	 * @return a numeric array representing the {@code serverVersion} given
	 */
	public int[] getNumericVersion(String serverVersion){
		if(!serverVersion.contains("-R") && serverVersion.contains("_")){
			serverVersion = serverVersion.substring(0, serverVersion.indexOf("R") - 1) + "-" + serverVersion.substring(serverVersion.indexOf("R"));
			serverVersion = serverVersion.replace("_", ".");
		}
		Integer rev = null;
		if(serverVersion.contains("-")){// Removes stuff like SNAPSHOT and R
			String in = serverVersion.split("-")[1];
			if(in.equalsIgnoreCase("SNAPSHOT")) snapshot = true;
			in = in.replaceAll("\\D", "");
			if(!in.isEmpty()) rev = ObjectParser.isInt(in);
			serverVersion = serverVersion.split("-")[0];
		}
		// Removes any non-numeric characters and replaces it with a period
		// "+" is to turn groups of non-numeric characters into a single period
		serverVersion = serverVersion.replaceAll("\\D+", ".");
		// Split by period for numeric version comparison
		String[] parts = serverVersion.split("\\.");
		
		int size = serverVersion.split("\\.").length;
		int[] numericVersionParts = new int[size + (rev != null ? 1 : 0)];
		for(int i = 0; i < parts.length; i++){
			try{
				numericVersionParts[i] = ObjectParser.isInt(parts[i]);
			}catch(NullPointerException ex){
			}
		}
		if(rev != null) numericVersionParts[numericVersionParts.length - 1] = rev;
		if(numericVersionParts.length == 0) throw new IllegalArgumentException("Invalid version: " + serverVersion);
		return numericVersionParts;
	}
	
	/**
	 * Gets this version in string format
	 *
	 * @return the version represented by this object as a string
	 */
	public String getVersion(){
		return version;
	}
	
	/**
	 * Returns an array of integers that represents this server version
	 *
	 * @return a numeric array representing this version instance
	 */
	public int[] getNumericVersion(){
		return numericVersion;
	}
	
	/**
	 * @return If the version is a snapshot
	 */
	public boolean isSnapshot(){
		return snapshot;
	}
	
	/**
	 * Returns whether this version is identical to the given version
	 * <p>
	 * For example: 1.20.0 matches 1.20.0, but not 1.20.1 or 1.20.2
	 * <p>
	 * 1.20 will not match with 1.20.0
	 *
	 * @param version server version to make a comparison against e.g. 1.20.0
	 * @return true if the two versions are identical
	 */
	public boolean isIdentical(String version){
		return isIdentical(new Version(version));
	}
	
	/**
	 * Returns whether the given version is compatible with this version
	 * <p>
	 * Makes a comparison to see if the given version is more recent (compatible) or identical
	 * to this version. For example, if this version is 1.19.3, a version of 1.19.4, 1.20, 2.0 will be considered
	 * compatible, whereas 1.19.1 will not
	 * <p>
	 * 1.19 is assumed to be 1.19.0 if compared against a version with 3 or more parts.
	 *
	 * @param version server version to make a comparison against e.g. 1.19.3
	 * @return true if the {@code minimumRequiredVersion} is compatible with this version
	 */
	public boolean isCompatible(String version){
		return isCompatible(new Version(version));
	}
	
	/**
	 * Returns whether this version supports the given version
	 * <p>
	 * Makes a comparison to see if the version given is earlier (supported) or identical
	 * to this version. For example, if this version is 1.19.3, a version of 1.19.2, or 1.18 will be considered
	 * supported, whereas 1.19.4 will not
	 * <p>
	 * 1.19 is assumed to be 1.19.0 if compared against a version with 3 or more parts.
	 *
	 * @param version server version to make a comparison against e.g. 1.19.3
	 * @return true if {@code latestAllowedVersion} is supported by this version
	 */
	public boolean isSupported(String version){
		return isSupported(new Version(version));
	}
	
	/**
	 * Returns whether this version is identical to the given version
	 * <p>
	 * For example: 1.20.0 matches 1.20.0, but not 1.20.1 or 1.20.2
	 * <p>
	 * 1.20 will not match with 1.20.0
	 *
	 * @param version server version to make a comparison against e.g. 1.20.0
	 * @return true if the two versions are identical
	 */
	public boolean isIdentical(int[] version){
		return isIdentical(new Version(version));
	}
	
	/**
	 * Returns whether the given version is compatible with this version
	 * <p>
	 * Makes a comparison to see if the given version is more recent (compatible) or identical
	 * to this version. For example, if this version is 1.19.3, a version of 1.19.4, 1.20, 2.0 will be considered
	 * compatible, whereas 1.19.1 will not
	 * <p>
	 * 1.19 is assumed to be 1.19.0 if compared against a version with 3 or more parts.
	 *
	 * @param version server version to make a comparison against e.g. 1.19.3
	 * @return true if the {@code minimumRequiredVersion} is compatible with this version
	 */
	public boolean isCompatible(int[] version){
		return isCompatible(new Version(version));
	}
	
	/**
	 * Returns whether this version supports the given version
	 * <p>
	 * Makes a comparison to see if the version given is earlier (supported) or identical
	 * to this version. For example, if this version is 1.19.3, a version of 1.19.2, or 1.18 will be considered
	 * supported, whereas 1.19.4 will not
	 * <p>
	 * 1.19 is assumed to be 1.19.0 if compared against a version with 3 or more parts.
	 *
	 * @param version server version to make a comparison against e.g. 1.19.3
	 * @return true if {@code latestAllowedVersion} is supported by this version
	 */
	public boolean isSupported(int[] version){
		return isSupported(new Version(version));
	}
	
	/**
	 * Returns whether this version is identical to the given version
	 * <p>
	 * For example: 1.20.0 matches 1.20.0, but not 1.20.1 or 1.20.2
	 * <p>
	 * 1.20 will not match with 1.20.0
	 *
	 * @param version server version to make a comparison against e.g. 1.20.0
	 * @return true if the two versions are identical
	 */
	public boolean isIdentical(Version version){
		if(isSnapshot() != version.isSnapshot()) return false;
		if(numericVersion.length != version.getNumericVersion().length) return false;
		for(int in = 0; in < numericVersion.length; in++){
			if(numericVersion[in] != version.getNumericVersion()[in]) return false;
		}
		return true;
	}
	
	/**
	 * Returns whether the given version is compatible with this version
	 * <p>
	 * Makes a comparison to see if the given version is more recent (compatible) or identical
	 * to this version. For example, if this version is 1.19.3, a version of 1.19.4, 1.20, 2.0 will be considered
	 * compatible, whereas 1.19.1, 1.19 will not
	 * <p>
	 * 1.19 is assumed to be 1.19.0 if compared against a version with 3 or more parts.
	 *
	 * @param version server version to make a comparison against e.g. 1.19.3
	 * @return true if the {@code minimumRequiredVersion} is compatible with this version
	 */
	public boolean isCompatible(Version version){
		if(isIdentical(version)) return true;
		var compare = version.getNumericVersion();
		for(int in = 0; in < Math.max(numericVersion.length, compare.length); in++){
			int comp = in < compare.length ? compare[in] : 0;
			int curr = in < numericVersion.length ? numericVersion[in] : 0;
			// Prioritize the first number we see.
			if(comp > curr){
				return true;
			}else if(comp < curr){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns whether this version supports the given version
	 * <p>
	 * Makes a comparison to see if the version given is earlier (supported) or identical
	 * to this version. For example, if this version is 1.19.3, a version of 1.19.2, or 1.18 will be considered
	 * supported, whereas 1.19.4 will not.
	 * <p>
	 * 1.19 is assumed to be 1.19.0 if compared against a version with 3 or more parts.
	 *
	 * @param version server version to make a comparison against e.g. 1.19.3
	 * @return true if {@code latestAllowedVersion} is supported by this version
	 */
	public boolean isSupported(Version version){
		if(isIdentical(version)) return true;
		var compare = version.getNumericVersion();
		for(int in = 0; in < Math.max(numericVersion.length, compare.length); in++){
			int comp = in < compare.length ? compare[in] : 0;
			int curr = in < numericVersion.length ? numericVersion[in] : 0;
			if(comp < curr){
				return true;
			}else if(comp > curr){
				return false;
			}
		}
		return true;
	}
}