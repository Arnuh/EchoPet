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

package com.dsh105.echopet.api.pet;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.data.PetData;
import com.dsh105.echopet.compat.api.entity.nms.IEntityPet;
import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityPetHandle;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityNoClipPet;
import com.dsh105.echopet.compat.api.event.PetSpawnEvent;
import com.dsh105.echopet.compat.api.event.PetTeleportEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.LocationUtil;
import com.dsh105.echopet.compat.api.util.PetNames;
import com.dsh105.echopet.compat.api.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Pet implements IPet{
	
	private IEntityPet entityPet;
	private IPetType petType;
	private UUID ownerUUID;
	private IPet rider;
	private String name;
	private final Map<PetData<?>, Object> petData = new LinkedHashMap<>();
	private InventoryView dataMenu;
	
	private boolean isRider = false;
	
	protected boolean ownerIsMounting = false;
	protected boolean ownerRiding = false, isHat = false;
	protected boolean isHidden = false;
	
	public Pet(Player owner){
		if(owner != null){
			this.ownerUUID = owner.getUniqueId();
			setPetType();
		}
	}
	
	protected void setPetType(){
		EntityPetType entityPetType = this.getClass().getAnnotation(EntityPetType.class);
		if(entityPetType != null){
			setPetType(entityPetType.petType());
		}
	}
	
	protected void setPetType(IPetType petType){
		this.petType = petType;
		setPetName(getPetType().getDefaultName(getNameOfOwner()));
	}
	
	@Override
	public boolean isSpawned(){
		if(entityPet == null) return false;
		return !entityPet.isDead();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public IEntityPet spawnPet(Player owner, boolean ignoreHidden){
		if(entityPet != null){
			return entityPet;
		}
		if(owner != null){
			// if(!EchoPet.getPlugin().getVanishProvider().isVanished(owner)){// We don't spawn pets at all if the player is vanished due to bounding boxes.
			if(isHidden && !ignoreHidden){
				return null;
			}
			this.entityPet = EchoPet.getPlugin().getSpawnUtil().spawn(this, owner);
			if(this.entityPet != null){
				// Does a way exist to add this earlier?
				Entity entity = entityPet.getEntity();
				entity.getPersistentDataContainer().set(EchoPet.getPlugin().getPetNamespacedKey(), PersistentDataType.BYTE, (byte) 1);
				
				this.applyPetName();
				// If we got here, a new entity was  spawned on our position anyway.
				// this.teleportToOwner();
				for(Map.Entry<PetData<?>, Object> entry : getData().entrySet()){
					PetData<Object> pd = (PetData<Object>) entry.getKey();
					EchoPet.getManager().executePetDataAction(owner, this, pd, entry.getValue());
				}
				EchoPet.getPlugin().getServer().getPluginManager().callEvent(new PetSpawnEvent(this));
				if(rider != null && !rider.isSpawned()){
					spawnRider();
				}
			}
		}else{
			EchoPet.getManager().removePet(this, false);
		}
		return entityPet;
	}
	
	@Override
	public IEntityPet getEntityPet(){
		return this.entityPet;
	}
	
	/**
	 * Shortcut for {@link IEntityPet#getHandle()}
	 */
	@Override
	public IEntityPetHandle getHandle(){
		return getEntityPet().getHandle();
	}
	
	@Override
	public Entity getCraftPet(){
		return this.getEntityPet().getEntity();
	}
	
	@Override
	public Location getLocation(){
		return this.getCraftPet().getLocation();
	}
	
	@Override
	public Player getOwner(){
		if(this.ownerUUID == null){
			return null;
		}
		return Bukkit.getPlayer(ownerUUID);
	}
	
	@Override
	public String getNameOfOwner(){
		Player owner = getOwner();
		return owner == null ? "" : owner.getName();
	}
	
	@Override
	public UUID getOwnerUUID(){
		return ownerUUID;
	}
	
	@Override
	public IPetType getPetType(){
		return this.petType;
	}
	
	@Override
	public boolean isRider(){
		return this.isRider;
	}
	
	@Override
	public void setIsRider(){
		this.isRider = true;
	}
	
	@Override
	public boolean isOwnerInMountingProcess(){
		return ownerIsMounting;
	}
	
	@Override
	public IPet getRider(){
		return this.rider;
	}
	
	@Override
	public String getPetName(){
		return name;
	}
	
	@Override
	public String getPetNameWithoutColours(){
		return ChatColor.stripColor(this.getPetName());
	}
	
	@Override
	public String serialisePetName(){
		return getPetName().replace(ChatColor.COLOR_CHAR, '&');
	}
	
	@Override
	public boolean setPetName(String name){
		return this.setPetName(name, true);
	}
	
	@Override
	public boolean setPetName(String name, boolean sendFailMessage){
		if(PetNames.allow(name, this)){
			this.name = ChatColor.translateAlternateColorCodes('&', name);
			if(EchoPet.getPlugin().getMainConfig().getBoolean("stripDiacriticsFromNames", true)){
				this.name = StringUtil.stripDiacritics(this.name);
			}
			if(this.name == null || this.name.equalsIgnoreCase("")){
				this.name = this.petType.getDefaultName(this.getNameOfOwner());
			}
			this.applyPetName();
			return true;
		}else{
			if(sendFailMessage){
				if(this.getOwner() != null){
					Lang.sendTo(this.getOwner(), Lang.NAME_NOT_ALLOWED.toString().replace("%name%", name));
				}
			}
			return false;
		}
	}
	
	protected void applyPetName(){
		if(this.getEntityPet() != null && this.getCraftPet() != null){
			this.getCraftPet().setCustomName(this.name);
			this.getCraftPet().setCustomNameVisible(getPetType().isTagVisible());
		}
	}
	
	@Override
	public Map<PetData<?>, Object> getData(){
		return this.petData;
	}
	
	@Override
	public int getMaxPassengers(){
		return 1;
	}
	
	@Override
	public IPet createRider(final IPetType pt, boolean sendFailMessage){
		Player owner = getOwner();
		if(pt == PetType.HUMAN){
			if(sendFailMessage){
				Lang.sendTo(owner, Lang.RIDERS_DISABLED.toString().replace("%type%", StringUtil.capitalise(getPetType().toString())));
			}
			return null;
		}
		if(!getPetType().allowRidersFor()){
			if(sendFailMessage){
				Lang.sendTo(owner, Lang.RIDERS_DISABLED.toString().replace("%type%", StringUtil.capitalise(getPetType().toString())));
			}
			return null;
		}
		if(getMaxPassengers() == 1 && this.isOwnerRiding()){
			this.ownerRidePet(false);
		}
		if(this.rider != null){
			this.removeRider(true, true);
		}
		IPet newRider = pt.getNewPetInstance(owner);
		if(newRider == null){
			if(sendFailMessage){
				Lang.sendTo(owner, Lang.PET_TYPE_NOT_COMPATIBLE.toString().replace("%type%", StringUtil.capitalise(getPetType().toString())));
			}
			return null;
		}
		if(isSpawned()){
			newRider.spawnPet(owner, false);
		}
		this.rider = newRider;
		this.rider.setIsRider();
		if(isSpawned() && rider.isSpawned()){
			getCraftPet().addPassenger(rider.getCraftPet());
		}
		return this.rider;
	}
	
	@Override
	public void setRider(IPet newRider){
		if(!isSpawned()) return;
		if(!getPetType().allowRidersFor()){
			Lang.sendTo(this.getOwner(), Lang.RIDERS_DISABLED.toString().replace("%type%", StringUtil.capitalise(this.getPetType().toString())));
			return;
		}
		if(getMaxPassengers() == 1 && this.isOwnerRiding()){
			this.ownerRidePet(false);
		}
		if(rider != null){
			this.removeRider(true, true);
		}
		this.rider = newRider;
		this.rider.setIsRider();
		spawnRider();
	}
	
	@Override
	public boolean spawnRider(){
		if(rider == null || rider.isSpawned()){
			return false;
		}
		IEntityPet entityPet = rider.spawnPet(getOwner(), false);
		if(entityPet == null){
			return false;
		}
		getCraftPet().addPassenger(rider.getCraftPet());
		return true;
	}
	
	@Override
	public void despawnRider(boolean makeSound, boolean makeParticles){
		if(rider == null){
			return;
		}
		rider.removePet(makeSound, makeParticles);
	}
	
	@Override
	public void removeRider(boolean makeSound, boolean makeParticles){
		if(rider == null){
			return;
		}
		despawnRider(makeSound, makeParticles);
		rider = null;
	}
	
	@Override
	public void removePet(boolean makeSound, boolean makeParticles){
		if(getEntityPet() != null && this.getCraftPet() != null && makeParticles){
			getLocation().getWorld().spawnParticle(Particle.CLOUD, getLocation(), 1);
			getLocation().getWorld().spawnParticle(Particle.LAVA, getLocation(), 1);
		}
		setAsHat(false);
		removeRider(makeSound, makeParticles);
		if(getEntityPet() != null){
			getEntityPet().remove(makeSound);
			entityPet = null;
		}
	}
	
	@Override
	public boolean teleportToOwner(){
		if(this.getOwner() == null || this.getOwner().getLocation() == null){
			EchoPet.getManager().removePet(this, true);
			return false;
		}
		if(!isSpawned()) return false;
		IPet rider = getRider();
		removeRider(false, false);
		boolean tele = teleport(LocationUtil.centerLocation(getOwner().getLocation()));
		if(tele && rider != null){
			this.rider = rider;
			if(rider.spawnPet(getOwner(), true) != null){
				getCraftPet().addPassenger(rider.getCraftPet());
			}
		}
		return tele;
	}
	
	@Override
	public boolean teleport(Location to){
		if(this.getOwner() == null || this.getOwner().getLocation() == null){
			EchoPet.getManager().removePet(this, false);
			return false;
		}
		if(!isSpawned()) return false;
		PetTeleportEvent teleportEvent = new PetTeleportEvent(this, this.getLocation(), to);
		EchoPet.getPlugin().getServer().getPluginManager().callEvent(teleportEvent);
		if(teleportEvent.isCancelled()) return false;
		Location l = teleportEvent.getTo();
		if(l.getWorld() == this.getLocation().getWorld()){
			if(getRider() != null && getRider().isSpawned()){
				this.getRider().getCraftPet().eject();
				this.getRider().getCraftPet().teleport(l);
			}
			this.getCraftPet().teleport(l);
			if(getRider() != null && getRider().isSpawned()){
				this.getCraftPet().addPassenger(this.getRider().getCraftPet());
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isOwnerRiding(){
		return this.ownerRiding;
	}
	
	@Override
	public boolean isHat(){
		return this.isHat;
	}
	
	@Override
	public void ownerRidePet(boolean flag){
		if(this.ownerRiding == flag) return;
		
		this.ownerIsMounting = true;
		
		if(this.isHat){
			this.setAsHat(false);
		}
		
		if(!isSpawned()){
			this.ownerIsMounting = false;
			return;
		}
		
		if(!flag){
			if(getCraftPet() != null){
				getCraftPet().eject();
				if(getEntityPet() instanceof IEntityNoClipPet noClipPet){
					noClipPet.noClip(true);
				}
			}
			ownerIsMounting = false;
		}else{
			if(getCraftPet() != null){
				if(getRider() != null && getRider().isSpawned()){
					getRider().removePet(false, true);
				}
				new BukkitRunnable(){
					
					@Override
					public void run(){
						getCraftPet().addPassenger(getOwner());
						ownerIsMounting = false;
						if(getEntityPet() instanceof IEntityNoClipPet noClipPet){
							noClipPet.noClip(false);
						}
					}
				}.runTaskLater(EchoPet.getPlugin(), 5L);
			}else{
				ownerIsMounting = false;
			}
		}
		this.teleportToOwner();
		this.ownerRiding = flag;
		if(getEntityPet() != null)
			getLocation().getWorld().spawnParticle(Particle.PORTAL, getLocation(), 1);
		EchoPet.getManager().setData(this, PetData.RIDE, ownerRiding);
	}
	
	@Override
	public void setAsHat(boolean flag){
		if(isHat == flag) return;
		if(ownerRiding){
			ownerRidePet(false);
		}
		
		if(!isSpawned()) return;
		
		this.teleportToOwner();
		
		if(!flag){
			getOwner().eject();
		}else{
			getOwner().addPassenger(getCraftPet());
		}
		this.isHat = flag;
		if(getEntityPet() != null)
			getLocation().getWorld().spawnParticle(Particle.PORTAL, getLocation(), 1);
		Location l = this.getLocation().clone();
		l.setY(l.getY() - 1D);
		if(getEntityPet() != null)
			getLocation().getWorld().spawnParticle(Particle.PORTAL, getLocation(), 1);
		// Lots of ways call setAsHat, might as well properly sync the petdata in here.
		EchoPet.getManager().setData(this, PetData.HAT, isHat);
	}
	
	@Override
	public InventoryView getInventoryView(){
		return dataMenu;
	}
	
	@Override
	public void setInventoryView(InventoryView dataMenu){
		this.dataMenu = dataMenu;
	}
	
	@Override
	public boolean isHidden(){
		return isHidden;
	}
	
	@Override
	public void setHidden(boolean isHidden){
		this.isHidden = isHidden;
	}
	
	@Override
	public void generatePetInfo(List<String> info){
		info.add(ChatColor.GOLD + " - Pet Type: " + ChatColor.YELLOW + StringUtil.capitalise(getPetType().toString()));
		info.add(ChatColor.GOLD + " - Name: " + ChatColor.YELLOW + getPetName());
	}
}
