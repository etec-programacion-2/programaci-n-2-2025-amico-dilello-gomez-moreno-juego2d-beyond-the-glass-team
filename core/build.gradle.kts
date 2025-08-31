plugins {
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
    maven("https://jitpack.io") // necesario para KTX
}

dependencies {
    implementation("io.github.libktx:ktx-app:1.10.0")
    implementation("io.github.libktx:ktx-graphics:1.10.0")
}
