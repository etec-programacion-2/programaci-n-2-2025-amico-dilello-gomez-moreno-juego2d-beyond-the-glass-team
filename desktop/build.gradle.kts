plugins {
    application
    kotlin("jvm") version "1.9.24"
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
}

val gdxVersion = "1.11.0"

dependencies {
    implementation(project(":core"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")    
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop") 
}

application {
    mainClass.set("org.example.desktop.DesktopLauncherKt")
}