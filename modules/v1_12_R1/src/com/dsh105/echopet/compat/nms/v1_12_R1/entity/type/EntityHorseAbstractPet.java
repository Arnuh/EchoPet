package com.dsh105.echopet.compat.nms.v1_12_R1.entity.type;

import java.util.UUID;

import com.dsh105.echopet.compat.api.entity.HorseVariant;
import com.dsh105.echopet.compat.api.entity.IPet;
import com.dsh105.echopet.compat.api.entity.SizeCategory;
import com.dsh105.echopet.compat.api.entity.type.nms.IEntityHorseAbstractPet;
import com.dsh105.echopet.compat.api.entity.type.pet.IHorseAbstractPet;
import com.dsh105.echopet.compat.nms.v1_12_R1.entity.EntityAgeablePet;
import com.google.common.base.Optional;

import net.minecraft.server.v1_12_R1.*;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Nov 19, 2016
 */
public abstract class EntityHorseAbstractPet extends EntityAgeablePet implements IEntityHorseAbstractPet{

	// EntityHorseAbstract: Zombie, Skeleton
	private static final DataWatcherObject<Byte> VISUAL = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherRegistry.a);// feet kicking, whatev
	private static final DataWatcherObject<Optional<UUID>> OWNER = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherRegistry.m);
	private int rearingCounter = 0;
	private int stepSoundCount = 0;

	public EntityHorseAbstractPet(World world){
		super(world);
	}

	public EntityHorseAbstractPet(World world, IPet pet){
		super(world, pet);
	}

	@Override
	protected void initDatawatcher(){
		super.initDatawatcher();
		this.datawatcher.register(VISUAL, (byte) 0);
		this.datawatcher.register(OWNER, Optional.absent());
	}

	@Override
	protected void makeStepSound(BlockPosition pos, Block block){
		SoundEffectType soundeffecttype = block.getStepSound();
		if(this.world.getType(pos) == Blocks.SNOW){
			soundeffecttype = Blocks.SNOW_LAYER.getStepSound();
		}
		if(!block.getBlockData().getMaterial().isLiquid()){
			HorseVariant enumhorsetype = ((IHorseAbstractPet) getPet()).getVariant();
			if((isVehicle()) && (!enumhorsetype.hasChest())){
				this.stepSoundCount += 1;
				if((this.stepSoundCount > 5) && (this.stepSoundCount % 3 == 0)){
					makeSound("entity.horse.gallop", soundeffecttype.a() * 0.15F, soundeffecttype.b());
					if((enumhorsetype == HorseVariant.HORSE) && (this.random.nextInt(10) == 0)){
						makeSound("entity.horse.breathe", soundeffecttype.a() * 0.6F, soundeffecttype.b());
					}
				}else if(this.stepSoundCount <= 5){
					makeSound("entity.horse.step_wood", soundeffecttype.a() * 0.15F, soundeffecttype.b());
				}
			}else if(soundeffecttype == SoundEffectType.a){
				makeSound("entity.horse.step_wood", soundeffecttype.a() * 0.15F, soundeffecttype.b());
			}else{
				makeSound("entity.horse.step", soundeffecttype.a() * 0.15F, soundeffecttype.b());
			}
		}
	}

	@Override
	public void g(float sideMot, float forwMot){
		super.g(sideMot, forwMot);
		if(forwMot <= 0.0F){
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
		return (((Byte) this.datawatcher.get(VISUAL)).byteValue() & i) != 0;
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
		byte b0 = ((Byte) this.datawatcher.get(VISUAL)).byteValue();
		if(flag){
			this.datawatcher.set(VISUAL, Byte.valueOf((byte) (b0 | i)));
		}else{
			this.datawatcher.set(VISUAL, Byte.valueOf((byte) (b0 & (i ^ 0xFFFFFFFF))));
		}
	}

	public void setVariant(HorseVariant variant){}
}
