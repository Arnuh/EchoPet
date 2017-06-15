package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.api.pet.Pet;
import com.dsh105.echopet.compat.api.entity.type.pet.IIllagerAbstractPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since May 23, 2017
 */
public class IllagerAbstractPet extends Pet implements IIllagerAbstractPet{

	public IllagerAbstractPet(Player owner){
		super(owner);
	}
}
