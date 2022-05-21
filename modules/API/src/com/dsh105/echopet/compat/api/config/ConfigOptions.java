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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.api.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetDataCategory;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.util.menu.SelectorIcon;
import com.dsh105.echopet.compat.api.util.menu.SelectorLayout;
import org.bukkit.ChatColor;
import org.bukkit.Material;


public class ConfigOptions extends Options{
	
	public static ConfigOptions instance;
	
	public ConfigOptions(YAMLConfig config){
		super(config);
		instance = this;
		SelectorLayout.loadLayout();
	}
	
	public String getCommandString(){
		return this.config.getString("commandString", "pet");
	}
	
	public boolean useSql(){
		return this.config.getBoolean("sql.use", false);
	}
	
	@Override
	public void setDefaults(){
		set("commandString", "pet");
		
		set("checkForUpdates", true, "EchoPet will notify certain", "players of new updates if they are available.");
		
		set("sql.use", false);
		set("sql.host", "localhost");
		set("sql.port", 3306);
		set("sql.database", "echopet");
		set("sql.username", "root");
		set("sql.password", "");
		set("sql.prefix", "echopet");
		
		set("petNames.My Pet", "allow", "List of Pet Names which are `deny` or `allow`.");
		set("petNamesRegexMatching", true);
		List<Map<String, String>> petNamesRegex = new ArrayList<>();
		Map<String, String> nameRegex = new HashMap<>();
		nameRegex.put(".*administrator.*", "deny");
		petNamesRegex.add(nameRegex);
		set("petNamesRegex", petNamesRegex);
		
		set("stripDiacriticsFromNames", true);
		
		set("enableHumanSkinFixing", true, "Connects to Mojang session servers to attempt to fix human skins");
		set("loadSavedPets", true, "Auto-load pets from last session");
		set("multiworldLoadOverride", true, "When true, if -loadSavedPets-", "is set to false, Pets will", "still be loaded when", "players switch worlds");
		
		set("sendLoadMessage", true, "Send message that pet was loaded if -loadSavedPets- is true");
		set("sendForceMessage", true, "For all data values forced, EchoPet will notify the player", "(if set to true).");
		
		set("worlds.world", true);
		set("worlds.enableByDefault", true, "Allow/disallow Pets for any worlds not mentioned.");
		
		if(config.getConfigurationSection("worldguard.regions") == null){
			set("worldguard.regions.echopet", true);
			set("worldguard.regions.allowByDefault", true);
		}
		set("worldguard.regionEnterCheck", true);
		
		set("petSelector.allowDrop", true);
		set("petSelector.showDisabledPets", true);
		set("petSelector.giveOnJoin.enable", false);
		set("petSelector.giveOnJoin.usePerm", false);
		set("petSelector.giveOnJoin.perm", "echopet.selector.join");
		set("petSelector.giveOnJoin.slot", 9);
		set("petSelector.clearInvOnJoin", false);
		set("petSelector.item.name", "&aPets");
		set("petSelector.item.lore", "&7Right click to open");
		set("petSelector.item.material", Material.BONE.name());
		
		boolean loadDefault = this.config.get("petSelector.menu.slots") == null;
		int pageCount = SelectorLayout.getTotalPageCount();
		set("petSelector.menu.pages", pageCount);
		set("petSelector.menu.title", "Pets Page: ");
		if(loadDefault){
			for(int page = 0; page <= pageCount; page++){
				for(SelectorIcon icon : SelectorLayout.getDefaultLayout()){
					if(icon.getPage() == page){
						set("petSelector.menu.page-" + page + "." + icon.getSlot() + ".command", icon.getCommand());
						set("petSelector.menu.page-" + page + "." + icon.getSlot() + ".petType", icon.getPetType() == null ? "" : icon.getPetType().toString());
						set("petSelector.menu.page-" + page + "." + icon.getSlot() + ".material", icon.getMaterial().name());
						if(icon.getPetType() != null){
							set("petSelector.menu.page-" + page + "." + icon.getSlot() + ".entityName", icon.getPetType().getMinecraftName());
						}
						set("petSelector.menu.page-" + page + "." + icon.getSlot() + ".name", (icon.getName() == null ? "" : icon.getName()).replace(ChatColor.COLOR_CHAR, '&'));
						List<String> lore = new ArrayList<>();
						for(String s : icon.getLore()){
							lore.add(s.replace(ChatColor.COLOR_CHAR, '&'));
						}
						set("petSelector.menu.page-" + page + "." + icon.getSlot() + ".lore", lore);
					}
				}
			}
		}
		
		for(PetType petType : PetType.values){
			String configOption = petType.getConfigKeyName();
			String path = "pets." + configOption + ".";
			set(path + "enable", true);
			set(path + "tagVisible", true);
			set(path + "defaultName", petType.getDefaultName());
			set(path + "interactMenu", true);
			set(path + "startFollowDistance", 6);
			set(path + "stopFollowDistance", 2);
			set(path + "teleportDistance", 10);
			set(path + "followSpeedModifier", 1);
			
			set(path + "walkSpeed", 0.37D);
			set(path + "rideSpeed", 0.2D);
			set(path + "flySpeed", 0.5D);
			set(path + "jumpHeight", 0.6D);
			
			set(path + "ignoreFallDamage", true);
			
			boolean canFly = petType == PetType.BAT || petType.equals(PetType.BEE) || petType == PetType.BLAZE || petType == PetType.GHAST || petType == PetType.SQUID || petType == PetType.WITHER || petType == PetType.VEX || petType == PetType.PHANTOM;
			set(path + "canFly", canFly);
			if(config.contains(path + "allow.riders")){
				set(path + "riders", config.getBoolean(path + "allow.riders", true));
				config.removeKey(path + "allow.riders");
			}else{
				set(path + "riders", true);
			}
			
			String dataPath = path + "data.";
			for(PetData<?> pd : petType.getAllowedDataTypes()){
				handlePetData(petType, pd, configOption, dataPath);
			}
			for(PetDataCategory category : petType.getAllowedCategories()){
				for(PetData<?> pd : category.getData()){
					handlePetData(petType, pd, configOption, dataPath);
				}
			}
			config.removeKey("pets." + configOption + ".allow");
			config.removeKey("pets." + configOption + ".force");
			for(Class<?> c = petType.getPetClass(); c != null; c = c.getSuperclass()){
				try{
					for(Class<?> in : c.getInterfaces()){
						if(!IPet.class.isAssignableFrom(in)) continue;
						for(Field f : in.getFields()){
							Object obj = f.get(null);
							if(!(obj instanceof PetConfigEntry<?> entry)) continue;
							set(path + entry.getConfigKey(), entry.getDefaultValue(), entry.getComments());
						}
					}
				}catch(Exception ignored){
				}
			}
		}
	}
	
