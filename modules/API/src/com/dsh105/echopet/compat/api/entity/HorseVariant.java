package com.dsh105.echopet.compat.api.entity;


/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
public enum HorseVariant{
	/**
	 * A normal horse
	 */
	HORSE,
	/**
	 * A donkey
	 */
	DONKEY,
	/**
	 * A mule
	 */
	MULE,
	/**
	 * An undead horse
	 */
	UNDEAD_HORSE,
	/**
	 * A skeleton horse
	 */
	SKELETON_HORSE,
	/**
	 * Not really a horse :)
	 */
	LLAMA;

	public boolean hasChest(){
		return this == DONKEY || this == MULE;
	}
}