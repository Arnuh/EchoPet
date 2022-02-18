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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.IDataManager;
import com.dsh105.echopet.compat.api.plugin.PetStorage;
import com.dsh105.echopet.compat.api.plugin.SavedType;
import com.dsh105.echopet.compat.api.plugin.action.ActionChain;
import com.dsh105.echopet.compat.api.plugin.action.SyncBukkitAction;
import com.dsh105.echopet.compat.api.util.GeneralUtil;
import com.dsh105.echopet.compat.api.util.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

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
			String path = savedType.getOldFileStuff() + "." + player.getUniqueId();
			IPetType petType = pet.getPetType();
			
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.type", petType.toString());
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.name", pet.serialisePetName());
			EchoPet.getConfig(EchoPet.ConfigType.DATA).removeKey(path + ".pet.data");
			for(PetData pd : pet.getPetData()){
				if(pd.ignoreSaving()) continue;
				EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), true);
			}
			
			String riderPath = path + ".rider";
			// IPet rider = pet.getRider();
			if(rider != null){
				IPetType riderType = rider.getPetType();
				
				EchoPet.getConfig(EchoPet.ConfigType.DATA).set(riderPath + ".type", riderType.toString());
				EchoPet.getConfig(EchoPet.ConfigType.DATA).set(riderPath + ".name", rider.serialisePetName());
				for(PetData pd : rider.getPetData()){
					if(pd.ignoreSaving()) continue;
					EchoPet.getConfig(EchoPet.ConfigType.DATA).set(riderPath + ".data." + pd.toString().toLowerCase(), true);
				}
			}else{
				EchoPet.getConfig(EchoPet.ConfigType.DATA).removeKey(riderPath);
			}
			EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
			return pet;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error loading pet data for " + player.getUniqueId(), ex));
	}
	
	@Override
	public ActionChain<Boolean> save(Player player, PetStorage pet, @Nullable PetStorage rider, SavedType savedType){
		return SyncBukkitAction.execute(plugin, ()->{
			IPetType pt = pet.petType;
			String petName = pet.petName;
			if(pet.petName == null || pet.petName.equalsIgnoreCase("")){
				petName = pt.getDefaultName(player.getName());
			}
			
			String path = savedType.getOldFileStuff() + "." + player.getUniqueId();
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.type", pt.toString());
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.name", petName);
			
			for(PetData pd : pet.petDataList){
				if(pd.ignoreSaving()) continue;
				EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), true);
			}
			
			String riderPath = path + ".rider";
			if(rider != null){
				IPetType riderType = rider.petType;
				String riderName = rider.petName;
				if(rider.petName == null || rider.petName.equalsIgnoreCase("")){
					riderName = pt.getDefaultName(player.getName());
				}
				
				if(riderType != null){
					EchoPet.getConfig(EchoPet.ConfigType.DATA).set(riderPath + ".type", riderType.toString());
					EchoPet.getConfig(EchoPet.ConfigType.DATA).set(riderPath + ".name", riderName);
					for(PetData pd : rider.petDataList){
						if(pd.ignoreSaving()) continue;
						EchoPet.getConfig(EchoPet.ConfigType.DATA).set(riderPath + ".data." + pd.toString().toLowerCase(), true);
					}
				}
			}else{
				EchoPet.getConfig(EchoPet.ConfigType.DATA).removeKey(riderPath);
			}
			EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
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
		String path = savedType.getOldFileStuff() + "." + player.getUniqueId();
		if(EchoPet.getConfig(EchoPet.ConfigType.DATA).get(path) != null){
			IPetType petType = PetType.get(EchoPet.getConfig(EchoPet.ConfigType.DATA).getString(path + ".pet.type"));
			if(petType == null){
				return null;
			}
			String name = EchoPet.getConfig(EchoPet.ConfigType.DATA).getString(path + ".pet.name");
			if(name == null || name.equalsIgnoreCase("")){
				name = petType.getDefaultName(player.getName());
			}
			if(!petType.isEnabled()){
				return null;
			}
			IPet pet = EchoPet.getManager().createPet(player, petType, true);
			if(pet == null){
				return null;
			}
			pet.setPetName(name);
			
			List<PetData> data = new ArrayList<>();
			ConfigurationSection cs = EchoPet.getConfig(EchoPet.ConfigType.DATA).getConfigurationSection(path + ".pet.data");
			if(cs != null){
				for(String key : cs.getKeys(false)){
					if(GeneralUtil.isEnumType(PetData.class, key.toUpperCase())){
						PetData pd = PetData.valueOf(key.toUpperCase());
						data.add(pd);
					}else{
						Logger.log(Logger.LogLevel.WARNING, "Error whilst loading data Pet Save Data for " + pet.getNameOfOwner() + ". Unknown enum type: " + key + ".", true);
					}
				}
			}
			
			if(!data.isEmpty()){
				EchoPet.getManager().setData(pet, data, true);
			}
			
			createRider(player, pet, savedType);
			return pet;
		}
		return null;
	}
	
	public void createRider(Player player, IPet pet, SavedType savedType){
		String path = savedType.getOldFileStuff() + "." + player.getUniqueId() + ".rider";
		if(EchoPet.getConfig(EchoPet.ConfigType.DATA).get(path + ".type") != null){
			IPetType riderPetType = PetType.get(EchoPet.getConfig(EchoPet.ConfigType.DATA).getString(path + ".type"));
			if(riderPetType == null){
				return;
			}
			String riderName = EchoPet.getConfig(EchoPet.ConfigType.DATA).getString(path + ".name");
			if(riderName == null || riderName.equalsIgnoreCase("")){
				riderName = riderPetType.getDefaultName(pet.getNameOfOwner());
			}
			if(pet.getPetType().allowRidersFor()){
				IPet rider = pet.createRider(riderPetType, true);
				if(rider != null){
					rider.setPetName(riderName);
					List<PetData> riderData = new ArrayList<>();
					ConfigurationSection mcs = EchoPet.getConfig(EchoPet.ConfigType.DATA).getConfigurationSection(path + ".data");
					if(mcs != null){
						for(String key : mcs.getKeys(false)){
							if(GeneralUtil.isEnumType(PetData.class, key.toUpperCase())){
								PetData pd = PetData.valueOf(key.toUpperCase());
								riderData.add(pd);
							}else{
								Logger.log(Logger.LogLevel.WARNING, "Error whilst loading data Pet Rider Save Data for " + pet.getNameOfOwner() + ". Unknown enum type: " + key + ".", true);
							}
						}
					}
					if(!riderData.isEmpty()){
						EchoPet.getManager().setData(rider, riderData, true);
					}
				}
			}
		}
	}
	
	@Override
	public ActionChain<Boolean> remove(Player player, SavedType savedType){
		return SyncBukkitAction.execute(plugin, ()->{
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(savedType.getOldFileStuff() + "." + player.getUniqueId(), null);
			EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
			return true;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error deleting pet data for " + player.getUniqueId(), ex));
	}
	
	@Override
	public ActionChain<Boolean> removeAll(){
		return SyncBukkitAction.execute(plugin, ()->{
			for(String key : EchoPet.getConfig(EchoPet.ConfigType.DATA).getKeys(true)){
				if(EchoPet.getConfig(EchoPet.ConfigType.DATA).get(key) != null){
					EchoPet.getConfig(EchoPet.ConfigType.DATA).set(key, null);
				}
			}
			EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
			return true;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error deleting pet data", ex));
	}
}
