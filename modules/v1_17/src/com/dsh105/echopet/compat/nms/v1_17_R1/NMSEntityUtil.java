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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.compat.nms.v1_17_R1;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import com.dsh105.echopet.compat.api.reflection.FieldUtil;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.sensor.CustomSensorTypes;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

/*
 * From EntityAPI :)
 */
@SuppressWarnings("unchecked")
public class NMSEntityUtil{
	
	private static Field jumpField;
	
	static{
		try{
			FakeEntity fakeEntity = new FakeEntity();
			fakeEntity.setJumping(true);
			for(Field field : LivingEntity.class.getDeclaredFields()){
				if(!Modifier.isProtected(field.getModifiers())){
					continue;
				}
				if(boolean.class.isAssignableFrom(field.getType())){
					field.setAccessible(true);
					if(field.getBoolean(fakeEntity)){
						jumpField = field;
						break;
					}
					field.setAccessible(false);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static Field getJumpingField(){
		return jumpField;
	}
	
	private static class FakeEntity extends LivingEntity{
		
		protected FakeEntity(){
			super(EntityType.BAT, null);
		}
		
		@Override
		public Iterable<ItemStack> getArmorSlots(){
			return null;
		}
		
		@Override
		public ItemStack getItemBySlot(EquipmentSlot equipmentSlot){
			return null;
		}
		
		@Override
		public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack){
		
		}
		
		@Override
		public HumanoidArm getMainArm(){
			return null;
		}
	}
	
	/** Villager behavior does casts on behavior memory "INTERACTION_TARGET"<br>
	 * These casts assume the result is a EntityVillager, and when its not, we crash<br>
	 * If we take control of SensorNearestLivingEntities we can attempt to prevent our custom entities from appearing in behavior memory.<br>
	 * With this sensor we can block them from entering "MOBS" and "VISIBLE_MOBS" which seems to be used for any other memory.<br>
	 *<br>
	 * This requires the plugin to load before worlds or else villagers can be created before the fix is applied.
	 */
	public static boolean fixVillagerSensor(){
		return fix(Villager.class, CustomSensorTypes.ORG_NEAREST_LIVING_ENTITIES, CustomSensorTypes.NEAREST_LIVING_ENTITIES);
	}
	
	public static boolean fixAxolotlSensor(){
		return fix(Axolotl.class, CustomSensorTypes.ORG_NEAREST_ADULT, CustomSensorTypes.NEAREST_ADULT);
	}
	
	private static boolean fix(Class<?> entityClass, SensorType<?> originalSensor, SensorType<?> customSensor){
		try{
			//System.out.println(entityClass.getSimpleName() + " - " + (originalSensor != null));
			for(Field field : entityClass.getDeclaredFields()){
				if(!(field.getGenericType() instanceof ParameterizedType type)){
					continue;
				}
				String typeName = type.getActualTypeArguments()[0].getTypeName();
				if(!typeName.contains(SensorType.class.getSimpleName())){// meh
					continue;
				}
				field.setAccessible(true);
				ImmutableList<? extends SensorType<? extends Sensor<?>>> sensors = (ImmutableList<? extends SensorType<? extends Sensor<?>>>) field.get(null);
				
				FieldUtil.setFinalStatic(field, copyAndReplace(sensors, originalSensor, customSensor));
				field.setAccessible(false);
			}
			
			// Replace the field in the class enum in case its for some reason grabbed later.
			for(Field field : SensorType.class.getDeclaredFields()){
				if(Modifier.isPrivate(field.getModifiers())) continue;
				SensorType<?> sensor = (SensorType<?>) field.get(null);
				//System.out.println(field.getName() + " - " + (sensor != null));
				if(Objects.equals(sensor, originalSensor)){
					FieldUtil.setFinalStatic(field, customSensor);
					//System.out.println("Fixed SensorType enum");
					break;
				}
			}
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public static ImmutableList<Object> copyAndReplace(ImmutableList<? extends SensorType<?>> copyFrom, SensorType<?> replace, SensorType<?> replaceWith){
		// Safest? way to replace only the sensor we need to.
		// Supports if Mojang ever changes the list without anyone realizing.
		ImmutableList.Builder<Object> builder = ImmutableList.builder();
		for(Object o : copyFrom){
			if(Objects.equals(o, replace)){
				builder.add(replaceWith);
			}else{
				builder.add(o);
			}
		}
		return builder.build();
	}
}
