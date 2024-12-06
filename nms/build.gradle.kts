plugins {
	id("echopet-conventions")
	id("io.papermc.paperweight.userdev") version "1.7.6"
}

subprojects {
	apply(plugin = "io.papermc.paperweight.userdev")
}

dependencies {
	api(project(":api"))
	paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
}