/*
 * This file is part of EchoPet.
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dsh105.echopet.compat.nms.v1_16_R3;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.function.Supplier;
import com.dsh105.echopet.compat.api.reflection.FieldUtil;
import com.google.common.collect.ImmutableList;
import net.minecraft.server.v1_16_R3.ControllerJump;
import net.minecraft.server.v1_16_R3.ControllerLook;
import net.minecraft.server.v1_16_R3.ControllerMove;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.EntitySenses;
import net.minecraft.server.v1_16_R3.EntityVillager;
import net.minecraft.server.v1_16_R3.NavigationAbstract;
import net.minecraft.server.v1_16_R3.Sensor;
import net.minecraft.server.v1_16_R3.SensorNearestLivingEntities;
import net.minecraft.server.v1_16_R3.SensorType;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

/*
 * From EntityAPI :)
 */
public class NMSEntityUtil{
	
	public static NavigationAbstract getNavigation(LivingEntity livingEntity){
		if(livingEntity instanceof CraftLivingEntity){
			return getNavigation(((CraftLivingEntity) livingEntity).getHandle());
		}
		return null;
	}
	
	public static NavigationAbstract getNavigation(EntityLiving entityLiving){
		if(entityLiving instanceof EntityInsentient){
			return ((EntityInsentient) entityLiving).getNavigation();
		}
		return null;
	}
	
	public static EntitySenses getEntitySenses(LivingEntity livingEntity){
		if(livingEntity instanceof CraftLivingEntity){
			return getEntitySenses(((CraftLivingEntity) livingEntity).getHandle());
		}
		return null;
	}
	
	public static EntitySenses getEntitySenses(EntityLiving entityLiving){
		if(entityLiving instanceof EntityInsentient){
			return ((EntityInsentient) entityLiving).getEntitySenses();
		}
		return null;
	}
	
	public static ControllerJump getControllerJump(LivingEntity livingEntity){
		if(livingEntity instanceof CraftLivingEntity){
			return getControllerJump(((CraftLivingEntity) livingEntity).getHandle());
		}
		return null;
	}
	
	public static ControllerJump getControllerJump(EntityLiving entityLiving){
		if(entityLiving instanceof EntityInsentient){
			return ((EntityInsentient) entityLiving).getControllerJump();
		}
		return null;
	}
	
	public static ControllerMove getControllerMove(LivingEntity livingEntity){
		if(livingEntity instanceof CraftLivingEntity){
			return getControllerMove(((CraftLivingEntity) livingEntity).getHandle());
		}
		return null;
	}
	
	public static ControllerMove getControllerMove(EntityLiving entityLiving){
		if(entityLiving instanceof EntityInsentient){
			return ((EntityInsentient) entityLiving).getControllerMove();
		}
		return null;
	}
	
	public static ControllerLook getControllerLook(LivingEntity livingEntity){
		if(livingEntity instanceof CraftLivingEntity){
			return getControllerLook(((CraftLivingEntity) livingEntity).getHandle());
		}
		return null;
	}
	
	public static ControllerLook getControllerLook(EntityLiving entityLiving){
		if(entityLiving instanceof EntityInsentient){
			return ((EntityInsentient) entityLiving).getControllerLook();
		}
		return null;
	}
	
	public static boolean isInGuardedAreaOf(EntityLiving entityLiving, int x, int y, int z){
		// TODO: not used currently
		return false;
		/*if (entityLiving instanceof EntityCreature) {
		    return ((EntityCreature) entityLiving).d(new BlockPosition(x, y, z));
		} else {
		    return false;
		}*/
	}
	
	/*
	 * Hacky stuff to get around doTick() becoming final
	 */
	// protected static FieldAccessor<Set<?>> GOALS;
	// protected static FieldAccessor<Set<?>> ACTIVE_GOALS;
	// protected static MethodAccessor<Void> ADD_GOAL;
	// protected static FieldAccessor<Object> GOAL_SELECTOR;
	
