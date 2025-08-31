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
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.10.0")
    implementation("com.badlogicgames.gdx:gdx-platform:1.10.0:natives-desktop")
    implementation("io.github.libktx:ktx-app:1.10.0")
    implementation("io.github.libktx:ktx-graphics:1.10.0")
}

application {
    mainClass.set("org.example.desktop.DesktopLauncherKt")
}

tasks.named<JavaExec>("run") {
    workingDir = project.file("assets")
}
