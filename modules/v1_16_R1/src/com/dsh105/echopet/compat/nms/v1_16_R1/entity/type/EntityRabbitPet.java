/*
 * This file is part of EchoPet.
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.nms.v1_16_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityRabbitPet;
import com.dsh105.echopet.compat.nms.v1_16_R1.entity.EntityAgeablePet;
import com.dsh105.echopet.compat.nms.v1_16_R1.entity.ai.PetGoalFollowOwner;
import net.minecraft.server.v1_16_R1.ControllerJump;
import net.minecraft.server.v1_16_R1.DataWatcher;
import net.minecraft.server.v1_16_R1.DataWatcherObject;
import net.minecraft.server.v1_16_R1.DataWatcherRegistry;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.PathEntity;
import net.minecraft.server.v1_16_R1.Vec3D;
import net.minecraft.server.v1_16_R1.World;
import org.bukkit.entity.Rabbit;

@EntitySize(width = 0.6F, height = 0.7F)
@EntityPetType(petType = PetType.RABBIT)
public class EntityRabbitPet extends EntityAgeablePet implements IEntityRabbitPet{
	
	private static final DataWatcherObject<Integer> TYPE = DataWatcher.a(EntityRabbitPet.class, DataWatcherRegistry.b);
	private boolean onGroundLastTick = false;
	private int delay = 0;// bC
	
	public EntityRabbitPet(World world){
		super(EntityTypes.RABBIT, world);
		this.bp = new ControllerJumpRabbit(this);
	}
	
	public EntityRabbitPet(World world, IPet pet){
		super(EntityTypes.RABBIT, world, pet);
		this.bp = new ControllerJumpRabbit(this);
	}
	
	@Override
	public Rabbit.Type getRabbitType(){
		return TypeMapping.fromMagic(this.datawatcher.get(TYPE));
	}
	
	@Override
	public void setRabbitType(Rabbit.Type type){
		this.datawatcher.set(TYPE, TypeMapping.toMagic(type));
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(TYPE, Integer.valueOf(0));
	}
	
	@Override
	public void onLive(){
		super.onLive();
		if(this.delay > 0){
			this.delay -= 1;
		}
		if(this.onGround){
			if(!this.onGroundLastTick){
				setJumping(false);
				reset();// ef
			}
			ControllerJumpRabbit jumpController = (ControllerJumpRabbit) getControllerJump();
			if(!jumpController.c()){
				if(this.moveController.b() && this.delay == 0){
					PathEntity pathentity = ((PetGoalFollowOwner) petGoalSelector.getGoal("FollowOwner")).getNavigation().k();// Gets path towards the player.
					// if(pathentity != null && pathentity.e() < pathentity.d()){
					// Vec3D vec3d = pathentity.a(this);
					// a(vec3d.x, vec3d.z);
					// dl();
					// }
					Vec3D vec3d = new Vec3D(this.moveController.d(), this.moveController.e(), this.moveController.f());
					if((pathentity != null) && (pathentity.f() < pathentity.e())){
						vec3d = pathentity.a(this);
					}
					a(vec3d.x, vec3d.z);
					dl();
				}
			}else if(!jumpController.d()){
				((ControllerJumpRabbit) getControllerJump()).a(true);
			}
		}
		this.onGroundLastTick = this.onGround;
	}
	
	protected void jump(){// has movecontroller in it, 4 above datawatcher register.
		super.jump();
		double d0 = this.moveController.c();
		if(d0 > 0.0D){
			Vec3D mot = this.getMot();
			double d1 = mot.x * mot.x + mot.z * mot.z;
			if(d1 < 0.010000000000000002D){
				a(0.0F, 0.0F, 1.0F, 0.1F);
			}
		}
		this.world.broadcastEntityEffect(this, (byte) 1);// Does leg jump animation I think
	}
	
	private void reset(){
		resetDelay();// dC
		((ControllerJumpRabbit) getControllerJump()).a(false);// dD
	}
	
	private void resetDelay(){// dI()
		if(moveController.c() < 2.2D) delay = 10;
		else delay = 1;
	}
	
	public void eL(){// Above datawatcher register
		setJumping(true);// Plays ambient sound if true, does super.l(flag);
	}
	
	public class ControllerJumpRabbit extends ControllerJump{// Copied from EntityRabbit
		
		private EntityRabbitPet c;
		private boolean d = false;
		
		public ControllerJumpRabbit(EntityRabbitPet entityrabbit){
			super(entityrabbit);
			this.c = entityrabbit;
		}
		
		public boolean c(){
			return this.a;
		}
		
		public boolean d(){
			return this.d;
		}
		
		public void a(boolean flag){
			this.d = flag;
		}
		
		public void b(){
			if(this.a){
				this.c.dl();// this is the method above ^
				this.a = false;
			}
		}
	}
	
	static class TypeMapping{
		
		private static final int[] NMS_TYPES = new int[Rabbit.Type.values().length];
		private static final Rabbit.Type[] INVERSE = new Rabbit.Type[Rabbit.Type.values().length];
		
		static{
			set(Rabbit.Type.BROWN, 0);
			set(Rabbit.Type.WHITE, 1);
			set(Rabbit.Type.BLACK, 2);
			set(Rabbit.Type.BLACK_AND_WHITE, 3);
			set(Rabbit.Type.GOLD, 4);
			set(Rabbit.Type.SALT_AND_PEPPER, 5);
			set(Rabbit.Type.THE_KILLER_BUNNY, 99);
		}
		
		private static void set(Rabbit.Type type, int magicValue){
			NMS_TYPES[type.ordinal()] = magicValue;
			if(magicValue < INVERSE.length){
				INVERSE[magicValue] = type;
			}
		}
		
		protected static Rabbit.Type fromMagic(int magicValue){
			if(magicValue < INVERSE.length){
				return INVERSE[magicValue];
			}else if(magicValue == 99){
				return Rabbit.Type.THE_KILLER_BUNNY;
			}
			// a default
			return Rabbit.Type.BROWN;
		}
		
		protected static int toMagic(Rabbit.Type type){
			return NMS_TYPES[type.ordinal()];
		}
	}
}
