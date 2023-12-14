import org.apache.tools.ant.filters.ReplaceTokens

plugins {
	id("echopet-conventions")
	id("com.github.johnrengelman.shadow") version "8.0.0"
}

group = "com.dsh105.echopet"
version = "1.4.0-SNAPSHOT"

dependencies {
	api("com.zaxxer:HikariCP:5.0.1")
	api("org.bstats:bstats-bukkit:2.2.1")
	compileOnly("me.clip:placeholderapi:2.10.10")
	compileOnly("com.sk89q.worldguard:worldguard-legacy:7.0.0-SNAPSHOT")
	implementation(project(":api"))
	implementation(project(path = ":nms:v1_17_1", configuration = "reobf"))
	implementation(project(path = ":nms:v1_18_1", configuration = "reobf"))
	implementation(project(path = ":nms:v1_18_2", configuration = "reobf"))
	implementation(project(path = ":nms:v1_19", configuration = "reobf"))
	implementation(project(path = ":nms:v1_19_1", configuration = "reobf"))
	implementation(project(path = ":nms:v1_19_2", configuration = "reobf"))
	implementation(project(path = ":nms:v1_19_3", configuration = "reobf"))
	implementation(project(path = ":nms:v1_19_4", configuration = "reobf"))
	implementation(project(path = ":nms:v1_20", configuration = "reobf"))
	implementation(project(path = ":nms:v1_20_1", configuration = "reobf"))
	implementation(project(path = ":nms:v1_20_2", configuration = "reobf"))
	implementation(project(path = ":nms:v1_20_4", configuration = "reobf"))
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
		relocate("com.zaxxer", "com.dsh105.echopet.libs.com.zaxxer")
		relocate("com.codingforcookies.robert", "com.dsh105.echopet.libs.com.codingforcookies.robert")
		relocate("org.bstats", "com.dsh105.echopet.libs.org.bstats")
	}
}