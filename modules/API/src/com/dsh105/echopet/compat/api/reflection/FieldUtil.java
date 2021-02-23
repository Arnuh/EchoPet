package com.dsh105.echopet.compat.api.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Arnah
 * @since Feb 21, 2021
 **/
public class FieldUtil{
	
	public static void setFinalStatic(Field field, Object obj, Object value) throws Exception{
		field.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		
		field.set(obj, value);
	}
}