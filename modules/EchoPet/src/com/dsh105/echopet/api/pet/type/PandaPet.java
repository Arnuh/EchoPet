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

package com.dsh105.echopet.api.pet.type;

import com.dsh105.echopet.api.pet.AgeablePet;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PandaGene;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityPandaPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IPandaPet;
import org.bukkit.entity.Player;

/**
 * @author Arnah
 * @since Jul 03, 2019
 */
@EntityPetType(petType = PetType.PANDA)
public class PandaPet extends AgeablePet implements IPandaPet{
	
	private PandaGene mainGene = PandaGene.Normal, hiddenGene = PandaGene.Normal;
	private boolean rolling, sitting, layingDown;
	
	public PandaPet(Player owner){
		super(owner);
	}
	
	@Override
	public void setMainGene(PandaGene gene){
		this.mainGene = gene;
		((IEntityPandaPet) getEntityPet()).setMainGene(gene);
	}
	
	@Override
	public void setHiddenGene(PandaGene gene){
		this.hiddenGene = gene;
		((IEntityPandaPet) getEntityPet()).setHiddenGene(gene);
	}
	
	@Override
	public void setRolling(boolean rolling){
		this.rolling = rolling;
		((IEntityPandaPet) getEntityPet()).setRolling(rolling);
	}
	
	@Override
	public void setSitting(boolean sitting){
		this.sitting = sitting;
		((IEntityPandaPet) getEntityPet()).setSitting(sitting);
	}
	
	@Override
	public void setLayingDown(boolean layingDown){
		this.layingDown = layingDown;
		((IEntityPandaPet) getEntityPet()).setLayingDown(layingDown);
	}
}