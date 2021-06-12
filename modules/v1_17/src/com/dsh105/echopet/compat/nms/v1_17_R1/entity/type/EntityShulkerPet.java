/*
 * This file is part of EchoPet.
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

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
package com.dsh105.echopet.compat.nms.v1_17_R1.entity.type;

import java.util.Optional;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityShulkerPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityPet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.DyeColor;

@EntitySize(width = 1.0F, height = 1.0F)
@EntityPetType(petType = PetType.SHULKER)
public class EntityShulkerPet extends EntityPet implements IEntityShulkerPet{
	
	protected static final EntityDataAccessor<Direction> ATTACHED_FACE = SynchedEntityData.defineId(EntityShulkerPet.class, EntityDataSerializers.DIRECTION);
	protected static final EntityDataAccessor<Optional<BlockPos>> ATTACHED_BLOCK_POS = SynchedEntityData.defineId(EntityShulkerPet.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
	protected static final EntityDataAccessor<Byte> PEEK_TICK = SynchedEntityData.defineId(EntityShulkerPet.class, EntityDataSerializers.BYTE);// how many ticks its opened for
	protected static final EntityDataAccessor<Byte> COLOR_DW = SynchedEntityData.defineId(EntityShulkerPet.class, EntityDataSerializers.BYTE);
	
	public EntityShulkerPet(Level world){
		super(EntityType.SHULKER, world);
	}
	
	public EntityShulkerPet(Level world, IPet pet){
		super(EntityType.SHULKER, world, pet);
	}
	
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(ATTACHED_FACE, Direction.DOWN);
		this.entityData.define(ATTACHED_BLOCK_POS, Optional.empty());
		this.entityData.define(PEEK_TICK, (byte) 0);
		this.entityData.define(COLOR_DW, (byte) net.minecraft.world.item.DyeColor.PURPLE.getId());
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
	
	@Override
	public void setOpen(boolean open){
		if(open){
			entityData.set(PEEK_TICK, Byte.MAX_VALUE);// since we don't have the ai nothing decreases it except us.
		}else{
			entityData.set(PEEK_TICK, (byte) 0);
		}
	}
	
	@Override
	public void setColor(DyeColor color){
		entityData.define(COLOR_DW, (byte) net.minecraft.world.item.DyeColor.byId(color.ordinal()).getId());
	}
}