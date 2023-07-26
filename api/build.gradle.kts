plugins {
	id("echopet-conventions")
}

dependencies {
	compileOnly("com.sk89q.worldguard:worldguard-legacy:7.0.0-SNAPSHOT")
	api("com.codingforcookies:robert:1.2-SNAPSHOT") {
		exclude(module = "spigot-api")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks {
	test {
		useJUnitPlatform()
	}
}