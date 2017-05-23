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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.config.PetItem;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.plugin.EchoPet;

public class SelectorLayout {

	private static ArrayList<SelectorIcon> selectorLayout = new ArrayList<SelectorIcon>();

	private static ItemStack selectorItem;

	public static ItemStack getSelectorItem() {
		if(selectorItem != null) return selectorItem;
		YAMLConfig config = ConfigOptions.instance.getConfig();
		String name = config.getString("petSelector.item.name", "&aPets");
		String material = config.getString("petSelector.item.material", Material.BONE.name());
		int materialData = config.getInt("petSelector.item.materialData", 0);
		List<String> lore = config.config().getStringList("petSelector.item.lore");
		if (lore == null) {
			lore = new ArrayList<String>();
		}
		ItemStack i = new ItemStack(Material.valueOf(material), 1, (short) materialData);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		ArrayList<String> loreList = new ArrayList<String>();
		if (lore.size() > 0) {
			for (String s : lore) {
				loreList.add(ChatColor.translateAlternateColorCodes('&', s));
			}
		}
		if (!loreList.isEmpty()) {
			meta.setLore(loreList);
		}
		i.setItemMeta(meta);
		selectorItem = i;
		return i;
	}

	@SuppressWarnings("deprecation")
	public static void loadLayout(){
		selectorLayout.clear();
		YAMLConfig config = ConfigOptions.instance.getConfig();
		String s = "petSelector.menu";
		int pageCount = config.getInt(s + ".pages", SelectorLayout.getTotalPageCount());
		for(int page = 0; page <= pageCount; page++){
			ConfigurationSection slots = config.getConfigurationSection(s + ".page-" + page);
			if(slots == null){
				EchoPet.getPlugin().getLogger().log(Level.SEVERE, "No slots for page: " + page + ". Please regenerate the config.yml");
				continue;
			}
			for(String slotStr : slots.getKeys(false)){
				int slot = Integer.parseInt(slotStr);
				String cmd = config.getString(s + ".page-" + page + "." + slot + ".command");
				String petType = config.getString(s + ".page-" + page + "." + slot + ".petType");
				PetType pt = null;
				if (petType != null && GeneralUtil.isEnumType(PetType.class, petType.toUpperCase())) {
					pt = PetType.valueOf(petType.toUpperCase());
				}
				Material material = null;
				int materialID = config.getInt(s + ".page-" + page + "." + slot + ".materialId", -1);
				if(materialID >= 0){
					material = Material.getMaterial(materialID);
				}else{
					material = Material.getMaterial(config.getString(s + ".page-" + page + "." + slot + ".material"));
				}
				int data = config.getInt(s + ".page-" + page + "." + slot + ".materialData", -1);// Support old configs
				String entityTag = "Pig";
				if(data > 0){
					EntityType et = EntityType.fromId(data);
					if(et != null){
						entityTag = et.name();
					}
				}else{
					entityTag = config.getString(s + ".page-" + page + "." + slot + ".entityName", "Pig");
				}
				String name = config.getString(s + ".page-" + page + "." + slot + ".name");
				if (name == null) {
					continue;
				}
				List<String> lore = config.config().getStringList(s + ".page-" + page + "." + slot + ".lore");
				if (lore == null) {
					lore = new ArrayList<String>();
				}
				ArrayList<String> loreList = new ArrayList<String>();
				if (lore.size() > 0) {
					for (String part : lore) {
						loreList.add(ChatColor.translateAlternateColorCodes('&', part));
					}
				}
				if(material == null) return;
				if(material.equals(Material.MONSTER_EGG)) selectorLayout.add(new SelectorIcon(page, slot, cmd, pt, material, entityTag, name, loreList.toArray(new String[0])));
				else selectorLayout.add(new SelectorIcon(page, slot, cmd, pt, material, data, name, loreList.toArray(new String[0])));
			}
		}

	}

	public static Map<Integer, Map<Integer, SelectorIcon>> getLoadedLayout(){
		Map<Integer, Map<Integer, SelectorIcon>> layout = new HashMap<>();
		for (SelectorIcon icon : selectorLayout) {
			if (!ConfigOptions.instance.getConfig().getBoolean("petSelector.showDisabledPets", true) && icon.getPetType() != null) {
				if (!ConfigOptions.instance.allowPetType(icon.getPetType())) {
					continue;
				}
			}
			Map<Integer, SelectorIcon> page = layout.get(icon.getPage());
			if(page == null) page = new HashMap<>();
			page.put(icon.getSlot(), icon);
			layout.put(icon.getPage(), page);
		}
		return layout;
	}

	public static int getTotalPageCount(){
		int pageCount = 0;
		for(SelectorIcon icon : SelectorLayout.getDefaultLayout()){
			if(icon.getPage() > pageCount) pageCount = icon.getPage();
		}
		return pageCount;
	}

	public static ArrayList<SelectorIcon> getDefaultLayout() {
		ArrayList<SelectorIcon> layout = new ArrayList<SelectorIcon>();
		int count = 0;
		int highestPage = 0;// uh
		for (PetItem item : PetItem.values()) {
			int page = (int) ((double) count / 45);
			if(page > highestPage) highestPage = page;
			if(item.getPetType() != null && !item.getPetType().equals(PetType.HUMAN) && !(item.getMaterialData() > 0)){
				if(item.getPetType().isCompatible()){
					String entityTypeName = item.getPetType().getMinecraftName();
					if(entityTypeName != null) layout.add(new SelectorIcon(page, count - page * 45, item.getCommand(), item.petType, item.getMat(), entityTypeName, item.getName()));
				}else continue;// don't allow count++
			}
			else if(item.getMaterialData() > 0) layout.add(new SelectorIcon(page, count - page * 45, item.getCommand(), item.petType, item.getMat(), item.getMaterialData(), item.getName()));
			count++;
		}
		SelectorItem[] selectorItems = new SelectorItem[]{SelectorItem.BACK, SelectorItem.TOGGLE, SelectorItem.CALL, SelectorItem.HAT, SelectorItem.CLOSE, SelectorItem.RIDE, SelectorItem.NAME, SelectorItem.MENU, SelectorItem.NEXT};
		for(int i = 0; i <= highestPage; i++){
			int pos = 45;
			for(SelectorItem item : selectorItems){
				layout.add(new SelectorIcon(i, pos++, item.getCommand(), null, item.getMat(), item.getData(), item.getName()));
			}
		}
		return layout;
	}
}