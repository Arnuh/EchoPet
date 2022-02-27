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

package com.dsh105.echopet.api;

import java.util.Map;
import java.util.logging.Level;
import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.config.YAMLConfig;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.plugin.IDataManager;
import com.dsh105.echopet.compat.api.plugin.PetStorage;
import com.dsh105.echopet.compat.api.plugin.SavedType;
import com.dsh105.echopet.compat.api.plugin.action.ActionChain;
import com.dsh105.echopet.compat.api.plugin.action.SyncBukkitAction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import static com.dsh105.echopet.compat.api.plugin.EchoPet.ConfigType;
import static com.dsh105.echopet.compat.api.plugin.EchoPet.getConfig;
import static com.dsh105.echopet.compat.api.plugin.EchoPet.getManager;

public class FileDataManager implements IDataManager{
	
	private final Plugin plugin;
	
	public FileDataManager(Plugin plugin){
		this.plugin = plugin;
	}
	
	@Override
	public ActionChain<IPet> save(Player player, IPet pet, SavedType savedType){
		if(pet.isRider()){
			return new SyncBukkitAction<IPet>(plugin)::setAction;
		}
		final IPet rider = pet.getRider();
		return SyncBukkitAction.execute(plugin, ()->{
			YAMLConfig config = getConfig(ConfigType.DATA);
			
			String path = savedType.getOldFileStuff() + "." + player.getUniqueId();
			IPetType petType = pet.getPetType();
			config.set(path + ".pet.type", petType.toString());
			config.set(path + ".pet.name", pet.serialisePetName());
			config.removeKey(path + ".pet.data");
			for(Map.Entry<PetData<?>, Object> entry : pet.getData().entrySet()){
				if(entry.getKey().ignoreSaving()) continue;
				config.set(path + ".pet.data." + entry.getKey().getConfigKeyName(), entry.getValue());
			}
			
			String riderPath = path + ".rider";
			// IPet rider = pet.getRider();
			if(rider != null){
				IPetType riderType = rider.getPetType();
				
				config.set(riderPath + ".type", riderType.toString());
				config.set(riderPath + ".name", rider.serialisePetName());
				for(Map.Entry<PetData<?>, Object> entry : rider.getData().entrySet()){
					if(entry.getKey().ignoreSaving()) continue;
					config.set(riderPath + ".data." + entry.getKey().getConfigKeyName(), entry.getValue());
				}
			}else{
				config.removeKey(riderPath);
			}
			config.saveConfig();
			return pet;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error loading pet data for " + player.getUniqueId(), ex));
	}
	
	@Override
	public ActionChain<Boolean> save(Player player, PetStorage pet, @Nullable PetStorage rider, SavedType savedType){
		return SyncBukkitAction.execute(plugin, ()->{
			YAMLConfig config = getConfig(ConfigType.DATA);
			
			IPetType pt = pet.petType;
			String petName = pet.petName;
			if(pet.petName == null || pet.petName.equalsIgnoreCase("")){
				petName = pt.getDefaultName(player.getName());
			}
			
			String path = savedType.getOldFileStuff() + "." + player.getUniqueId();
			config.set(path + ".pet.type", pt.toString());
			config.set(path + ".pet.name", petName);
			
			for(Map.Entry<PetData<?>, Object> entry : pet.petDataList.entrySet()){
				PetData<?> data = entry.getKey();
				if(data.ignoreSaving()) continue;
				config.set(path + ".pet.data." + data.getConfigKeyName(), entry.getValue());
			}
			
			String riderPath = path + ".rider";
			if(rider != null){
				IPetType riderType = rider.petType;
				String riderName = rider.petName;
				if(rider.petName == null || rider.petName.equalsIgnoreCase("")){
					riderName = pt.getDefaultName(player.getName());
				}
				
				if(riderType != null){
					config.set(riderPath + ".type", riderType.toString());
					config.set(riderPath + ".name", riderName);
					for(Map.Entry<PetData<?>, Object> entry : rider.petDataList.entrySet()){
						PetData<?> data = entry.getKey();
						if(data.ignoreSaving()) continue;
						config.set(riderPath + ".data." + data.getConfigKeyName(), entry.getValue());
					}
				}
			}else{
				config.removeKey(riderPath);
			}
			config.saveConfig();
			return true;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error loading pet data for " + player.getUniqueId(), ex));
	}
	
	@Override
	public ActionChain<IPet> load(Player player, SavedType savedType){
		return SyncBukkitAction.execute(plugin, ()->{
			IPet pet = create(player, savedType);
			if(pet == null && savedType.equals(SavedType.Default)){
				pet = create(player, SavedType.Auto);
			}
			return pet;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error loading pet data for " + player.getUniqueId(), ex));
	}
	
	private IPet create(Player player, SavedType savedType){
		YAMLConfig config = getConfig(ConfigType.DATA);
		
		String path = savedType.getOldFileStuff() + "." + player.getUniqueId();
		if(config.get(path) == null){
			return null;
		}
		IPetType petType = PetType.get(config.getString(path + ".pet.type"));
		if(petType == null){
			return null;
		}
		String name = config.getString(path + ".pet.name");
		if(name == null || name.equalsIgnoreCase("")){
			name = petType.getDefaultName(player.getName());
		}
		if(!petType.isEnabled()){
			return null;
		}
		IPet pet = getManager().createPet(player, petType, true);
		if(pet == null){
			return null;
		}
		pet.setPetName(name);
		
		ConfigurationSection cs = config.getConfigurationSection(path + ".pet.data");
		if(cs != null){
			for(String key : cs.getKeys(false)){
				PetData<?> pd = PetData.get(key);
				if(pd == null){
					plugin.getLogger().log(Level.WARNING, "Error whilst loading data Pet Save Data for " + pet.getNameOfOwner() + ". Unknown enum type: " + key + ".");
					continue;
				}
				Object value = pd.getParser().parse(config.getString(path + ".pet.data." + key));
				getManager().setData(pet, pd, value);
			}
		}
		
		createRider(player, pet, savedType);
		return pet;
	}
	
	public void createRider(Player player, IPet pet, SavedType savedType){
		YAMLConfig config = getConfig(ConfigType.DATA);
		
		String path = savedType.getOldFileStuff() + "." + player.getUniqueId() + ".rider";
		if(config.get(path + ".type") != null){
			IPetType riderPetType = PetType.get(config.getString(path + ".type"));
			if(riderPetType == null){
				return;
			}
			String riderName = config.getString(path + ".name");
			if(riderName == null || riderName.equalsIgnoreCase("")){
				riderName = riderPetType.getDefaultName(pet.getNameOfOwner());
			}
			if(pet.getPetType().allowRidersFor()){
				IPet rider = pet.createRider(riderPetType, true);
				if(rider != null){
					rider.setPetName(riderName);
					ConfigurationSection mcs = config.getConfigurationSection(path + ".data");
					if(mcs != null){
						for(String key : mcs.getKeys(false)){
							PetData<?> pd = PetData.get(key);
							if(pd == null){
								plugin.getLogger().log(Level.WARNING, "Error whilst loading data Pet Rider Save Data for " + pet.getNameOfOwner() + ". Unknown enum type: " + key + ".", true);
								continue;
							}
							Object value = pd.getParser().parse(config.getString(path + ".pet.data." + key));
							getManager().setData(pet, pd, value);
						}
					}
				}
			}
		}
	}
	
	@Override
	public ActionChain<Boolean> remove(Player player, SavedType savedType){
		return SyncBukkitAction.execute(plugin, ()->{
			YAMLConfig config = getConfig(ConfigType.DATA);
			config.set(savedType.getOldFileStuff() + "." + player.getUniqueId(), null);
			config.saveConfig();
			return true;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error deleting pet data for " + player.getUniqueId(), ex));
	}
	
	@Override
	public ActionChain<Boolean> removeAll(){
		return SyncBukkitAction.execute(plugin, ()->{
			YAMLConfig config = getConfig(ConfigType.DATA);
			for(String key : config.getKeys(true)){
				if(config.get(key) != null){
					config.set(key, null);
				}
			}
			config.saveConfig();
			return true;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error deleting pet data", ex));
	}
}
