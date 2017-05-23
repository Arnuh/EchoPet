package com.dsh105.echopet.compat.nms.v1_10_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.*;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityGuardianPet;
import com.dsh105.echopet.compat.nms.v1_10_R1.entity.EntityPet;

import net.minecraft.server.v1_10_R1.DataWatcher;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;
import net.minecraft.server.v1_10_R1.World;

@EntitySize(width = 0.85F, height = 0.85F)
@EntityPetType(petType = PetType.GUARDIAN)
public class EntityGuardianPet extends EntityPet implements IEntityGuardianPet{

	private static final DataWatcherObject<Byte> ELDER = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.a);
	private static final DataWatcherObject<Integer> b = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.b);

	public EntityGuardianPet(World world){
		super(world);
	}

	public EntityGuardianPet(World world, IPet pet){
		super(world, pet);
	}

	@Override
	protected String getIdleSound(){
		if(isElder()) return isInWater() ? "entity.elder_guardian.ambient" : "entity.elder_guardian.ambient_land";
		return isInWater() ? "entity.guardian.ambient" : "entity.guardian.ambient_land";
	}

	@Override
	protected String getDeathSound(){
		if(isElder()) return isInWater() ? "entity.elder_guardian.death" : "entity.elder_guardian.death_land";
		return isInWater() ? "entity.guardian.death" : "entity.guardian.death_land";
	}

	@Override
	public SizeCategory getSizeCategory(){
		return isElder() ? SizeCategory.GIANT : SizeCategory.LARGE;
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(ELDER, Byte.valueOf((byte) 0));
		this.datawatcher.register(b, Integer.valueOf(0));
	}

	@Override
	public void onLive(){
		super.onLive();
	}

	@Override
	public boolean isElder(){
		return a(4);
	}

	@Override
	public void setElder(boolean flag){
		a(4, flag);
	}

	private boolean a(int paramInt){
		return (((Byte) this.datawatcher.get(ELDER)).byteValue() & paramInt) != 0;
	}

	private void a(int paramInt, boolean paramBoolean){
		int i = ((Byte) this.datawatcher.get(ELDER)).byteValue();
		if(paramBoolean){
			this.datawatcher.set(ELDER, Byte.valueOf((byte) (i | paramInt)));
		}else{
			this.datawatcher.set(ELDER, Byte.valueOf((byte) (i & (paramInt ^ 0xFFFFFFFF))));
		}
	}
}
