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
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.plugin.PetStorage;
import org.bukkit.ChatColor;
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
				result.put(petData, null);
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
		
		if(!petType.isEnabled()){
			Lang.sendTo(sender, Lang.PET_TYPE_DISABLED.toString().replace("%type%", StringUtil.capitalise(petType.toString().replace("_", ""))));
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
				petDataList.replace(dataTemp, null, dataTemp.getParser().defaultValue(petType));
			}
		}
		
		for(PetData<?> dataTemp : petDataList.keySet()){
			if(!Perm.hasDataPerm(sender, true, petType, dataTemp, false)){
				return null;
			}
		}
		
		return new PetStorage(petType, name, petDataList);
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