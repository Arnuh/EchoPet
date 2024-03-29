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
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.data.PetData;
import com.dsh105.echopet.compat.api.entity.data.PetDataAction;
import com.dsh105.echopet.compat.api.entity.data.PetDataCategory;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.IPetManager;
import com.dsh105.echopet.compat.api.plugin.PetStorage;
import com.dsh105.echopet.compat.api.plugin.SavedType;
import com.dsh105.echopet.compat.api.plugin.action.ActionChain;
import com.dsh105.echopet.compat.api.plugin.action.SyncBukkitAction;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.PetUtil;
import com.dsh105.echopet.compat.api.util.StringUtil;
import com.dsh105.echopet.compat.api.util.WorldUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PetManager implements IPetManager{
	
	private final ArrayList<IPet> pets = new ArrayList<>();
	
	@Override
	public ArrayList<IPet> getPets(){
		return pets;
	}
	
	@Override
	public ActionChain<IPet> loadPets(Player player, SavedType savedType, boolean sendMessage, boolean checkWorldOverride){
		SyncBukkitAction<IPet> action = new SyncBukkitAction<>(null);
		if((!checkWorldOverride || !EchoPet.getOptions().getConfig().getBoolean("multiworldLoadOverride", true)) && !EchoPet.getOptions().getConfig().getBoolean("loadSavedPets", true)){
			return action::setAction;
		}
		EchoPet.getDataManager().load(player, savedType).andThen(petStorage->{
			if(petStorage == null){
				return;
			}
			PetStorage riderStorage = petStorage.rider;
			IPet pet;
			if(riderStorage != null){
				pet = createPet(player, petStorage.petType, riderStorage.petType);
			}else{
				pet = createPet(player, petStorage.petType, true);
			}
			if(pet == null){
				return;
			}
			pet.setPetName(petStorage.petName);
			pet.getData().putAll(petStorage.petDataList);
			if(riderStorage != null){
				IPet rider = pet.getRider();
				rider.setPetName(riderStorage.petName);
				rider.getData().putAll(riderStorage.petDataList);
			}
			if(sendMessage){
				if(savedType.equals(SavedType.Default)){
					Lang.sendTo(player, Lang.DEFAULT_PET_LOAD.toString().replace("%petname%", pet.getPetName()));
				}else{
					Lang.sendTo(player, Lang.AUTOSAVE_PET_LOAD.toString().replace("%petname%", pet.getPetName()));
				}
			}
			// Should we force after applying saved pet data?
			// forceAllValidData(pet);
			action.execute(pet);
		});
		return action::setAction;
	}
	
	@Override
	public void removeAllPets(){
		Iterator<IPet> i = pets.listIterator();
		while(i.hasNext()){
			IPet p = i.next();
			EchoPet.getDataManager().save(p.getOwner(), p, SavedType.Auto);
			p.removePet(true, true);
			i.remove();
		}
	}
	
	@Override
	public IPet createPet(Player owner, IPetType petType, boolean sendMessageOnFail){
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
	public @Nullable IPet createPet(Player owner, IPetType petType, IPetType riderType){
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
			if(player.getUniqueId().equals(pi.getOwnerUUID())){
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
	
	/**
	 * Force all data specified in config file and notify player.
	 */
	@Override
	public void forceAllValidData(IPet pet){
		List<PetData<?>> tempData = new ArrayList<>();
		handlePetData(pet, tempData);
		
		List<PetData<?>> tempRiderData = new ArrayList<>();
		IPet rider = pet.getRider();
		if(rider != null){
			handlePetData(rider, tempRiderData);
		}
		
		if(EchoPet.getOptions().getConfig().getBoolean("sendForceMessage", true)){
			String dataToString = tempRiderData.isEmpty() ? PetUtil.dataToString(tempData, tempRiderData) : PetUtil.dataToString(tempData);
			if(dataToString != null){
				Lang.sendTo(pet.getOwner(), Lang.DATA_FORCE_MESSAGE.toString().replace("%data%", dataToString));
			}
		}
	}
	
	private void handlePetData(IPet pet, List<PetData<?>> resultData){
		IPetType petType = pet.getPetType();
		for(PetData<?> data : petType.getAllowedDataTypes()){
			attemptPetData(pet, petType, data, resultData);
		}
		for(PetDataCategory category : petType.getAllowedCategories()){
			for(PetData<?> data : category.getData()){
				attemptPetData(pet, petType, data, resultData);
			}
		}
	}
	
	private void attemptPetData(IPet pet, IPetType petType, PetData<?> data, List<PetData<?>> resultData){
		if(!petType.isDataForced(data)) return;
		setData(pet, data, data.getParser().defaultValue(petType));
		resultData.add(data);
	}
	
	@Override
	public void removePets(Player player, boolean makeDeathSound){
		Iterator<IPet> i = pets.listIterator();
		while(i.hasNext()){
			IPet p = i.next();
			if(player.getUniqueId().equals(p.getOwnerUUID())){
				p.removePet(makeDeathSound, true);
				i.remove();
			}
		}
	}
	
	@Override
	public void removePet(IPet pet, boolean makeDeathSound){
		pet.removePet(makeDeathSound, true);
		pets.remove(pet);
	}
	
	@Override
	public void setData(IPet pet, Map<PetData<?>, Object> data){
		for(Map.Entry<PetData<?>, Object> entry : data.entrySet()){
			setData(pet, entry.getKey(), entry.getValue());
		}
	}
	
	@Override
	public void setData(IPet pet, PetData<?> pd, @Nullable Object value){
		// Removed others in the same category
		// Because we can only have 1 active at a time.
		for(PetDataCategory category : PetDataCategory.values){
			if(category.hasData(pd)){
				pet.getData().keySet().removeIf(data->!pd.equals(data) && category.hasData(data));
				break;
			}
		}
		if(value == null){
			removeData(pet, pd);
			return;
		}
		pet.getData().put(pd, value);
	}
	
	@Override
	public boolean removeData(IPet pet, PetData<?> data){
		return pet.getData().remove(data) != null;
	}
	
	@Override
	public void executePetDataAction(Player player, IPet pet, PetData<?> data){
		executePetDataAction(player, pet, data, pet.getData().get(data));
	}
	
	@Override
	public void executePetDataAction(Player player, IPet pet, PetData<?> data, Object value){
		executePetDataAction(player, pet, PetDataCategory.getByData(pet.getPetType(), data), data, value);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void executePetDataAction(Player player, IPet pet, PetDataCategory category, PetData<?> data, Object value){
		PetDataAction<Object> action = (PetDataAction<Object>) data.getAction();
		Consumer<Object> setter = action.get(player, pet, category);
		if(setter != null){
			setter.accept(value);
		}
	}
}
