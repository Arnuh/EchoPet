package com.dsh105.echopet.compat.nms.v1_13_R2.entity.type;

import org.bukkit.DyeColor;
import org.bukkit.entity.Llama;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityLlamaPet;

import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.DataWatcherRegistry;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;

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
		super(EntityTypes.LLAMA, world);
	}

	public EntityLlamaPet(World world, IPet pet){
		super(EntityTypes.LLAMA, world, pet);
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
	public void setSkinColor(Llama.Color skinColor){
		this.datawatcher.set(VARIANT, skinColor.ordinal());
	}
}
