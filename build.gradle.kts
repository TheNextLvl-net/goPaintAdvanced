import io.papermc.hangarpublishplugin.model.Platforms
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("java-library")

    id("io.github.goooler.shadow") version "8.1.7"
    id("io.papermc.hangar-publish-plugin") version "0.1.2"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "net.thenextlvl.gopaint"
version = "1.1.3"

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.32")
    compileOnly("net.thenextlvl.core:annotations:2.0.1")
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")

    implementation("org.bstats:bstats-bukkit:3.0.2")

    api("net.thenextlvl.core:i18n:1.0.18")
    api("net.thenextlvl.core:adapters:1.0.9")

    api(project(":api"))

    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

paper {
    name = "goPaintAdvanced"
    main = "net.thenextlvl.gopaint.GoPaintPlugin"
    authors = listOf("Arcaniax", "TheMeinerLP", "NonSwag")
    apiVersion = "1.20"

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
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.shadowJar {
    dependencies {
        relocate("org.bstats", "${rootProject.group}.metrics")
    }
}

tasks.runServer {
    minecraftVersion("1.20.6")
    jvmArgs("-Dcom.mojang.eula.agree=true")
}

val versionString: String = project.version as String
val isRelease: Boolean = !versionString.contains("-pre")

hangarPublish { // docs - https://docs.papermc.io/misc/hangar-publishing
    publications.register("plugin") {
        id.set("goPaintAdvanced")
        version.set(versionString)
        channel.set(if (isRelease) "Release" else "Snapshot")
        apiKey.set(System.getenv("HANGAR_API_TOKEN"))
        platforms.register(Platforms.PAPER) {
            jar.set(tasks.shadowJar.flatMap { it.archiveFile })
            platformVersions.set(listOf("1.20.6"))
            dependencies {
                url("FastAsyncWorldEdit", "https://hangar.papermc.io/IntellectualSites/FastAsyncWorldEdit") {
                    required.set(true)
                }
            }
        }
    }
}
