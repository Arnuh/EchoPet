import org.apache.tools.ant.filters.ReplaceTokens

plugins {
	id("echopet-conventions")
	// https://github.com/Goooler/shadow/releases
	id("io.github.goooler.shadow") version "8.1.7"
}

group = "com.dsh105.echopet"
version = "1.4.0-SNAPSHOT"

dependencies {
	api("com.zaxxer:HikariCP:5.0.1")
	api("org.bstats:bstats-bukkit:2.2.1")
	compileOnly("me.clip:placeholderapi:2.10.10")
	compileOnly("com.sk89q.worldguard:worldguard-legacy:7.0.0-SNAPSHOT")
	implementation(project(":api"))
	implementation(project(path = ":nms:v1_20_6", configuration = "shadow"))
	implementation(project(path = ":nms:v1_21", configuration = "shadow"))
	implementation(project(path = ":nms:v1_21_1", configuration = "shadow"))
}

tasks {
	processResources {
		filesMatching(listOf("plugin.yml", "paper-plugin.yml")) {
			filter<ReplaceTokens>("tokens" to mapOf("VERSION" to version, "BUILD_NUMBER" to (System.getenv("BUILD_NUMBER") ?: "")))
		}
	}
	
	shadowJar {
		archiveBaseName.set("EchoPet")
		archiveClassifier.set("")
		archiveVersion.set("")
		manifest {
			attributes(Pair("paperweight-mappings-namespace", "mojang"))
		}
		relocate("com.zaxxer", "com.dsh105.echopet.libs.com.zaxxer")
		relocate("com.codingforcookies.robert", "com.dsh105.echopet.libs.com.codingforcookies.robert")
		relocate("org.bstats", "com.dsh105.echopet.libs.org.bstats")
	}
}