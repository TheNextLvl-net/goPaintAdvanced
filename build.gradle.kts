import io.papermc.hangarpublishplugin.model.Platforms
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-rc3"
    id("io.papermc.hangar-publish-plugin") version "0.1.3"
    id("de.eldoria.plugin-yml.paper") version "0.7.1"
    id("xyz.jpenilla.run-paper") version "3.0.0-beta.1"
    id("com.modrinth.minotaur") version "2.+"
}

group = "net.thenextlvl.gopaint"
version = "1.6.0"

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")

    implementation("net.thenextlvl.core:adapters:2.0.2")
    implementation("org.bstats:bstats-bukkit:3.1.1-SNAPSHOT")
    implementation(project(":api"))
}

paper {
    name = "goPaintAdvanced"
    main = "net.thenextlvl.gopaint.GoPaintPlugin"
    authors = listOf("Arcaniax", "TheMeinerLP", "NonSwag")
    apiVersion = "1.21.5"
    foliaSupported = true

    serverDependencies {
        register("FastAsyncWorldEdit") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = true
        }
    }

    website = "https://thenextlvl.net"
    provides = listOf("goPaint")

    permissions {
        register("gopaint.use") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("gopaint.admin") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("gopaint.world.bypass") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.compileJava {
    options.release.set(21)
}

tasks.shadowJar {
    dependencies {
        relocate("org.bstats", "${rootProject.group}.metrics")
    }
}

tasks.runServer {
    minecraftVersion("1.21.6")
    jvmArgs("-Dcom.mojang.eula.agree=true")
}

val versionString: String = project.version as String
val isRelease: Boolean = !versionString.contains("-pre")

val versions: List<String> = (property("gameVersions") as String)
    .split(",")
    .map { it.trim() }

hangarPublish { // docs - https://docs.papermc.io/misc/hangar-publishing
    publications.register("plugin") {
        id.set("goPaintAdvanced")
        version.set(versionString)
        changelog = System.getenv("CHANGELOG")
        channel.set(if (isRelease) "Release" else "Snapshot")
        apiKey.set(System.getenv("HANGAR_API_TOKEN"))
        platforms.register(Platforms.PAPER) {
            jar.set(tasks.shadowJar.flatMap { it.archiveFile })
            platformVersions.set(versions)
            dependencies {
                url("FastAsyncWorldEdit", "https://hangar.papermc.io/IntellectualSites/FastAsyncWorldEdit") {
                    required.set(true)
                }
            }
        }
    }
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("a2wQ6jIv")
    changelog = System.getenv("CHANGELOG")
    versionType = if (isRelease) "release" else "beta"
    uploadFile.set(tasks.shadowJar)
    gameVersions.set(versions)
    syncBodyFrom.set(rootProject.file("README.md").readText())
    loaders.add("paper")
    loaders.add("folia")
    dependencies {
        required.project("fastasyncworldedit")
    }
}
