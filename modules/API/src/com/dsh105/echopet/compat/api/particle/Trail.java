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
package com.dsh105.echopet.compat.api.particle;

import java.util.Collection;
import java.util.Set;
import com.dsh105.echopet.compat.api.entity.IPet;

/**
 * @since Jul 11, 2016
 */
public interface Trail{
	
	String getName();
	
	String getParticleType();
	
	String getPermission();
	
	boolean canToggle();
	
	int getTickDelay();
	
	float getSpeed();
	
	int getCount();
	
	double getX();
	
	double getY();
	
	double getZ();
	
	float getXOffset();
	
	float getYOffset();
	
	float getZOffset();
	
	Trail getParentTrail();
	
	void setParentTrail(Trail parentTrail);
	
	Collection<String> getSubTrailNames();
	
	Set<Trail> getSubTrails();
	
	void addSubTrail(Trail subTrail);
	
	void start(final IPet pet);
	
	void cancel();
	
	void displayTrail(final IPet pet);
	
	Trail clone();
}
