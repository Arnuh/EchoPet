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


import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

/**
 * @since Jun 24, 2016
 */
public class StringUtil{
	
	/**
	 * Represents an empty array of strings
	 */
	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final Pattern DIACRITICS_AND_FRIENDS = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");
	
	private StringUtil(){
	}
	
	/**
	 * Remove the specified ChatColors from a String
	 *
	 * @param input          String to be sanitized
	 * @param colorsToRemove ChatColors to be removed
	 * @return sanitized input
	 */
	public String removeColor(String input, ChatColor... colorsToRemove){
		String result = input;
		for(ChatColor color : colorsToRemove){
			input = input.replaceAll("(?i)" + ChatColor.COLOR_CHAR + color.getChar(), "");
		}
		return result;
	}
	
	/**
	 * Convert a number of Objects into Strings
	 *
	 * @param listToConvert Objects to be converted
	 * @return String[] Objects converted into Strings
	 */
	public static String[] convert(List<?> listToConvert){
		if(listToConvert.size() <= 0){
			return StringUtil.EMPTY_STRING_ARRAY;
		}
		String[] stringArray = new String[listToConvert.size()];
		for(int i = 0; i < stringArray.length; i++){
			stringArray[i] = listToConvert.get(i).toString();
		}
		return stringArray;
	}
	
	/**
	 * Convert a number of Objects into Strings
	 *
	 * @param arrayToConvert Objects to be converted
	 * @return String[] Objects converted into Strings
	 */
	public static String[] convert(Object... arrayToConvert){
		if(arrayToConvert instanceof String[]){
			return (String[]) arrayToConvert;
		}
		return convert(Arrays.asList(arrayToConvert));
	}
	
	public static String capitalise(String string){
		return capitalise(string, true);
	}
	
	/**
	 * Capitalizes the first letter of a String
	 *
	 * @param string         the String to be capitalized
	 * @param forceLowerCase whether to force lower case on other parts of the String
	 * @return capitalized String
	 */
	public static String capitalise(String string, boolean forceLowerCase){
		String[] parts = string.split("\\s+");
		for(int i = 0; i < parts.length; i++){
			if(parts[i].length() > 0){
				parts[i] = parts[i].substring(0, 1).toUpperCase() + (forceLowerCase ? parts[i].substring(1).toLowerCase() : parts[i].substring(1));
			}
		}
		return combineArray(0, " ", parts);
	}
	
	/**
	 * Separates a string array from the given start index
	 *
	 * @param startIndex index to begin the separation at, inclusive
	 * @param string     string array to separate
	 * @return the new separated array of strings
	 */
	public static String[] separate(int startIndex, String... string){
		if(startIndex >= string.length || string.length <= 0){
			return new String[0];
		}
		String[] str = new String[string.length - startIndex];
		System.arraycopy(string, startIndex, str, startIndex, string.length - startIndex);
		return str;
	}
	
	/**
	 * Builds a sentence list from an array of strings.
	 * Example: {"one", "two", "three"} returns "one, two and three".
	 *
	 * @param words The string array to build into a list,
	 * @return String representing the list.
	 */
	public static String buildSentenceList(String... words){
		Validate.notEmpty(words);
		if(words.length == 1){
			return words[0];
		}else if(words.length == 2){
			return combineArray(0, " and ", words);
		}else{
			// This is where the fun starts!
			String[] initial = Arrays.copyOfRange(words, 0, words.length - 1);
			String list = combineArray(0, ", ", initial);
			list += " and " + words[words.length - 1];
			return list;
		}
	}
	
	/**
	 * Combines a set of strings into a single string, separated by the given character set
	 *
	 * @param startIndex index to begin the separation at, inclusive
	 * @param string     array to combine
	 * @param separator  character set included between each part of the given array
	 * @return the combined string
	 * @deprecated use {@link #combineArray(int, String, String...)}
	 */
	@Deprecated
	public static String combineSplit(int startIndex, String[] string, String separator){
		return combineArray(startIndex, separator, string);
	}
	
	/**
	 * Combines a set of strings into a single string, separated by the given character set
	 *
	 * @param separator   character set included between each part of the given array
	 * @param stringArray array to combine
	 * @return the combined string
	 */
	public static String combineArray(String separator, String... stringArray){
		return combineArray(0, separator, stringArray);
	}
	
	/**
	 * Combines a set of strings into a single string, separated by the given character set
	 *
	 * @param startIndex  index to begin the separation at, inclusive
	 * @param separator   character set included between each part of the given array
	 * @param stringArray array to combine
	 * @return the combined string
	 */
	public static String combineArray(int startIndex, String separator, String... stringArray){
		return combineArray(startIndex, stringArray.length, separator, stringArray);
	}
	
