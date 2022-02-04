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

import java.util.ArrayList;
import java.util.List;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.PetStorage;
import com.dsh105.echopet.compat.api.plugin.uuid.UUIDMigration;
import com.dsh105.echopet.compat.api.util.GeneralUtil;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.MiscUtil;
import com.dsh105.echopet.compat.api.util.Perm;
import com.dsh105.echopet.compat.api.util.PetUtil;
import com.dsh105.echopet.compat.api.util.StringUtil;
import com.dsh105.echopet.compat.api.util.WorldUtil;
import com.dsh105.echopet.compat.api.util.menu.PetMenu;
import com.dsh105.echopet.compat.api.util.menu.SelectorLayout;
import com.dsh105.echopet.compat.api.util.menu.SelectorMenu;
import com.dsh105.echopet.conversation.NameFactory;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetCommand implements CommandExecutor{
	
	// private String cmdLabel;
	
	public PetCommand(String commandLabel){
		// this.cmdLabel = commandLabel;
	}
	
	private List<BaseComponent> getHelp(CommandSender sender, int page){
		List<BaseComponent> helpMessages = new ArrayList<>();
		for(HelpEntry he : HelpEntry.values){
			helpMessages.add(he.get(sender));
		}
		return MiscUtil.getPage(helpMessages, 5, page);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
		if(args.length == 0){
			if(Perm.BASE.hasPerm(sender, true, true)){
				Lang.sendTo(sender, Lang.HELP.toString().replace("%cmd%", "pet help"));
				return true;
			}else{
				return true;
			}
			
		}
		
		// Setting the pet and rider names
		// Supports colour coding
		else if(args.length >= 1 && args[0].equalsIgnoreCase("name")){
			if(Perm.BASE_NAME.hasPerm(sender, true, false)){
				Player p = (Player) sender;
				IPet pet = EchoPet.getManager().getPet(p);
				if(pet == null){
					Lang.sendTo(sender, Lang.NO_PET.toString());
					return true;
				}
				if(args.length >= 2 && args[1].equals("rider")){
					if(pet.getRider() == null){
						Lang.sendTo(sender, Lang.NO_RIDER.toString());
						return true;
					}
					if(args.length == 2){
						NameFactory.askForName(p, pet.getRider(), false);
					}else{
						String name = ChatColor.translateAlternateColorCodes('&', StringUtil.combineArray(2, " ", args));
						if(name.length() > 32){
							Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
							return true;
						}
						pet.getRider().setPetName(name);
						Lang.sendTo(sender, Lang.NAME_RIDER.toString().replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " "))).replace("%name%", name));
					}
					return true;
				}else{
					if(args.length == 1){
						NameFactory.askForName(p, pet, false);
					}else{
						String name = ChatColor.translateAlternateColorCodes('&', StringUtil.combineArray(1, " ", args));
						if(name.length() > 32){
							Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
							return true;
						}
						pet.setPetName(name);
						Lang.sendTo(sender, Lang.NAME_PET.toString().replace("%type%", StringUtil.capitalise(pet.getPetType().toString().replace("_", " "))).replace("%name%", name));
					}
					return true;
				}
			}else{
				return true;
			}
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("select")){
				if(sender instanceof Player){
					// We can exempt the player from having the appropriate permission here
					Player p = (Player) sender;
					if(p.getOpenInventory() != null && p.getOpenInventory().getTitle().equals("Pets")){
						p.closeInventory();
						return true;
					}
				}
				if(Perm.BASE_SELECT.hasPerm(sender, true, false)){
					Player p = (Player) sender;
					new SelectorMenu(p, 0).open(p);
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("selector")){
				if(Perm.BASE_SELECTOR.hasPerm(sender, true, false)){
					Player p = (Player) sender;
					p.getInventory().addItem(SelectorLayout.getSelectorItem());
					Lang.sendTo(sender, Lang.ADD_SELECTOR.toString());
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("call")){
				if(Perm.BASE_CALL.hasPerm(sender, true, false)){
					Player player = (Player) sender;
					IPet pet = EchoPet.getManager().getPet(player);
					if(pet == null){
						Lang.sendTo(sender, Lang.NO_PET.toString());
						return true;
					}
					if(pet.isHidden()){
						pet.setHidden(false);
						pet.spawnPet(player, false);
					}
					pet.teleportToOwner();
					Lang.sendTo(sender, Lang.PET_CALL.toString());
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("toggle")){
				if(Perm.BASE_TOGGLE.hasPerm(sender, true, false)){
					Player player = (Player) sender;
					IPet p = EchoPet.getManager().getPet(player);
					if(p == null){// no pet hidden, load it.
						IPet pet = EchoPet.getManager().loadPets(player, false, false, false);
						if(pet == null){
							Lang.sendTo(sender, Lang.NO_HIDDEN_PET.toString());
							return true;
						}
						if(WorldUtil.allowPets(player.getLocation())){
							Lang.sendTo(sender, Lang.SHOW_PET.toString().replace("%type%", StringUtil.capitalise(pet.getPetType().toString())));
							pet.setHidden(false);
							pet.spawnPet(player, true);
							return true;
						}else{
							Lang.sendTo(sender, Lang.PETS_DISABLED_HERE.toString().replace("%world%", player.getWorld().getName()));
							return true;
						}
					}else{
						if(p.isSpawned()){
							p.setHidden(true);
							p.removePet(false, false);
							Lang.sendTo(sender, Lang.HIDE_PET.toString());
						}else{
							if(WorldUtil.allowPets(player.getLocation())){
								Lang.sendTo(sender, Lang.SHOW_PET.toString().replace("%type%", StringUtil.capitalise(p.getPetType().toString())));
								p.setHidden(false);
								p.spawnPet(player, true);
								return true;
							}else{
								Lang.sendTo(sender, Lang.PETS_DISABLED_HERE.toString().replace("%world%", player.getWorld().getName()));
								p.setHidden(true);
								p.removePet(false, false);
								return true;
							}
						}
					}
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("hide")){
				if(Perm.BASE_HIDE.hasPerm(sender, true, false)){
					Player player = (Player) sender;
					IPet pet = EchoPet.getManager().getPet(player);
					if(pet == null){
						Lang.sendTo(sender, Lang.NO_PET.toString());
						return true;
					}
					pet.setHidden(true);
					pet.removePet(false, false);
					Lang.sendTo(sender, Lang.HIDE_PET.toString());
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("show")){
				if(Perm.BASE_SHOW.hasPerm(sender, true, false)){
					Player player = (Player) sender;
					EchoPet.getManager().removePets(player, true);
					IPet pet = EchoPet.getManager().getPet(player);
					if(pet == null) pet = EchoPet.getManager().loadPets(player, false, false, false);
					if(pet == null){
						Lang.sendTo(sender, Lang.NO_HIDDEN_PET.toString());
						return true;
					}
					if(WorldUtil.allowPets(player.getLocation())){
						Lang.sendTo(sender, Lang.SHOW_PET.toString().replace("%type%", StringUtil.capitalise(pet.getPetType().toString())));
						pet.setHidden(false);
						pet.spawnPet(player, true);
						return true;
					}else{
						Lang.sendTo(sender, Lang.PETS_DISABLED_HERE.toString().replace("%world%", player.getWorld().getName()));
						pet.setHidden(true);
						pet.removePet(false, false);
						return true;
					}
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("menu")){
				if(Perm.BASE_MENU.hasPerm(sender, true, false)){
					Player player = (Player) sender;
					IPet p = EchoPet.getManager().getPet(player);
					if(p == null){
						Lang.sendTo(sender, Lang.NO_PET.toString());
						return true;
					}
					new PetMenu(p).open(false);
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("hat")){
				if(sender instanceof Player){
					Player p = (Player) sender;
					IPet pi = EchoPet.getManager().getPet(p);
					if(pi == null){
						Lang.sendTo(sender, Lang.NO_PET.toString());
						return true;
					}
					if(Perm.hasDataPerm(sender, true, pi.getPetType(), PetData.HAT, false)){
						pi.setAsHat(!pi.isHat());
						if(pi.isHat()){
							Lang.sendTo(sender, Lang.HAT_PET_ON.toString());
						}else{
							Lang.sendTo(sender, Lang.HAT_PET_OFF.toString());
						}
						return true;
					}else{
						return true;
					}
				}
			}else if(args[0].equalsIgnoreCase("ride")){
				if(sender instanceof Player){
					Player p = (Player) sender;
					IPet pi = EchoPet.getManager().getPet(p);
					if(pi == null){
						Lang.sendTo(sender, Lang.NO_PET.toString());
						return true;
					}
					if(Perm.hasDataPerm(sender, true, pi.getPetType(), PetData.RIDE, false)){
						pi.ownerRidePet(!pi.isOwnerRiding());
						if(pi.isOwnerRiding()){
							Lang.sendTo(sender, Lang.RIDE_PET_ON.toString());
						}else{
							Lang.sendTo(sender, Lang.RIDE_PET_OFF.toString());
						}
						return true;
					}else{
						return true;
					}
				}
			}
			
			// Help page 1
			else if(args[0].equalsIgnoreCase("help")){
				if(Perm.BASE.hasPerm(sender, true, true)){
					if(sender instanceof Player player){
						int pageCount = MiscUtil.getPageCount(HelpEntry.values.length, 5);
						sender.sendMessage(ChatColor.RED + "------------ EchoPet Help 1/" + pageCount + " ------------");
						sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
						for(BaseComponent component : getHelp(sender, 1)){
							player.spigot().sendMessage(component);
						}
						sender.sendMessage(Lang.TIP_HOVER_PREVIEW.toString());
					}else{
						sender.sendMessage(ChatColor.RED + "------------ EchoPet Help 1/6 ------------");
						sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
						for(String s : HelpPage.getHelpPage(1)){
							sender.sendMessage(s);
						}
					}
					return true;
				}else{
					return true;
				}
			}
			
			// List of all pet types
			else if(args[0].equalsIgnoreCase("list")){
				if(Perm.LIST.hasPerm(sender, true, true)){
					boolean inline = !(sender instanceof Player);
					
					TextComponent message = new TextComponent(Lang.VALID_PET_TYPES.toString() + " ");
					// Should I be using a component builder here??
					for(PetType type : PetType.values){
						boolean access = Perm.hasTypePerm(sender, false, Perm.BASE_PETTYPE, true, type);
						net.md_5.bungee.api.ChatColor format = access ? net.md_5.bungee.api.ChatColor.DARK_GREEN : net.md_5.bungee.api.ChatColor.DARK_RED;
						net.md_5.bungee.api.ChatColor highlight = access ? net.md_5.bungee.api.ChatColor.GREEN : net.md_5.bungee.api.ChatColor.RED;
						TextComponent petMessage = new TextComponent();
						petMessage.setColor(highlight);
						petMessage.setText(StringUtil.capitalise(type.toString().replace("_", " ")));
						
						List<PetData> registeredData = type.getAllowedDataTypes();
						List<String> registeredStringData = new ArrayList<String>();
						
						StringBuilder dataBuilder = new StringBuilder();
						dataBuilder.append(format)
							.append("Valid data types: ");
						int length = 0;
						for(PetData data : registeredData){
							String dataName = StringUtil.capitalise(data.toString().replace("_", " "));
							boolean dataAccess = Perm.hasDataPerm(sender, false, type, data, true);
							if(dataAccess){
								registeredStringData.add(StringUtil.capitalise(data.toString().replace("_", " ")));
								if(length >= 35){
									dataBuilder.append("\n");
									length = 0;
								}
								dataBuilder.append(highlight)
									.append(dataName)
									.append(format)
									.append(", ");
								length += dataName.length();
							}
						}
						
						if(registeredStringData.isEmpty()){
							petMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(format + "No valid data types.")));
						}else{
							if(inline){// Console
								TextComponent reg = new TextComponent(" (" + StringUtil.combine(", ", registeredStringData) + ")");
								reg.setColor(format);
								petMessage.addExtra(reg);
							}else{
								String data = dataBuilder.substring(0, dataBuilder.length() - 2);
								// Make data a component?
								petMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(data)));
							}
						}
						message.addExtra(petMessage);
						
						TextComponent sep = new TextComponent(", ");
						sep.setColor(format);
						message.addExtra(sep);
					}
					if(sender instanceof Player player){
						player.spigot().sendMessage(message);
						sender.sendMessage(Lang.TIP_HOVER_LIST_PREVIEW.toString());
					}else{
						sender.sendMessage(message.toLegacyText());
					}
					return true;
				}else{
					return true;
				}
			}
			
			// Get all the info for a specific pet and send it to the player
			else if(args[0].equalsIgnoreCase("info")){
				if(Perm.BASE_INFO.hasPerm(sender, true, false)){
					IPet pi = EchoPet.getManager().getPet((Player) sender);
					if(pi == null){
						Lang.sendTo(sender, Lang.NO_PET.toString());
						return true;
					}
					sender.sendMessage(ChatColor.RED + "------------ EchoPet Pet Info ------------");
					for(String s : PetUtil.generatePetInfo(pi)){
						sender.sendMessage(s);
					}
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("remove")){
				if(Perm.BASE_REMOVE.hasPerm(sender, true, false)){
					IPet pi = EchoPet.getManager().getPet((Player) sender);
					if(pi == null){
						Lang.sendTo(sender, Lang.NO_PET.toString());
						return true;
					}
					EchoPet.getManager().clearFileData("autosave", pi);
					EchoPet.getSqlManager().clearFromDatabase(pi.getOwner());
					EchoPet.getManager().removePet(pi, true);
					Lang.sendTo(sender, Lang.REMOVE_PET.toString());
					return true;
				}else{
					return true;
				}
			}else{
				if(!(sender instanceof Player)){
					Lang.sendTo(sender, Lang.IN_GAME_ONLY.toString().replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineArray(0, " ", args))));
					return true;
				}
				
				PetStorage UPD = PetUtil.formPetFromArgs(sender, args[0], false);
				if(UPD == null){
					return true;
				}
				IPetType petType = UPD.petType;
				ArrayList<PetData> petDataList = UPD.petDataList;
				
				if(petType == null || petDataList == null){
					return true;
				}
				
				if(Perm.hasTypePerm(sender, true, Perm.BASE_PETTYPE, false, petType)){
					IPet pi = EchoPet.getManager().createPet((Player) sender, petType, true);
					if(pi == null){
						return true;
					}
					if(!petDataList.isEmpty()){
						EchoPet.getManager().setData(pi, petDataList, true);
					}
					if(UPD.petName != null && !UPD.petName.equalsIgnoreCase("")){
						if(Perm.BASE_NAME.hasPerm(sender, true, false)){
							if(UPD.petName.length() > 32){
								Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
							}else{
								pi.setPetName(UPD.petName);
							}
						}
					}
					pi.setHidden(false);
					if(pi.spawnPet((Player) sender, true) != null){
						EchoPet.getManager().saveFileData("autosave", pi);
						EchoPet.getSqlManager().saveToDatabase(pi, false);
						Lang.sendTo(sender, Lang.CREATE_PET.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
					}
					return true;
				}else{
					return true;
				}
			}
			
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("rider")){
				if(args[1].equalsIgnoreCase("remove")){
					if(Perm.BASE_REMOVE.hasPerm(sender, true, false)){
						IPet pi = EchoPet.getManager().getPet((Player) sender);
						if(pi == null){
							Lang.sendTo(sender, Lang.NO_PET.toString());
							return true;
						}
						if(pi.getRider() == null){
							Lang.sendTo(sender, Lang.NO_RIDER.toString());
							return true;
						}
						pi.removeRider(true, true);
						EchoPet.getManager().saveFileData("autosave", pi);
						EchoPet.getSqlManager().saveToDatabase(pi, false);
						Lang.sendTo(sender, Lang.REMOVE_RIDER.toString());
						return true;
					}else{
						return true;
					}
				}else{
					if(!(sender instanceof Player)){
						Lang.sendTo(sender, Lang.IN_GAME_ONLY.toString().replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineArray(0, " ", args))));
						return true;
					}
					
					IPet pi = EchoPet.getManager().getPet((Player) sender);
					
					if(pi == null){
						Lang.sendTo(sender, Lang.NO_PET.toString());
						return true;
					}
					
					PetStorage UPD = PetUtil.formPetFromArgs(sender, args[1], false);
					if(UPD == null){
						return true;
					}
					IPetType petType = UPD.petType;
					ArrayList<PetData> petDataList = UPD.petDataList;
					
					if(petType == null || petDataList == null){
						return true;
					}
					
					if(!petType.allowRidersFor()){
						Lang.sendTo(sender, Lang.RIDERS_DISABLED.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", " "))));
						return true;
					}
					
					if(Perm.hasTypePerm(sender, true, Perm.BASE_PETTYPE, false, petType)){
						IPet rider = pi.createRider(petType, true);
						if(rider == null){
							return true;
						}
						if(!petDataList.isEmpty()){
							EchoPet.getManager().setData(rider, petDataList, true);
						}
						if(UPD.petName != null && !UPD.petName.equalsIgnoreCase("")){
							if(Perm.BASE_NAME.hasPerm(sender, true, false)){
								if(UPD.petName.length() > 32){
									Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
								}else{
									rider.setPetName(UPD.petName);
								}
							}
						}
						EchoPet.getManager().saveFileData("autosave", pi);
						EchoPet.getSqlManager().saveToDatabase(pi, false);
						Lang.sendTo(sender, Lang.CHANGE_RIDER.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
						return true;
					}else{
						return true;
					}
				}
			}
			
			// Help pages 1 through to 3
			else if(args[0].equalsIgnoreCase("help")){
				if(Perm.BASE.hasPerm(sender, true, true)){
					int page = 1;
					if(GeneralUtil.isInt(args[1])){
						page = Integer.parseInt(args[1]);
					}
					int pageCount = MiscUtil.getPageCount(HelpEntry.values.length, 5);
					if(sender instanceof Player player){
						sender.sendMessage(ChatColor.RED + "------------ EchoPet Help " + page + "/" + pageCount + " ------------");
						sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
						if(page > pageCount){
							Lang.sendTo(sender, Lang.HELP_INDEX_TOO_BIG.toString().replace("%index%", args[1]));
							return true;
						}
						for(BaseComponent component : getHelp(sender, page)){
							player.spigot().sendMessage(component);
						}
						sender.sendMessage(Lang.TIP_HOVER_PREVIEW.toString());
					}else{
						sender.sendMessage(ChatColor.RED + "------------ EchoPet Help " + page + "/" + pageCount + " ------------");
						sender.sendMessage(ChatColor.RED + "Key: <> = Required      [] = Optional");
						for(String s : HelpPage.getHelpPage(page)){
							sender.sendMessage(s);
						}
					}
					return true;
				}else{
					return true;
				}
			}else if(args[0].equalsIgnoreCase("default")){
				if(args[1].equalsIgnoreCase("remove")){
					if(Perm.BASE_DEFAULT_REMOVE.hasPerm(sender, true, false)){
						String path = "default." + UUIDMigration.getIdentificationFor((Player) sender) + ".";
						if(EchoPet.getConfig(EchoPet.ConfigType.DATA).get(path + "pet.type") == null){
							Lang.sendTo(sender, Lang.NO_DEFAULT.toString());
							return true;
						}
						
						EchoPet.getManager().clearFileData("default", (Player) sender);
						EchoPet.getSqlManager().clearFromDatabase((Player) sender);
						Lang.sendTo(sender, Lang.REMOVE_DEFAULT.toString());
						return true;
					}else{
						return true;
					}
				}
			}else{
				PetStorage UPD = PetUtil.formPetFromArgs(sender, args[0], false);
				if(UPD == null){
					return true;
				}
				IPetType petType = UPD.petType;
				ArrayList<PetData> petDataList = UPD.petDataList;
				
				PetStorage UMD = PetUtil.formPetFromArgs(sender, args[1], false);
				if(UMD == null){
					return true;
				}
				IPetType riderType = UMD.petType;
				ArrayList<PetData> riderDataList = UMD.petDataList;
				
				if(petType == null || petDataList == null || riderType == null || riderDataList == null){
					return true;
				}
				
				if(Perm.hasTypePerm(sender, true, Perm.BASE_PETTYPE, false, petType) && Perm.hasTypePerm(sender, true, Perm.BASE_PETTYPE, false, riderType)){
					IPet pi = EchoPet.getManager().createPet(((Player) sender), petType, riderType);
					if(pi == null){
						return true;
					}
					if(!petDataList.isEmpty()){
						EchoPet.getManager().setData(pi, petDataList, true);
					}
					if(UPD.petName != null && !UPD.petName.equalsIgnoreCase("")){
						if(Perm.BASE_NAME.hasPerm(sender, true, false)){
							if(UPD.petName.length() > 32){
								Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
							}else{
								pi.setPetName(UPD.petName);
							}
						}
					}
					if(!riderDataList.isEmpty()){
						EchoPet.getManager().setData(pi.getRider(), riderDataList, true);
					}
					if(UMD.petName != null && !UMD.petName.equalsIgnoreCase("")){
						if(Perm.BASE_NAME.hasPerm(sender, true, false)){
							if(UMD.petName.length() > 32){
								Lang.sendTo(sender, Lang.PET_NAME_TOO_LONG.toString());
							}else{
								pi.getRider().setPetName(UMD.petName);
							}
						}
					}
					if(pi.spawnPet(((Player) sender), false) != null){
						EchoPet.getManager().saveFileData("autosave", pi);
						EchoPet.getSqlManager().saveToDatabase(pi, false);
						if(pi.getRider().isSpawned()){
							Lang.sendTo(sender, Lang.CREATE_PET_WITH_RIDER.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))).replace("%mtype%", StringUtil.capitalise(riderType.toString().replace("_", ""))));
						}else{
							Lang.sendTo(sender, Lang.CREATE_PET.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
						}
					}
					return true;
				}else{
					return true;
				}
			}
			
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("default")){
				if(args[1].equalsIgnoreCase("set")){
					if(args[2].equalsIgnoreCase("current")){
						if(Perm.BASE_DEFAULT_SET_CURRENT.hasPerm(sender, true, false)){
							IPet pi = EchoPet.getManager().getPet(((Player) sender));
							if(pi == null){
								Lang.sendTo(sender, Lang.NO_PET.toString());
								return true;
							}
							
							EchoPet.getManager().saveFileData("default", pi);
							Lang.sendTo(sender, Lang.SET_DEFAULT_TO_CURRENT.toString());
							return true;
						}else{
							return true;
						}
					}else{
						PetStorage UPD = PetUtil.formPetFromArgs(sender, args[2], false);
						if(UPD == null){
							return true;
						}
						IPetType petType = UPD.petType;
						ArrayList<PetData> petDataList = UPD.petDataList;
						
						if(petType == null || petDataList == null){
							return true;
						}
						
						if(Perm.hasTypePerm(sender, true, Perm.BASE_DEFAULT_SET_PETTYPE, false, petType)){
							EchoPet.getManager().saveFileData("default", (Player) sender, UPD);
							Lang.sendTo(sender, Lang.SET_DEFAULT.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
							return true;
						}else{
							return true;
						}
					}
				}
			}
		}else if(args.length == 4){
			if(args[0].equalsIgnoreCase("default")){
				if(args[1].equalsIgnoreCase("set")){
					PetStorage UPD = PetUtil.formPetFromArgs(sender, args[2], false);
					if(UPD == null){
						return true;
					}
					IPetType petType = UPD.petType;
					ArrayList<PetData> petDataList = UPD.petDataList;
					
					PetStorage UMD = PetUtil.formPetFromArgs(sender, args[3], false);
					if(UMD == null){
						return true;
					}
					IPetType riderType = UMD.petType;
					ArrayList<PetData> riderDataList = UMD.petDataList;
					
					if(petType == null || petDataList == null || riderType == null || riderDataList == null){
						return true;
					}
					
					if(Perm.hasTypePerm(sender, true, Perm.BASE_DEFAULT_SET_PETTYPE, false, petType) && Perm.hasTypePerm(sender, true, Perm.BASE_DEFAULT_SET_PETTYPE, false, petType)){
						EchoPet.getManager().saveFileData("default", (Player) sender, UPD, UMD);
						Lang.sendTo(sender, Lang.SET_DEFAULT_WITH_RIDER.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))).replace("%mtype%", StringUtil.capitalise(riderType.toString().replace("_", ""))));
						return true;
					}else{
						return true;
					}
				}
			}
		}
		// Something went wrong. Maybe the player didn't use a command correctly?
		// Send them a message with the exact command to make sure
		if(!HelpPage.sendRelevantHelpMessage(sender, args)){
			Lang.sendTo(sender, Lang.COMMAND_ERROR.toString().replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineArray(0, " ", args))));
		}
		return true;
	}
}
