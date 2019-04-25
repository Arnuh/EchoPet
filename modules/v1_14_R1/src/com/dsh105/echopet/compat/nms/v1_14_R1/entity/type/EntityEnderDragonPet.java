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
package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityEnderDragonPet;
import com.dsh105.echopet.compat.api.event.PetRideJumpEvent;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.Logger;
import com.dsh105.echopet.compat.nms.v1_14_R1.entity.EntityNoClipPet;

import net.minecraft.server.v1_13_R2.*;

@EntitySize(width = 16.0F, height = 8.0F)
@EntityPetType(petType = PetType.ENDERDRAGON)
public class EntityEnderDragonPet extends EntityNoClipPet implements IComplex, IMonster, IEntityEnderDragonPet{

	public static final DataWatcherObject<Integer> PHASE = DataWatcher.a(EntityEnderDragonPet.class, DataWatcherRegistry.b);
	private double targetX;// 1.8.8: a
	private double targetY;// 1.8.8: b
	private double targetZ;// 1.8.8: c
	public int someIndex = -1;
	public double[][] circlePoints = new double[64][3];// idfk its a point of yaw,y positions
	private EntityComplexPart[] children;
	private EntityComplexPart head;
	private EntityComplexPart body;
	private EntityComplexPart tail1;
	private EntityComplexPart tail2;
	private EntityComplexPart tail3;
	private EntityComplexPart wing1;
	private EntityComplexPart wing2;
	private float someInc2;// incs are used for when to play the flapping sound based on the movement of the dragon.
	private float someInc1;
	private boolean useless1;// I can never see where this is set to true. If true it finds a target.
	private boolean useless2;// Gets set to true if the dragon breaks blocks(i think). If true it reduces "mot" by 20%? and reduces occurrence of wing flaps by 50%
	private Entity target;// Idk why we even save this.

	public EntityEnderDragonPet(World world){
		super(EntityTypes.ENDER_DRAGON, world);
	}

	public EntityEnderDragonPet(World world, IPet pet){
		super(EntityTypes.ENDER_DRAGON, world, pet);
		this.children = new EntityComplexPart[]{this.head = new EntityComplexPart(this, "head", 6.0F, 6.0F), this.body = new EntityComplexPart(this, "body", 8.0F, 8.0F), this.tail1 = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.tail2 = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.tail3 = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.wing1 = new EntityComplexPart(this, "wing", 4.0F, 4.0F), this.wing2 = new EntityComplexPart(this, "wing", 4.0F, 4.0F)};
		this.noClip(true);
		this.targetY = 100.0D;
		this.ak = true;// in EntityDragon constructor
	}

	protected void initDatawatcher(){
		super.initDatawatcher();
		getDataWatcher().register(PHASE, Integer.valueOf(DragonControllerPhase.HOVER.b()));
	}

	@Override
	public void resizeBoundingBox(boolean flag){
		this.setSize(flag ? 8.0F : 16.0F, flag ? 4.0F : 8.0F);
	}

	public double[] b(int i, float f){
		if(this.getHealth() <= 0.0F){
			f = 0.0F;
		}
		f = 1.0F - f;
		int j = this.someIndex - i * 1 & 63;
		int k = this.someIndex - i * 1 - 1 & 63;
		double[] adouble = new double[3];
		double d0 = this.circlePoints[j][0];
		double d1 = MathHelper.g(this.circlePoints[k][0] - d0);
		adouble[0] = d0 + d1 * (double) f;
		d0 = this.circlePoints[j][1];
		d1 = this.circlePoints[k][1] - d0;
		adouble[1] = d0 + d1 * (double) f;
		adouble[2] = this.circlePoints[j][2] + (this.circlePoints[k][2] - this.circlePoints[j][2]) * (double) f;
		return adouble;
	}

