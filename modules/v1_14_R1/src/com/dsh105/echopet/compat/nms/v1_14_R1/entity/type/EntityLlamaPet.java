package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityLlamaPet;
import net.minecraft.server.v1_14_R1.DataWatcher;
import net.minecraft.server.v1_14_R1.DataWatcherObject;
import net.minecraft.server.v1_14_R1.DataWatcherRegistry;
import net.minecraft.server.v1_14_R1.EntityInsentient;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;
import org.bukkit.DyeColor;
import org.bukkit.entity.Llama;

@EntitySize(width = 0.9F, height = 1.87F)
@EntityPetType(petType = PetType.LLAMA)
public class EntityLlamaPet extends EntityHorseChestedAbstractPet implements IEntityLlamaPet{
	
	private static final DataWatcherObject<Integer> STRENGTH = DataWatcher.a(EntityLlamaPet.class, DataWatcherRegistry.b);// changes storage
	private static final DataWatcherObject<Integer> COLOR = DataWatcher.a(EntityLlamaPet.class, DataWatcherRegistry.b);// carpet color
	private static final DataWatcherObject<Integer> VARIANT = DataWatcher.a(EntityLlamaPet.class, DataWatcherRegistry.b);// Like an outfit
	
	public EntityLlamaPet(EntityTypes<? extends EntityInsentient> type, World world){
		super(type, world);
	}
	
	public EntityLlamaPet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet){
		super(type, world, pet);
	}
	
	public EntityLlamaPet(World world){
		this(EntityTypes.LLAMA, world);
	}
	
	public EntityLlamaPet(World world, IPet pet){
		this(EntityTypes.LLAMA, world, pet);
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
