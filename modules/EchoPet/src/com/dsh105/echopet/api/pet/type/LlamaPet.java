package com.dsh105.echopet.api.pet.type;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.dsh105.echopet.compat.api.entity.LlamaSkin;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityLlamaPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ILlamaPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntityPetType(petType = PetType.LLAMA)
public class LlamaPet extends HorseChestedAbstractPet implements ILlamaPet{

	private DyeColor carpetColor = null;
	private LlamaSkin skinColor = LlamaSkin.WHITE;

	public LlamaPet(Player owner){
		super(owner);
	}

	@Override
	public HorseVariant getVariant(){
		return HorseVariant.LLAMA;
	}

	@Override
	public DyeColor getCarpetColor(){
		return carpetColor;
	}

	@Override
	public LlamaSkin getSkinColor(){
		return skinColor;
	}

	@Override
	public void setCarpetColor(DyeColor color){
		((IEntityLlamaPet) getEntityPet()).setCarpetColor(color);
		carpetColor = color;
	}

	@Override
	public void setSkinColor(LlamaSkin skinColor){
		((IEntityLlamaPet) getEntityPet()).setSkinColor(skinColor);
		this.skinColor = skinColor;
	}
}