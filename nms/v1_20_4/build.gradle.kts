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
	paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
}

tasks {
	assemble {
		dependsOn(reobfJar)
	}
	
	shadowJar {
		relocate("com.dsh105.echopet.nms", "com.dsh105.echopet.compat.nms.v1_20_4")
	}
}