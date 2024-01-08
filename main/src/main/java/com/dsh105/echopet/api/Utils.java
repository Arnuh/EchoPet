package com.dsh105.echopet.api;

import org.bukkit.Location;

public class Utils {
    public static Location centerLocation(Location location) {
        Location centerLocation = location.clone();
        centerLocation.setX(location.getBlockX() + 0.5);
        centerLocation.setY(location.getY());
        centerLocation.setZ(location.getBlockZ() + 0.5);
        return centerLocation;
    }
}
