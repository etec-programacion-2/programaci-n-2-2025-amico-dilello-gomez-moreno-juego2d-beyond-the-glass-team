package org.example

import org.example.MiJuego
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setTitle("Beyond the Glass")
    config.setWindowedMode(1280, 720)
    config.useVsync(true)
    config.setForegroundFPS(60)

    Lwjgl3Application(MiJuego(), config)
}
