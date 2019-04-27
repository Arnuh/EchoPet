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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.api.entity;

import java.util.List;

import com.dsh105.echopet.compat.api.util.Version;
import com.dsh105.echopet.compat.api.util.menu.DataMenu.DataMenuType;
import com.google.common.collect.ImmutableList;

public enum PetData {

	ANGRY("angry", DataMenuType.BOOLEAN),
	BABY("baby", DataMenuType.BOOLEAN),
	BLACK("black", DataMenuType.COLOR, DataMenuType.OCELOT_TYPE, DataMenuType.HORSE_VARIANT, DataMenuType.RABBIT_TYPE, DataMenuType.LLAMA_COLOR),
	BLACK_AND_WHITE("blackandwhite", DataMenuType.RABBIT_TYPE),
	BLACKSMITH("blacksmith", DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION),
	BLACK_DOTS("blackSpot", DataMenuType.HORSE_MARKING),
	BLUE("blue", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR, DataMenuType.PARROT_VARIANT),
	BROWN("brown", DataMenuType.COLOR, DataMenuType.RABBIT_TYPE, DataMenuType.LLAMA_COLOR),
	BROWN_LLAMA("brown", DataMenuType.LLAMA_VARIANT),
	BUTCHER("butcher", DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION),
	CHESTED("chested", DataMenuType.BOOLEAN),
	CHESTNUT("chestnut", DataMenuType.HORSE_VARIANT),
	CREAMY("creamy", DataMenuType.HORSE_VARIANT, DataMenuType.LLAMA_VARIANT),
	CYAN("cyan", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR, DataMenuType.PARROT_VARIANT),
	DARK_BROWN("darkbrown", DataMenuType.HORSE_VARIANT),
	DIAMOND("diamond", DataMenuType.HORSE_ARMOUR),
	DONKEY("donkey", DataMenuType.HORSE_TYPE),
	ELDER("elder", DataMenuType.BOOLEAN),
	FARMER("farmer", DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION),
	FIRE("fire", DataMenuType.BOOLEAN),
	GRAY("gray", DataMenuType.COLOR, DataMenuType.HORSE_VARIANT, DataMenuType.LLAMA_COLOR, DataMenuType.PARROT_VARIANT),
	GRAY_LLAMA("gray", DataMenuType.LLAMA_VARIANT),
	SILVER("silver", DataMenuType.COLOR, DataMenuType.HORSE_VARIANT, DataMenuType.LLAMA_COLOR),
	GREEN("green", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR, DataMenuType.PARROT_VARIANT),
	GOLD("gold", DataMenuType.HORSE_ARMOUR, DataMenuType.RABBIT_TYPE),
	HUSK("husk", new Version("1.10-R1"), 1, DataMenuType.ZOMBIE_PROFESSION),
	IRON("iron", DataMenuType.HORSE_ARMOUR),
	THE_KILLER_BUNNY("killerbunny", DataMenuType.RABBIT_TYPE),
	LARGE("large", DataMenuType.SIZE),
	LIBRARIAN("librarian", DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION),
	LIGHT_BLUE("lightBlue", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	LIME("lime", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	MAGENTA("magenta", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	MEDIUM("medium", DataMenuType.SIZE),
	MULE("mule", DataMenuType.HORSE_TYPE),
	NOARMOUR("noarmour", DataMenuType.HORSE_ARMOUR),
	NONE("noMarking", DataMenuType.HORSE_MARKING),
	HORSE("normal", DataMenuType.HORSE_TYPE),
	ORANGE("orange", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	PINK("pink", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	POWER("powered", DataMenuType.BOOLEAN),
	PRIEST("priest", DataMenuType.PROFESSION, DataMenuType.ZOMBIE_PROFESSION),
	PURPLE("purple", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	RED("red", DataMenuType.OCELOT_TYPE, DataMenuType.COLOR, DataMenuType.LLAMA_COLOR, DataMenuType.PARROT_VARIANT),
	SADDLE("saddle", DataMenuType.BOOLEAN),
	SALT_AND_PEPPER("saltandpepper", DataMenuType.RABBIT_TYPE),
	SCREAMING("screaming", DataMenuType.BOOLEAN),
	SHEARED("sheared", DataMenuType.BOOLEAN),
	SHIELD("shield", DataMenuType.BOOLEAN),
	SIAMESE("siamese", DataMenuType.OCELOT_TYPE),
	SKELETON_HORSE("skeleton", DataMenuType.HORSE_TYPE),
	SMALL("small", DataMenuType.SIZE),
	TAMED("tamed", DataMenuType.BOOLEAN),
	VILLAGER("villager", DataMenuType.BOOLEAN),
	WHITEFIELD("whitePatch", DataMenuType.HORSE_MARKING),
	WHITE_DOTS("whiteSpot", DataMenuType.HORSE_MARKING),
	WHITE_SOCKS("whiteSocks", DataMenuType.HORSE_MARKING),
	WHITE("white", DataMenuType.COLOR, DataMenuType.HORSE_VARIANT, DataMenuType.RABBIT_TYPE, DataMenuType.LLAMA_COLOR),
	WHITE_LLAMA("white", DataMenuType.LLAMA_VARIANT),
	WILD("wild", DataMenuType.OCELOT_TYPE),
	YELLOW("yellow", DataMenuType.COLOR, DataMenuType.LLAMA_COLOR),
	UNDEAD_HORSE("zombie", DataMenuType.HORSE_TYPE),
	STANDING_UP("standingup", DataMenuType.BOOLEAN),
	NORMAL("normal", DataMenuType.SKELETON_TYPE),
	WITHER("wither", DataMenuType.SKELETON_TYPE),
	STRAY("stray", new Version("1.10-R1"), 0, DataMenuType.SKELETON_TYPE),
	LEFT_SHOULDER("leftshoulder", new Version("1.12-R1"), 0, DataMenuType.BOOLEAN),
	RIGHT_SHOULDER("rightshoulder", new Version("1.12-R1"), 0, DataMenuType.BOOLEAN),
	OPEN("open", DataMenuType.BOOLEAN);


    private String configOptionString;
	private List<DataMenuType> t;
	private Version version;
	private int versionCheckType;

	private PetData(String configOptionString, DataMenuType... t){
		this(configOptionString, new Version(), 1, t);
	}

	private PetData(String configOptionString, Version version, int versionCheckType, DataMenuType... t){
        this.configOptionString = configOptionString;
        this.t = ImmutableList.copyOf(t);
		this.version = version;
		this.versionCheckType = versionCheckType;
    }

    public String getConfigOptionString() {
        return this.configOptionString;
    }

	public List<DataMenuType> getTypes(){
        return this.t;
    }

	public boolean isType(DataMenuType t){
        return this.t.contains(t);
    }

	public boolean isCompatible(){
		switch (versionCheckType){
			case 0:{// ==
				return version.isIdentical(new Version());
			}
			case 1:{// <=
				return version.isSupported(new Version());
			}
			case 2:{// >=
				return version.isCompatible(new Version());
			}
		}
		return true;
	}

}
