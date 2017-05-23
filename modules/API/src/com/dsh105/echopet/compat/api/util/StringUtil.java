package com.dsh105.echopet.compat.api.util;


/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Jun 24, 2016
 */
public class StringUtil{

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
