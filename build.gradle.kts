import io.papermc.hangarpublishplugin.model.Platforms
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id("java")
    id("java-library")

    alias(libs.plugins.minotaur)
    alias(libs.plugins.shadow)
    alias(libs.plugins.hangar.publish.plugin)
    alias(libs.plugins.plugin.yml.paper)
    alias(libs.plugins.run.paper)
}

group = "net.onelitefeather.bettergopaint"
version = "1.1.0"

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    // Paper / Spigot
    compileOnly(libs.paper)
    // Fawe / WorldEdit
    implementation(platform(libs.fawe.bom))
    compileOnlyApi(libs.fawe.bukkit)
    // Utils
    implementation(libs.serverlib)
    implementation(libs.paperlib)
    // Stats
    implementation(libs.bstats)
    // Internationalization
    implementation(libs.core.internationalization)
    // Commands
    implementation(libs.cloud.annotations)
    implementation(libs.cloud.minecraft.extras)
    implementation(libs.cloud.paper)
    annotationProcessor(libs.cloud.annotations)
}

paper {
    name = "goPaintAdvanced"
    main = "net.onelitefeather.bettergopaint.BetterGoPaint"
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
