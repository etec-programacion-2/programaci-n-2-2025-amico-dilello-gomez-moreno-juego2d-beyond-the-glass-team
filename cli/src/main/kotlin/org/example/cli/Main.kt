package org.example.cli

import org.example.core.GameEngine
import org.example.core.MiJuego

// ... (Definiciones de CLI_RenderService y CLI_InputService)

/**
 * Función principal para iniciar la aplicación de línea de comandos.
 */
fun main() {
    println("=== BEYOND THE GLASS - CLI ===")
    
    val cliRenderService = CLI_RenderService()
    val cliInputService = CLI_InputService()
    val juego = MiJuego()
    
    // 1. Crear el gestor de estados para la CLI.
    val cliGameStateManager = CLI_GameStateManager(cliRenderService)

    // 2. Se inyectan las cuatro dependencias en el GameEngine.
    val consoleGame = GameEngine(cliRenderService, cliInputService, juego, cliGameStateManager)
    
    // Lógica de inicio y simulación para la CLI.
    println("Ingrese su nombre:")
    val nombre = readLine() ?: "Jugador"
    juego.startGame(nombre)
    
    juego.updateScore(100)
    
    // Llamada al bucle central UNA sola vez (un frame) para demostrar la orquestación.
    // consoleGame.run() causaría un OutOfMemoryError, por lo que usamos updateFrame().
    consoleGame.updateFrame()

    println(juego.getGameInfo())
    
    println("Presione Enter para terminar...")
    readLine()
    
    juego.stopGame()
    println("¡Gracias por jugar!")
}