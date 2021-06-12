package com.dsh105.echopet.compat.nms.v1_17_R1;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityPet;
import net.minecraft.server.v1_17_R1.AxisAlignedBB;
import net.minecraft.server.v1_17_R1.BehaviorController;
import net.minecraft.server.v1_17_R1.EntityLiving;
import net.minecraft.server.v1_17_R1.MemoryModuleType;
import net.minecraft.server.v1_17_R1.SensorNearestLivingEntities;
import net.minecraft.server.v1_17_R1.WorldServer;

/**
 * @author Arnah
 * @since Feb 21, 2021
 **/
public class FakeSensorNearestLivingEntities extends SensorNearestLivingEntities{
	
	public FakeSensorNearestLivingEntities(){}
	
	// Straight up copied from SensorNearestLivingEntities except with an extra instanceof check
	// I just used intellij for the code.
	
	protected void a(WorldServer var0, EntityLiving var1){
		AxisAlignedBB var2 = var1.getBoundingBox().grow(16.0D, 16.0D, 16.0D);
		List<EntityLiving> var3 = var0.a(EntityLiving.class, var2, (var1x)->{
			return var1x != var1 && var1x.isAlive() && !(var1x instanceof EntityPet);
		});
		var3.sort(Comparator.comparingDouble(var1::h));
		BehaviorController<?> var4 = var1.getBehaviorController();
		var4.setMemory(MemoryModuleType.MOBS, var3);
		var4.setMemory(MemoryModuleType.VISIBLE_MOBS, var3.stream().filter((var1x)->{
			return a(var1, var1x);
		}).collect(Collectors.toList()));
	}
}