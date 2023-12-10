plugins {
	id("echopet-conventions")
	id("io.papermc.paperweight.userdev") version "1.5.11"
}

subprojects {
	apply(plugin = "io.papermc.paperweight.userdev")
}

dependencies {
	api(project(":api"))
	paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
}