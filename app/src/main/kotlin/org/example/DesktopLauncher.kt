package com.tudominio.tujuego

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main() {
    val config = Lwjgl3ApplicationConfiguration().apply {
        setTitle("Mi App Desktop con KTX")
        setWindowedMode(1024, 768)
        setForegroundFPS(60)
        useVsync(true)
    }
    
    Lwjgl3Application(MiJuego(), config)
}