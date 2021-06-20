package com.dsh105.echopet.compat.nms.v1_17_R1;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityPet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.phys.AABB;

public class FakeSensorNearestLivingEntities extends NearestLivingEntitySensor{
	
	public FakeSensorNearestLivingEntities(){}
	
	// Straight up copied from NearestLivingEntitySensor except with an extra instanceof check
	// I just used intellij for the code.
	
	@Override
	protected void doTick(ServerLevel var0, LivingEntity var1){
		AABB var2 = var1.getBoundingBox().inflate(16.0D, 16.0D, 16.0D);
		List<LivingEntity> var3 = var0.getEntitiesOfClass(LivingEntity.class, var2, (var1x)->{
			return var1x != var1 && var1x.isAlive() && !(var1x instanceof EntityPet);
		});
		var3.sort(Comparator.comparingDouble(var1::distanceToSqr));
		Brain<?> var4 = var1.getBrain();
		var4.setMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES, var3);
		var4.setMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, var3.stream().filter((var1x)->{
			return isEntityTargetable(var1, var1x);
		}).collect(Collectors.toList()));
	}
}