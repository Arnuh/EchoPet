package com.dsh105.echopet.compat.api.particle;

import java.util.Collection;
import java.util.Set;

import com.dsh105.echopet.compat.api.entity.IPet;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://codingforcookies.com/
 * @since Jul 11, 2016
 */
public interface Trail{

	public String getName();

	public String getParticleType();

	public String getPermission();

	public boolean canToggle();

	public int getTickDelay();

	public float getSpeed();

	public int getCount();

	public double getX();

	public double getY();

	public double getZ();

	public float getXOffset();

	public float getYOffset();

	public float getZOffset();

	public Trail getParentTrail();

	public void setParentTrail(Trail parentTrail);

	public Collection<String> getSubTrailNames();

	public Set<Trail> getSubTrails();

	public void addSubTrail(Trail subTrail);

	public void start(final IPet pet);

	public void cancel();

	public void displayTrail(final IPet pet);

	public Trail clone();
}
