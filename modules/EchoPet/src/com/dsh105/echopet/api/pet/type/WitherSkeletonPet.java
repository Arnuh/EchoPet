package com.dsh105.echopet.api.pet.type;

import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SkeletonType;
import com.dsh105.echopet.compat.api.entity.type.pet.IWitherSkeletonPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntityPetType(petType = PetType.WITHERSKELETON)
public class WitherSkeletonPet extends SkeletonPet implements IWitherSkeletonPet{

	public WitherSkeletonPet(Player owner){
		super(owner);
	}

	@Override
	public SkeletonType getSkeletonType(){
		return SkeletonType.WITHER;
	}
}