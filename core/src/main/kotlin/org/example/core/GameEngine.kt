package org.example.core

/**
 * El GameEngine es el orquestador central que ejecuta el Game Loop.
 */
class GameEngine(
    val renderService: RenderService,
    val inputService: InputService,
    private val gameLogicService: GameLogicService,
    private val gameStateManager: GameStateManager
) {
    private var lastTime = System.nanoTime()

    /**
     * Ejecuta una sola iteración (frame) del bucle principal del juego.
     */
    fun updateFrame() {
        val currentTime = System.nanoTime()
        val deltaTime = (currentTime - lastTime) / 1_000_000_000.0f
        lastTime = currentTime

        // 1. ACTUALIZAR ESTADO (Update)
        // Pasamos el inputService y deltaTime al gestor de estados.
        gameStateManager.update(inputService, deltaTime) 
        
        // 2. RENDERIZAR SALIDA (Render)
        gameStateManager.render()
    }
    
    fun run() {
        while (gameLogicService.isRunning()) {
            updateFrame()
        }
    }

    fun stop() {
        gameLogicService.stopGame()
    }
}