	public static void clearGoals(Object nmsEntityHandle){
		/*if(GOALS == null || ACTIVE_GOALS == null || GOAL_SELECTOR == null){
			initializeFields();
		}
		GOALS.get(GOAL_SELECTOR.get(nmsEntityHandle)).clear();
		ACTIVE_GOALS.get(GOAL_SELECTOR.get(nmsEntityHandle)).clear();*/
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	protected static void initializeFields(){
		/*try{
			ClassTemplate goalTemplate = new Reflection().reflect(MinecraftReflection.getMinecraftClass("PathfinderGoalSelector"));
			List<SafeMethod<Void>> methodCandidates = goalTemplate.getSafeMethods(withArguments(int.class, MinecraftReflection.getMinecraftClass("PathfinderGoal")));
			if(methodCandidates.size() > 0){
				ADD_GOAL = methodCandidates.get(0).getAccessor();
			}else{
				throw new RuntimeException("Failed to get the addGoal method!");
			}
			List<SafeField<Set<?>>> fieldCandidates = goalTemplate.getSafeFields(withType(Set.class));
			if(fieldCandidates.size() > 1){
				GOALS = fieldCandidates.get(0).getAccessor();
				ACTIVE_GOALS = fieldCandidates.get(0).getAccessor();
			}else{
				throw new RuntimeException("Failed to initialize the goal-lists!");
			}
			// The GoalSelector
			ClassTemplate entityTemplate = new Reflection().reflect(MinecraftReflection.getMinecraftClass("EntityInsentient"));
			List<SafeField<Object>> candidates = entityTemplate.getSafeFields(withType(goalTemplate.getReflectedClass()));
			if(candidates.size() > 0){
				GOAL_SELECTOR = candidates.get(0).getAccessor(); // the normal selector is the first one
			}else{
				throw new RuntimeException("Failed to initialize the GoalSelector field for the entities");
			}
		}catch(Exception ಠ_ಠ){
			throw new RuntimeException("Failed to initialize the goal-related fields!", ಠ_ಠ);
		}*/
	}
	
	private static final Supplier<SensorNearestLivingEntities> sensorSupplier = FakeSensorNearestLivingEntities::new;
	
	private static ImmutableList<SensorType<? extends Sensor<? super EntityVillager>>> getSensors(){
		// From EntityVillager
		return ImmutableList.of(SensorType.c, SensorType.d, SensorType.b, SensorType.e, SensorType.f, SensorType.g, SensorType.h, SensorType.i, SensorType.j);
	}
	
	/** Villager behavior does casts on behavior memory "INTERACTION_TARGET"<br>
	 * These casts assume the result is a EntityVillager, and when its not, we crash<br>
	 * If we take control of SensorNearestLivingEntities we can attempt to prevent our custom entities from appearing in behavior memory.<br>
	 * With this sensor we can block them from entering "MOBS" and "VISIBLE_MOBS" which seems to be used for any other memory.<br>
	 *<br>
	 * This requires the plugin to load before worlds or else villagers can be created before the fix is applied.
	 */
	public static boolean doVillagerFix(){
		
		try{
			// Probably have a cleaner way to do this but I want to get rid of most the reflection util classes and depends anyway.
			
			Constructor<?> constructor = SensorType.class.getDeclaredConstructors()[0];
			constructor.setAccessible(true);
			
			// Replace the field in the class enum in case its for some reason grabbed later.
			for(Field field : SensorType.class.getDeclaredFields()){
				SensorType<?> sensor = (SensorType<?>) field.get(null);
				if(sensor.a() instanceof SensorNearestLivingEntities){
					FieldUtil.setFinalStatic(field, null, constructor.newInstance(sensorSupplier));
					//System.out.println("Found sensor field: " + field.getName());
					break;
				}
			}
			
			// Replace the cached list of sensors for villagers.
			for(Field field : EntityVillager.class.getDeclaredFields()){
				if(!(field.getGenericType() instanceof ParameterizedType)){
					continue;
				}
				ParameterizedType type = (ParameterizedType) field.getGenericType();
				String typeName = type.getActualTypeArguments()[0].getTypeName();
				if(typeName.contains(SensorType.class.getSimpleName())){// meh
					FieldUtil.setFinalStatic(field, null, getSensors());
					//System.out.println("Found sensor list: " + field.getName());
					break;
				}
			}
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
}
