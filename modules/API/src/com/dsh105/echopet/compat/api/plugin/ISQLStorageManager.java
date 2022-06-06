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

package com.dsh105.echopet.compat.api.plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetType;
import org.bukkit.entity.Player;

public interface ISQLStorageManager extends IStorageManager{
	
	Connection getConnection() throws SQLException;
	
	default void prepareSave(PreparedStatement ps, Player player, SavedType savedType, final IPet pet, final IPet rider) throws SQLException{
		String riderType = null, riderName = null, riderData = null;
		if(rider != null){
			riderType = rider.getPetType().toString();
			riderName = rider.getPetName();
			riderData = serializePetData(rider.getData());
		}
		prepareSave(ps, player, savedType, pet.getPetType().toString(), pet.getPetName(), serializePetData(pet.getData()), riderType, riderName, riderData);
	}
	
	default void prepareSave(PreparedStatement ps, Player player, SavedType savedType, final PetStorage pet, final PetStorage rider) throws SQLException{
		String riderType = null, riderName = null, riderData = null;
		if(rider != null){
			riderType = rider.petType.toString();
			riderName = rider.petName;
			riderData = serializePetData(rider.petDataList);
		}
		prepareSave(ps, player, savedType, pet.petType.toString(), pet.petName, serializePetData(pet.petDataList), riderType, riderName, riderData);
	}
	
	default void prepareSave(PreparedStatement ps, Player player, SavedType savedType, String petType, String petName, String petData, String riderType, String riderName, String riderData) throws SQLException{
		ps.setString(1, player.getUniqueId().toString());
		ps.setInt(2, savedType.getId());
		ps.setString(3, petType);
		ps.setString(4, petName);
		ps.setString(5, petData);
		ps.setString(6, riderType);
		ps.setString(7, riderName);
		ps.setString(8, riderData);
	}
	
	default PetStorage create(ResultSet rs) throws SQLException{
		IPetType type = PetType.get(rs.getString("type"));
		if(type == null){
			return null;
		}
		Map<PetData<?>, Object> dataList = deserializePetData(rs.getString("data"));
		
		PetStorage pet = new PetStorage(type, rs.getString("name"), dataList);
		
		if(pet.petType.allowRidersFor() && rs.getString("rider_type") != null){
			IPetType mt = PetType.get(rs.getString("rider_type"));
			if(mt == null){
				return null;
			}
			Map<PetData<?>, Object> riderDataList = deserializePetData(rs.getString("rider_data"));
			pet.rider = new PetStorage(mt, rs.getString("rider_name"), riderDataList);
		}
		return pet;
	}
	
	default String serializePetData(Map<PetData<?>, Object> data){
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<PetData<?>, Object> entry : data.entrySet()){
			if(sb.length() != 0) sb.append(';');
			sb.append(entry.getKey().getConfigKeyName())
				.append(':')
				.append(entry.getValue());
		}
		return sb.toString();
	}
	
	default Map<PetData<?>, Object> deserializePetData(String data){
		Map<PetData<?>, Object> result = new LinkedHashMap<>();
		if(data == null) return result;
		for(String s : data.split(";")){
			String[] split = s.split(":");
			if(split.length != 2) continue;
			PetData<?> pd = PetData.get(split[0]);
			if(pd == null) continue;
			Object value = pd.getParser().parse(split[1]);
			if(value == null) continue;
			result.put(pd, value);
		}
		return result;
	}
}
