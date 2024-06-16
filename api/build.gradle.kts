plugins {
    id("java")
    id("java-library")
}

group = "net.thenextlvl.gopaint"
version = "1.1.1"

repositories {
    mavenCentral()
    maven("https://repo.thenextlvl.net/releases")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.32")
    compileOnly("net.thenextlvl.core:annotations:2.0.1")
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")

    implementation(platform("com.intellectualsites.bom:bom-newest:1.27"))
    compileOnlyApi("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit")

    annotationProcessor("org.projectlombok:lombok:1.18.32")
}
