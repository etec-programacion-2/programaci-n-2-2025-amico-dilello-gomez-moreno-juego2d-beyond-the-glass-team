package org.example.cli

import org.example.core.MiJuego

/**
 * Interfaz de consola - USA la lógica del core
 */
fun main() {
    println("=== BEYOND THE GLASS - CLI ===")
    
    // Usar la lógica del core
    val juego = MiJuego()
    
    println("Ingrese su nombre:")
    val nombre = readLine() ?: "Jugador"
    
    juego.startGame(nombre)
    
    // Simular algunas acciones del juego
    juego.updateScore(100)
    println(juego.getGameInfo())
    
    juego.updateScore(50)
    println(juego.getGameInfo())
    
    println("Presione Enter para terminar...")
    readLine()
    
    juego.stopGame()
    println("¡Gracias por jugar!")
}

//de aca para abajo es lo nuevo

fun main() {
    // Escenario 1: Juego en la Consola
    println("Iniciando juego con la interfaz de consola...")
    val cliRenderService = CLI_RenderService()
    val cliInputService = object : InputService {
        // Implementación de InputService para la consola
        override fun isKeyPressed(keyCode: Int): Boolean = false
        override fun getMousePosition(): Pair<Float, Float> = Pair(0.0f, 0.0f)
    }
    val gameManager = object : GameStateManager {
        // Implementación de GameStateManager para el ejemplo
        override fun changeState(newState: GameState) {}
        override fun update() { println("Actualizando estado del juego...") }
        override fun render() { println("Renderizando estado del juego...") }
    }
    val consoleGame = GameEngine(cliRenderService, cliInputService, gameManager)
    // consoleGame.run() // Descomentar para ejecutar

    // ---

    // Escenario 2: Juego con JavaFX (conceptual)
    // Para esto necesitarías una clase principal de JavaFX y el código de configuración.
    // La idea es que la instanciación sería similar:
    // val javaFxRenderService = JavaFX_RenderService(graphicsContext)
    // val javaFxInputService = JavaFX_InputService(...)
    // val javaFxGame = GameEngine(javaFxRenderService, javaFxInputService, gameManager)
    // javaFxGame.run() // Descomentar para ejecutar
}
