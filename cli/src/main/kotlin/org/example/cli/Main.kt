package org.example.cli

import org.example.core.*

fun main() {
    println("=== BEYOND THE GLASS - CLI ===")

    val cliRenderService = CLI_RenderService()
    val cliInputService = CLI_InputService()
    val juego = MiJuego()
    val cliGameStateManager = CLI_GameStateManager(cliRenderService, juego)

    try {
        juego.loadLevel("level1.txt")
    } catch (e: Exception) {
        println("ERROR FATAL: ${e.message}")
        return
    }

    cliInputService.start()
    var gameRunning = true
    val frameTimeMillis = 100L

    while (gameRunning) {
        val action = cliInputService.getAction()
        if (System.`in`.available() > 0 && System.`in`.read().toChar().lowercaseChar() == 'q') {
            gameRunning = false
            continue
        }

        // CAMBIO: Llamamos a 'updateCli' en lugar de 'update'
        cliGameStateManager.updateCli(action, frameTimeMillis / 1000f)

        // CAMBIO: Llamamos a 'render' directamente desde el manager
        cliGameStateManager.render()
        
        Thread.sleep(frameTimeMillis)
    }

    cliInputService.stop()
    println("Juego terminado. ¡Gracias por jugar!")
}
