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
    // Repository for AxMinions API - you might need to change this
    // I could not find the repository URL. Please replace this with the correct one.
    // It might be JitPack, or a custom repository from Artillex-Studios.
    // For example: maven { url = uri("https://jitpack.io") }
    // Or: maven { url = uri("https://repo.artillex-studios.com/snapshots") }
    maven { url = uri("https://repo.artillex-studios.com/releases") }
}

dependencies {
    // Kotlin standard library
    implementation(kotlin("stdlib-jdk8"))

    // Paper API
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

    // AxMinions API - you will need to find the correct dependency string.
    // I am using a placeholder here.
    // You can often find this on the plugin's Spigot page, GitHub, or documentation.
    // Example for JitPack: compileOnly("com.github.user:repo:tag")
    compileOnly("com.artillexstudios:axminions-api:1.0.0") // This is a guess, please verify
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jar {
    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
