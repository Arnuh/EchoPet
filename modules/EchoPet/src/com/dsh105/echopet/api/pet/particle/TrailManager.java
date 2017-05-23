package com.dsh105.echopet.api.pet.particle;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.dsh105.commodus.config.YAMLConfig;
import com.dsh105.echopet.compat.api.particle.Trail;
import com.dsh105.echopet.compat.api.particle.Trails;
import com.dsh105.echopet.compat.api.util.ParticleEffect;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Jul 11, 2016
 */
public class TrailManager implements Trails{

	List<ParticleTrail> trails = Lists.newArrayList();

	public TrailManager(YAMLConfig config){
		Map<String, ParticleTrail> trails = new HashMap<>();
		if(config.config().isSet("trails")){
			ConfigurationSection cs = config.getConfigurationSection("trails");
			for(String key : cs.getKeys(false)){
				String particleName = config.getString("trails." + key + ".particleName").toUpperCase();
				if(ParticleEffect.fromName(particleName) == null){
					System.out.println("Unknown particle effect: " + particleName);
					return;
				}
				String permission = config.getString("trails." + key + ".permission");
				boolean canToggle = config.getBoolean("trails." + key + ".canToggle", true);
				Collection<String> subTrails = config.getStringList("trails." + key + ".subtrails");
				int interval = config.getInt("trails." + key + ".interval");
				float speed = (float) config.getDouble("trails." + key + ".speed");
				int count = config.getInt("trails." + key + ".count");
				double x = config.getDouble("trails." + key + ".x");
				double y = config.getDouble("trails." + key + ".y");
				double z = config.getDouble("trails." + key + ".z");
				float xOffset = (float) config.getDouble("trails." + key + ".xOffset");
				float yOffset = (float) config.getDouble("trails." + key + ".yOffset");
				float zOffset = (float) config.getDouble("trails." + key + ".zOffset");
				trails.put(key.toLowerCase(), new ParticleTrail(key, particleName, permission, canToggle, subTrails, interval, speed, count, x, y, z, xOffset, yOffset, zOffset));
			}
		}
		Map<String, ParticleTrail> cleanedUp = new HashMap<>();
		for(ParticleTrail trail : trails.values()){
			loadSubTrails(cleanedUp, trails, trail);
		}
		trails.clear();
	}

	protected void loadSubTrails(Map<String, ParticleTrail> cleanedUp, Map<String, ParticleTrail> trails, ParticleTrail trail){
		boolean newTrail = true;
		for(String trailName : trail.getSubTrailNames()){
			ParticleTrail subTrail = trails.get(trailName.toLowerCase());
			if(subTrail != null){
				if(cleanedUp.containsKey(subTrail.getName().toLowerCase())){
					subTrail = cleanedUp.get(subTrail.getName().toLowerCase());
					subTrail = subTrail.clone();
					newTrail = false;
				}else{
					subTrail = subTrail.clone();
					loadSubTrails(cleanedUp, trails, subTrail);
					cleanedUp.put(subTrail.getName().toLowerCase(), subTrail);
				}
				subTrail.setParentTrail(trail);
				trail.addSubTrail(subTrail);
			}else{
				System.out.println("Unknown trail: " + trailName);
				continue;
			}
		}
		if(newTrail){
			System.out.println("Loaded trail: " + trail.getName() + " with " + trail.getSubTrails().size() + " subtrails.");
			addTrail(trail);
		}
	}

	public List<Trail> getTrails(){// should be using a set..but i wanna transform
		return Lists.transform(trails, new Function<ParticleTrail, Trail>(){

			public Trail apply(ParticleTrail t){
				return (Trail) t;
			}
		});
	}

	public Trail getTrailByName(String name){
		for(Trail particle : getTrails()){
			if(particle.getName().equalsIgnoreCase(name)) return particle;
		}
		return null;
	}

	public void addTrail(ParticleTrail particle){
		trails.add(particle);
	}
}
