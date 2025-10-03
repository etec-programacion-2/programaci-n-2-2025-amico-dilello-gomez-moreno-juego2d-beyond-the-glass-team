package org.example.cli

import org.example.core.*
import java.io.File // Necesario para simular la existencia de level1.txt

// ----------------------------------------------------------------------
// Implementaciones/Adaptadores CLI (Deben estar en este archivo o importados)
// ----------------------------------------------------------------------

/**
 * ADAPTADOR CLI para RenderService.
 * DIP: Implementa la interfaz de RenderService para la consola.
 */
class CLI_RenderService : RenderService {
    override fun drawSprite(sprite: Any, x: Float, y: Float) {
        // En un entorno de consola, dibujar un sprite es solo una impresión.
    }
    
    override fun render() {
        // Imprime un separador para simular el cambio de frame.
        println("--- Renderizando nuevo frame de la consola ---")
    }
}

/**
 * ADAPTADOR CLI para InputService.
 * DIP: Implementa la interfaz de InputService para la consola.
 */
class CLI_InputService : InputService {
    override fun isKeyPressed(keyCode: Int): Boolean {
        // En CLI no hay detección de teclas en tiempo real, se simula con 'false'.
        return false
    }
    
    override fun getMousePosition(): Pair<Float, Float> = Pair(0.0f, 0.0f)
}

/**
 * ADAPTADOR CLI para GameStateManager.
 * DIP: Implementa la interfaz de GameStateManager.
 */
class CLI_GameStateManager(
    private val renderService: RenderService
) : GameStateManager {
    
    override fun changeState(newState: GameState) {
        println("CLI Manager: Nuevo estado -> $newState")
    }
    
    override fun update() {
        // En la CLI, el update real lo hace el GameEngine, esto es solo una traza.
        println("CLI Manager: Lógica de juego actualizada (simulada).")
    }
    
    override fun render() {
        renderService.render()
    }
}

// ----------------------------------------------------------------------
// FUNCIÓN MAIN: Orquestador del módulo CLI
// ----------------------------------------------------------------------

/**
 * Función principal para iniciar la aplicación de línea de comandos.
 * SRP: Única responsabilidad es configurar los adaptadores CLI, cargar el nivel e iniciar el GameEngine.
 */
fun main() {
    println("=== BEYOND THE GLASS - CLI ===")
    
    val levelLoader = LevelLoader()
    val juego = MiJuego() // Instancia del Core

    // --- DEMOSTRACIÓN DE CARGA DE NIVEL ---
    println("\n--- DEMOSTRACIÓN DE CARGA DE NIVEL ---")
    
    // Simulación de carga (asegúrate de que 'level1.txt' esté en resources del core)
    try {
        val levelData = levelLoader.loadLevel("level1.txt")
        
        // **PASO CLAVE DE INTEGRACIÓN:** Inicializa MiJuego con los datos cargados.
        juego.initializeLevel(levelData) 
        
        // Imprime el resumen para validar el éxito de la carga (Criterio de Aceptación)
        println("Nivel cargado con éxito. Resumen de entidades:")
        println(" -> Jugador comienza en: ${levelData.playerStart}")
        println(" -> Total de plataformas: ${levelData.platforms.size}")
        println(" -> Total de enemigos: ${levelData.enemies.size}")
        println(" -> Total de coleccionables: ${levelData.collectibles.size}")

    } catch (e: Exception) {
        println("FATAL ERROR: No se pudo cargar el nivel.")
        e.printStackTrace()
        return // Sale del main si hay un error
    }
    // ----------------------------------------

    // Lógica de juego (Setup del GameEngine con adaptadores CLI)
    val cliRenderService = CLI_RenderService()
    val cliInputService = CLI_InputService()
    val cliGameStateManager = CLI_GameStateManager(cliRenderService)
    
    // Inyección de Dependencias: El GameEngine recibe las implementaciones concretas (DIP).
    val consoleGame = GameEngine(
        cliRenderService, 
        cliInputService, 
        juego, // Implementa GameLogicService
        cliGameStateManager
    )
    
    println("\nIniciando simulación de juego...")
    println("Ingrese su nombre:")
    val nombre = readLine() ?: "Jugador"
    juego.startGame(nombre)

    // Simular 3 frames de juego para ver el efecto de la física
    val deltaTime = 1f / 60f // Delta time constante para simulación

    repeat(3) {
        // Llamada a la lógica del core (MiJuego) directamente, como lo haría el GameEngine
        juego.updateGame(deltaTime, cliInputService)
        consoleGame.updateFrame()
        println(juego.getGameInfo())
    }
    
    println("Presione Enter para terminar...")
    readLine()
    
    juego.stopGame()
    println("¡Gracias por jugar!")
}