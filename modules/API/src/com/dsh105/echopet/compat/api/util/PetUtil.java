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

package com.dsh105.echopet.compat.api.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.dsh105.echopet.compat.api.entity.IAgeablePet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.pet.IBlazePet;
import com.dsh105.echopet.compat.api.entity.type.pet.ICreeperPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IEndermanPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseAbstractPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseChestedAbstractPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorsePet;
import com.dsh105.echopet.compat.api.entity.type.pet.ILlamaPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IMagmaCubePet;
import com.dsh105.echopet.compat.api.entity.type.pet.IParrotPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPigPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPigZombiePet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISheepPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IShulkerPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ISlimePet;
import com.dsh105.echopet.compat.api.entity.type.pet.IVexPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IVillagerDataHolder;
import com.dsh105.echopet.compat.api.entity.type.pet.IWitherPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IWolfPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IZombiePet;
import com.dsh105.echopet.compat.api.plugin.PetStorage;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetUtil{
	
	private static Map<PetData<?>, Object> parseData(CommandSender sender, String input){
		Map<PetData<?>, Object> result = new LinkedHashMap<>();
		for(String data : input.split(",")){
			if(!data.contains("=")){
				PetData<?> petData = PetData.get(data);
				if(petData == null){
					Lang.sendTo(sender, Lang.INVALID_PET_DATA_TYPE.toString().replace("%data%", StringUtil.capitalise(data)));
					return null;
				}
				result.put(petData, petData.getParser().defaultValue());
			}else{
				String[] split = data.split("=");
				if(split.length != 2){
					Lang.sendTo(sender, Lang.STRING_ERROR.toString().replace("%string%", data));
					return null;
				}
				PetData<?> petData = PetData.get(split[0]);
				if(petData == null){
					Lang.sendTo(sender, Lang.INVALID_PET_DATA_TYPE.toString().replace("%data%", StringUtil.capitalise(split[0])));
					return null;
				}
				try{
					Object parserResult = petData.getParser().parse(split[1]);
					if(parserResult == null){
						Lang.sendTo(sender, Lang.INVALID_PET_DATA_VALUE.toString().replace("%data%", StringUtil.capitalise(split[0])).replace("%value%", split[1]));
						return null;
					}
					result.put(petData, parserResult);
				}catch(Exception ex){
					Lang.sendTo(sender, Lang.INVALID_PET_DATA_VALUE.toString().replace("%data%", StringUtil.capitalise(split[0])).replace("%value%", split[1]));
					return null;
				}
			}
		}
		return result;
	}
	
	public static PetStorage formPetFromArgs(CommandSender sender, String input, boolean petAdmin){
		IPetType petType = null;
		String name = "";
		Map<PetData<?>, Object> petDataList = new LinkedHashMap<>();
		
		String[] dataValues = input.split("[;:]");
		if(dataValues.length == 1){
			petType = PetType.get(input);
		}else{
			for(String value : dataValues){
				if(input.contains(";" + value)){
					name = value;
				}else if(value.contains(",") || value.contains("=")){
					Map<PetData<?>, Object> result = parseData(sender, value);
					if(result == null) return null;
					petDataList.putAll(result);
				}else{
					IPetType checkType = PetType.get(value);
					if(checkType != null){
						petType = checkType;
					}else if(input.contains(value + ";") || input.contains(value + ":")){
						Lang.sendTo(sender, Lang.INVALID_PET_TYPE.toString().replace("%type%", StringUtil.capitalise(value)));
						return null;
					}else{
						Map<PetData<?>, Object> result = parseData(sender, value);
						if(result == null) return null;
						petDataList.putAll(result);
					}
				}
			}
		}
		
		if(petType == null){
			Lang.sendTo(sender, Lang.INVALID_PET_TYPE.toString().replace("%type%", StringUtil.capitalise(input)));
			return null;
		}
		
		if(!petDataList.isEmpty()){
			for(PetData<?> dataTemp : petDataList.keySet()){
				if(!petType.isValidData(dataTemp)){
					Lang.sendTo(sender, Lang.INVALID_PET_DATA_TYPE_FOR_PET.toString().replace("%data%", StringUtil.capitalise(dataTemp.toString().replace("_", ""))).replace("%type%", StringUtil.capitalise(petType.toString().replace("_", " "))));
					return null;
				}
				if(!petType.isDataAllowed(dataTemp)){
					Lang.sendTo(sender, Lang.DATA_TYPE_DISABLED.toString().replace("%data%", StringUtil.capitalise(dataTemp.toString().replace("_", ""))));
					return null;
				}
			}
		}
		
		if(!petType.isEnabled()){
			Lang.sendTo(sender, Lang.PET_TYPE_DISABLED.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
			return null;
		}
		
		for(PetData<?> dataTemp : petDataList.keySet()){
			if(!Perm.hasDataPerm(sender, true, petType, dataTemp, false)){
				return null;
			}
		}
		
		return new PetStorage(petDataList, petType, name);
	}
	
	@Deprecated
	public static List<String> generatePetInfo(IPet pt){
		List<String> info = new ArrayList<String>();
		info.add(ChatColor.GOLD + " - Pet Type: " + ChatColor.YELLOW + StringUtil.capitalise(pt.getPetType().toString()));
		info.add(ChatColor.GOLD + " - Name: " + ChatColor.YELLOW + pt.getPetName());
		if(pt instanceof IAgeablePet){
			info.add(ChatColor.GOLD + " - Baby: " + ChatColor.YELLOW + ((IAgeablePet) pt).isBaby());
		}
		if(pt.getPetType() == PetType.ZOMBIE){
			info.add(ChatColor.GOLD + " - Baby: " + ChatColor.YELLOW + ((IZombiePet) pt).isBaby());
		}
		if(pt.getPetType() == PetType.PIGZOMBIE){
			info.add(ChatColor.GOLD + " - Baby: " + ChatColor.YELLOW + ((IPigZombiePet) pt).isBaby());
		}
		info.addAll(generatePetDataInfo(pt));
		
		if(pt.getRider() != null){
			info.add(ChatColor.RED + "Rider:");
			info.addAll(generatePetInfo(pt.getRider()));
		}
		
		return info;
	}
	
	@Deprecated
	public static List<String> generatePetDataInfo(IPet pt){
		List<String> info = new ArrayList<String>();
		if(pt.getPetType() == PetType.BLAZE){
			info.add(ChatColor.GOLD + " - On Fire: " + ChatColor.YELLOW + ((IBlazePet) pt).isOnFire());
		}
		
		if(pt.getPetType() == PetType.CREEPER){
			info.add(ChatColor.GOLD + " - Powered: " + ChatColor.YELLOW + ((ICreeperPet) pt).isPowered());
		}
		
		if(pt.getPetType() == PetType.ENDERMAN){
			info.add(ChatColor.GOLD + " - Screaming: " + ChatColor.YELLOW + ((IEndermanPet) pt).isScreaming());
		}
		
		if(pt.getPetType() == PetType.SHEEP){
			String color = "";
			color = ((ISheepPet) pt).getColor() == null ? "Default" : StringUtil.capitalise(((ISheepPet) pt).getColor().toString().replace("_", " "));
			info.add(ChatColor.GOLD + " - Wool Colour: " + ChatColor.YELLOW + color);
		}
		
		if(pt.getPetType() == PetType.VILLAGER || pt.getPetType() == PetType.ZOMBIE){
			String prof = StringUtil.capitalise(((IVillagerDataHolder) pt).getProfession().toString().replace("_", " "));
			info.add(ChatColor.GOLD + " - Profession: " + ChatColor.YELLOW + prof);
			prof = StringUtil.capitalise(((IVillagerDataHolder) pt).getType().toString().replace("_", " "));
			info.add(ChatColor.GOLD + " - Type: " + ChatColor.YELLOW + prof);
			prof = StringUtil.capitalise(((IVillagerDataHolder) pt).getLevel().toString().replace("_", " "));
			info.add(ChatColor.GOLD + " - Level: " + ChatColor.YELLOW + prof);
		}
		
		if(pt.getPetType() == PetType.OCELOT){
			// String oType = "";
			// oType = ((IOcelotPet) pt).getCatType() == null ? "Default" : StringUtil.capitalise(((IOcelotPet) pt).getCatType().toString().replace("_", " "));
			// info.add(ChatColor.GOLD + " - Ocelot Type: " + ChatColor.YELLOW + oType);
		}
		
		if(pt.getPetType() == PetType.PIG){
			info.add(ChatColor.GOLD + " - Saddled: " + ChatColor.YELLOW + ((IPigPet) pt).hasSaddle());
		}
		
		if(pt.getPetType() == PetType.SLIME){
			String size = "";
			size = StringUtil.capitalise(((ISlimePet) pt).getSize() + "");
			String s = " (Small)";
			if(size.equals("2")) s = " (Medium)";
			if(size.equals("4")) s = " (Large)";
			info.add(ChatColor.GOLD + " - Slime Size: " + ChatColor.YELLOW + size + s);
		}
		
		if(pt.getPetType() == PetType.MAGMACUBE){
			String size = "";
			size = StringUtil.capitalise(((IMagmaCubePet) pt).getSize() + "");
			String s = " (Small)";
			if(size.equals("2")) s = " (Medium)";
			if(size.equals("4")) s = " (Large)";
			info.add(ChatColor.GOLD + " - Slime Size: " + ChatColor.YELLOW + size + s);
		}
		
		if(pt.getPetType() == PetType.WOLF){
			info.add(ChatColor.GOLD + " - Tamed (Wolf): " + ChatColor.YELLOW + ((IWolfPet) pt).isTamed());
			info.add(ChatColor.GOLD + " - Angry (Wolf): " + ChatColor.YELLOW + ((IWolfPet) pt).isAngry());
			String color = "";
			color = ((IWolfPet) pt).getCollarColor() == null ? "Red" : StringUtil.capitalise(((IWolfPet) pt).getCollarColor().toString().replace("_", " "));
			info.add(ChatColor.GOLD + " - Collar Colour: " + ChatColor.YELLOW + color);
		}
		
		if(pt.getPetType() == PetType.WITHER){
			info.add(ChatColor.GOLD + " - Shielded: " + ChatColor.YELLOW + ((IWitherPet) pt).isShielded());
		}
		if(pt.getPetType() == PetType.VEX){
			info.add(ChatColor.GOLD + " - Powered: " + ChatColor.YELLOW + ((IVexPet) pt).isPowered());
		}
		
		if(pt.getPetType() == PetType.HORSE || pt.getPetType() == PetType.SKELETONHORSE || pt.getPetType() == PetType.ZOMBIEHORSE || pt.getPetType() == PetType.DONKEY || pt.getPetType() == PetType.MULE){
			info.add(ChatColor.GOLD + " - Saddled: " + ChatColor.YELLOW + ((IHorseAbstractPet) pt).isSaddled());
			if(new Version("1.10-R1").isSupported(new Version())){
				HorseVariant variant = ((IHorsePet) pt).getVariant();
				info.add(ChatColor.GOLD + " - Variant: " + ChatColor.YELLOW + StringUtil.capitalise(variant.toString().replace("_", " ")));
				if(pt.getPetType() == PetType.HORSE){
					info.add(ChatColor.GOLD + " - Color: " + ChatColor.YELLOW + StringUtil.capitalise(((IHorsePet) pt).getColor().toString().replace("_", " ")));
					info.add(ChatColor.GOLD + " - Style: " + ChatColor.YELLOW + StringUtil.capitalise(((IHorsePet) pt).getStyle().toString().replace("_", " ")));
				}
			}else{
				info.add(ChatColor.GOLD + " - Variant: " + ChatColor.YELLOW + StringUtil.capitalise(pt.getPetType().getMinecraftName().replace("_", " ")));
				if(pt.getPetType() == PetType.HORSE){
					info.add(ChatColor.GOLD + " - Color: " + ChatColor.YELLOW + StringUtil.capitalise(((IHorsePet) pt).getColor().toString().replace("_", " ")));
					info.add(ChatColor.GOLD + " - Style: " + ChatColor.YELLOW + StringUtil.capitalise(((IHorsePet) pt).getStyle().toString().replace("_", " ")));
				}
			}
			if(pt.getPetType() == PetType.DONKEY || pt.getPetType() == PetType.MULE || new Version("1.10-R1").isSupported(new Version())){// Using 1.11 horse, or <= 1.10
				info.add(ChatColor.GOLD + " - Chested: " + ChatColor.YELLOW + ((IHorseChestedAbstractPet) pt).isChested());
			}
		}
		if(pt.getPetType() == PetType.LLAMA){
			DyeColor carpetColor = ((ILlamaPet) pt).getCarpetColor();
			info.add(ChatColor.GOLD + " - Carpet Colour: " + ChatColor.YELLOW + (carpetColor == null ? "None" : StringUtil.capitalise(carpetColor.toString().replace("_", " "))));
			info.add(ChatColor.GOLD + " - Variant: " + ChatColor.YELLOW + StringUtil.capitalise(((ILlamaPet) pt).getSkinColor().toString().replace("_", " ")));
		}
		
		if(pt.getPetType() == PetType.SHULKER){
			info.add(ChatColor.GOLD + " - Open: " + ChatColor.YELLOW + ((IShulkerPet) pt).isOpen());
			if(new Version("1.11-R1").isCompatible(new Version())){
				info.add(ChatColor.GOLD + " - Color: " + ChatColor.YELLOW + StringUtil.capitalise(((IShulkerPet) pt).getColor().toString().replace("_", " ")));
			}
		}
		if(pt.getPetType() == PetType.PARROT){
			info.add(ChatColor.GOLD + " - Variant: " + ChatColor.YELLOW + StringUtil.capitalise(((IParrotPet) pt).getVariant().toString().replace("_", " ")));
		}
		return info;
	}
	
	public static List<String> getPetList(CommandSender sender, boolean petAdmin){
		List<String> list = new ArrayList<String>();
		String admin = petAdmin ? "admin" : "";
		for(PetType pt : PetType.values){
			ChatColor color1 = ChatColor.GREEN;
			ChatColor color2 = ChatColor.DARK_GREEN;
			String separator = ", ";
			if(sender instanceof Player){
				
				if(!sender.hasPermission("echopet.pet" + admin + ".type." + pt.getConfigKeyName())){
					color1 = ChatColor.RED;
					color2 = ChatColor.DARK_RED;
				}
				
				StringBuilder builder = new StringBuilder();
				
				builder.append(color1)
					.append("- ")
					.append(StringUtil.capitalise(pt.toString().toLowerCase().replace("_", " ")));
				
				if(!pt.getAllowedDataTypes().isEmpty()){
					builder.append(color2)
						.append("    ");
					for(PetData<?> data : pt.getAllowedDataTypes()){
						builder.append(color2)
							.append(StringUtil.capitalise(data.toString().toLowerCase().replace("_", "")));
						builder.append(separator);
					}
					builder.deleteCharAt(builder.length() - separator.length());
				}
				
				list.add(builder.toString());
			}else{
				StringBuilder builder = new StringBuilder();
				
				builder.append(color1)
					.append("- ")
					.append(StringUtil.capitalise(pt.toString().toLowerCase().replace("_", " ")));
				
				if(!pt.getAllowedDataTypes().isEmpty()){
					builder.append(color2)
						.append(" (");
					for(PetData<?> data : pt.getAllowedDataTypes()){
						builder.append(separator);
						builder.append(color2)
							.append(StringUtil.capitalise(data.toString().toLowerCase().replace("_", "")));
					}
					builder.deleteCharAt(builder.length() - separator.length());
					builder.append(color2)
						.append(")");
				}
				
				list.add(builder.toString().replace(" )", ")"));
			}
		}
		return list;
	}
	
	public static String dataToString(List<PetData<?>> data){
		if(data.isEmpty()){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for(PetData<?> pd : data){
			builder.append(pd.getConfigKeyName());
			builder.append(", ");
		}
		builder.deleteCharAt(builder.length() - 2);
		return builder.toString();
	}
	
	public static String dataToString(List<PetData<?>> data, List<PetData<?>> riderData){
		if(data.isEmpty()){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for(PetData<?> pd : data){
			builder.append(pd.getConfigKeyName());
			builder.append(", ");
		}
		for(PetData<?> pd : riderData){
			builder.append(pd.getConfigKeyName());
			builder.append("(Rider), ");
		}
		builder.deleteCharAt(builder.length() - 2);
		return builder.toString();
	}
}