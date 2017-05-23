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

package com.dsh105.echopet.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;

import com.dsh105.commodus.GeneralUtil;
import com.dsh105.commodus.StringUtil;
import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.pet.*;
import com.dsh105.echopet.compat.api.particle.Trail;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.IPetManager;
import com.dsh105.echopet.compat.api.plugin.PetStorage;
import com.dsh105.echopet.compat.api.plugin.uuid.UUIDMigration;
import com.dsh105.echopet.compat.api.util.*;
import com.dsh105.echopet.compat.api.util.menu.DataMenu.DataMenuType;


public class PetManager implements IPetManager {

    private ArrayList<IPet> pets = new ArrayList<IPet>();

    @Override
    public ArrayList<IPet> getPets() {
        return pets;
    }

    @Override
    public IPet loadPets(Player p, boolean findDefault, boolean sendMessage, boolean checkWorldOverride) {
        if (EchoPet.getOptions().sqlOverride()) {
            IPet pet = EchoPet.getSqlManager().createPetFromDatabase(p);
            if (pet == null) {
                return null;
            } else {
                if (sendMessage) {
                    Lang.sendTo(p, Lang.DATABASE_PET_LOAD.toString().replace("%petname%", pet.getPetName()));
                }
            }
            return pet;
        } else if (EchoPet.getConfig(EchoPet.ConfigType.DATA).get("default." + UUIDMigration.getIdentificationFor(p) + ".pet.type") != null && findDefault) {
            IPet pi = this.createPetFromFile("default", p);
            if (pi == null) {
                return null;
            } else {
                if (sendMessage) {
                    Lang.sendTo(p, Lang.DEFAULT_PET_LOAD.toString().replace("%petname%", pi.getPetName()));
                }
            }
            return pi;
        } else if ((checkWorldOverride && EchoPet.getOptions().getConfig().getBoolean("multiworldLoadOverride", true)) || EchoPet.getOptions().getConfig().getBoolean("loadSavedPets", true)) {
            if (EchoPet.getConfig(EchoPet.ConfigType.DATA).get("autosave." + UUIDMigration.getIdentificationFor(p) + ".pet.type") != null) {
                IPet pi = this.createPetFromFile("autosave", p);
                if (pi == null) {
                    return null;
                } else {
                    if (sendMessage) {
                        Lang.sendTo(p, Lang.AUTOSAVE_PET_LOAD.toString().replace("%petname%", pi.getPetName()));
                    }
                }
                return pi;
            }
        }
        return null;
    }

    @Override
    public void removeAllPets() {
        Iterator<IPet> i = pets.listIterator();
        while (i.hasNext()) {
            IPet p = i.next();
            saveFileData("autosave", p);
            EchoPet.getSqlManager().saveToDatabase(p, false);
			p.removePet(true, true);
			p.setLastRider(null);
            i.remove();
        }
    }

