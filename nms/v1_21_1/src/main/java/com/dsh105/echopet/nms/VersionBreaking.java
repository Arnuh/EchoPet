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

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class VersionBreaking{
	
	public static void setMaxUpStep(Entity entity, float maxStepUp){
		// entity.setMaxUpStep(maxStepUp);
	}
	
	public static void setFlyingSpeed(LivingEntity entity, float flyingSpeed){
		if(entity.getAttribute(Attributes.FLYING_SPEED) == null)
			entity.getAttributes().registerAttribute(Attributes.FLYING_SPEED);

		entity.getAttribute(Attributes.FLYING_SPEED).setBaseValue(flyingSpeed);
	}
	
	public static double getWaterEfficiency(LivingEntity entity){
		return entity.getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY);
	}
}
