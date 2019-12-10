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
import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.echopet.compat.api.config.ConfigOptions;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SelectorLayout{
	
	private static List<SelectorIcon> selectorLayout = new ArrayList<>();
	
	private static ItemStack selectorItem;
	
	public static ItemStack getSelectorItem(){
		if(selectorItem != null) return selectorItem;
		YAMLConfig config = ConfigOptions.instance.getConfig();
		String name = config.getString("petSelector.item.name", "&aPets");
		String material = config.getString("petSelector.item.material", Material.BONE.name());
		List<String> lore = config.config().getStringList("petSelector.item.lore");
		if(lore == null){
			lore = new ArrayList<>();
		}
		ItemStack i = new ItemStack(Material.valueOf(material), 1);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		List<String> loreList = new ArrayList<>();
		if(lore.size() > 0){
			for(String s : lore){
				loreList.add(ChatColor.translateAlternateColorCodes('&', s));
			}
		}
		if(!loreList.isEmpty()){
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
				if(petType != null && GeneralUtil.isEnumType(PetType.class, petType.toUpperCase())){
					pt = PetType.valueOf(petType.toUpperCase());
				}
				Material material = Material.getMaterial(config.getString(s + ".page-" + page + "." + slot + ".material"));
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
				if(name == null){
					continue;
				}
				List<String> lore = config.config().getStringList(s + ".page-" + page + "." + slot + ".lore");
				if(lore == null){
					lore = new ArrayList<>();
				}
				ArrayList<String> loreList = new ArrayList<>();
				if(lore.size() > 0){
					for(String part : lore){
						loreList.add(ChatColor.translateAlternateColorCodes('&', part));
					}
				}
				if(material == null) return;
				if(material.name().equalsIgnoreCase("MONSTER_EGG") || material.name().endsWith("SPAWN_EGG")) selectorLayout.add(new SelectorIcon(page, slot, cmd, pt, material, entityTag, name, loreList));
				else selectorLayout.add(new SelectorIcon(page, slot, cmd, pt, material, name, loreList));
			}
		}
		
	}
	
	public static Map<Integer, Map<Integer, SelectorIcon>> getLoadedLayout(){
		Map<Integer, Map<Integer, SelectorIcon>> layout = new HashMap<>();
		for(SelectorIcon icon : selectorLayout){
			if(!ConfigOptions.instance.getConfig().getBoolean("petSelector.showDisabledPets", true) && icon.getPetType() != null){
				if(!ConfigOptions.instance.allowPetType(icon.getPetType())){
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
	
	public static List<SelectorIcon> getDefaultLayout(){
		List<SelectorIcon> layout = new ArrayList<>();
		int count = 0;
		int highestPage = 0;// uh
		for(PetType type : PetType.values){
			if(type.equals(PetType.HUMAN)) continue;
			if(!type.isCompatible()) continue;
			int page = (int) ((double) count / 45);
			if(page > highestPage) highestPage = page;
			String entityTypeName = type.getMinecraftName();
			if(entityTypeName != null){
				layout.add(new SelectorIcon(page, count - page * 45, "pet " + type.name().toLowerCase(), type, type.getUIMaterial(), entityTypeName, type.getDefaultName(), new ArrayList<>()));
			}
			count++;
		}
		SelectorItem[] selectorItems = new SelectorItem[]{SelectorItem.BACK, SelectorItem.TOGGLE, SelectorItem.CALL, SelectorItem.HAT, SelectorItem.CLOSE, SelectorItem.RIDE, SelectorItem.NAME, SelectorItem.MENU, SelectorItem.NEXT};
		for(int i = 0; i <= highestPage; i++){
			int pos = 45;
			for(SelectorItem item : selectorItems){
				layout.add(new SelectorIcon(i, pos++, item.getCommand(), null, item.getMat(), item.getName(), new ArrayList<>()));
			}
		}
		return layout;
	}
}