    @Override
    public IPet createPet(Player owner, PetType petType, boolean sendMessageOnFail) {
        if (ReflectionUtil.BUKKIT_VERSION_NUMERIC == 178 && petType == PetType.HUMAN) {
            if (sendMessageOnFail) {
                Lang.sendTo(owner, Lang.HUMAN_PET_DISABLED.toString());
            }
            return null;
        }
        removePets(owner, true);
        if (!WorldUtil.allowPets(owner.getLocation())) {
            if (sendMessageOnFail) {
                Lang.sendTo(owner, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(owner.getWorld().getName())));
            }
            return null;
        }
        if (!EchoPet.getOptions().allowPetType(petType)) {
            if (sendMessageOnFail) {
                Lang.sendTo(owner, Lang.PET_TYPE_DISABLED.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
            }
            return null;
        }
        IPet pi = petType.getNewPetInstance(owner);
        if (pi == null) {
            if (sendMessageOnFail) {
                Lang.sendTo(owner, Lang.PET_TYPE_NOT_COMPATIBLE.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
            }
            return null;
        }
        forceAllValidData(pi);
        pets.add(pi);
        return pi;
    }

    @Override
    public IPet createPet(Player owner, PetType petType, PetType riderType) {
        if (ReflectionUtil.BUKKIT_VERSION_NUMERIC == 178 && (petType == PetType.HUMAN) || riderType == PetType.HUMAN) {
            Lang.sendTo(owner, Lang.HUMAN_PET_DISABLED.toString());
            return null;
        }
        removePets(owner, true);
        if (!WorldUtil.allowPets(owner.getLocation())) {
            Lang.sendTo(owner, Lang.PETS_DISABLED_HERE.toString().replace("%world%", StringUtil.capitalise(owner.getWorld().getName())));
            return null;
        }
        if (!EchoPet.getOptions().allowPetType(petType)) {
            Lang.sendTo(owner, Lang.PET_TYPE_DISABLED.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
            return null;
        }
        IPet pi = petType.getNewPetInstance(owner);
        if (pi == null) {
            Lang.sendTo(owner, Lang.PET_TYPE_NOT_COMPATIBLE.toString().replace("%type%", StringUtil.capitalise(petType.toString())));
            return null;
        }
		pi.createRider(riderType, true);
        forceAllValidData(pi);
        pets.add(pi);
        return pi;
    }

    @Override
    public IPet getPet(Player player) {
        for (IPet pi : pets) {
            if (UUIDMigration.getIdentificationFor(player).equals(pi.getOwnerIdentification())) {
                return pi;
            }
        }
        return null;
    }

    @Override
    public IPet getPet(Entity pet) {
        for (IPet pi : pets) {
            IPet rider = pi.getRider();
            if (pi.getEntityPet().equals(pet) || (rider != null && rider.getEntityPet().equals(pet))) {
                return pi;
            }
            if (pi.getCraftPet().equals(pet) || (rider != null && rider.getCraftPet().equals(pet))) {
                return pi;
            }
        }
        return null;
    }

    // Force all data specified in config file and notify player.
    @Override
    public void forceAllValidData(IPet pi) {
        ArrayList<PetData> tempData = new ArrayList<PetData>();
        for (PetData data : PetData.values()) {
            if (EchoPet.getOptions().forceData(pi.getPetType(), data)) {
                tempData.add(data);
            }
        }
        setData(pi, tempData.toArray(new PetData[tempData.size()]), true);

        ArrayList<PetData> tempRiderData = new ArrayList<PetData>();
        if (pi.getRider() != null) {
            for (PetData data : PetData.values()) {
                if (EchoPet.getOptions().forceData(pi.getPetType(), data)) {
                    tempRiderData.add(data);
                }
            }
            setData(pi.getRider(), tempRiderData.toArray(new PetData[tempData.size()]), true);
        }

        if (EchoPet.getOptions().getConfig().getBoolean("sendForceMessage", true)) {
            String dataToString = tempRiderData.isEmpty() ? PetUtil.dataToString(tempData, tempRiderData) : PetUtil.dataToString(tempData);
            ;
            if (dataToString != null) {
                Lang.sendTo(pi.getOwner(), Lang.DATA_FORCE_MESSAGE.toString().replace("%data%", dataToString));
            }
        }
    }

    @Override
    public void updateFileData(String type, IPet pet, ArrayList<PetData> list, boolean b) {
        EchoPet.getSqlManager().saveToDatabase(pet, pet.isRider());
        String w = pet.getOwner().getWorld().getName();
        String path = type + "." + w + "." + pet.getOwnerIdentification();
        for (PetData pd : list) {
            EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), b);
        }
        EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
    }

