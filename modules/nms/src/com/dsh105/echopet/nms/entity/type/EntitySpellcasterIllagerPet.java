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

package com.dsh105.echopet.nms.entity.type;

import com.dsh105.echopet.compat.api.entity.pet.IPet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public abstract class EntitySpellcasterIllagerPet extends EntityAbstractIllagerPet{
	
	// SpellcasterIllager.IllagerSpell
	private static final EntityDataAccessor<Byte> DATA_SPELL_CASTING_ID = SynchedEntityData.defineId(EntitySpellcasterIllagerPet.class, EntityDataSerializers.BYTE);
	
	public EntitySpellcasterIllagerPet(EntityType<? extends Mob> type, Level world){
		super(type, world);
	}
	
	public EntitySpellcasterIllagerPet(EntityType<? extends Mob> type, Level world, IPet pet){
		super(type, world, pet);
	}
	
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_SPELL_CASTING_ID, (byte) 0);
	}
}