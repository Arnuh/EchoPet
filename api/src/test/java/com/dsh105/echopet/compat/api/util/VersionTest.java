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

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VersionTest{
	
	@Test
	void isCompatible(){
		Version version = new Version("1.19-R2");
		// Confirm documentation is correct
		assertTrue(version.isCompatible("1.19-R3"));
		assertTrue(version.isCompatible("1.20-R1"));
		assertTrue(version.isCompatible("2.0-R1"));
		assertFalse(version.isCompatible("1.19-R1"));
		// Extra tests
		assertFalse(version.isCompatible("1.7-R2"));
	}
	
	@Test
	void isSupported(){
		Version version = new Version("1.7-R3");
		// Confirm documentation is correct
		assertTrue(version.isSupported("1.7-R2"));
		assertTrue(version.isSupported("1.7-R1"));
		assertFalse(version.isSupported("1.7-R4"));
		// Extra tests
		assertTrue(version.isSupported("1.6-R4"));
		assertTrue(version.isSupported("1.0-R1"));
		assertFalse(version.isSupported("2.0-R3"));
	}
}