    @Override
    public IPet createPetFromFile(String type, Player p) {
        if (EchoPet.getOptions().getConfig().getBoolean("loadSavedPets", true)) {
            String path = type + "." + UUIDMigration.getIdentificationFor(p);
            if (EchoPet.getConfig(EchoPet.ConfigType.DATA).get(path) != null) {
                PetType petType = PetType.valueOf(EchoPet.getConfig(EchoPet.ConfigType.DATA).getString(path + ".pet.type"));
                String name = EchoPet.getConfig(EchoPet.ConfigType.DATA).getString(path + ".pet.name");
                if (name.equalsIgnoreCase("") || name == null) {
                    name = petType.getDefaultName(p.getName());
                }
                if (petType == null) return null;
                if (!EchoPet.getOptions().allowPetType(petType)) {
                    return null;
                }
                IPet pi = this.createPet(p, petType, true);
                if (pi == null) {
                    return null;
                }
                //Pet pi = petType.getNewPetInstance(p, petType);
                //Pet pi = new Pet(p, petType);

                pi.setPetName(name);
				ConfigurationSection trails = EchoPet.getConfig(EchoPet.ConfigType.DATA).getConfigurationSection(path + ".pet.trail");
				if(trails != null){
					for(String key : trails.getKeys(false)){
						Trail trail = EchoPet.getPlugin().getTrailManager().getTrailByName(key);
						if(trail == null) continue;
						Trail newTrail = trail.clone();
						newTrail.start(pi);
						pi.addTrail(newTrail);
					}
				}

                ArrayList<PetData> data = new ArrayList<PetData>();
                ConfigurationSection cs = EchoPet.getConfig(EchoPet.ConfigType.DATA).getConfigurationSection(path + ".pet.data");
                if (cs != null) {
                    for (String key : cs.getKeys(false)) {
                        if (GeneralUtil.isEnumType(PetData.class, key.toUpperCase())) {
                            PetData pd = PetData.valueOf(key.toUpperCase());
                            data.add(pd);
                        } else {
                            Logger.log(Logger.LogLevel.WARNING, "Error whilst loading data Pet Save Data for " + pi.getNameOfOwner() + ". Unknown enum type: " + key + ".", true);
                        }
                    }
                }

                if (!data.isEmpty()) {
                    setData(pi, data.toArray(new PetData[data.size()]), true);
                }

                this.loadRiderFromFile(type, pi);

                forceAllValidData(pi);
                return pi;
            }
        }
        return null;
    }

    @Override
    public void loadRiderFromFile(IPet pet) {
        this.loadRiderFromFile("autosave", pet);
    }

