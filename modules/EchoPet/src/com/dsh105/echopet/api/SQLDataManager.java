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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.dsh105.echopet.compat.api.plugin.action.AsyncBukkitAction;
import com.dsh105.echopet.compat.api.plugin.action.SyncBukkitAction;
import com.dsh105.echopet.compat.api.util.SQLUtil;
import com.dsh105.echopet.compat.api.util.TableMigrationUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SQLDataManager implements IDataManager{
	
	private final Plugin plugin;
	
	public SQLDataManager(Plugin plugin){
		this.plugin = plugin;
	}
	
	@Override
	public ActionChain<IPet> save(Player player, IPet pet, SavedType savedType){
		if(pet.isRider()){
			return new SyncBukkitAction<IPet>(plugin)::setAction;
		}
		final IPet rider = pet.getRider();
		return AsyncBukkitAction.execute(plugin, ()->{
			try(Connection con = EchoPet.getPlugin().getConnection()){
				try(PreparedStatement ps = con.prepareStatement("INSERT INTO " + TableMigrationUtil.LATEST_TABLE + " (OwnerName, PetType, PetName, PetData, RiderPetType, RiderPetName, RiderPetData) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE PetType = VALUES(PetType), PetName = VALUES(PetName), PetData = VALUES(PetData), RiderPetType = VALUES(RiderPetType), RiderPetName = VALUES(RiderPetName), RiderPetData = VALUES(RiderPetData);")){
					ps.setString(1, player.getUniqueId().toString());
					ps.setString(2, pet.getPetType().toString());
					ps.setString(3, pet.getPetName());
					ps.setLong(4, SQLUtil.serializePetData(pet.getPetData()));
					// IPet rider = pet.getRider();
					if(rider != null){
						ps.setString(5, rider.getPetType().toString());
						ps.setString(6, rider.getPetName());
						ps.setLong(7, SQLUtil.serializePetData(rider.getPetData()));
					}else{
						ps.setString(5, null);
						ps.setString(6, null);
						ps.setLong(7, 0);
					}
					ps.executeUpdate();
				}
			}catch(Exception ex){
				plugin.getLogger().log(Level.SEVERE, "Error saving pet data for " + player.getUniqueId(), ex);
			}
			return pet;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error saving pet data for " + player.getUniqueId(), ex));
	}
	
	@Override
	public ActionChain<Boolean> save(Player player, PetStorage pet, PetStorage rider, SavedType savedType){
		return AsyncBukkitAction.execute(plugin, ()->{
			try(Connection con = EchoPet.getPlugin().getConnection()){
				try(PreparedStatement ps = con.prepareStatement("INSERT INTO " + TableMigrationUtil.LATEST_TABLE + " (OwnerName, PetType, PetName, PetData, RiderPetType, RiderPetName, RiderPetData) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE PetType = VALUES(PetType), PetName = VALUES(PetName), PetData = VALUES(PetData), RiderPetType = VALUES(RiderPetType), RiderPetName = VALUES(RiderPetName), RiderPetData = VALUES(RiderPetData);")){
					ps.setString(1, player.getUniqueId().toString());
					ps.setString(2, pet.petType.toString());
					ps.setString(3, pet.petName);
					ps.setLong(4, SQLUtil.serializePetData(pet.petDataList));
					if(rider != null){
						ps.setString(5, rider.petType.toString());
						ps.setString(6, rider.petName);
						ps.setLong(7, SQLUtil.serializePetData(rider.petDataList));
					}else{
						ps.setString(5, null);
						ps.setString(6, null);
						ps.setLong(7, 0);
					}
					ps.executeUpdate();
					return true;
				}
			}catch(Exception ex){
				plugin.getLogger().log(Level.SEVERE, "Error saving pet data for " + player.getUniqueId(), ex);
			}
			return false;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error saving pet data for " + player.getUniqueId(), ex));
	}
	
	@Override
	public ActionChain<IPet> load(Player player, SavedType savedType){
		return AsyncBukkitAction.execute(plugin, ()->{
			try(Connection con = EchoPet.getPlugin().getConnection()){
				try(PreparedStatement ps = con.prepareStatement("SELECT PetType, PetName, PetData, RiderPetType, RiderPetName, RiderPetData FROM " + TableMigrationUtil.LATEST_TABLE + " WHERE OwnerName = ?;")){
					ps.setString(1, player.getUniqueId().toString());
					try(ResultSet rs = ps.executeQuery()){
						if(!rs.next()){
							return null;
						}
						return create(player, rs);
					}
				}
			}catch(Exception ex){
				plugin.getLogger().log(Level.SEVERE, "Error loading pet data for " + player.getUniqueId(), ex);
			}
			return null;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error loading pet data for " + player.getUniqueId(), ex));
	}
	
	private IPet create(Player player, ResultSet rs) throws SQLException{
		IPetType type = PetType.get(rs.getString("PetType"));
		if(type == null){
			return null;
		}
		IPet pet = EchoPet.getManager().createPet(player, type, false);
		if(pet == null){
			return null;
		}
		pet.setPetName(rs.getString("PetName"));
		
		List<PetData> dataList = SQLUtil.deserializePetData(rs.getLong("PetData"));
		for(PetData data : dataList){
			EchoPet.getManager().setData(pet, data, true);
		}
		
		if(rs.getString("RiderPetType") != null){
			IPetType mt = PetType.get(rs.getString("RiderPetType"));
			if(mt == null){
				return null;
			}
			IPet rider = pet.createRider(mt, false);
			if(rider != null){
				rider.setPetName(rs.getString("RiderPetName"));
				List<PetData> riderDataList = SQLUtil.deserializePetData(rs.getLong("RiderPetData"));
				for(PetData data : riderDataList){
					EchoPet.getManager().setData(rider, data, true);
				}
			}
		}
		return pet;
	}
	
	@Override
	public ActionChain<Boolean> remove(Player player, SavedType savedType){
		return AsyncBukkitAction.execute(plugin, ()->{
			try(Connection con = EchoPet.getPlugin().getConnection()){
				try(PreparedStatement ps = con.prepareStatement("DELETE FROM " + TableMigrationUtil.LATEST_TABLE + " WHERE OwnerName = ?;")){
					ps.setString(1, player.getUniqueId().toString());
					ps.executeUpdate();
				}
				return true;
			}catch(SQLException ex){
				plugin.getLogger().log(Level.SEVERE, "Failed to delete Pet data for " + player.getUniqueId(), ex);
			}
			return false;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error deleting pet data for " + player.getUniqueId(), ex));
	}
	
	@Override
	public ActionChain<Boolean> removeAll(){
		return AsyncBukkitAction.execute(plugin, ()->{
			try(Connection con = EchoPet.getPlugin().getConnection()){
				try(PreparedStatement ps = con.prepareStatement("DELETE FROM " + TableMigrationUtil.LATEST_TABLE + ";")){
					ps.executeUpdate();
				}
				return true;
			}catch(SQLException ex){
				plugin.getLogger().log(Level.SEVERE, "Failed to delete Pet data", ex);
			}
			return false;
		}, ex->plugin.getLogger().log(Level.SEVERE, "Error deleting pet data", ex));
	}
}