	private void handlePetData(IPetType petType, PetData<?> pd, String configOption, String dataPath){
		String petData = dataPath + pd.getConfigKeyName() + ".";
		if(config.contains("pets." + configOption + ".allow." + pd.getConfigKeyName())){
			set(petData + "allow", config.getBoolean("pets." + configOption + ".allow." + pd.getConfigKeyName(), true));
			set(petData + "force", config.getBoolean("pets." + configOption + ".force." + pd.getConfigKeyName(), false));
			config.removeKey("pets." + configOption + ".allow." + pd.getConfigKeyName());
			config.removeKey("pets." + configOption + ".force." + pd.getConfigKeyName());
		}else{
			set(petData + "allow", true);
			set(petData + "force", false);
		}
		Object defaultValue = pd.getParser().configDefaultValue(petType);
		if(defaultValue == null) defaultValue = pd.getParser().defaultValue(petType);
		set(petData + "default", defaultValue);
		
		String petDataItem = petData + "item.";
		Material defaultMaterial = pd.getMaterial() != null ? pd.getMaterial().defaultMaterial(petType) : null;
		if(defaultMaterial != null){
			set(petDataItem + "material", defaultMaterial.name());
			set(petDataItem + "name", pd.getDefaultName());
			if(!pd.getDefaultLore().isEmpty()){
				set(petDataItem + "lore", pd.getDefaultLore());
			}
		}
	}
}