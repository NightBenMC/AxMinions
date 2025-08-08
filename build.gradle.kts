import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.20"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    // Repository for Paper API
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    // Repository for AxMinions API
    maven { url = uri("https://repo.artillex-studios.com/releases") }
}

dependencies {
    // Kotlin standard library
    implementation(kotlin("stdlib-jdk8"))

    // Paper API
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

    // The AxMinions API is included in the main AxMinions plugin.
    // You need to depend on the AxMinions plugin jar itself.
    // The group and name are likely correct, but you will need to find the correct version.
    // You can usually find this on the plugin's download page or by asking the developers.
    compileOnly("com.artillexstudios:axminions:LATEST") // Replace LATEST with the actual version of AxMinions you are using
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jar {
    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
