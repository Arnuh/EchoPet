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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.IEntityNoClipPet;
import com.dsh105.echopet.compat.api.entity.IEntityPet;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.PetData;
import com.dsh105.echopet.compat.api.entity.PetDataCategory;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.event.PetSpawnEvent;
import com.dsh105.echopet.compat.api.event.PetTeleportEvent;
import com.dsh105.echopet.compat.api.particle.Trail;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.plugin.uuid.UUIDMigration;
import com.dsh105.echopet.compat.api.util.Lang;
import com.dsh105.echopet.compat.api.util.PetNames;
import com.dsh105.echopet.compat.api.util.StringSimplifier;
import com.dsh105.echopet.compat.api.util.StringUtil;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Pet implements IPet{
	
	private IEntityPet entityPet;
	private IPetType petType;
	
	private Object ownerIdentification;
	private Pet rider, lastRider;
	private String name;
	private final ArrayList<PetData> petData = new ArrayList<>();
	private InventoryView dataMenu;
	private final List<com.dsh105.echopet.compat.api.particle.Trail> trails = Lists.newArrayList();
	
	private boolean isRider = false;
	
	protected boolean ownerIsMounting = false;
	protected boolean ownerRiding = false, isHat = false;
	protected boolean isHidden = false;
	
	public Pet(Player owner){
		if(owner != null){
			this.ownerIdentification = UUIDMigration.getIdentificationFor(owner);
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
				this.applyPetName();
				this.teleportToOwner();
				for(PetData pd : getPetData()){
					if(pd.getAction() == null) continue;
					pd.getAction().click(owner, this, PetDataCategory.getByData(getPetType(), pd), true);
				}
				EchoPet.getPlugin().getServer().getPluginManager().callEvent(new PetSpawnEvent(this));
				for(Trail t : trails){
					t.start(this);
				}
				if(lastRider != null && !lastRider.isSpawned()){
					setRider(lastRider);
					setLastRider(null);
				}
			}
			// }
		}else{
			EchoPet.getManager().saveFileData("autosave", this);
			EchoPet.getSqlManager().saveToDatabase(this, false);
			EchoPet.getManager().removePet(this, false);
		}
		return entityPet;
	}
	
	@Override
	public IEntityPet getEntityPet(){
		return this.entityPet;
	}
	
	@Override
	public LivingEntity getCraftPet(){
		return this.getEntityPet().getEntity();
	}
	
	@Override
	public Location getLocation(){
		return this.getCraftPet().getLocation();
	}
	
	@Override
	public Player getOwner(){
		if(this.ownerIdentification == null){
			return null;
		}
		if(this.ownerIdentification instanceof UUID){
			return Bukkit.getPlayer((UUID) ownerIdentification);
		}else{
			return Bukkit.getPlayerExact((String) this.ownerIdentification);
		}
	}
	
	@Override
	public String getNameOfOwner(){
		if(this.ownerIdentification instanceof String){
			return (String) this.ownerIdentification;
		}else{
			return this.getOwner() == null ? "" : this.getOwner().getName();
		}
	}
	
	@Override
	public UUID getOwnerUUID(){
		if(this.ownerIdentification instanceof UUID){
			return (UUID) this.ownerIdentification;
		}else{
			return this.getOwner() == null ? null : this.getOwner().getUniqueId();
		}
	}
	
	@Override
	public Object getOwnerIdentification(){
		return ownerIdentification;
	}
	
	@Override
	public IPetType getPetType(){
		return this.petType;
	}
	
	@Override
	public boolean isRider(){
		return this.isRider;
	}
	
	protected void setRider(){
		this.isRider = true;
	}
	
	@Override
	public boolean isOwnerInMountingProcess(){
		return ownerIsMounting;
	}
	
	@Override
	public Pet getRider(){
		return this.rider;
	}
	
	@Override
	public Pet getLastRider(){
		return this.lastRider;
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
				this.name = StringSimplifier.stripDiacritics(this.name);
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
	public ArrayList<PetData> getPetData(){
		return this.petData;
	}
	
	@Override
	public void setLastRider(IPet lastRider){
		if(lastRider == null) this.lastRider = null;
		else this.lastRider = (Pet) lastRider;
	}
	
	@Override
	public void removeRider(boolean makeSound, boolean makeParticles){
		if(rider != null){
			lastRider = rider;
			rider.removePet(makeSound, makeParticles);
			rider = null;
			getCraftPet().eject();
		}
	}
	
	@Override
	public void removePet(boolean makeSound, boolean makeParticles){
		if(getEntityPet() != null && this.getCraftPet() != null && makeParticles){
			getLocation().getWorld().spawnParticle(Particle.CLOUD, getLocation(), 1);
			getLocation().getWorld().spawnParticle(Particle.LAVA, getLocation(), 1);
		}
		setAsHat(false);
		removeRider(makeSound, makeParticles);
		for(com.dsh105.echopet.compat.api.particle.Trail trail : trails){
			trail.cancel();
		}
		if(this.getEntityPet() != null){
			this.getEntityPet().remove(makeSound);
			this.entityPet = null;
		}
	}
	
	@Override
	public boolean teleportToOwner(){
		if(this.getOwner() == null || this.getOwner().getLocation() == null){
			EchoPet.getManager().saveFileData("autosave", this);
			EchoPet.getSqlManager().saveToDatabase(this, false);
			EchoPet.getManager().removePet(this, true);
			return false;
		}
		if(!isSpawned()) return false;
		Pet rider = getRider();
		removeRider(false, false);
		boolean tele = teleport(this.getOwner().getLocation());
		if(tele && rider != null){
			this.rider = rider;
			this.rider.spawnPet(getOwner(), true);
			getCraftPet().addPassenger(rider.getCraftPet());
			EchoPet.getSqlManager().saveToDatabase(rider, true);
		}
		return tele;
	}
	
	@Override
	public boolean teleport(Location to){
		if(this.getOwner() == null || this.getOwner().getLocation() == null){
			EchoPet.getManager().saveFileData("autosave", this);
			EchoPet.getSqlManager().saveToDatabase(this, false);
			EchoPet.getManager().removePet(this, false);
			return false;
		}
		if(!isSpawned()) return false;
		PetTeleportEvent teleportEvent = new PetTeleportEvent(this, this.getLocation(), to);
		EchoPet.getPlugin().getServer().getPluginManager().callEvent(teleportEvent);
		if(teleportEvent.isCancelled()) return false;
		Location l = teleportEvent.getTo();
		if(l.getWorld() == this.getLocation().getWorld()){
			if(this.getRider() != null){
				this.getRider().getCraftPet().eject();
				this.getRider().getCraftPet().teleport(l);
			}
			this.getCraftPet().teleport(l);
			if(this.getRider() != null){
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
		getLocation().getWorld().spawnParticle(Particle.PORTAL, getLocation(), 1);
		Location l = this.getLocation().clone();
		l.setY(l.getY() - 1D);
		getLocation().getWorld().spawnParticle(Particle.PORTAL, getLocation(), 1);
		// Lots of ways call setAsHat, might as well properly sync the petdata in here.
		EchoPet.getManager().setData(this, PetData.HAT, isHat);
	}
	
	@Override
	public Pet createRider(final IPetType pt, boolean sendFailMessage){
		if(pt == PetType.HUMAN){
			if(sendFailMessage){
				Lang.sendTo(this.getOwner(), Lang.RIDERS_DISABLED.toString().replace("%type%", StringUtil.capitalise(this.getPetType().toString())));
			}
			return null;
		}
		if(!getPetType().allowRidersFor()){
			if(sendFailMessage){
				Lang.sendTo(this.getOwner(), Lang.RIDERS_DISABLED.toString().replace("%type%", StringUtil.capitalise(this.getPetType().toString())));
			}
			return null;
		}
		if(this.isOwnerRiding()){
			this.ownerRidePet(false);
		}
		if(this.rider != null){
			this.removeRider(true, true);
		}
		IPet newRider = pt.getNewPetInstance(this.getOwner());
		if(newRider == null){
			if(sendFailMessage){
				Lang.sendTo(getOwner(), Lang.PET_TYPE_NOT_COMPATIBLE.toString().replace("%type%", StringUtil.capitalise(getPetType().toString())));
			}
			return null;
		}
		if(isSpawned()) newRider.spawnPet(getOwner(), false);
		this.rider = (Pet) newRider;
		this.rider.setRider();
		if(isSpawned()) getCraftPet().addPassenger(newRider.getCraftPet());
		EchoPet.getSqlManager().saveToDatabase(rider, true);
		
		return this.rider;
	}
	
	@Override
	public void setRider(IPet newRider){
		if(!isSpawned()) return;
		if(!getPetType().allowRidersFor()){
			Lang.sendTo(this.getOwner(), Lang.RIDERS_DISABLED.toString().replace("%type%", StringUtil.capitalise(this.getPetType().toString())));
			return;
		}
		if(this.isOwnerRiding()){
			this.ownerRidePet(false);
		}
		if(this.rider != null){
			this.removeRider(true, true);
		}
		if(!newRider.isSpawned()) newRider.spawnPet(getOwner(), false);
		this.rider = (Pet) newRider;
		this.rider.setRider();
		getCraftPet().addPassenger(rider.getCraftPet());
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
	public List<Trail> getTrails(){
		return this.trails;
	}
	
	@Override
	public void addTrail(Trail trail){
		trails.add(trail);
	}
	
	@Override
	public void removeTrail(Trail trail){
		trails.remove(trail);
	}
	
	@Override
	public boolean isHidden(){
		return isHidden;
	}
	
	@Override
	public void setHidden(boolean isHidden){
		this.isHidden = isHidden;
	}
}