	/**
	 * Combines a set of strings into a single string, separated by the given character set
	 *
	 * @param startIndex  index to begin the separation at, inclusive
	 * @param endIndex    index to end the separation at, exclusive
	 * @param separator   character set included between each part of the given array
	 * @param stringArray array to combine
	 * @return the combined string
	 */
	public static String combineArray(int startIndex, int endIndex, String separator, String... stringArray){
		if(stringArray == null || startIndex >= endIndex){
			return "";
		}else{
			StringBuilder builder = new StringBuilder();
			for(int i = startIndex; i < endIndex; i++){
				builder.append(stringArray[i]);
				builder.append(separator);
			}
			builder.delete(builder.length() - separator.length(), builder.length());
			return builder.toString();
		}
	}
	
	/**
	 * Combines a collection of strings into a single string, separated by the given character set
	 *
	 * @param separator        character set included between each part of the given array
	 * @param stringCollection collection of strings to combine
	 * @return the combined string
	 */
	public static String combine(String separator, Collection<String> stringCollection){
		return combineArray(separator, stringCollection.toArray(EMPTY_STRING_ARRAY));
	}
	
	/**
	 * Combines a collection of strings into a single string, separated by the given character set
	 *
	 * @param startIndex       index to begin the separation at, inclusive
	 * @param separator        character set included between each part of the given array
	 * @param stringCollection collection of strings to combine
	 * @return the combined string
	 */
	public static String combine(int startIndex, String separator, Collection<String> stringCollection){
		return combineArray(startIndex, separator, stringCollection.toArray(EMPTY_STRING_ARRAY));
	}
	
	/**
	 * Combines a collection of strings into a single string, separated by the given character set
	 *
	 * @param startIndex       index to begin the separation at, inclusive
	 * @param endIndex         index to end the separation at, exclusive
	 * @param separator        character set included between each part of the given array
	 * @param stringCollection collection of strings to combine
	 * @return the combined string
	 */
	public static String combine(int startIndex, int endIndex, String separator, Collection<String> stringCollection){
		return combineArray(startIndex, endIndex, separator, stringCollection.toArray(EMPTY_STRING_ARRAY));
	}
	
	/**
	 * Combines and splits a set of strings into a new array, such that the length of the new array is
	 * originalLength-{@code startIndex}
	 *
	 * @param startIndex  index to begin the separation at, inclusive
	 * @param separator   character set included between each part of the given array
	 * @param stringArray array to combine and split
	 * @return the newly formed array
	 */
	public static String[] splitArgs(int startIndex, String separator, String... stringArray){
		return splitArgs(startIndex, stringArray.length, separator, stringArray);
	}
	
	/**
	 * Combines and splits a set of strings into a new array, such that the length of the new array is
	 * originalLength-{@code startIndex}
	 *
	 * @param startIndex  index to begin the separation at, inclusive
	 * @param endIndex    index to end the separation at, exclusive
	 * @param separator   character set included between each part of the given array
	 * @param stringArray array to combine and split
	 * @return the newly formed array
	 */
	public static String[] splitArgs(int startIndex, int endIndex, String separator, String... stringArray){
		String combined = combineArray(startIndex, endIndex, separator, stringArray);
		if(combined.isEmpty()){
			return new String[0];
		}
		return combined.split(separator);
	}
	
	/**
	 * Strips all diacritics (special characters) from the given string
	 * <p/>
	 * From http://stackoverflow.com/a/1453284
	 *
	 * @param input string to remove diacritics from
	 * @return the stripped string
	 */
	public static String stripDiacritics(String input){
		input = Normalizer.normalize(input, Normalizer.Form.NFD);
		input = DIACRITICS_AND_FRIENDS.matcher(input).replaceAll("");
		return input;
	}
	
	/**
	 * Convert a String to a UUID
	 * <p/>
	 * This method will add the required dashes, if not found in String
	 *
	 * @param input UUID to convert
	 * @return UUID converted UUID
	 * @throws IllegalArgumentException if the input could not be converted
	 */
	public static UUID convertUUID(String input) throws IllegalArgumentException{
		try{
			return UUID.fromString(input);
		}catch(IllegalArgumentException ex){
			return UUID.fromString(input.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
		}
	}
	
	/**
	 * Limits a String to an amount of characters
	 *
	 * @param input     string to limit character length
	 * @param maxLength maximum length of characters allowed
	 * @return the {@code input} limited to {@code maxLength} characters
	 */
	public static String limitCharacters(String input, int maxLength){
		if(input.length() <= maxLength){
			return input;
		}
		
		return input.substring(0, maxLength);
	}
	
	/**
	 * Makes an enum name human readable (fixes spaces, capitalization, etc)
	 *
	 * @param enumName The name of the enum to neaten up.
	 * @return The human-readable enum name.
	 */
	public static String makeEnumHumanReadable(String enumName){
		StringBuilder builder = new StringBuilder(enumName.length() + 1);
		String[] words = enumName.split("_");
		for(String word : words){
			if(word.length() <= 2){
				builder.append(word); // assume that it's an abbrevation
			}else{
				builder.append(word.charAt(0));
				builder.append(word.substring(1).toLowerCase());
			}
			builder.append(' ');
		}
		return builder.substring(0, enumName.length());
	}
}
