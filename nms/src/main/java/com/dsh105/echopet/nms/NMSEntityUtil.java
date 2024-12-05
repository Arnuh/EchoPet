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
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.Nullable;

/*
 * From EntityAPI :)
 */
public class NMSEntityUtil{
	
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
				attributes.put(Attributes.FLYING_SPEED.value(), instance);
			}else{
				EchoPet.getPlugin().getLogger().warning("Failed to add flying speed attribute for" + petType);
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
