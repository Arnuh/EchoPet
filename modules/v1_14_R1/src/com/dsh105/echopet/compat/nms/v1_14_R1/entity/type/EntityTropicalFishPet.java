package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityTropicalFishPet;

import net.minecraft.server.v1_14_R1.DataWatcher;
import net.minecraft.server.v1_14_R1.DataWatcherObject;
import net.minecraft.server.v1_14_R1.DataWatcherRegistry;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;


@EntitySize(width = 0.5F, height = 0.4F)
@EntityPetType(petType = PetType.TROPICALFISH)
public class EntityTropicalFishPet extends EntityFishPet implements IEntityTropicalFishPet{

	private static final DataWatcherObject<Integer> DATA = DataWatcher.a(EntityTropicalFishPet.class, DataWatcherRegistry.b);

	public EntityTropicalFishPet(World world){
		super(EntityTypes.TROPICAL_FISH, world);
	}

	public EntityTropicalFishPet(World world, IPet pet){
		super(EntityTypes.TROPICAL_FISH, world, pet);
	}

	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(DATA, 0);
	}

	public void setVariantData(boolean large, TropicalFish.Pattern pattern, DyeColor bodyColor, DyeColor patternColor){
		int variantData = patternColor.ordinal() << 24;
		variantData |= bodyColor.ordinal() << 16;
		variantData |= pattern.ordinal() << 8;
		variantData |= (large ? 1 : 0);
		datawatcher.set(DATA, variantData);
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.TINY;
	}
}
