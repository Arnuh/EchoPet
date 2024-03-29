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

package com.dsh105.echopet.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.data.PetData;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.PetStorage;
import com.dsh105.echopet.compat.api.plugin.SavedType;
import com.dsh105.echopet.compat.api.util.GeneralUtil;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.Perm;
import com.dsh105.echopet.compat.api.util.PetUtil;
import com.dsh105.echopet.compat.api.util.StringUtil;
import com.dsh105.echopet.compat.api.util.menu.PetMenu;
import com.dsh105.echopet.compat.api.util.menu.SelectorLayout;
import com.dsh105.echopet.compat.api.util.menu.SelectorMenu;
import com.dsh105.echopet.conversation.NameFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;

public class PetAdminCommand implements CommandExecutor{
	
	public String cmdLabel;
	
	public PetAdminCommand(String commandLabel){
		this.cmdLabel = commandLabel;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(args.length == 0){
			if(Perm.ADMIN.hasPerm(sender, true, true)){
				Lang.sendTo(sender, Lang.HELP.toString().replace("%cmd%", "pet help"));
				return true;
			}else{
				return true;
			}
			
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("reload")){
				if(Perm.ADMIN_RELOAD.hasPerm(sender, true, true)){
					EchoPet.getPlugin().getMainConfig().reloadConfig();
					EchoPet.getPlugin().getLangConfig().reloadConfig();
					SelectorLayout.loadLayout();
					Lang.sendTo(sender, Lang.ADMIN_RELOAD_CONFIG.toString());
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("help")){
				if(Perm.ADMIN.hasPerm(sender, true, true)){
					sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help 1/6 ------------");
					sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
					for(String s : AdminHelpPage.getHelpPage(1)){
						sender.sendMessage(s);
					}
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("list")){
				if(Perm.ADMIN.hasPerm(sender, true, true)){
					sender.sendMessage(ChatColor.RED + "------------ EchoPet Pet List ------------");
					for(String s : PetUtil.getPetList(sender, true)){
						sender.sendMessage(s);
					}
					return true;
				}else{
					return true;
				}
			}
			
		}else if(args.length >= 1 && args[0].equalsIgnoreCase("name")){
			if(Perm.ADMIN_NAME.hasPerm(sender, true, true)){
				
				if(args.length >= 2 && args[1].equals("rider")){
					Player target = Bukkit.getPlayer(args[2]);
					if(target == null){
						Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[2]));
						return true;
					}
					IPet pet = EchoPet.getManager().getPet(target);
					if(pet == null){
						Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
						return true;
					}
					
					if(pet.getRider() == null){
						Lang.sendTo(sender, Lang.ADMIN_NO_RIDER.toString().replace("%player%", target.getName()));
						return true;
					}
					
					if(args.length == 2){
						if(sender instanceof Conversable){
							NameFactory.askForName((Conversable) sender, pet.getRider(), true);
						}else{
							Lang.sendTo(sender, Lang.IN_GAME_ONLY.toString().replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineArray(0, " ", args))));
						}
					}else{
						String name = ChatColor.translateAlternateColorCodes('&', StringUtil.combineArray(3, " ", args));
						if(name.length() > 32){
							Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
							return true;
						}
						pet.getRider().setPetName(name);
						Lang.sendTo(sender, Lang.ADMIN_NAME_RIDER.toString().replace("%player%", target.getName()).replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " "))).replace("%name%", name));
						Lang.sendTo(target, Lang.NAME_RIDER.toString().replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " "))).replace("%name%", name));
					}
				}else{
					Player target = Bukkit.getPlayer(args[1]);
					if(target == null){
						Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
						return true;
					}
					
					IPet pet = EchoPet.getManager().getPet(target);
					if(pet == null){
						Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
						return true;
					}
					if(args.length == 2){
						if(sender instanceof Conversable){
							NameFactory.askForName((Conversable) sender, pet, true);
						}else{
							Lang.sendTo(sender, Lang.IN_GAME_ONLY.toString().replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineArray(0, " ", args))));
						}
					}else{
						String name = ChatColor.translateAlternateColorCodes('&', StringUtil.combineArray(2, " ", args));
						if(name.length() > 32){
							Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
							return true;
						}
						pet.setPetName(name);
						Lang.sendTo(sender, Lang.ADMIN_NAME_PET.toString().replace("%player%", target.getName()).replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " "))).replace("%name%", name));
						Lang.sendTo(target, Lang.NAME_PET.toString().replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " "))).replace("%name%", name));
					}
				}
				
			}else{
				return true;
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("menu")){
				if(Perm.ADMIN_MENU.hasPerm(sender, true, true)){
					Player target = Bukkit.getPlayer(args[1]);
					if(target == null || !target.isOnline()){
						Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
						return true;
					}
					IPet pet = EchoPet.getManager().getPet(target);
					
					if(pet == null){
						Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
						return true;
					}
					new PetMenu(pet).open(false);
					Lang.sendTo(sender, Lang.ADMIN_OPEN_MENU.toString().replace("%player%", target.getName()).replace("%type%", StringUtil.capitalise(pet.getPetType().toString())));
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("call")){
				if(Perm.ADMIN_CALL.hasPerm(sender, true, true)){
					Player target = Bukkit.getPlayer(args[1]);
					if(target == null){
						Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
						return true;
					}
					IPet pet = EchoPet.getManager().getPet(target);
					
					if(pet == null){
						Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
						return true;
					}
					if(pet.isHidden()){
						pet.setHidden(false);
						pet.spawnPet(target, false);
					}
					pet.teleportToOwner();
					Lang.sendTo(target, Lang.PET_CALL.toString());
					Lang.sendTo(sender, Lang.ADMIN_PET_CALL.toString().replace("%player%", target.getName()));
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("show")){
				if(Perm.ADMIN_SHOW.hasPerm(sender, true, true)){
					Player target = Bukkit.getPlayer(args[1]);
					if(target == null || !target.isOnline()){
						Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
						return true;
					}
					IPet pet = EchoPet.getManager().getPet(target);
					
					if(pet == null){
						Lang.sendTo(sender, Lang.ADMIN_NO_HIDDEN_PET.toString());
						return true;
					}
					Lang.sendTo(sender, Lang.ADMIN_SHOW_PET.toString().replace("%player%", target.getName()).replace("%type%", StringUtil.capitalise(pet.getPetType().toString())));
					Lang.sendTo(target, Lang.SHOW_PET.toString().replace("%type%", StringUtil.capitalise(pet.getPetType().toString())));
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("hide")){
				if(Perm.ADMIN_HIDE.hasPerm(sender, true, true)){
					Player target = Bukkit.getPlayer(args[1]);
					if(target == null || !target.isOnline()){
						Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
						return true;
					}
					IPet pet = EchoPet.getManager().getPet(target);
					
					if(pet == null){
						Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
						return true;
					}
					EchoPet.getDataManager().save(target, pet, SavedType.Auto);
					EchoPet.getManager().removePet(pet, true);
					Lang.sendTo(target, Lang.HIDE_PET.toString());
					Lang.sendTo(sender, Lang.ADMIN_HIDE_PET.toString().replace("%player%", target.getName()));
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("select")){
				if(Perm.ADMIN_SELECT.hasPerm(sender, true, true)){
					Player target = Bukkit.getPlayer(args[1]);
					if(target == null || !target.isOnline()){
						Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
						return true;
					}
					if(target.getOpenInventory() != null && target.getOpenInventory().getTitle().equals("Pets")){
						target.closeInventory();
						Lang.sendTo(sender, Lang.ADMIN_CLOSE_SELECTOR.toString().replace("%player%", target.getName()));
						return true;
					}
					new SelectorMenu(target, 0).open(target);
					Lang.sendTo(sender, Lang.ADMIN_OPEN_SELECTOR.toString().replace("%player%", target.getName()));
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("selector")){
				if(Perm.ADMIN_SELECTOR.hasPerm(sender, true, true)){
					Player target = Bukkit.getPlayer(args[1]);
					if(target == null || !target.isOnline()){
						Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
						return true;
					}
					target.getInventory().addItem(SelectorLayout.getSelectorItem());
					Lang.sendTo(target, Lang.ADD_SELECTOR.toString());
					Lang.sendTo(sender, Lang.ADMIN_ADD_SELECTOR.toString().replace("%player%", target.getName()));
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("info")){
				if(Perm.ADMIN_INFO.hasPerm(sender, true, true)){
					Player target = Bukkit.getPlayer(args[1]);
					if(target == null || !target.isOnline()){
						Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
						return true;
					}
					IPet pet = EchoPet.getManager().getPet(target);
					
					if(pet == null){
						Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
						return true;
					}
					sender.sendMessage(ChatColor.RED + "------------ EchoPet Pet Info ------------");
					List<String> petInfo = new LinkedList<>();
					pet.generatePetInfo(petInfo);
					if(pet.getRider() != null){
						petInfo.add(ChatColor.RED + "Rider:");
						pet.getRider().generatePetInfo(petInfo);
					}
					for(String s : petInfo){
						sender.sendMessage(s);
					}
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("help")){
				if(Perm.ADMIN.hasPerm(sender, true, true)){
					if(GeneralUtil.isInt(args[1])){
						sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help " + args[1] + "/6 ------------");
						sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
						for(String s : AdminHelpPage.getHelpPage(Integer.parseInt(args[1]))){
							sender.sendMessage(s);
						}
						return true;
					}
					sender.sendMessage(ChatColor.RED + "------------ EchoPet Admin Help 1/6 ------------");
					sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
					for(String s : AdminHelpPage.getHelpPage(1)){
						sender.sendMessage(s);
					}
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("remove")){
				if(Perm.ADMIN_REMOVE.hasPerm(sender, true, true)){
					Player target = args[1].contains("-") ? Bukkit.getPlayer(UUID.fromString(args[1])) : Bukkit.getPlayer(args[1]);
					if(target == null){
						Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
					}else if(!target.isOnline()){
						String path = "autosave." + target.getUniqueId();
						if(EchoPet.getConfig(EchoPet.ConfigType.DATA).get(path + ".pet.type") == null){
							Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER_DATA.toString().replace("%player%", args[1]));
						}else{
							EchoPet.getDataManager().remove(target, SavedType.Auto);
							Lang.sendTo(sender, Lang.ADMIN_PET_REMOVED.toString().replace("%player%", args[1]));
						}
					}else{
						IPet pet = EchoPet.getManager().getPet(target);
						
						if(pet == null){
							Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
							return true;
						}
						
						EchoPet.getDataManager().remove(target, SavedType.Auto);
						EchoPet.getManager().removePet(pet, true);
						
						Lang.sendTo(sender, Lang.ADMIN_PET_REMOVED.toString().replace("%player%", target.getName()));
						Lang.sendTo(target, Lang.REMOVE_PET.toString());
					}
				}
				return true;
			}else if(args[0].equalsIgnoreCase("hat")){
				Player target = Bukkit.getPlayer(args[1]);
				if(target == null || !target.isOnline()){
					Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
					return true;
				}
				IPet pet = EchoPet.getManager().getPet(target);
				
				if(pet == null){
					Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
					return true;
				}
				if(Perm.hasTypePerm(sender, true, Perm.ADMIN_HAT, true, pet.getPetType())){
					pet.setAsHat(!pet.isHat());
					if(pet.isHat()){
						Lang.sendTo(target, Lang.HAT_PET_ON.toString());
						Lang.sendTo(sender, Lang.ADMIN_HAT_PET_ON.toString().replace("%player%", target.getName()));
					}else{
						Lang.sendTo(target, Lang.HAT_PET_OFF.toString());
						Lang.sendTo(sender, Lang.ADMIN_HAT_PET_OFF.toString().replace("%player%", target.getName()));
					}
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("ride")){
				Player target = Bukkit.getPlayer(args[1]);
				if(target == null || !target.isOnline()){
					Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
					return true;
				}
				IPet pet = EchoPet.getManager().getPet(target);
				
				if(pet == null){
					Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
					return true;
				}
				if(Perm.hasTypePerm(sender, true, Perm.ADMIN_RIDE, true, pet.getPetType())){
					pet.ownerRidePet(!pet.isOwnerRiding());
					if(pet.isOwnerRiding()){
						Lang.sendTo(target, Lang.RIDE_PET_ON.toString());
						Lang.sendTo(sender, Lang.ADMIN_RIDE_PET_ON.toString().replace("%player%", target.getName()));
					}else{
						Lang.sendTo(target, Lang.RIDE_PET_OFF.toString());
						Lang.sendTo(sender, Lang.ADMIN_RIDE_PET_OFF.toString().replace("%player%", target.getName()));
					}
					return true;
				}else{
					return true;
				}
			}else{
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null || !target.isOnline()){
					Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
					return true;
				}
				PetStorage UPD = PetUtil.formPetFromArgs(sender, args[1], true);
				if(UPD == null){
					return true;
				}
				IPetType petType = UPD.petType;
				Map<PetData<?>, Object> petDataList = UPD.petDataList;
				
				if(petType == null || petDataList == null){
					return true;
				}
				
				if(Perm.hasTypePerm(sender, true, Perm.ADMIN_PETTYPE, true, petType)){
					IPet pet = EchoPet.getManager().createPet(target, petType, true);
					if(pet == null){
						return true;
					}
					if(!petDataList.isEmpty()){
						EchoPet.getManager().setData(pet, petDataList);
					}
					if(UPD.petName != null && !UPD.petName.equalsIgnoreCase("")){
						if(Perm.ADMIN_NAME.hasPerm(sender, true, true)){
							if(UPD.petName.length() > 32){
								Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
							}else{
								pet.setPetName(UPD.petName);
							}
						}
					}
					EchoPet.getDataManager().save(target, pet, SavedType.Auto);
					Lang.sendTo(target, Lang.CREATE_PET.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
					Lang.sendTo(sender, Lang.ADMIN_CREATE_PET.toString().replace("%player%", target.getName()).replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
					return true;
				}else{
					return true;
				}
			}
			
		}else if(args.length >= 3 && args[0].equalsIgnoreCase("default")){
			if(args.length == 3 && args[2].equalsIgnoreCase("remove")){
				if(Perm.ADMIN_DEFAULT_REMOVE.hasPerm(sender, true, true)){
					String name = args[1];
					Player target = Bukkit.getPlayer(args[1]);
					if(target == null){
						Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", name));
						return true;
					}
					String path = "default." + target.getUniqueId() + ".";
					if(EchoPet.getConfig(EchoPet.ConfigType.DATA).get(path + "pet.type") == null){
						Lang.sendTo(sender, Lang.ADMIN_NO_DEFAULT.toString().replace("%player%", name));
						return true;
					}
					
					EchoPet.getDataManager().remove(target, SavedType.Default);
					Lang.sendTo(sender, Lang.ADMIN_REMOVE_DEFAULT.toString().replace("%player%", name));
					return true;
				}else{
					return true;
				}
			}else if(args.length == 4 && args[2].equalsIgnoreCase("set")){ // args[3] == pet info
				if(args[3].equalsIgnoreCase("current")){
					if(Perm.ADMIN_DEFAULT_SET_CURRENT.hasPerm(sender, true, true)){
						Player target = Bukkit.getPlayer(args[1]);
						if(target == null){
							Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
							return true;
						}
						IPet pet = EchoPet.getManager().getPet(target);
						
						if(pet == null){
							Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
							return true;
						}
						
						EchoPet.getDataManager().save(target, pet, SavedType.Default);
						Lang.sendTo(sender, Lang.ADMIN_SET_DEFAULT_TO_CURRENT.toString().replace("%player%", args[2]));
						return true;
					}else{
						return true;
					}
				}else{
					String name = args[1];
					if(Bukkit.getPlayer(args[1]) != null){
						name = Bukkit.getPlayer(args[1]).getName();
					}
					
					PetStorage UPD = PetUtil.formPetFromArgs(sender, args[3], false);
					if(UPD == null){
						return true;
					}
					IPetType petType = UPD.petType;
					Map<PetData<?>, Object> petDataList = UPD.petDataList;
					
					if(petType == null || petDataList == null){
						return true;
					}
					
					if(Perm.hasTypePerm(sender, true, Perm.ADMIN_DEFAULT_SET_PETTYPE, true, petType)){
						EchoPet.getDataManager().save(Bukkit.getPlayer(args[1]), UPD, null, SavedType.Default);
						Lang.sendTo(sender, Lang.ADMIN_SET_DEFAULT.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))).replace("%player%", name));
					}
					return true;
				}
			}else if(args.length == 5 && args[2].equalsIgnoreCase("set")){
				String name = args[1];
				if(Bukkit.getPlayer(args[1]) != null){
					name = Bukkit.getPlayer(args[1]).getName();
				}
				
				PetStorage UPD = PetUtil.formPetFromArgs(sender, args[3], false);
				if(UPD == null){
					return true;
				}
				IPetType petType = UPD.petType;
				Map<PetData<?>, Object> petDataList = UPD.petDataList;
				
				PetStorage UMD = PetUtil.formPetFromArgs(sender, args[3], false);
				if(UMD == null){
					return true;
				}
				IPetType riderType = UMD.petType;
				Map<PetData<?>, Object> riderDataList = UMD.petDataList;
				
				if(petType == null || petDataList == null || riderType == null || riderDataList == null){
					return true;
				}
				
				if(Perm.hasTypePerm(sender, true, Perm.ADMIN_DEFAULT_SET_PETTYPE, true, petType) && Perm.hasTypePerm(sender, true, Perm.ADMIN_DEFAULT_SET_PETTYPE, true, riderType)){
					EchoPet.getDataManager().save(Bukkit.getPlayer(args[1]), UPD, UMD, SavedType.Default);
					Lang.sendTo(sender, Lang.ADMIN_SET_DEFAULT_WITH_RIDER.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))).replace("%mtype%", StringUtil.capitalise(riderType.toString().replace("_", ""))).replace("%player%", name));
				}
				return true;
			}
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("rider")){
				if(args[1].equalsIgnoreCase("remove")){
					if(Perm.ADMIN_REMOVE.hasPerm(sender, true, true)){
						Player target = Bukkit.getPlayer(args[2]);
						if(target == null){
							Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
						}else{
							IPet pet = EchoPet.getManager().getPet(target);
							
							if(pet == null){
								Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
								return true;
							}
							
							if(pet.getRider() == null){
								Lang.sendTo(sender, Lang.ADMIN_NO_RIDER.toString().replace("%player%", target.getName()));
								return true;
							}
							
							EchoPet.getDataManager().remove(target, SavedType.Auto);
							EchoPet.getManager().removePet(pet, true);
							
							Lang.sendTo(sender, Lang.ADMIN_REMOVE_RIDER.toString().replace("%player%", target.getName()));
							Lang.sendTo(target, Lang.REMOVE_RIDER.toString());
						}
					}
					return true;
				}else{
					Player target = Bukkit.getPlayer(args[1]);
					if(target == null){
						Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[1]));
						return true;
					}
					IPet pet = EchoPet.getManager().getPet(target);
					
					if(pet == null){
						Lang.sendTo(sender, Lang.ADMIN_NO_PET.toString().replace("%player%", target.getName()));
						return true;
					}
					
					PetStorage UPD = PetUtil.formPetFromArgs(sender, args[2], true);
					if(UPD == null){
						return true;
					}
					IPetType petType = UPD.petType;
					Map<PetData<?>, Object> petDataList = UPD.petDataList;
					
					if(petType == null || petDataList == null){
						return true;
					}
					
					if(!petType.allowRidersFor()){
						Lang.sendTo(sender, Lang.RIDERS_DISABLED.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", " "))));
						return true;
					}
					
					if(Perm.hasTypePerm(sender, true, Perm.ADMIN_PETTYPE, true, petType)){
						IPet rider = pet.createRider(petType, true);
						if(rider == null){
							return true;
						}
						if(!petDataList.isEmpty()){
							EchoPet.getManager().setData(rider, petDataList);
						}
						if(UPD.petName != null && !UPD.petName.equalsIgnoreCase("")){
							if(Perm.ADMIN_NAME.hasPerm(sender, true, true)){
								if(UPD.petName.length() > 32){
									Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
								}else{
									rider.setPetName(UPD.petName);
								}
							}
						}
						EchoPet.getDataManager().save(target, pet, SavedType.Auto);
						Lang.sendTo(target, Lang.CHANGE_RIDER.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
						Lang.sendTo(sender, Lang.ADMIN_CHANGE_RIDER.toString().replace("%player%", target.getName()).replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
						return true;
					}else{
						return true;
					}
				}
			}else{
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null){
					Lang.sendTo(sender, Lang.ADMIN_NULL_PLAYER.toString().replace("%player%", args[0]));
					return true;
				}
				
				PetStorage UPD = PetUtil.formPetFromArgs(sender, args[1], false);
				if(UPD == null){
					return true;
				}
				IPetType petType = UPD.petType;
				Map<PetData<?>, Object> petDataList = UPD.petDataList;
				
				PetStorage UMD = PetUtil.formPetFromArgs(sender, args[2], false);
				if(UMD == null){
					return true;
				}
				IPetType riderType = UMD.petType;
				Map<PetData<?>, Object> riderDataList = UMD.petDataList;
				
				if(petType == null || petDataList == null || riderType == null || riderDataList == null){
					return true;
				}
				
				if(Perm.hasTypePerm(sender, true, Perm.ADMIN_PETTYPE, true, petType) && Perm.hasTypePerm(sender, true, Perm.ADMIN_PETTYPE, true, riderType)){
					IPet pi = EchoPet.getManager().createPet(target, petType, riderType);
					if(pi == null){
						return true;
					}
					if(!petDataList.isEmpty()){
						EchoPet.getManager().setData(pi, petDataList);
					}
					if(UPD.petName != null && !UPD.petName.equalsIgnoreCase("")){
						if(Perm.ADMIN_NAME.hasPerm(sender, true, true)){
							if(UPD.petName.length() > 32){
								Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
							}else{
								pi.setPetName(UPD.petName);
							}
						}
					}
					if(!riderDataList.isEmpty()){
						EchoPet.getManager().setData(pi.getRider(), riderDataList);
					}
					if(UMD.petName != null && !UMD.petName.equalsIgnoreCase("")){
						if(Perm.ADMIN_NAME.hasPerm(sender, true, true)){
							if(UPD.petName.length() > 32){
								Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
							}else{
								pi.getRider().setPetName(UPD.petName);
							}
						}
					}
					EchoPet.getDataManager().save(target, pi, SavedType.Auto);
					IPet rider = pi.getRider();
					if(rider != null && rider.isSpawned()){
						Lang.sendTo(target, Lang.CREATE_PET_WITH_RIDER.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))).replace("%mtype%", StringUtil.capitalise(riderType.toString().replace("_", ""))));
						Lang.sendTo(sender, Lang.ADMIN_CREATE_PET_WITH_RIDER.toString().replace("%player%", target.getName()).replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))).replace("%mtype%", StringUtil.capitalise(riderType.toString().replace("_", ""))));
					}else{
						Lang.sendTo(target, Lang.CREATE_PET.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
						Lang.sendTo(sender, Lang.ADMIN_CREATE_PET.toString().replace("%player%", target.getName()).replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
					}
					return true;
				}else{
					return true;
				}
			}
		}
		
		// Something went wrong. Maybe the player didn't use a command correctly?
		// Send them a message with the exact command to make sure
		if(!AdminHelpPage.sendRelevantHelpMessage(sender, args)){
			Lang.sendTo(sender, Lang.ADMIN_COMMAND_ERROR.toString().replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineArray(0, " ", args))));
		}
		return true;
	}
}