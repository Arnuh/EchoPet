package com.dsh105.echopet.nms.entity.type;

import com.dsh105.echopet.compat.api.entity.EntityPetType;
import com.dsh105.echopet.compat.api.entity.PetType;
import com.dsh105.echopet.compat.api.entity.nms.IEntityAnimalPet;
import com.dsh105.echopet.compat.api.entity.nms.handle.IEntityPetHandle;
import com.dsh105.echopet.compat.api.entity.pet.IPet;
import com.dsh105.echopet.compat.api.entity.type.pet.ICamelPet;
import com.dsh105.echopet.nms.VersionBreaking;
import com.dsh105.echopet.nms.entity.EntityPetGiveMeAccess;
import com.dsh105.echopet.nms.entity.INMSEntityPetHandle;
import com.dsh105.echopet.nms.entity.ai.brain.PetCamelAi;
import com.dsh105.echopet.nms.entity.handle.EntityCamelPetHandle;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;

@EntityPetType(petType = PetType.CAMEL)
public class EntityCamelPet extends Camel implements IEntityAnimalPet, EntityPetGiveMeAccess{
	
	protected ICamelPet pet;
	private final INMSEntityPetHandle petHandle;
	// how do I init this without static or constructor
	private static boolean thisIsDumb = false;
	private static boolean usesBrain = false;
	
	public EntityCamelPet(Level world, ICamelPet pet){
		super(EntityType.CAMEL, world);
		this.pet = pet;
		this.petHandle = new EntityCamelPetHandle(this);
	}
	
	protected Brain.Provider<EntityCamelPet> petBrainProvider(){
		return PetCamelAi.brainProvider(usesBrain());
	}
	
	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic){
		if(usesBrain()){
			return PetCamelAi.makeBrain(petBrainProvider().makeBrain(dynamic));
		}else{
			return petBrainProvider().makeBrain(dynamic);
		}
	}
	
	@Override
	public void openCustomInventoryScreen(Player player){
		// Disable saddle inventory.
	}
	
	// Pet handling
	
	@Override
	public LivingEntity getEntity(){
		return (LivingEntity) getBukkitEntity();
	}
	
	@Override
	public void remove(boolean makeSound){
		petHandle.remove(makeSound);
	}
	
	@Override
	public boolean isDead(){
		return dead;
	}
	
	@Override
	public float getMaxUpStep(){
		return maxUpStep();
	}
	
	@Override
	public void setLocation(Location location){
		this.absMoveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		this.level = ((CraftWorld) location.getWorld()).getHandle();
	}
	
	@Override
	public IPet getPet(){
		return pet;
	}
	
	@Override
	public IEntityPetHandle getHandle(){
		return petHandle;
	}
	
	@Override
	public boolean usesBrain(){
		if(thisIsDumb) return usesBrain;
		thisIsDumb = true;
		return usesBrain = ICamelPet.BRAIN_ENABLED.get(PetType.TADPOLE);
	}
	
	@Override
	public SoundEvent publicDeathSound(){
		return getDeathSound();
	}
	
	@Override
	public boolean isPersistenceRequired(){
		return true;
	}
	
	@Override
	public void tick(){
		super.tick();
		petHandle.tick();
	}
	
	@Override
	public void travel(Vec3 vec3d){
		Vec3 result = petHandle.travel(vec3d);
		if(result == null){
			VersionBreaking.setFlyingSpeed(this, 0.02F);
			super.travel(vec3d);
			return;
		}
		setSpeed(petHandle.getSpeed());
		super.travel(result);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbttagcompound){}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbttagcompound){}
}
