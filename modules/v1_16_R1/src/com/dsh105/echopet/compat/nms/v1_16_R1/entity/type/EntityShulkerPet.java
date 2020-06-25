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
package com.dsh105.echopet.compat.nms.v1_16_R1.entity.type;

import java.util.Optional;
import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.EntitySize;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityShulkerPet;
import com.dsh105.echopet.compat.nms.v1_16_R1.entity.EntityPet;
import net.minecraft.server.v1_16_R1.BlockPosition;
import net.minecraft.server.v1_16_R1.DataWatcher;
import net.minecraft.server.v1_16_R1.DataWatcherObject;
import net.minecraft.server.v1_16_R1.DataWatcherRegistry;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.EnumColor;
import net.minecraft.server.v1_16_R1.EnumDirection;
import net.minecraft.server.v1_16_R1.World;
import org.bukkit.DyeColor;

/**
 * @since Mar 7, 2016
 */

@EntitySize(width = 1.0F, height = 1.0F)
@EntityPetType(petType = PetType.SHULKER)
public class EntityShulkerPet extends EntityPet implements IEntityShulkerPet{
	
	protected static final DataWatcherObject<EnumDirection> ATTACHED_FACE = DataWatcher.a(EntityShulkerPet.class, DataWatcherRegistry.n);
	protected static final DataWatcherObject<Optional<BlockPosition>> ATTACHED_BLOCK_POS = DataWatcher.a(EntityShulkerPet.class, DataWatcherRegistry.m);
	protected static final DataWatcherObject<Byte> PEEK_TICK = DataWatcher.a(EntityShulkerPet.class, DataWatcherRegistry.a);// how many ticks its opened for
	protected static final DataWatcherObject<Byte> COLOR_DW = DataWatcher.a(EntityShulkerPet.class, DataWatcherRegistry.a);
	
	public EntityShulkerPet(World world){
		super(EntityTypes.SHULKER, world);
	}
	
	public EntityShulkerPet(World world, IPet pet){
		super(EntityTypes.SHULKER, world, pet);
	}
	
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(ATTACHED_FACE, EnumDirection.DOWN);
		this.datawatcher.register(ATTACHED_BLOCK_POS, Optional.empty());
		this.datawatcher.register(PEEK_TICK, (byte) 0);
		this.datawatcher.register(COLOR_DW, (byte) EnumColor.PURPLE.getColorIndex());
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		return SizeCategory.REGULAR;
	}
	
	@Override
	public void setOpen(boolean open){
		if(open){
			datawatcher.set(PEEK_TICK, Byte.MAX_VALUE);// since we don't have the ai nothing decreases it except us.
		}else{
			datawatcher.set(PEEK_TICK, (byte) 0);
		}
	}
	
	@Override
	public void setColor(DyeColor color){
		datawatcher.register(COLOR_DW, (byte) EnumColor.fromColorIndex(color.ordinal()).getColorIndex());// is enumcolor stuff needed?
	}
}
