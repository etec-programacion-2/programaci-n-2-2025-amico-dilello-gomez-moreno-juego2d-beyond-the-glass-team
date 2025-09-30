package org.example.cli

import org.example.core.GameEngine
import org.example.core.MiJuego
import org.example.core.LevelLoader
import org.example.core.LevelData
// ... (imports y definiciones de CLI_RenderService, CLI_InputService, CLI_GameStateManager) ...

// ... (Definiciones de CLI_RenderService, CLI_InputService, CLI_GameStateManager) ...

/**
 * Función principal para iniciar la aplicación de línea de comandos.
 */
fun main() {
    println("=== BEYOND THE GLASS - CLI ===")

    // --- DEMOSTRACIÓN DE CARGA DE NIVEL (BLOQUE DE PRUEBA) ---
    println("\n--- DEMOSTRACIÓN DE CARGA DE NIVEL ---")
    val levelLoader = LevelLoader()
    var levelData: LevelData? = null
    
    try {
        // Carga el archivo de nivel.
        levelData = levelLoader.loadLevel("level1.txt")
        
        // --- CÓDIGO DE IMPRESIÓN REINSERTADO ---
        println("Nivel cargado con éxito. Resumen de entidades:")
        println(" -> Jugador comienza en: ${levelData.playerStart}")
        println(" -> Total de plataformas: ${levelData.platforms.size}")
        println("    - P1 Dimensión: ${levelData.platforms[0].tangibleInDimension}")
        println(" -> Total de enemigos: ${levelData.enemies.size}")
        println(" -> Total de coleccionables: ${levelData.collectibles.size}")
        // ----------------------------------------
        
    } catch (e: Exception) {
        println("FATAL ERROR: No se pudo cargar el nivel.")
        e.printStackTrace()
        return // Sale del main si hay un error
    }
    // --------------------------------------------------------

    // Lógica de juego (solo si la carga fue exitosa)
    val cliRenderService = CLI_RenderService()
    val cliInputService = CLI_InputService()
    val juego = MiJuego()
    val cliGameStateManager = CLI_GameStateManager(cliRenderService)
    val consoleGame = GameEngine(cliRenderService, cliInputService, juego, cliGameStateManager)
    
    println("\nIniciando simulación de juego...")
    println("Ingrese su nombre:")
    val nombre = readLine() ?: "Jugador"
    juego.startGame(nombre)
    juego.updateScore(100)
    
    consoleGame.updateFrame()

    println(juego.getGameInfo())
    
    println("Presione Enter para terminar...")
    readLine()
    
    juego.stopGame()
    println("¡Gracias por jugar!")
}
