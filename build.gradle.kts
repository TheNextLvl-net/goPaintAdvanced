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
version = "1.1.0"

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    // Paper / Spigot
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    // Fawe / WorldEdit
    implementation(platform("com.intellectualsites.bom:bom-newest:1.27"))
    compileOnlyApi("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit")
    // Utils
    implementation("dev.notmyfault.serverlib:ServerLib")
    implementation("io.papermc:paperlib")
    // Stats
    implementation("org.bstats:bstats-bukkit:3.0.2")
    // Internationalization
    implementation("net.thenextlvl.core:i18n:1.0.18")
    // Commands
    implementation("org.incendo:cloud-minecraft-extras:2.0.0-beta.8")
    implementation("org.incendo:cloud-paper:2.0.0-beta.8")
    implementation("org.incendo:cloud-annotations:2.0.0-rc.2")
    annotationProcessor("org.incendo:cloud-annotations:2.0.0-rc.2")
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
        register("bettergopaint.command.admin.reload") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("bettergopaint.use") {
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("bettergopaint.admin") {
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
        register("bettergopaint.world.bypass") {
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.shadowJar {
    dependencies {
        relocate("org.incendo.serverlib", "${rootProject.group}.serverlib")
        relocate("org.bstats", "${rootProject.group}.metrics")
        relocate("io.papermc.lib", "${rootProject.group}.paperlib")
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
        }
    }
}
