package org.example.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

/**
 * Punto de entrada (`main`) de la aplicación de escritorio.
 * Su única responsabilidad es configurar y lanzar la ventana de LibGDX.
 *
 * ---
 * @see "Issue BTG-001: Configuración del proyecto multi-módulo (desktop)."
 * ---
 */
fun main() {
    // Configuración de la ventana
    val config = Lwjgl3ApplicationConfiguration().apply {
        setTitle("Beyond the Glass") // Título de la ventana
        setWindowedMode(800, 600) // Tamaño de la ventana
        useVsync(true) // Sincronización vertical
    }
    // Crea la aplicación de LibGDX, pasándole nuestra clase principal DesktopGame.
    Lwjgl3Application(DesktopGame(), config)
}