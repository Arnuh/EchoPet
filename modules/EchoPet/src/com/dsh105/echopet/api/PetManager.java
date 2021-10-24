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
import java.util.Iterator;
import java.util.List;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetDataCategory;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.particle.Trail;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.IPetManager;
import com.dsh105.echopet.compat.api.plugin.PetStorage;
import com.dsh105.echopet.compat.api.plugin.uuid.UUIDMigration;
import com.dsh105.echopet.compat.api.util.GeneralUtil;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.Logger;
import com.dsh105.echopet.compat.api.util.PetUtil;
import com.dsh105.echopet.compat.api.util.ReflectionUtil;
import com.dsh105.echopet.compat.api.util.StringUtil;
import com.dsh105.echopet.compat.api.util.WorldUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PetManager implements IPetManager{
	
	private final ArrayList<IPet> pets = new ArrayList<>();
	
	@Override
	public ArrayList<IPet> getPets(){
		return pets;
	}
	
	@Override
	public IPet loadPets(Player p, boolean findDefault, boolean sendMessage, boolean checkWorldOverride){
		if(EchoPet.getOptions().sqlOverride()){
			IPet pet = EchoPet.getSqlManager().createPetFromDatabase(p);
			if(pet == null){
				return null;
			}else{
				if(sendMessage){
					Lang.sendTo(p, Lang.DATABASE_PET_LOAD.toString().replace("%petname%", pet.getPetName()));
				}
			}
			return pet;
		}else if(EchoPet.getConfig(EchoPet.ConfigType.DATA).get("default." + UUIDMigration.getIdentificationFor(p) + ".pet.type") != null && findDefault){
			IPet pi = this.createPetFromFile("default", p);
			if(pi == null){
				return null;
			}else{
				if(sendMessage){
					Lang.sendTo(p, Lang.DEFAULT_PET_LOAD.toString().replace("%petname%", pi.getPetName()));
				}
			}
			return pi;
		}else if((checkWorldOverride && EchoPet.getOptions().getConfig().getBoolean("multiworldLoadOverride", true)) || EchoPet.getOptions().getConfig().getBoolean("loadSavedPets", true)){
			if(EchoPet.getConfig(EchoPet.ConfigType.DATA).get("autosave." + UUIDMigration.getIdentificationFor(p) + ".pet.type") != null){
				IPet pi = this.createPetFromFile("autosave", p);
				if(pi == null){
					return null;
				}else{
					if(sendMessage){
						Lang.sendTo(p, Lang.AUTOSAVE_PET_LOAD.toString().replace("%petname%", pi.getPetName()));
					}
				}
				return pi;
			}
		}
		return null;
	}
	
	@Override
	public void removeAllPets(){
		Iterator<IPet> i = pets.listIterator();
		while(i.hasNext()){
			IPet p = i.next();
			saveFileData("autosave", p);
			EchoPet.getSqlManager().saveToDatabase(p, false);
			p.removePet(true, true);
			p.setLastRider(null);
			i.remove();
		}
	}
	
	@Override
	public IPet createPet(Player owner, IPetType petType, boolean sendMessageOnFail){
		if(ReflectionUtil.BUKKIT_VERSION_NUMERIC == 178 && petType == PetType.HUMAN){
			if(sendMessageOnFail){
				Lang.sendTo(owner, Lang.HUMAN_PET_DISABLED.toString());
			}
			return null;
		}
		removePets(owner, true);
		if(!WorldUtil.allowPets(owner.getLocation())){
			if(sendMessageOnFail){
				Lang.sendTo(owner, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(owner.getWorld().getName())));
			}
			return null;
		}
		if(!petType.isEnabled()){
			if(sendMessageOnFail){
				Lang.sendTo(owner, Lang.PET_TYPE_DISABLED.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
			}
			return null;
		}
		IPet pi = petType.getNewPetInstance(owner);
		if(pi == null){
			if(sendMessageOnFail){
				Lang.sendTo(owner, Lang.PET_TYPE_NOT_COMPATIBLE.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
			}
			return null;
		}
		forceAllValidData(pi);
		pets.add(pi);
		return pi;
	}
	
	@Override
	public IPet createPet(Player owner, IPetType petType, IPetType riderType){
		if(ReflectionUtil.BUKKIT_VERSION_NUMERIC == 178 && (petType == PetType.HUMAN) || riderType == PetType.HUMAN){
			Lang.sendTo(owner, Lang.HUMAN_PET_DISABLED.toString());
			return null;
		}
		removePets(owner, true);
		if(!WorldUtil.allowPets(owner.getLocation())){
			Lang.sendTo(owner, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(owner.getWorld().getName())));
			return null;
		}
		if(!petType.isEnabled()){
			Lang.sendTo(owner, Lang.PET_TYPE_DISABLED.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
			return null;
		}
		IPet pi = petType.getNewPetInstance(owner);
		if(pi == null){
			Lang.sendTo(owner, Lang.PET_TYPE_NOT_COMPATIBLE.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
			return null;
		}
		pi.createRider(riderType, true);
		forceAllValidData(pi);
		pets.add(pi);
		return pi;
	}
	
	@Override
	public IPet getPet(Player player){
		for(IPet pi : pets){
			if(UUIDMigration.getIdentificationFor(player).equals(pi.getOwnerIdentification())){
				return pi;
			}
		}
		return null;
	}
	
	@Override
	public IPet getPet(Entity pet){
		for(IPet pi : pets){
			IPet rider = pi.getRider();
			if(pi.getEntityPet().equals(pet) || (rider != null && rider.getEntityPet().equals(pet))){
				return pi;
			}
			if(pi.getCraftPet().equals(pet) || (rider != null && rider.getCraftPet().equals(pet))){
				return pi;
			}
		}
		return null;
	}
	
	// Force all data specified in config file and notify player.
	@Override
	public void forceAllValidData(IPet pi){
		List<PetData> tempData = new ArrayList<>();
		for(PetData data : PetData.values){
			if(pi.getPetType().isDataForced(data)){
				tempData.add(data);
			}
		}
		setData(pi, tempData, true);
		
		List<PetData> tempRiderData = new ArrayList<>();
		if(pi.getRider() != null){
			for(PetData data : PetData.values){
				if(pi.getPetType().isDataForced(data)){
					tempRiderData.add(data);
				}
			}
			setData(pi.getRider(), tempData, true);
		}
		
		if(EchoPet.getOptions().getConfig().getBoolean("sendForceMessage", true)){
			String dataToString = tempRiderData.isEmpty() ? PetUtil.dataToString(tempData, tempRiderData) : PetUtil.dataToString(tempData);
			if(dataToString != null){
				Lang.sendTo(pi.getOwner(), Lang.DATA_FORCE_MESSAGE.toString().replace("%data%", dataToString));
			}
		}
	}
	
	@Override
	public void updateFileData(String type, IPet pet, ArrayList<PetData> list, boolean b){
		EchoPet.getSqlManager().saveToDatabase(pet, pet.isRider());
		String w = pet.getOwner().getWorld().getName();
		String path = type + "." + w + "." + pet.getOwnerIdentification();
		for(PetData pd : list){
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), b);
		}
		EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
	}
	
	@Override
	public IPet createPetFromFile(String type, Player p){
		if(EchoPet.getOptions().getConfig().getBoolean("loadSavedPets", true)){
			String path = type + "." + UUIDMigration.getIdentificationFor(p);
			if(EchoPet.getConfig(EchoPet.ConfigType.DATA).get(path) != null){
				IPetType petType = PetType.get(EchoPet.getConfig(EchoPet.ConfigType.DATA).getString(path + ".pet.type"));
				if(petType == null){
					return null;
				}
				String name = EchoPet.getConfig(EchoPet.ConfigType.DATA).getString(path + ".pet.name");
				if(name == null || name.equalsIgnoreCase("")){
					name = petType.getDefaultName(p.getName());
				}
				if(!petType.isEnabled()){
					return null;
				}
				IPet pet = this.createPet(p, petType, true);
				if(pet == null){
					return null;
				}
				pet.setPetName(name);
				ConfigurationSection trails = EchoPet.getConfig(EchoPet.ConfigType.DATA).getConfigurationSection(path + ".pet.trail");
				if(trails != null){
					for(String key : trails.getKeys(false)){
						Trail trail = EchoPet.getPlugin().getTrailManager().getTrailByName(key);
						if(trail == null) continue;
						Trail newTrail = trail.clone();
						newTrail.start(pet);
						pet.addTrail(newTrail);
					}
				}
				
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
					setData(pet, data, true);
				}
				
				this.loadRiderFromFile(type, pet);
				
				forceAllValidData(pet);
				return pet;
			}
		}
		return null;
	}
	
	@Override
	public void loadRiderFromFile(IPet pet){
		this.loadRiderFromFile("autosave", pet);
	}
	
	@Override
	public void loadRiderFromFile(String type, IPet pet){
		if(pet.getOwner() != null){
			String path = type + "." + pet.getOwnerIdentification() + ".rider";
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
							setData(rider, riderData, true);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void removePets(Player player, boolean makeDeathSound){
		Iterator<IPet> i = pets.listIterator();
		while(i.hasNext()){
			IPet p = i.next();
			if(UUIDMigration.getIdentificationFor(player).equals(p.getOwnerIdentification())){
				p.removePet(makeDeathSound, true);
				p.setLastRider(null);
				i.remove();
			}
		}
	}
	
	@Override
	public void removePet(IPet pi, boolean makeDeathSound){
		pi.removePet(makeDeathSound, true);
		pi.setLastRider(null);
		pets.remove(pi);
	}
	
	@Override
	public void saveFileData(String type, IPet pet){
		clearFileData(type, pet);
		
		String path = type + "." + pet.getOwnerIdentification();
		IPetType petType = pet.getPetType();
		
		EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.type", petType.toString());
		EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.name", pet.serialisePetName());
		
		for(Trail trail : pet.getTrails()){
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.trail." + trail.getName(), true);
		}
		for(PetData pd : pet.getPetData()){
			if(pd.ignoreSaving()) continue;
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), true);
		}
		
		if(pet.getRider() != null){
			IPetType riderType = pet.getRider().getPetType();
			
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".rider.type", riderType.toString());
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".rider.name", pet.getRider().serialisePetName());
			for(PetData pd : pet.getRider().getPetData()){
				if(pd.ignoreSaving()) continue;
				EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".rider.data." + pd.toString().toLowerCase(), true);
			}
		}
		EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
	}
	
	@Override
	public void saveFileData(String type, Player p, PetStorage UPD, PetStorage UMD){
		clearFileData(type, p);
		IPetType pt = UPD.petType;
		String petName = UPD.petName;
		if(UPD.petName == null || UPD.petName.equalsIgnoreCase("")){
			petName = pt.getDefaultName(p.getName());
		}
		IPetType riderType = UMD.petType;
		String riderName = UMD.petName;
		if(UMD.petName == null || UMD.petName.equalsIgnoreCase("")){
			riderName = pt.getDefaultName(p.getName());
		}
		
		String path = type + "." + UUIDMigration.getIdentificationFor(p);
		EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.type", pt.toString());
		EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.name", petName);
		
		for(PetData pd : UPD.petDataList){
			if(pd.ignoreSaving()) continue;
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), true);
		}
		
		if(riderType != null){
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".rider.type", riderType.toString());
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".rider.name", riderName);
			for(PetData pd : UMD.petDataList){
				if(pd.ignoreSaving()) continue;
				EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".rider.data." + pd.toString().toLowerCase(), true);
			}
			
		}
		EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
	}
	
	@Override
	public void saveFileData(String type, Player p, PetStorage UPD){
		clearFileData(type, p);
		IPetType pt = UPD.petType;
		String petName = UPD.petName;
		
		String path = type + "." + UUIDMigration.getIdentificationFor(p);
		EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.type", pt.toString());
		EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.name", petName);
		
		for(PetData pd : UPD.petDataList){
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), true);
		}
		EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
	}
	
	@Override
	public void clearAllFileData(){
		for(String key : EchoPet.getConfig(EchoPet.ConfigType.DATA).getKeys(true)){
			if(EchoPet.getConfig(EchoPet.ConfigType.DATA).get(key) != null){
				EchoPet.getConfig(EchoPet.ConfigType.DATA).set(key, null);
			}
		}
		EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
	}
	
	@Override
	public void clearFileData(String type, IPet pi){
		EchoPet.getConfig(EchoPet.ConfigType.DATA).set(type + "." + pi.getOwnerIdentification(), null);
		EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
	}
	
	@Override
	public void clearFileData(String type, Player p){
		EchoPet.getConfig(EchoPet.ConfigType.DATA).set(type + "." + UUIDMigration.getIdentificationFor(p), null);
		EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
	}
	
	@Override
	public void setData(IPet pet, List<PetData> data, boolean b){
		for(PetData pd : data){
			setData(pet, pd, b);
		}
	}
	
	@Override
	public void setData(IPet pet, PetData pd, boolean b){
		// Removed others in the same category
		// Because we can only have 1 active at a time.
		for(PetDataCategory category : PetDataCategory.values){
			if(category.hasData(pd)){
				pet.getPetData().removeIf(data->!pd.equals(data) && category.hasData(data));
				break;
			}
		}
		
		if(b){
			if(!pet.getPetData().contains(pd)){
				pet.getPetData().add(pd);
			}
		}else{
			pet.getPetData().remove(pd);
		}
	}
}
