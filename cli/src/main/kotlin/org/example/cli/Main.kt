package org.example.cli

import org.example.core.*
import java.lang.Thread.sleep

fun main() {
    println("=== BEYOND THE GLASS - CLI ===")

    val levelLoader = LevelLoader()
    val levelData = try {
        levelLoader.loadLevel("level1.txt")
    } catch (e: Exception) {
        println("FATAL ERROR: No se pudo cargar el nivel.")
        e.printStackTrace()
        return
    }

    val initialWorldState = WorldState(
        player = Player(position = levelData.playerStart.copy(), size = Vector2D(2.0f, 2.0f), currentDimension = Dimension.A),
        platforms = levelData.platforms,
        enemies = levelData.enemies,
        collectibles = levelData.collectibles,
        currentDimension = Dimension.A
    )

    val cliRenderService = CLI_RenderService()
    val cliInputService = CLI_InputService()
    val juego = MiJuego()

    // ¡AQUÍ ESTÁ LA CONEXIÓN QUE FALTABA!
    // Ahora le pasamos el 'cliInputService' al constructor.
    val cliGameStateManager = CLI_GameStateManager(cliRenderService, cliInputService, initialWorldState)

    val consoleGame = GameEngine(cliRenderService, cliInputService, juego, cliGameStateManager)

    // Iniciamos el hilo que escucha el teclado en segundo plano.
    cliInputService.start()

    println("\n¡El juego ha comenzado! Usa 'a', 'd', 'w', 's' y presiona Enter.")
    println("===> Presiona Ctrl+C en la terminal para detener el juego. <===")
    sleep(1000)

    try {
        while (true) {
            consoleGame.updateFrame()
            sleep(50)
        }
    } finally {
        cliInputService.stop()
        println("\nJuego detenido.")
    }
}