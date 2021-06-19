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

package com.dsh105.echopet.compat.nms.v1_17_R1.entity.type;

import java.util.Optional;
import java.util.UUID;
import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorseAbstractPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityAgeablePet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class EntityHorseAbstractPet extends EntityAgeablePet implements IEntityHorseAbstractPet{
	
	// EntityHorseAbstract: Zombie, Skeleton
	private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(EntityHorseAbstractPet.class, EntityDataSerializers.BYTE);// feet kicking, whatev
	private static final EntityDataAccessor<Optional<UUID>> DATA_ID_OWNER_UUID = SynchedEntityData.defineId(EntityHorseAbstractPet.class, EntityDataSerializers.OPTIONAL_UUID);
	private int rearingCounter = 0;
	private int gallopSoundCounter = 0;
	protected boolean canGallop = true;
	
	private static final int FLAG_TAME = 0x2, FLAG_SADDLE = 0x4, FLAG_BRED = 0x8, FLAG_EATING = 0x10, FLAG_STANDING = 0x20, FLAG_OPEN_MOUTH = 0x40;
	
	public EntityHorseAbstractPet(EntityType<? extends Mob> type, Level world){
		super(type, world);
	}
	
	public EntityHorseAbstractPet(EntityType<? extends Mob> type, Level world, IPet pet){
		super(type, world, pet);
	}
	
	@Override
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(DATA_ID_FLAGS, (byte) 0);
		this.entityData.define(DATA_ID_OWNER_UUID, Optional.empty());
	}
	
	@Override
	protected void playStepSound(BlockPos blockposition, BlockState iblockdata){
		if(!iblockdata.getMaterial().isLiquid()){
			BlockState iblockdata1 = this.level.getBlockState(blockposition.up());
			SoundType soundeffecttype = iblockdata.getSoundType();
			if(iblockdata1.is(Blocks.SNOW)){
				soundeffecttype = iblockdata1.getSoundType();
			}
			
			if(this.isVehicle() && this.canGallop){
				++this.gallopSoundCounter;
				if(this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0){
					this.playGallopSound(soundeffecttype);
				}else if(this.gallopSoundCounter <= 5){
					this.playSound(SoundEvents.HORSE_STEP_WOOD, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
				}
			}else if(soundeffecttype == SoundType.WOOD){
				this.playSound(SoundEvents.HORSE_STEP_WOOD, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
			}else{
				this.playSound(SoundEvents.HORSE_STEP, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
			}
		}
	}
	
	protected void playGallopSound(SoundType soundeffecttype){
		this.playSound(SoundEvents.HORSE_GALLOP, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
	}
	
	@Override
	public void travel(Vec3 motion){
		super.travel(motion);
		// forward
		if(motion.x <= 0.0F){
			this.gallopSoundCounter = 0;
		}
	}
	
	@Override
	public SizeCategory getSizeCategory(){
		if(this.isBaby()){
			return SizeCategory.TINY;
		}else{
			return SizeCategory.LARGE;
		}
	}
	
	@Override
	public void onLive(){
		super.onLive();
		if(rearingCounter > 0 && ++rearingCounter > 20){
			setHorseVisual(FLAG_OPEN_MOUTH, false);
		}
	}
	
	@Override
	protected void doJumpAnimation(){
		makeSound("entity.horse.gallop", 0.4F, 1.0F);
		this.rearingCounter = 1;
		setHorseVisual(FLAG_OPEN_MOUTH, true);
	}
	
	@Override
	public void setSaddled(boolean flag){
		this.setHorseVisual(FLAG_SADDLE, flag);
	}
	
	public boolean getHorseVisual(int i){
		return (this.entityData.get(DATA_ID_FLAGS) & i) != 0;
	}
	
	public void setHorseVisual(int i, boolean flag){
		byte b0 = this.entityData.get(DATA_ID_FLAGS);
		if(flag){
			this.entityData.set(DATA_ID_FLAGS, (byte) (b0 | i));
		}else{
			this.entityData.set(DATA_ID_FLAGS, (byte) (b0 & ~i));
		}
	}
	
	public void setVariant(HorseVariant variant){}
}
