package com.dsh105.echopet.compat.api.util;


/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 25, 2016
 */
public class EntityUtil{

	public static String newToOldEggEntityName(String newName){
		switch (newName){
			case "Ocelot":
				return "Ozelot";
			case "Mooshroom":
				return "MushroomCow";
			case "MagmaCube":
				return "LavaSlime";
			case "Horse":
				return "EntityHorse";
			case "ZombiePigman":
				return "PigZombie";
		}
		return newName;
	}
}