	//
	public void tick(){// Just earch for "0.2F /" in entity dragon.
		// This is all custom.
		Entity passenger = passengers.size() > 0 ? passengers.get(0) : null;
		if(passenger != null && (passenger instanceof EntityHuman)){
			EntityHuman human = (EntityHuman) passenger;
			if(human.getBukkitEntity() == this.getPlayerOwner().getPlayer()){
				float sideMot = ((EntityLiving) passenger).bh;
				// float forw = ((EntityLiving) passenger).bi;
				// Apparently ender dragon uses up motion instead of forward?
				float upMot = ((EntityLiving) passenger).bj;
				// System.out.println(side + ", " + forw + ", " + up);
				Vector v = new Vector();
				Location l = new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);
				if(sideMot < 0.0F){
					l.setYaw(passenger.yaw - 90);
					v.add(l.getDirection().normalize().multiply(-0.5));
				}else if(sideMot > 0.0F){
					l.setYaw(passenger.yaw + 90);
					v.add(l.getDirection().normalize().multiply(-0.5));
				}
				if(upMot < 0.0F){
					l.setYaw(passenger.yaw);
					v.add(l.getDirection().normalize().multiply(0.5));
				}else if(upMot > 0.0F){
					l.setYaw(passenger.yaw);
					v.add(l.getDirection().normalize().multiply(0.5));
				}
				this.yaw = passenger.yaw - 180;// fixes the enderdragon position
				this.lastYaw = this.yaw;
				this.pitch = passenger.pitch * 0.5F;
				this.setYawPitch(this.yaw, this.pitch);
				this.aO = this.aP = this.yaw;
				if(this.FIELD_JUMP != null){
					try{
						if(this.FIELD_JUMP.getBoolean(passenger)){
							PetRideJumpEvent rideEvent = new PetRideJumpEvent(this.getPet(), this.jumpHeight);
							EchoPet.getPlugin().getServer().getPluginManager().callEvent(rideEvent);
							if(!rideEvent.isCancelled()){
								v.setY(0.5F);
							}
						}else{
							if(((EntityLiving) passenger).pitch >= 50){
								v.setY(-0.4F);
							}
						}
					}catch(IllegalArgumentException e){
						Logger.log(Logger.LogLevel.WARNING, "Failed to initiate LivingPet Flying Motion for " + this.getPlayerOwner().getName() + "'s LivingPet.", e, true);
					}catch(IllegalAccessException e){
						Logger.log(Logger.LogLevel.WARNING, "Failed to initiate LivingPet Flying Motion for " + this.getPlayerOwner().getName() + "'s LivingPet.", e, true);
					}catch(IllegalStateException e){
						Logger.log(Logger.LogLevel.WARNING, "Failed to initiate LivingPet Flying Motion for " + this.getPlayerOwner().getName() + "'s LivingPet.", e, true);
					}
				}
				l.add(v.multiply(Math.pow(this.rideSpeed, this.rideSpeed)));
				this.setPos(l.getX(), l.getY(), l.getZ());
				this.updateComplexParts();
				return;
			}
		}
		// Shit grabbed from m() in 1.8.8
		if(this.world.isClientSide){
			float f = MathHelper.cos(this.someInc1 * 3.1415927F * 2.0F);
			float f1 = MathHelper.cos(this.someInc2 * 3.1415927F * 2.0F);
			if(f1 <= -0.3F && f >= -0.3F && !this.isSilent()){
				this.world.a(this.locX, this.locY, this.locZ, getSoundFromString("entity.enderdragon.flap"), bV(), 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
			}
		}
		this.someInc2 = this.someInc1;
		// hp check for explosion but we don't do that
		// For EnderCrystals
		// this.n();
		float f = 0.2F / (MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 10.0F + 1.0F);
		f *= (float) Math.pow(2.0D, this.motY);
		if(this.useless2){
			this.someInc1 += f * 0.5F;
		}else{
			this.someInc1 += f;
		}
		this.yaw = MathHelper.g(this.yaw);
		if(isNoAI()){
			this.someInc1 = 0.5F;
		}else{
			if(this.someIndex < 0){
				for(int i = 0; i < this.circlePoints.length; ++i){
					this.circlePoints[i][0] = this.yaw;
					this.circlePoints[i][1] = this.locY;
				}
			}
			if(++this.someIndex == this.circlePoints.length){
				this.someIndex = 0;
			}
		}
		this.circlePoints[this.someIndex][0] = (double) this.yaw;
		this.circlePoints[this.someIndex][1] = this.locY;
		if(this.world.isClientSide){
			/*if(this.bg > 0){
				double d3 = this.locX + (this.bh - this.locX) / this.bg;
				double d0 = this.locY + (this.bi - this.locY) / this.bg;
				double d1 = this.locZ + (this.bj - this.locZ) / this.bg;
				double d2 = MathHelper.g(this.bg - this.yaw);
				this.yaw = ((float) (this.yaw + d2 / this.bg));
				this.pitch = ((float) (this.pitch + (this.bl - this.pitch) / this.bg));
				this.bg -= 1;
				setPosition(d3, d0, d1);
				setYawPitch(this.yaw, this.pitch);
			}*/
		}else{
			double d0 = this.targetX - this.locX;
			double d1 = this.targetY - this.locY;
			double d2 = this.targetZ - this.locZ;
			double d4 = d0 * d0 + d1 * d1 + d2 * d2;
			if(this.target != null){
				this.targetX = this.target.locX;
				this.targetZ = this.target.locZ;
				double d5 = this.targetX - this.locX;
				double d6 = this.targetZ - this.locZ;
				double d7 = Math.sqrt(d5 * d5 + d6 * d6);
				double d44 = 0.4000000059604645D + d7 / 80.0D - 1.0D;
				if(d44 > 10.0D){
					d44 = 10.0D;
				}
				this.targetY = (this.target.getBoundingBox().minY + d44);
			}else{
				this.targetX += this.random.nextGaussian() * 2.0D;
				this.targetZ += this.random.nextGaussian() * 2.0D;
			}
			if((this.useless1) || (d4 < 100.0D) || (d4 > 22500.0D) || (this.positionChanged) /*|| (this.E)*/){
				// Finds a target.
				// cf();
			}
			float f3 = 0.6F;
			// d0 /= MathHelper.sqrt(d3 * d3 + d1 * d1);
			// d0 = MathHelper.a(d0, -f3, f3);
			d1 = MathHelper.a(d2 / MathHelper.sqrt(d1 * d1 + d4 * d4), -f3, f3);
			this.motY += d1 * 0.10000000149011612D;
			this.yaw = MathHelper.g(this.yaw);
			double d5 = 180.0D - MathHelper.a(d0, d2) * 180.0D / 3.1415927410125732D;
			double d6 = MathHelper.g(d5 - this.yaw);
			if(d6 > 50.0D){
				d6 = 50.0D;
			}
			if(d6 < -50.0D){
				d6 = -50.0D;
			}
			Vec3D vec3d = new Vec3D(this.targetX - this.locX, this.targetY - this.locY, this.targetZ - this.locZ).a();
			// double d44 = -MathHelper.cos(this.yaw * 3.1415927F / 180.0F);
			// Vec3D vec3d1 = new Vec3D(MathHelper.sin(this.yaw * 3.1415927F / 180.0F), this.motY, d44).a();
			Vec3D vec3d2 = new Vec3D(MathHelper.sin(this.yaw * 0.017453292F), this.motY, -MathHelper.cos(this.yaw * 0.017453292F)).a();
			//
			float f4 = Math.max(((float) vec3d2.b(vec3d) + 0.5F) / 1.5F, 0.0F);
			this.bk *= 0.8F;
			float f55 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0F + 1.0F;
			/*double d10 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0D + 1.0D;
			if(d10 > 40.0D){
				d10 = 40.0D;
			}*/
			// this.bk = ((float)(this.bk + d5 * idragoncontroller.h()));
			this.bk = ((float) (this.bk + d6 * (0.699999988079071D / d4 / f55)));
			this.yaw += this.bk * 0.1F;
			float f5 = (float) (2.0D / (d4 + 1.0D));

			a(0.0F, 0.0F, -1.0F, 0.06F * (f4 * f5 + (1.0F - f5)));
			// float f7 = 0.06F;
			// a(0.0F, -1.0F, f7 * (f4 * f6 + (1.0F - f6)));
			if(this.useless2){
				move(EnumMoveType.SELF, this.motX * 0.800000011920929D, this.motY * 0.800000011920929D, this.motZ * 0.800000011920929D);
			}else{
				move(EnumMoveType.SELF, this.motX, this.motY, this.motZ);
			}
			Vec3D vec3d3 = new Vec3D(this.motX, this.motY, this.motZ).a();
			float f7 = ((float) vec3d3.b(vec3d2) + 1.0F) / 2.0F;
			f7 = 0.8F + 0.15F * f7;
			this.motX *= f7;
			this.motZ *= f7;
			this.motY *= 0.9100000262260437D;
		}
		this.updateComplexParts();

	}