    @Override
    public void loadRiderFromFile(String type, IPet pet) {
        if (pet.getOwner() != null) {
            String path = type + "." + pet.getOwnerIdentification();
            if (EchoPet.getConfig(EchoPet.ConfigType.DATA).get(path + ".rider.type") != null) {
                PetType riderPetType = PetType.valueOf(EchoPet.getConfig(EchoPet.ConfigType.DATA).getString(path + ".rider.type"));
                String riderName = EchoPet.getConfig(EchoPet.ConfigType.DATA).getString(path + ".rider.name");
                if (riderName.equalsIgnoreCase("") || riderName == null) {
                    riderName = riderPetType.getDefaultName(pet.getNameOfOwner());
                }
                if (riderPetType == null) return;
                if (EchoPet.getOptions().allowRidersFor(pet.getPetType())) {
                    IPet rider = pet.createRider(riderPetType, true);
					if(rider != null && rider.getEntityPet() != null){
                        rider.setPetName(riderName);
                        ArrayList<PetData> riderData = new ArrayList<PetData>();
                        ConfigurationSection mcs = EchoPet.getConfig(EchoPet.ConfigType.DATA).getConfigurationSection(path + ".rider.data");
                        if (mcs != null) {
                            for (String key : mcs.getKeys(false)) {
                                if (GeneralUtil.isEnumType(PetData.class, key.toUpperCase())) {
                                    PetData pd = PetData.valueOf(key.toUpperCase());
                                    riderData.add(pd);
                                } else {
                                    Logger.log(Logger.LogLevel.WARNING, "Error whilst loading data Pet Rider Save Data for " + pet.getNameOfOwner() + ". Unknown enum type: " + key + ".", true);
                                }
                            }
                        }
                        if (!riderData.isEmpty()) {
                            setData(pet, riderData.toArray(new PetData[riderData.size()]), true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void removePets(Player player, boolean makeDeathSound) {
        Iterator<IPet> i = pets.listIterator();
        while (i.hasNext()) {
            IPet p = i.next();
            if (UUIDMigration.getIdentificationFor(player).equals(p.getOwnerIdentification())) {
				p.removePet(makeDeathSound, true);
				p.setLastRider(null);
                i.remove();
            }
        }
    }

    @Override
    public void removePet(IPet pi, boolean makeDeathSound) {
		pi.removePet(makeDeathSound, true);
		pi.setLastRider(null);
        pets.remove(pi);
    }

    @Override
    public void saveFileData(String type, IPet pet) {
        clearFileData(type, pet);

        String path = type + "." + pet.getOwnerIdentification();
        PetType petType = pet.getPetType();

        EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.type", petType.toString());
        EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.name", pet.serialisePetName());

		for(Trail trail : pet.getTrails()){
			EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.trail." + trail.getName(), true);
		}
        for (PetData pd : pet.getPetData()) {
            EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), true);
        }

        if (pet.getRider() != null) {
            PetType riderType = pet.getRider().getPetType();

            EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".rider.type", riderType.toString());
            EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".rider.name", pet.getRider().serialisePetName());
            for (PetData pd : pet.getRider().getPetData()) {
                EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".rider.data." + pd.toString().toLowerCase(), true);
            }
        }
        EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
    }

    @Override
    public void saveFileData(String type, Player p, PetStorage UPD, PetStorage UMD) {
        clearFileData(type, p);
        PetType pt = UPD.petType;
        PetData[] data = UPD.petDataList.toArray(new PetData[UPD.petDataList.size()]);
        String petName = UPD.petName;
        if (UPD.petName == null || UPD.petName.equalsIgnoreCase("")) {
            petName = pt.getDefaultName(p.getName());
        }
        PetType riderType = UMD.petType;
        PetData[] riderData = UMD.petDataList.toArray(new PetData[UMD.petDataList.size()]);
        String riderName = UMD.petName;
        if (UMD.petName == null || UMD.petName.equalsIgnoreCase("")) {
            riderName = pt.getDefaultName(p.getName());
        }

        String path = type + "." + UUIDMigration.getIdentificationFor(p);
        EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.type", pt.toString());
        EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.name", petName);

        for (PetData pd : data) {
            EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), true);
        }

        if (riderData != null && riderType != null) {
            EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".rider.type", riderType.toString());
            EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".rider.name", riderName);
            for (PetData pd : riderData) {
                EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".rider.data." + pd.toString().toLowerCase(), true);
            }

        }
        EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
    }

    @Override
    public void saveFileData(String type, Player p, PetStorage UPD) {
        clearFileData(type, p);
        PetType pt = UPD.petType;
        PetData[] data = UPD.petDataList.toArray(new PetData[UPD.petDataList.size()]);
        String petName = UPD.petName;

        String path = type + "." + UUIDMigration.getIdentificationFor(p);
        EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.type", pt.toString());
        EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.name", petName);

        for (PetData pd : data) {
            EchoPet.getConfig(EchoPet.ConfigType.DATA).set(path + ".pet.data." + pd.toString().toLowerCase(), true);
        }
        EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
    }

    @Override
    public void clearAllFileData() {
        for (String key : EchoPet.getConfig(EchoPet.ConfigType.DATA).getKeys(true)) {
            if (EchoPet.getConfig(EchoPet.ConfigType.DATA).get(key) != null) {
                EchoPet.getConfig(EchoPet.ConfigType.DATA).set(key, null);
            }
        }
        EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
    }

    @Override
    public void clearFileData(String type, IPet pi) {
        EchoPet.getConfig(EchoPet.ConfigType.DATA).set(type + "." + pi.getOwnerIdentification(), null);
        EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
    }

    @Override
    public void clearFileData(String type, Player p) {
        EchoPet.getConfig(EchoPet.ConfigType.DATA).set(type + "." + UUIDMigration.getIdentificationFor(p), null);
        EchoPet.getConfig(EchoPet.ConfigType.DATA).saveConfig();
    }

    @Override
    public void setData(IPet pet, PetData[] data, boolean b) {
        for (PetData pd : data) {
            setData(pet, pd, b);
        }
    }

    @Override
    public void setData(IPet pet, PetData pd, boolean b) {
		if(pet.getEntityPet() != null){
			PetType petType = pet.getPetType();
			if(petType.isDataAllowed(pd)){
				if(pd == PetData.BABY && pet instanceof IAgeablePet){
					((IAgeablePet) pet).setBaby(b);
				}
				if(pd == PetData.POWER){
					if(petType == PetType.CREEPER){
						((ICreeperPet) pet).setPowered(b);
					}else{
						((IVexPet) pet).setPowered(b);
					}
				}
				if(pd.isType(DataMenuType.SIZE)){
					int i = 1;
					if(pd == PetData.MEDIUM){
						i = 2;
					}else if(pd == PetData.LARGE){
						i = 4;
					}
					if(petType == PetType.SLIME){
						((ISlimePet) pet).setSize(i);
					}
					if(petType == PetType.MAGMACUBE){
						((IMagmaCubePet) pet).setSize(i);
					}
				}
				if(pd.isType(DataMenuType.CAT_TYPE) && petType == PetType.OCELOT){
					try{
						org.bukkit.entity.Ocelot.Type t = org.bukkit.entity.Ocelot.Type.valueOf(pd.toString() + (pd == PetData.WILD ? "_OCELOT" : "_CAT"));
						if(t != null){
							((IOcelotPet) pet).setCatType(t);
						}
					}catch(Exception e){
						Logger.log(Logger.LogLevel.SEVERE, "Encountered exception whilst attempting to convert PetData to Ocelot.Type.", e, true);
					}
				}
				if(pd == PetData.ANGRY){
					((IWolfPet) pet).setAngry(b);
				}
				if(pd == PetData.TAMED){
					((IWolfPet) pet).setTamed(b);
				}
				if(pd.isType(DataMenuType.PROFESSION) || pd.isType(DataMenuType.ZOMBIE_PROFESSION)){
					Profession p = Profession.valueOf(pd.toString());
					if(p != null){
						if(petType == PetType.ZOMBIE || petType == PetType.ZOMBIEVILLAGER){
							((IZombiePet) pet).setVillagerProfession(p);
						}else{
							((IVillagerPet) pet).setProfession(p);
						}
					}
				}
				if(pd.isType(DataMenuType.COLOR) && (petType == PetType.SHEEP || petType == PetType.WOLF || petType == PetType.SHULKER)){
					try{
						DyeColor dc = pd.toString().equalsIgnoreCase("LIGHT_GRAY") ? DyeColor.SILVER : DyeColor.valueOf(pd.toString());
						if(dc != null){
							if(petType == PetType.SHEEP){
								((ISheepPet) pet).setDyeColor(dc);
							}else if(petType == PetType.WOLF){
								((IWolfPet) pet).setCollarColor(dc);
							}else if(petType == PetType.SHULKER){
								((IShulkerPet) pet).setColor(dc);
							}
						}
					}catch(Exception e){
						Logger.log(Logger.LogLevel.SEVERE, "Encountered exception whilst attempting to convert PetData to DyeColor.", e, true);
					}
				}
				if(pd.isType(DataMenuType.PARROT_VARIANT) && petType == PetType.PARROT){
					try{
						ParrotVariant pv = ParrotVariant.valueOf(pd.toString());
						if(pv != null){
							((IParrotPet) pet).setVariant(pv);
						}
					}catch(Exception e){
						Logger.log(Logger.LogLevel.SEVERE, "Encountered exception whilst attempting to convert PetData to ParrotVariant.", e, true);
					}
				}
				if(pd.isType(DataMenuType.SKELETON_TYPE)){
					SkeletonType skeletonType = SkeletonType.valueOf(pd.toString());
					((ISkeletonPet) pet).setSkeletonType(skeletonType);
				}
				if(pd == PetData.FIRE){
					((IBlazePet) pet).setOnFire(b);
				}
				if(pd == PetData.SADDLE){
					if(petType == PetType.PIG){
						((IPigPet) pet).setSaddle(b);
					}else if(petType == PetType.HORSE || petType == PetType.MULE || petType == PetType.DONKEY || petType == PetType.SKELETONHORSE || petType == PetType.ZOMBIEHORSE){
						((IHorseAbstractPet) pet).setSaddled(b);
					}
				}
				if(pd == PetData.SHEARED){
					if(petType == PetType.SHEEP){
						((ISheepPet) pet).setSheared(b);
					}else{
						((ISnowmanPet) pet).setSheared(b);
					}
				}
				if(pd == PetData.SCREAMING){
					((IEndermanPet) pet).setScreaming(b);
				}
				if(pd == PetData.SHIELD){
					((IWitherPet) pet).setShielded(b);
				}
				if(pd == PetData.ELDER){
					((IGuardianPet) pet).setElder(b);
				}
				if(pd.isType(DataMenuType.RABBIT_TYPE) && petType == PetType.RABBIT){
					((IRabbitPet) pet).setRabbitType(Rabbit.Type.valueOf(pd.toString()));
				}
				if(pd == PetData.STANDING_UP){
					((IPolarBearPet) pet).setStandingUp(b);
				}
				if(petType == PetType.SHULKER){
					if(pd == PetData.OPEN){
						((IShulkerPet) pet).setOpen(b);
					}
				}
				if(petType == PetType.HORSE || petType == PetType.MULE || petType == PetType.DONKEY || petType == PetType.LLAMA){
					if(petType == PetType.MULE || petType == PetType.DONKEY || petType == PetType.LLAMA){
						if(pd == PetData.CHESTED){
							((IHorseChestedAbstractPet) pet).setChested(b);
						}
					}else{
						if(pd.isType(DataMenuType.HORSE_TYPE)){
							try{
								HorseVariant h = HorseVariant.valueOf(pd.toString());
								if(h != null){
									((IHorsePet) pet).setVariant(h);
								}
							}catch(Exception e){
								Logger.log(Logger.LogLevel.WARNING, "Encountered exception whilst attempting to convert PetData to Horse.Type.", e, true);
							}
						}
						if(pd.isType(DataMenuType.HORSE_VARIANT)){
							try{
								Horse.Color v = Horse.Color.valueOf(pd.toString());
								if(v != null){
									((IHorsePet) pet).setColor(v);
								}
							}catch(Exception e){
								Logger.log(Logger.LogLevel.WARNING, "Encountered exception whilst attempting to convert PetData to Horse.Variant.", e, true);
							}
						}
						if(pd.isType(DataMenuType.HORSE_MARKING)){
							try{
								Horse.Style m;
								if(pd.equals(PetData.WHITE_SOCKS)){
									m = Horse.Style.WHITE;
								}else{
									m = Horse.Style.valueOf(pd.toString());
								}
								if(m != null){
									((IHorsePet) pet).setStyle(m);
								}
							}catch(Exception e){
								Logger.log(Logger.LogLevel.WARNING, "Encountered exception whilst attempting to convert PetData to Horse.Marking.", e, true);
							}
						}
						if(pd.isType(DataMenuType.HORSE_ARMOUR)){
							try{
								HorseArmour a;
								if(pd == PetData.NOARMOUR){
									a = HorseArmour.NONE;
								}else{
									a = HorseArmour.valueOf(pd.toString());
								}
								if(a != null){
									((IHorsePet) pet).setArmour(a);
								}
							}catch(Exception e){
								Logger.log(Logger.LogLevel.WARNING, "Encountered exception whilst attempting to convert PetData to Horse.Armour.", e, true);
							}
						}
					}
				}
				if(pd.isType(DataMenuType.LLAMA_COLOR) && petType == PetType.LLAMA){
					try{
						DyeColor dc = pd.toString().equalsIgnoreCase("LIGHT_GRAY") ? DyeColor.SILVER : DyeColor.valueOf(pd.toString());
						if(dc != null){
							((ILlamaPet) pet).setCarpetColor(dc);
						}
					}catch(Exception e){
						Logger.log(Logger.LogLevel.SEVERE, "Encountered exception whilst attempting to convert PetData to DyeColor.", e, true);
					}
				}
				if(pd.isType(DataMenuType.LLAMA_VARIANT) && petType == PetType.LLAMA){
					LlamaSkin skin = LlamaSkin.valueOf(pd.toString().split("_")[0]);
					((ILlamaPet) pet).setSkinColor(skin);
				}
			}
		}
            
		ListIterator<PetData> i = pet.getPetData().listIterator();
		while(i.hasNext()){
			PetData petData = i.next();
			if(petData != pd){
				ListIterator<DataMenuType> i2 = pd.getTypes().listIterator();
				while(i2.hasNext()){
					DataMenuType type = i2.next();
					if(type != DataMenuType.BOOLEAN && petData.isType(type)){
						i.remove();
						break;
                    }
                }
            }
		}

		if(b){
			if(!pet.getPetData().contains(pd)){
				pet.getPetData().add(pd);
			}
		}else{
			if(pet.getPetData().contains(pd)){
				pet.getPetData().remove(pd);
            }
        }
    }
}
