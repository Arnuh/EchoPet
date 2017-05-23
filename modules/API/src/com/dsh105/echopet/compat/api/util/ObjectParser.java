package com.dsh105.echopet.compat.api.util;


/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Jun 24, 2016
 */
public class ObjectParser{

	/**
	 * @param input The string you would like to parse to a Byte
	 * @return The Byte object, null if not a Byte.
	 */
	public static Byte isByte(String input){
		try{
			return Byte.parseByte(input);
		}catch(NumberFormatException nfe){
			return null;
		}
	}

	/**
	 * @param input The string you would like to parse to a Short
	 * @return The Short object, null if not a Short.
	 */
	public static Short isShort(String input){
		try{
			return Short.parseShort(input);
		}catch(NumberFormatException nfe){
			return null;
		}
	}

	/**
	 * @param input The string you would like to parse to a Integer
	 * @return The Integer object, null if not a Integer.
	 */
	public static Integer isInt(String input){
		try{
			return Integer.parseInt(input);
		}catch(NumberFormatException nfe){
			return null;
		}
	}

	/**
	 * @param input The string you would like to parse to a Long
	 * @return The Long object, null if not a Long.
	 */
	public static Long isLong(String input){
		try{
			return Long.parseLong(input);
		}catch(NumberFormatException nfe){
			return null;
		}
	}

	/**
	 * @param input The string you would like to parse to a Double
	 * @return The Double object, null if not a Double.
	 */
	public static Double isDouble(String input){
		try{
			return Double.parseDouble(input);
		}catch(NumberFormatException nfe){
			return null;
		}
	}

	/**
	 * @param input The string you would like to parse to a Float
	 * @return The Float object, null if not a Float.
	 */
	public static Float isFloat(String input){
		try{
			return Float.parseFloat(input);
		}catch(NumberFormatException nfe){
			return null;
		}
	}
}