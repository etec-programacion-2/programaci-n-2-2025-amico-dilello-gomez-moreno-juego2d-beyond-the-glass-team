package org.example.cli

import org.example.core.*
import java.lang.Thread.sleep

fun main() {
    println("=== BEYOND THE GLASS - CLI ===")

    // --- Carga de Nivel ---
    val levelLoader = LevelLoader()
    val levelData = try {
        levelLoader.loadLevel("level1.txt").also {
            println("Nivel cargado con éxito. Resumen de entidades:")
            println(" -> Jugador comienza en: ${it.playerStart}")
            println(" -> Total de plataformas: ${it.platforms.size}")
            println("    - P1 Dimensión: ${it.platforms.firstOrNull()?.tangibleInDimension}")
            println(" -> Total de enemigos: ${it.enemies.size}")
            println(" -> Total de coleccionables: ${it.collectibles.size}")
        }
    } catch (e: Exception) {
        println("FATAL ERROR: No se pudo cargar el nivel.")
        e.printStackTrace()
        return
    }

    // 1. CREAR EL ESTADO INICIAL DEL MUNDO
    val initialPlayer = Player(
        position = levelData.playerStart.copy(),
        size = Vector2D(2.0f, 2.0f),
        currentDimension = Dimension.A
    )
    val initialWorldState = WorldState(
        player = initialPlayer,
        platforms = levelData.platforms,
        enemies = levelData.enemies,
        collectibles = levelData.collectibles,
        currentDimension = Dimension.A
    )

    // 2. Inicializar servicios y GameStateManager
    val cliRenderService = CLI_RenderService()
    val cliInputService = CLI_InputService()
    val juego = MiJuego()
    val cliGameStateManager = CLI_GameStateManager(cliRenderService, initialWorldState)

    // 3. Inicializar el GameEngine
    val consoleGame = GameEngine(cliRenderService, cliInputService, juego, cliGameStateManager)

    println("\nIniciando simulación de juego...")
    println("Ingrese su nombre:")
    val nombre = readLine() ?: "Jugador"
    juego.startGame(nombre)

    // 4. EL VERDADERO GAME LOOP INFINITO
    println("¡Simulación iniciada! La animación se repetirá sin parar.")
    println("===> Presiona Ctrl+C en la terminal para detener el juego. <===")
    sleep(2000) // Una pausa para que puedas leer el mensaje

    while (true) {
        consoleGame.updateFrame()
        sleep(150) // Pausa de 150ms para que la animación sea fluida
    }
}
