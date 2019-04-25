package com.dsh105.echopet.compat.nms.v1_14_R1.entity.type;

import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityIllagerAbstractPet;
import com.dsh105.echopet.compat.nms.v1_14_R1.entity.EntityPet;

import net.minecraft.server.v1_14_R1.*;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since May 23, 2017
 */
public class EntityIllagerAbstractPet extends EntityPet implements IEntityIllagerAbstractPet{

	protected static final DataWatcherObject<Byte> a = DataWatcher.a(EntityIllagerAbstractPet.class, DataWatcherRegistry.a);

	public EntityIllagerAbstractPet(EntityTypes<? extends EntityInsentient> type, World world){
		super(type, world);
	}

	public EntityIllagerAbstractPet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet){
		super(type, world, pet);
	}

	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(a, (byte) 0);
	}

	protected void a(int paramInt, boolean paramBoolean){
		int i = this.datawatcher.get(a);
		if(paramBoolean){
			i |= paramInt;
		}else{
			i &= (paramInt ^ 0xFFFFFFFF);
		}
		this.datawatcher.set(a, (byte) (i & 0xFF));
	}

	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
}
