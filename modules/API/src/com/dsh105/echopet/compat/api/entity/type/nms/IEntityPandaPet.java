/*
 *
 *  * This file is part of EchoPet.
 *  * EchoPet is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  * EchoPet is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  * You should have received a copy of the GNU General Public License
 *  * along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.dsh105.echopet.compat.api.entity.type.nms;

import com.dsh105.echopet.compat.api.entity.IEntityAgeablePet;
import com.dsh105.echopet.compat.api.entity.PandaGene;

/**
 * @author Arnah
 * @since Jul 03, 2019
 */
public interface IEntityPandaPet extends IEntityAgeablePet{
	
	void setMainGene(PandaGene gene);
	
	void setHiddenGene(PandaGene gene);
	
	void setRolling(boolean rolling);
	
	void setSitting(boolean sitting);
	
	void setLayingDown(boolean layingDown);
}