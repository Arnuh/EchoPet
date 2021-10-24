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

package com.dsh105.echopet.compat.api.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Collection of maths-based geometry calculating methods
 */
public class GeometryUtil{
	
	private GeometryUtil(){
	}
	
	/**
	 * Generates a random floating point value between the given minimum and maximum
	 *
	 * @param min minimum value, inclusive
	 * @param max maximum value, inclusive
	 * @return a random floating point value
	 */
	public static float generateRandomFloat(float min, float max){
		float f = min + (GeneralUtil.random().nextFloat() * ((1 + max) - min));
		return GeneralUtil.random().nextBoolean() ? f : -f;
	}
	
	/**
	 * Generates a random floating point value
	 *
	 * @return a random floating point value
	 */
	public static float generateRandomFloat(){
		float f = GeneralUtil.random().nextFloat();
		return GeneralUtil.random().nextBoolean() ? f : -f;
	}
	
	/**
	 * Generates a list of locations within a circular shape, dependent on the provided conditions
	 *
	 * @param origin     origin or centre of the circle
	 * @param radius     radius of the circle
	 * @param height     height of the circle. If {@code height > 1}, the circle will become a cylinder
	 * @param hollow     true if the generated circle is to be hollow
	 * @param sphere     true if the shape generated is spherical
	 * @param includeAir true if air can be included in the list of locations
	 * @return a list of locations inside a generated circular shape
	 */
	public static List<Location> circle(Location origin, int radius, int height, boolean hollow, boolean sphere, boolean includeAir){
		List<Location> blocks = new ArrayList<>();
		int cx = origin.getBlockX(), cy = origin.getBlockY(), cz = origin.getBlockZ();
		for(int x = cx - radius; x <= cx + radius; x++){
			for(int z = cz - radius; z <= cz + radius; z++){
				for(int y = (sphere ? cy - radius : cy); y < (sphere ? cy + radius : cy + height); y++){
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
					if(dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1))){
						Location l = new Location(origin.getWorld(), x, y, z);
						if(!includeAir && l.getBlock().getType() == Material.AIR){
							continue;
						}
						blocks.add(l);
					}
				}
			}
		}
		return blocks;
	}
	
	/**
	 * Gets whether a certain location is within a given radius
	 *
	 * @param origin  origin or centre
	 * @param toCheck location to check
	 * @param range   border range
	 * @return true if the location is inside the border, false if not
	 */
	public static boolean isInBorder(Location origin, Location toCheck, int range){
		int x = origin.getBlockX(), z = origin.getBlockZ();
		int x1 = toCheck.getBlockX(), z1 = toCheck.getBlockZ();
		return !(x1 >= (x + range) || z1 >= (z + range) || x1 <= (x - range) || z1 <= (z - range));
	}
	
	/**
	 * Returns a list of entities near a given location
	 *
	 * @param entityType type (or super-type) of entity to include in the search
	 * @param origin     origin or centre of the area to search
	 * @param range      range to search within
	 * @param <T>        entity restriction to place on the search
	 * @return a list of nearby entities
	 */
	public static <T extends Entity> List<T> getNearbyEntities(Class<T> entityType, Location origin, int range){
		List<T> entities = new ArrayList<>();
		for(Entity entity : origin.getWorld().getEntities()){
			if(range <= 0 || isInBorder(origin, entity.getLocation(), range)){
				if(entityType.isAssignableFrom(entity.getClass())){
					entities.add((T) entity);
				}
			}
		}
		return entities;
	}
	
	/**
	 * Returns a list of entities near a given location
	 *
	 * @param origin origin or centre of the area to search
	 * @param range  range to search within
	 * @return a list of nearby entities
	 */
	public static List<Entity> getNearbyEntities(Location origin, int range){
		return getNearbyEntities(Entity.class, origin, range);
	}
	
	/**
	 * Returns a list of players near a given location
	 *
	 * @param origin origin or centre
	 * @param range  range to search within
	 * @return a list of nearby players
	 */
	public static List<Player> getNearbyPlayers(Location origin, int range){
		return getNearbyEntities(Player.class, origin, range);
	}
}