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

import com.google.common.collect.BiMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

/**
 * General utilities
 */
public class GeneralUtil {
	
	private GeneralUtil() {
	}
	
	private static Random RANDOM;
	
	/**
	 * Returns a random :D
	 *
	 * @return a random something
	 */
	public static Random random() {
		if (RANDOM == null) {
			RANDOM = new Random();
		}
		return RANDOM;
	}
	
	/**
	 * Tests whether or not the given String is an value in a particular Enum
	 * <p>
	 * {@code nameValue} is converted to upper case before being tested
	 *
	 * @param clazz     Enum to test
	 * @param nameValue String value to test
	 * @return true if the given String belongs to the Enum provided
	 */
	public static boolean isEnumType(Class<? extends Enum> clazz, String nameValue) {
		return toEnumType(clazz, nameValue) != null;
	}
	
	/**
	 * Converts the given String to an Enum constant
	 * <p>
	 * {@code nameValue} is converted to upper case before being converted
	 *
	 * @param clazz     Enum to convert to
	 * @param nameValue String value to convert
	 *                  @param <T> enum type
	 * @return an Enum constant belonging to the provided Enum class, or null if the given String does not belong to the
	 * enum provided
	 */
	public static <T extends Enum<T>> T toEnumType(Class<T> clazz, String nameValue) {
		try {
			return Enum.valueOf(clazz, nameValue.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Tests if the given String is an Integer
	 *
	 * @param toTest the String to be checked
	 * @return true if Integer
	 */
	public static boolean isInt(String toTest) {
		try {
			Integer.parseInt(toTest);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}
	
	/**
	 * Tests if the given String is an Double
	 *
	 * @param toTest the String to be checked
	 * @return true if Double
	 */
	public static boolean isDouble(String toTest) {
		try {
			Double.parseDouble(toTest);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}
	
	/**
	 * Parses a location from a set of String arguments
	 * <p>
	 * Arguments must be in the following order:
	 * <ul>
	 * <li>World name
	 * <li>X coordinate
	 * <li>Y coordinate
	 * <li>Z coordinate
	 * <li><b>Optional: </b>Yaw
	 * <li><b>Optional: </b>Pitch
	 * </ul>
	 *
	 * @param startIndex index to start parsing location from
	 * @param args       String arguments to parse location from
	 * @return a Location built from the given args, or null if it could not be parsed
	 * @throws java.lang.IllegalArgumentException if the world at {@code args[startIndex]} does not exist
	 */
	public static Location readLocation(int startIndex, String... args) {
		World world = Bukkit.getWorld(args[startIndex]);
		if (world == null) {
			throw new IllegalArgumentException("World does not exist!");
		}
		double[] coords = new double[5];
		for (int i = startIndex + 1, index = 0; i < startIndex + 6; i++, index++) {
			try {
				coords[index] = toDouble(args[i]);
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
				if (i <= startIndex + 3) {
					// coords MUST exist
					return null;
				}
			}
		}
		
		return new Location(world, coords[0], coords[1], coords[2], (float) coords[3], (float) coords[4]);
	}
	
	/**
	 * Gets the key at the specified value in a key-value map
	 * <p>
	 * <strong>This does NOT take into account the existence of multiple keys with the same value</strong>
	 *
	 * @param map   key-value map to search in
	 * @param value value to retrieve the key for
	 * @param <K>   the type of keys maintained by the given map
	 * @param <V>   the type of mapped values
	 * @return key mapping the given key, or null if not found
	 */
	public static <K, V> K getKeyAtValue(Map<K, V> map, V value) {
		if (map instanceof BiMap) {
			return ((BiMap<K, V>) map).inverse().get(value);
		}
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	/**
	 * Inverts the given map from key-value mappings to value-key mappings
	 * <p>
	 * Note: this may have unintended results if a certain value is included in the map more than once
	 *
	 * @param map map to invert
	 * @param <V> the type of keys maintained by the given map
	 * @param <K> the type of mapped values
	 * @return inverted map
	 */
	public static <V, K> Map<V, K> invertMap(Map<K, V> map) {
		Map<V, K> inverted = new HashMap<V, K>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			inverted.put(entry.getValue(), entry.getKey());
		}
		return inverted;
	}
	
	/**
	 * Attempts to convert a string into an integer value using Regex
	 *
	 * @param string the String to be checked
	 *               @return the converted integer
	 * @throws java.lang.NumberFormatException if the conversion failed
	 */
	public static int toInteger(String string) throws NumberFormatException {
		try {
			return Integer.parseInt(string.replaceAll("[^\\d]", ""));
		} catch (NumberFormatException e) {
			throw new NumberFormatException(string + " isn't a number!");
		}
	}
	
	/**
	 * Attempts to convert a string into an double value using Regex
	 *
	 * @param string the String to be checked
	 * @return the converted double
	 * @throws java.lang.NumberFormatException if the conversion failed
	 */
	public static double toDouble(String string) {
		try {
			return Double.parseDouble(string.replaceAll(".*?([\\d.]+).*", "$1"));
		} catch (NumberFormatException e) {
			throw new NumberFormatException(string + " isn't a number!");
		}
	}
	
	/**
	 * Transforms an integer into a set of digits
	 *
	 * @param number integer to transform
	 * @return a set of digits representing the original number
	 */
	public static int[] toDigits(int number) {
		ArrayList<Integer> digitsList = new ArrayList<>();
		if (number == 0) {
			return new int[]{0};
		}
		while (number != 0) {
			digitsList.add(number % 10);
			number /= 10;
		}
		
		int[] digits = new int[digitsList.size()];
		for (int i = digitsList.size() - 1; i >= 0; i--) {
			digits[i] = digitsList.get(digitsList.size() - i);
		}
		return digits;
	}
	
	/**
	 * Merges a set of arrays into a single list, maintaining original order
	 *
	 * @param arrays set of arrays to combine into a list
	 * @param <T>    type of object included in the set of arrays
	 * @return a combined list including all original objects in the given arrays
	 */
	public static <T> List<T> merge(T[]... arrays) {
		ArrayList<T> merged = new ArrayList<>();
		for (T[] array : arrays) {
			Collections.addAll(merged, array);
		}
		return merged;
	}
}