plugins {
	id("echopet-conventions")
}

dependencies {
	compileOnly("com.sk89q.worldguard:worldguard-legacy:7.0.0-SNAPSHOT")
	api("com.codingforcookies:robert:1.2-SNAPSHOT") {
		exclude(module = "spigot-api")
	}
}