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
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseAbstractPet;
import com.dsh105.echopet.compat.nms.v1_17_R1.entity.EntityAgeablePet;
import net.minecraft.server.v1_17_R1.Block;
import net.minecraft.server.v1_17_R1.BlockPosition;
import net.minecraft.server.v1_17_R1.Blocks;
import net.minecraft.server.v1_17_R1.DataWatcher;
import net.minecraft.server.v1_17_R1.DataWatcherObject;
import net.minecraft.server.v1_17_R1.DataWatcherRegistry;
import net.minecraft.server.v1_17_R1.EntityInsentient;
import net.minecraft.server.v1_17_R1.EntityTypes;
import net.minecraft.server.v1_17_R1.SoundEffectType;
import net.minecraft.server.v1_17_R1.Vec3D;
import net.minecraft.server.v1_17_R1.World;

public abstract class EntityHorseAbstractPet extends EntityAgeablePet implements IEntityHorseAbstractPet{
	
	// EntityHorseAbstract: Zombie, Skeleton
	private static final DataWatcherObject<Byte> VISUAL = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherRegistry.a);// feet kicking, whatev
	private static final DataWatcherObject<Optional<UUID>> OWNER = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherRegistry.o);
	private int rearingCounter = 0;
	private int stepSoundCount = 0;
	
	public EntityHorseAbstractPet(EntityTypes<? extends EntityInsentient> type, World world){
		super(type, world);
	}
	
	public EntityHorseAbstractPet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet){
		super(type, world, pet);
	}
	
	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(VISUAL, (byte) 0);
		this.datawatcher.register(OWNER, Optional.empty());
	}
	
	@Override
	protected void makeStepSound(BlockPosition pos, Block block){
		SoundEffectType soundeffecttype = block.getStepSound(block.getBlockData());
		if(this.world.getType(pos).getBlock() == block){
			soundeffecttype = Blocks.SNOW.getStepSound(block.getBlockData());
		}
		if(!block.getBlockData().getMaterial().isLiquid()){
			HorseVariant enumhorsetype = ((IHorseAbstractPet) getPet()).getVariant();
			if((isVehicle()) && (!enumhorsetype.hasChest())){
				this.stepSoundCount += 1;
				if((this.stepSoundCount > 5) && (this.stepSoundCount % 3 == 0)){
					makeSound("entity.horse.gallop", soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
					if((enumhorsetype == HorseVariant.HORSE) && (this.random.nextInt(10) == 0)){
						makeSound("entity.horse.breathe", soundeffecttype.getVolume() * 0.6F, soundeffecttype.getPitch());
					}
				}else if(this.stepSoundCount <= 5){
					makeSound("entity.horse.step_wood", soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
				}
			}else if(soundeffecttype == SoundEffectType.a){
				makeSound("entity.horse.step_wood", soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
			}else{
				makeSound("entity.horse.step", soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
			}
		}
	}
	
	@Override
	public void g(Vec3D motion){
		super.g(motion);
		// forward
		if(motion.x <= 0.0F){
			this.stepSoundCount = 0;
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
			setHorseVisual(64, false);
		}
	}
	
	@Override
	protected void doJumpAnimation(){
		makeSound("entity.horse.gallop", 0.4F, 1.0F);
		this.rearingCounter = 1;
		setHorseVisual(64, true);
	}
	
	@Override
	public void setSaddled(boolean flag){
		this.setHorseVisual(4, flag);
	}
	
	/**
	 * 2: Is tamed
	 * 4: Saddle
	 * 8: Has Chest - Separate datawatcher in 1.11+
	 * 16: Bred - 8 in 1.11+
	 * 32: Eating haystack
	 * 64: Rear
	 * 128: Mouth open
	 */
	public boolean getHorseVisual(int i){
		return (this.datawatcher.get(VISUAL).byteValue() & i) != 0;
	}
	
	/**
	 * 2: Is tamed
	 * 4: Saddle
	 * 8: Has Chest - Separate datawatcher in 1.11+
	 * 16: Bred - 8 in 1.11+
	 * 32: Eating haystack
	 * 64: Rear
	 * 128: Mouth open
	 */
	public void setHorseVisual(int i, boolean flag){
		byte b0 = this.datawatcher.get(VISUAL).byteValue();
		if(flag){
			this.datawatcher.set(VISUAL, Byte.valueOf((byte) (b0 | i)));
		}else{
			this.datawatcher.set(VISUAL, Byte.valueOf((byte) (b0 & (i ^ 0xFFFFFFFF))));
		}
	}
	
	public void setVariant(HorseVariant variant){}
}
