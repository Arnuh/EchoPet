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
package com.dsh105.echopet.compat.api.util.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.dsh105.echopet.compat.api.entity.PetData;

public enum DataMenuItem{
	/*
	BROWN_VARIANT(DataMenuType.LLAMA_VARIANT, PetData.BROWN_LLAMA, Material.BROWN_WOOL, 1, (short) 12, "Brown", "Wool Color"),
	CREAMY_VARIANT(DataMenuType.LLAMA_VARIANT, PetData.CREAMY, Material.YELLOW_WOOL, 1, (short) 4, "Creamy", "Wool Color"),
	GRAY_VARIANT(DataMenuType.LLAMA_VARIANT, PetData.GRAY_LLAMA, Material.GRAY_WOOL, 1, (short) 7, "Gray", "Wool Color"),
	WHITE_VARIANT(DataMenuType.LLAMA_VARIANT, PetData.WHITE_LLAMA, Material.WHITE_WOOL, 1, (short) 0, "White", "Wool Color"),
	WHITE_LLAMA(DataMenuType.LLAMA_COLOR, PetData.WHITE, Material.WHITE_CARPET, 1, (short) 0, "White", "Color"),
	ORANGE_LLAMA(DataMenuType.LLAMA_COLOR, PetData.ORANGE, Material.ORANGE_CARPET, 1, (short) 1, "Orange", "Color"),
	MAGENTA_LLAMA(DataMenuType.LLAMA_COLOR, PetData.MAGENTA, Material.MAGENTA_CARPET, 1, (short) 2, "Magenta", "Color"),
	LIGHT_BLUE_LLAMA(DataMenuType.LLAMA_COLOR, PetData.LIGHT_BLUE, Material.LIGHT_BLUE_CARPET, 1, (short) 3, "Light Blue", "Color"),
	YELLOW_LLAMA(DataMenuType.LLAMA_COLOR, PetData.YELLOW, Material.YELLOW_CARPET, 1, (short) 4, "Yellow", "Color"),
	LIME_LLAMA(DataMenuType.LLAMA_COLOR, PetData.LIME, Material.LIME_CARPET, 1, (short) 5, "Lime", "Color"),
	PINK_LLAMA(DataMenuType.LLAMA_COLOR, PetData.PINK, Material.PINK_CARPET, 1, (short) 6, "Pink", "Color"),
	GRAY_LLAMA(DataMenuType.LLAMA_COLOR, PetData.GRAY, Material.GRAY_CARPET, 1, (short) 7, "Gray", "Color"),
	SILVER_LLAMA(DataMenuType.LLAMA_COLOR, PetData.SILVER, Material.GRAY_CARPET, 1, (short) 8, "Silver", "Color"),
	CYAN_LLAMA(DataMenuType.LLAMA_COLOR, PetData.CYAN, Material.CYAN_CARPET, 1, (short) 9, "Cyan", "Color"),
	PURPLE_LLAMA(DataMenuType.LLAMA_COLOR, PetData.PURPLE, Material.PURPLE_CARPET, 1, (short) 10, "Purple", "Color"),
	BLUE_LLAMA(DataMenuType.LLAMA_COLOR, PetData.BLUE, Material.BLUE_CARPET, 1, (short) 11, "Blue", "Color"),
	BROWN_LLAMA(DataMenuType.LLAMA_COLOR, PetData.BROWN, Material.BROWN_CARPET, 1, (short) 12, "Brown", "Color"),
	GREEN_LLAMA(DataMenuType.LLAMA_COLOR, PetData.GREEN, Material.GREEN_CARPET, 1, (short) 13, "Green", "Color"),
	RED_LLAMA(DataMenuType.LLAMA_COLOR, PetData.RED, Material.RED_CARPET, 1, (short) 14, "Red", "Color"),
	BLACK_LLAMA(DataMenuType.LLAMA_COLOR, PetData.BLACK, Material.BLACK_CARPET, 1, (short) 15, "Black", "Color"),
	*/
	;
	private PetData dataLink;
	private Material mat;
	private String name;
	private List<String> lore;
	private int amount;
	private short data;

	private DataMenuItem(PetData dataLink, Material mat, int amount, short data, String name, String... lore){
		this.dataLink = dataLink;
		this.mat = mat;
		this.amount = amount;
		this.data = data;
		this.name = name;
		List<String> list = new ArrayList<>();
		for(String s : lore){
			s = ChatColor.GOLD + s;
			list.add(s);
		}
		this.lore = list;
	}

	public PetData getDataLink(){
		return this.dataLink;
	}
}