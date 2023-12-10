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

package com.dsh105.echopet.nms;

import java.lang.reflect.Field;
import java.util.Map;
import com.dsh105.echopet.compat.api.entity.IPetType;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.jetbrains.annotations.Nullable;

/*
 * From EntityAPI :)
 */
public class NMSEntityUtil{
	
	private static Field jumpField;
	
	public static Field getJumpingField(){
		if(jumpField == null){
			// How should this be handled if it fails?
			try{
				FakeEntity fakeEntity = new FakeEntity();
				for(Field field : LivingEntity.class.getDeclaredFields()){
					// Paper makes it public in 1.17
					/*if(!Modifier.isProtected(field.getModifiers())){
						continue;
					}*/
					if(boolean.class.isAssignableFrom(field.getType())){
						field.setAccessible(true);
						fakeEntity.setJumping(true);
						boolean ret = field.getBoolean(fakeEntity);
						fakeEntity.setJumping(false);
						boolean ret2 = field.getBoolean(fakeEntity);
						if(ret != ret2){
							jumpField = field;
							break;
						}else{ // Leave it accessible if its the proper field
							field.setAccessible(false);
						}
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return jumpField;
	}
	
	private static class FakeEntity extends LivingEntity{
		
		protected FakeEntity(){
			// Paper requires a World in the Entity class
			// shieldBlockingDelay = this.level.paperConfig.shieldBlockingDelay;
			super(EntityType.BAT, Bukkit.getWorlds().stream().findFirst().map(CraftWorld.class::cast).map(CraftWorld::getHandle).orElseThrow());
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
	
	private static Field attributeField;
	
	@SuppressWarnings("unchecked")
	public static void addFlyingSpeedAttribute(IPetType petType, AttributeMap attributeMap){
		try{
			if(attributeField == null){
				for(var field : AttributeMap.class.getDeclaredFields()){
					if(field.getType() == Map.class){
						field.setAccessible(true);
						attributeField = field;
						break;
					}
				}
			}
			if(attributeField != null){
				Map<Attribute, AttributeInstance> attributes = (Map<Attribute, AttributeInstance>) attributeField.get(attributeMap);
				var instance = new AttributeInstance(Attributes.FLYING_SPEED, d->{});
				instance.setBaseValue(IPet.GOAL_FLY_SPEED.getNumber(petType).doubleValue());
				attributes.put(Attributes.FLYING_SPEED, instance);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	// Not fully usable, wip for later.
	@SuppressWarnings("unchecked")
	public static <T> @Nullable EntityDataAccessor<T> getAccessor(Class<? extends Entity> entityClass, EntityDataSerializer<T> dataHandler, int offset){
		try{
			int pos = 0;
			for(Field f : entityClass.getDeclaredFields()){
				f.setAccessible(true);
				Object obj = f.get(null);
				if(obj instanceof EntityDataAccessor<?> accessor && pos++ == offset){
					// accessor.getSerializer() == dataHandler
					return (EntityDataAccessor<T>) accessor;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}
