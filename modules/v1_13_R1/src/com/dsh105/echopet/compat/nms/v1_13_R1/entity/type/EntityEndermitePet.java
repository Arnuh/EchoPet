package com.dsh105.echopet.compat.nms.v1_13_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityEndermitePet;
import com.dsh105.echopet.compat.nms.v1_13_R1.entity.EntityPet;

import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.Particles;
import net.minecraft.server.v1_13_R1.World;

@EntitySize(width = 0.4F, height = 0.3F)
@EntityPetType(petType = PetType.ENDERMITE)
public class EntityEndermitePet extends EntityPet implements IEntityEndermitePet{

	public EntityEndermitePet(World world){
		super(EntityTypes.ENDERMITE, world);
	}

	public EntityEndermitePet(World world, IPet pet){
		super(EntityTypes.ENDERMITE, world, pet);
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}

	@Override
	public void onLive(){
		super.onLive();
		for(int i = 0; i < 2; i++){
			this.world.addParticle(Particles.K, this.locX + (this.random.nextDouble() - 0.5D) * this.width, this.locY + this.random.nextDouble() * this.length, this.locZ + (this.random.nextDouble() - 0.5D) * this.width, (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
		}
	}
}