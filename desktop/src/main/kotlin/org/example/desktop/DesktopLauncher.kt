package org.example.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

// Función principal que inicia la aplicación de escritorio.
fun main() {
    val config = Lwjgl3ApplicationConfiguration().apply {
        setTitle("Beyond the Glass")
        setWindowedMode(800, 600)
        useVsync(true)
    }
    // Crea la aplicación de LibGDX, pasándole nuestra clase principal DesktopGame.
    Lwjgl3Application(DesktopGame(), config)
}