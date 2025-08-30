plugins {
    application
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))  // 🔹 da acceso a MiJuego
    implementation(libs.bundles.libgdx.desktop)
    implementation(libs.bundles.ktx.desktop.core)
}

application {
    mainClass.set("org.example.desktop.DesktopLauncherKt")
}

tasks.named<JavaExec>("run") {
    workingDir = project.file("assets") // 🔹 importante para que encuentre badlogic.jpg
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain(17)
}
