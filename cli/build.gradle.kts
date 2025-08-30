plugins {
    kotlin("jvm") version "1.9.10"
    application
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
    maven("https://jitpack.io") // necesario para KTX
}

dependencies {
    implementation(project(":core"))
}

application {
    mainClass.set("org.example.cli.MainKt")
}
