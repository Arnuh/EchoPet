plugins {
	id("echopet-conventions")
	id("io.papermc.paperweight.userdev") version "1.5.3"
}

subprojects {
	apply(plugin = "io.papermc.paperweight.userdev")
}

dependencies {
	api(project(":api"))
	paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}