	private void setPos(double x, double y, double z){
		double[] d0 = new double[]{x, y, z};
		double[] d1 = new double[]{this.locX, this.locY, this.locZ};
		for(int i = 0; i < 3; i++){
			if(this.world.getWorld().getBlockAt((int) x, (int) y, (int) z).getType().isSolid()){
				d0[i] = d1[i];
			}
		}
		this.setPosition(d0[0], d0[1], d0[2]);
	}

	private void updateComplexParts(){
		if(this.children != null){
			this.aI = this.yaw;
			this.head.width = this.head.length = 3.0F;
			this.tail1.width = this.tail1.length = 2.0F;
			this.tail2.width = this.tail2.length = 2.0F;
			this.tail3.width = this.tail3.length = 2.0F;
			this.body.length = 3.0F;
			this.body.width = 5.0F;
			this.wing1.length = 2.0F;
			this.wing1.width = 4.0F;
			this.wing2.length = 3.0F;
			this.wing2.width = 4.0F;
			float f1 = (float) (b(5, 1.0F)[1] - b(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
			float f2 = MathHelper.cos(f1);
			float f9 = -MathHelper.sin(f1);
			float f10 = this.yaw * 3.1415927F / 180.0F;
			float f11 = MathHelper.sin(f10);
			float f12 = MathHelper.cos(f10);
			this.body.tick();
			this.body.setPositionRotation(this.locX + f11 * 0.5F, this.locY, this.locZ - f12 * 0.5F, 0.0F, 0.0F);
			this.wing1.tick();
			this.wing1.setPositionRotation(this.locX + f12 * 4.5F, this.locY + 2.0D, this.locZ + f11 * 4.5F, 0.0F, 0.0F);
			this.wing2.tick();
			this.wing2.setPositionRotation(this.locX - f12 * 4.5F, this.locY + 2.0D, this.locZ - f11 * 4.5F, 0.0F, 0.0F);
			/*if ((!this.world.isClientSide) && (this.hurtTicks == 0)) {
			    launchEntities(this.world.getEntities(this, this.wing1.getBoundingBox().grow(4.0D, 2.0D, 4.0D).c(0.0D, -2.0D, 0.0D)));
			    launchEntities(this.world.getEntities(this, this.wing2.getBoundingBox().grow(4.0D, 2.0D, 4.0D).c(0.0D, -2.0D, 0.0D)));
			    damageEntities(this.world.getEntities(this, this.head.getBoundingBox().grow(1.0D, 1.0D, 1.0D)));
			}*/
			double[] adouble = b(5, 1.0F);
			double[] adouble1 = b(0, 1.0F);
			float f3 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F - this.bk * 0.01F);
			float f13 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F - this.bk * 0.01F);
			this.head.tick();
			// this.bD.setPositionRotation(this.locX + f13 * 6.5F * f8, this.locY + f3 + f9 * 6.5F, this.locZ - f14 * 6.5F * f8, 0.0F, 0.0F);
			this.head.setPositionRotation(this.locX + f3 * 5.5F * f2, this.locY + (adouble1[1] - adouble[1]) * 1.0D + f9 * 5.5F, this.locZ - f13 * 5.5F * f2, 0.0F, 0.0F);
			// 1.13 tick and set pos neck here
			// this.bE.tick();
			// this.bE.setPositionRotation(this.locX + f13 * 5.5F * f8, this.locY + f3 + f9 * 5.5F, this.locZ - f14 * 5.5F * f8, 0.0F, 0.0F);
			for(int j = 0; j < 3; j++){
				EntityComplexPart entitycomplexpart = null;
				if(j == 0){
					entitycomplexpart = this.tail1;
				}
				if(j == 1){
					entitycomplexpart = this.tail2;
				}
				if(j == 2){
					entitycomplexpart = this.tail3;
				}
				double[] adouble2 = b(12 + j * 2, 1.0F);
				float f14 = this.yaw * 3.1415927F / 180.0F + ((float) MathHelper.g(adouble2[0] - adouble[0])) * 3.1415927F / 180.0F * 1.0F;
				float f15 = MathHelper.sin(f14);
				float f16 = MathHelper.cos(f14);
				float f17 = 1.5F;
				float f18 = (j + 1) * 2.0F;
				entitycomplexpart.tick();// near setPositionRotations
				entitycomplexpart.setPositionRotation(this.locX - (f11 * f17 + f15 * f18) * f2, this.locY + (adouble2[1] - adouble[1]) * 1.0D - (f18 + f17) * f9 + 1.5D, this.locZ + (f12 * f17 + f16 * f18) * f2, 0.0F, 0.0F);
			}
			if(!this.world.isClientSide){
				// b is a method that handles destroying blocks.
				// this.bx = (b(this.head.getBoundingBox()) | b(this.body.getBoundingBox());
			}
		}
	}

	public World J_(){// In IComplex
		return world;
	}

	public boolean a(EntityComplexPart entityComplexPart, DamageSource damageSource, float f){// In IComplex
		if(entityComplexPart != this.head){
			f = f / 4.0F + 1.0F;
		}
		float f1 = this.yaw * 3.1415927F / 180.0F;
		float f2 = MathHelper.sin(f1);
		float f3 = MathHelper.cos(f1);
		this.targetX = this.locX + (double) (f2 * 5.0F) + (double) ((this.random.nextFloat() - 0.5F) * 2.0F);
		this.targetY = this.locY + (double) (this.random.nextFloat() * 3.0F) + 1.0D;
		this.targetZ = this.locZ - (double) (f3 * 5.0F) + (double) ((this.random.nextFloat() - 0.5F) * 2.0F);
		this.target = null;
		if(damageSource.getEntity() instanceof EntityHuman || damageSource.isExplosion()){
			// this.attack(damageSource.getEntity(), f);
		}
		return true;
	}

	public String getDeathSound(){
		return null;
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.GIANT;
	}

	@Override
	public boolean a(){
		return false;
	}
}

