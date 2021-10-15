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

package com.dsh105.echopet.compat.nms.v1_16_R3.entity.ai;

import com.dsh105.echopet.compat.api.ai.APetGoalFollowOwner;
import com.dsh105.echopet.compat.api.ai.PetGoalType;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.event.PetMoveEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.EntityPet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.type.EntityGhastPet;
import com.dsh105.echopet.compat.nms.v1_16_R3.entity.type.EntityVexPet;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.GenericAttributes;
import net.minecraft.server.v1_16_R3.Navigation;
import net.minecraft.server.v1_16_R3.PathEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;

public class PetGoalFollowOwner extends APetGoalFollowOwner
{

	private final EntityPet pet;
	private final Navigation nav;
	private int timer = 0;
	private final double speedModifier;
	private final double startDistanceSqr;
	private final double stopDistanceSqr;
	private final double teleportDistanceSqr;

	public PetGoalFollowOwner(EntityPet pet)
	{
		this.pet = pet;
		this.nav = (Navigation) pet.getNavigation();
		double sizeModifier = pet.getSizeCategory().getModifier();
		IPetType petType = pet.getPet().getPetType();
		double startDistance = petType.getStartFollowDistance() * sizeModifier;
		double stopDistance = petType.getStopFollowDistance() * sizeModifier;
		double teleportDistance = petType.getTeleportDistance() * sizeModifier;

		this.speedModifier = petType.getFollowSpeedModifier();
		this.startDistanceSqr = startDistance * startDistance;
		this.stopDistanceSqr = stopDistance * stopDistance;
		this.teleportDistanceSqr = teleportDistance * teleportDistance;
	}

	@Override
	public PetGoalType getType()
	{
		return PetGoalType.THREE;
	}

	@Override
	public String getDefaultKey()
	{
		return "FollowOwner";
	}

	@Override
	public boolean shouldStart()
	{
		if (!this.pet.isAlive())
		{
			return false;
		}
		else if (this.pet.getOwner() == null)
		{
			return false;
		}
		else if (this.pet.getPet().isOwnerRiding() || this.pet.getPet().isHat())
		{
			return false;
		}
		else if (this.pet.h(((CraftPlayer) this.pet.getOwner()).getHandle()) < this.startDistanceSqr)
		{
			return false;
		}
		else
		{
			return !(this.pet.getGoalTarget() != null && this.pet.getGoalTarget().isAlive());
		}
	}

	@Override
	public boolean shouldContinue()
	{
		if (this.nav.f())
		{// Navigation - bottom of class, just returns another method.
			return false;
		}
		else if (this.pet.getOwner() == null)
		{
			return false;
		}
		else if (this.pet.getPet().isOwnerRiding() || this.pet.getPet().isHat())
		{
			return false;
		}
		else
		{
			return !(this.pet.h(((CraftPlayer) this.pet.getOwner()).getHandle()) <= this.stopDistanceSqr);
		}
		// PetGoalMeleeAttack attackGoal = (PetGoalMeleeAttack) this.pet.petGoalSelector.getGoal("Attack");
		// return !(attackGoal != null && attackGoal.isActive);
	}

	@Override
	public void start()
	{
		this.timer = 0;
		// Set pathfinding radius
		pet.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(this.teleportDistanceSqr);
	}

	@Override
	public void finish()
	{
		this.nav.n();// Navigation abstract - return this.c == null || this.c.b();
	}

	@Override
	public void tick()
	{
		// PathfinderGoalFollowOwner
		EntityPlayer owner = ((CraftPlayer) this.pet.getOwner()).getHandle();
		// 1.9: this.d.getControllerLook().a(this.e, 10.0F, this.d.N());
		this.pet.getControllerLook().a(owner, 10.0F, (float) this.pet.O());
		if (--this.timer <= 0)
		{
			this.timer = 10;
			/*if (this.pet.getPlayerOwner().isFlying()) {
			    //Don't move pet when owner flying
			    return;
			}*/
			if (this.pet.h(owner) > teleportDistanceSqr && ((CraftPlayer) this.pet.getOwner()).getHandle().isOnGround() || this.pet.getOwner().isInsideVehicle())
			{
				this.pet.getPet().teleportToOwner();
				return;
			}
			PetMoveEvent moveEvent = new PetMoveEvent(this.pet.getPet(), this.pet.getLocation(), this.pet.getOwner().getLocation());
			EchoPet.getPlugin().getServer().getPluginManager().callEvent(moveEvent);
			if (moveEvent.isCancelled())
			{
				return;
			}
			if (pet.goalTarget == null)
			{
				PathEntity path;
				int followDistance = 3;//Required distance between the owner and target before it paths.
				if (pet instanceof EntityGhastPet || pet instanceof EntityVexPet)
				{
					path = pet.getNavigation().a(pet.getOwner().getLocation().getBlockX(), pet.getOwner().getLocation().getBlockY() + 5, pet.getOwner().getLocation().getBlockZ(), followDistance);
				}
				else
				{
					path = pet.getNavigation().a(owner, followDistance);
				}
				// Smooth path finding to entity instead of location
				pet.getNavigation().a(path, speedModifier);
			}
		}
	}

	public Navigation getNavigation()
	{
		return nav;
	}
}
