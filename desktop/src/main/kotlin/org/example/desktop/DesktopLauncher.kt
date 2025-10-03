package org.example.desktop 

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

// Función principal que inicia la aplicación de escritorio.
fun main() {
    val config = Lwjgl3ApplicationConfiguration().apply {
        setTitle("Beyond the Glass")
        setWindowedMode(1280, 720) // Establece una resolución inicial por si acaso
        setMaximized(true)
        useVsync(true)
        setWindowIcon("logo/beyondtheglasslogo.jpeg")
    }
    // Crea la aplicación de LibGDX, pasándole nuestra clase principal DesktopGame.
    Lwjgl3Application(DesktopGame(), config)
}