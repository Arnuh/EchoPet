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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VersionTest{
	
	@Test
	void isCompatible(){
		Version version = new Version("1.19.3");
		// Confirm documentation is correct
		assertTrue(version.isCompatible("1.19.4"));
		assertTrue(version.isCompatible("1.20"));
		assertTrue(version.isCompatible("2.0"));
		assertFalse(version.isCompatible("1.19.1"));
		// Extra tests
		assertFalse(version.isCompatible("1.7.2"));
		
		version = new Version("1.19");
		
		assertTrue(version.isCompatible("1.19.2"));
	}
	
	@Test
	void isSupported(){
		Version version = new Version("1.19.3");
		// Confirm documentation is correct
		assertTrue(version.isSupported("1.19.2"));
		assertTrue(version.isSupported("1.18"));
		assertFalse(version.isSupported("1.19.4"));
		// Extra tests
		assertTrue(version.isSupported("1.6.4"));
		assertTrue(version.isSupported("1.0"));
		assertFalse(version.isSupported("2.0.3"));
		assertTrue(version.isSupported("1.19"));
		assertFalse(version.isSupported("1.20"));
	}
	
	@Test
	public void testIsSupported(){
		// Assuming you have a constructor that takes a version string
		Version version1 = new Version("1.19.3");
		
		// Test with an earlier version
		Version earlierVersion = new Version("1.19.2");
		Assertions.assertTrue(version1.isSupported(earlierVersion));
		
		// Test with an identical version
		Version identicalVersion = new Version("1.19.3");
		Assertions.assertTrue(version1.isSupported(identicalVersion));
		
		// Test with a later version
		Version laterVersion = new Version("1.19.4");
		Assertions.assertFalse(version1.isSupported(laterVersion));
		
		// Test with a major version without patch number
		Version majorVersion = new Version("1.19");
		Assertions.assertTrue(version1.isSupported(majorVersion));
	}
}