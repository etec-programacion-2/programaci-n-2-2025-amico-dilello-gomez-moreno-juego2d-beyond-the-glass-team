plugins {
    application
    kotlin("jvm") version "1.9.10"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
    maven("https://jitpack.io") // igual que core
}

dependencies {
    implementation(project(":core"))
}

application {
    mainClass.set("org.example.cli.MainKt")
}
