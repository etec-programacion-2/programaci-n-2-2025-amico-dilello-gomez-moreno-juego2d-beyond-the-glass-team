package org.example.cli

import org.example.core.* import java.lang.Thread.sleep // Importar Thread.sleep para la simulación

/**
 * Función principal para iniciar la aplicación de línea de comandos.
 */
fun main() {
    println("=== BEYOND THE GLASS - CLI ===")

    // --- Carga de Nivel ---
    val levelLoader = LevelLoader()
    var levelData: LevelData? = null
    
    try {
        levelData = levelLoader.loadLevel("level1.txt")
        println("Nivel cargado con éxito. Resumen de entidades:")
        println(" -> Jugador comienza en: ${levelData.playerStart}")
        println(" -> Total de plataformas: ${levelData.platforms.size}")
        println("    - P1 Dimensión: ${levelData.platforms[0].tangibleInDimension}")
        println(" -> Total de enemigos: ${levelData.enemies.size}")
        println(" -> Total de coleccionables: ${levelData.collectibles.size}")
        
    } catch (e: Exception) {
        println("FATAL ERROR: No se pudo cargar el nivel.")
        e.printStackTrace()
        return 
    }
    
    val finalLevelData = levelData!! 

    // 1. CREAR EL ESTADO INICIAL DEL MUNDO (WorldState)
    val initialPlayer = Player(
        position = finalLevelData.playerStart.copy(),
        size = Vector2D(2.0f, 2.0f), 
        currentDimension = Dimension.A
    )
    val initialWorldState = WorldState(
        player = initialPlayer,
        platforms = finalLevelData.platforms,
        enemies = finalLevelData.enemies,
        collectibles = finalLevelData.collectibles,
        currentDimension = Dimension.A 
    )
    
    // 2. Inicializar servicios y GameStateManager
    val cliRenderService = CLI_RenderService()
    val cliInputService = CLI_InputService()
    val juego = MiJuego()
    
    // Inyectamos las dependencias
    val cliGameStateManager = CLI_GameStateManager(cliRenderService, initialWorldState)
    
    // 3. Inicializar el GameEngine
    val consoleGame = GameEngine(cliRenderService, cliInputService, juego, cliGameStateManager)
    
    println("\nIniciando simulación de juego...")
    println("Ingrese su nombre:")
    val nombre = readLine() ?: "Jugador"
    juego.startGame(nombre)
    
    // 4. EL GAME LOOP DE SIMULACIÓN (Ejecuta múltiples frames y redibuja - Criterio BTG-008)
    val framesToRun = 10 
    println("Simulando $framesToRun frames (verá al jugador moverse)...")

    repeat(framesToRun) { frame ->
        consoleGame.updateFrame()
        sleep(200) // Pausa de 200ms para poder observar el movimiento
    }
    
    println("\nSimulación terminada.")
    println(juego.getGameInfo())
    
    println("Presione Enter para terminar...")
    readLine()
    
    juego.stopGame()
}
