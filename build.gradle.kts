plugins {
    id("net.fabricmc.fabric-loom") version "1.19-SNAPSHOT"
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${property("loader_version")}")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks.processResources {
    val version = project.version
    inputs.property("version", version)
    filesMatching("fabric.mod.json") { expand("version" to version) }
}
