package com.dsh105.echopet.compat.nms.v1_12_R1.entity.type;

import org.bukkit.DyeColor;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityLlamaPet;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
@EntitySize(width = 0.9F, height = 1.87F)
@EntityPetType(petType = PetType.LLAMA)
public class EntityLlamaPet extends EntityHorseChestedAbstractPet implements IEntityLlamaPet{

	private static final DataWatcherObject<Integer> STRENGTH = DataWatcher.a(EntityLlamaPet.class, DataWatcherRegistry.b);// changes storage
	private static final DataWatcherObject<Integer> COLOR = DataWatcher.a(EntityLlamaPet.class, DataWatcherRegistry.b);// carpet color
	private static final DataWatcherObject<Integer> VARIANT = DataWatcher.a(EntityLlamaPet.class, DataWatcherRegistry.b);// Like an outfit

	public EntityLlamaPet(World world){
		super(world);
	}

	public EntityLlamaPet(World world, IPet pet){
		super(world, pet);
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(STRENGTH, 0);
		this.datawatcher.register(COLOR, -1);
		this.datawatcher.register(VARIANT, 0);
	}

	@Override
	public void setCarpetColor(DyeColor color){
		this.datawatcher.set(COLOR, color == null ? -1 : color.ordinal());
	}

	@Override
	public void setSkinColor(LlamaSkin skinColor){
		this.datawatcher.set(VARIANT, skinColor.ordinal());
	}
}
