plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    withSourcesJar()
    withJavadocJar()
}

tasks.compileJava {
    options.release.set(21)
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.thenextlvl.net/releases")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")

    compileOnlyApi(platform("com.intellectualsites.bom:bom-newest:1.55"))
    compileOnlyApi("com.fastasyncworldedit:FastAsyncWorldEdit-Core") { isTransitive = false }
    compileOnlyApi("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") {
        exclude("org.jetbrains", "annotations")
    }

    api("net.thenextlvl.core:i18n:3.2.2")
    api("net.thenextlvl.core:paper:2.2.1")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
    repositories.maven {
        val channel = if ((version as String).contains("-pre")) "snapshots" else "releases"
        url = uri("https://repo.thenextlvl.net/$channel")
        credentials {
            username = System.getenv("REPOSITORY_USER")
            password = System.getenv("REPOSITORY_TOKEN")
        }
    }
}
