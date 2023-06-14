plugins {
	id("echopet-conventions")
	id("com.github.johnrengelman.shadow") version "8.0.0"
}

group = "com.dsh105.echopet"
version = "1.4.0-SNAPSHOT"

dependencies {
	implementation(project(":nms")) {
		exclude(module = "api")
	}
	api(project(":api"))
	paperweight.paperDevBundle("1.18.1-R0.1-SNAPSHOT")
}

tasks {
	assemble {
		dependsOn(reobfJar)
	}
	
	shadowJar {
		relocate("com.dsh105.echopet.nms", "com.dsh105.echopet.compat.nms.v1_18_1")
		relocate("org.bukkit.craftbukkit.v1_19_R3", "org.bukkit.craftbukkit.v1_18_R1")
	}
}