plugins {
	id("echopet-conventions")
	id("io.github.goooler.shadow") version "8.1.7"
}

group = "com.dsh105.echopet"
version = "1.4.0-SNAPSHOT"

dependencies {
	implementation(project(":nms")) {
		exclude(module = "api")
	}
	paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
}

// For >= 1.20.5 when you don't care about supporting spigot
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

tasks {
	shadowJar {
		relocate("com.dsh105.echopet.nms", "com.dsh105.echopet.compat.nms.v1_21_1")
	}
}