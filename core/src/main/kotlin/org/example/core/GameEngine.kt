package org.example.core

/**
 * DIP/OCP: Contiene el bucle principal (Game Loop). Depende de abstracciones (interfaces), 
 * no de implementaciones concretas (CLI o LibGDX).
 */
class GameEngine(
    private val renderService: RenderService,
    private val inputService: InputService,
    private val juego: MiJuego,
    private val gameStateManager: GameStateManager
) {
    
    // Almacena el tiempo del último frame para calcular el DeltaTime
    private var lastFrameTime: Long = 0

    fun updateFrame() {
        // Calcular el tiempo transcurrido (DeltaTime) desde el último frame
        val currentTime = System.nanoTime()
        val deltaTime = if (lastFrameTime > 0) (currentTime - lastFrameTime) / 1_000_000_000.0f else 0.016f // 60 FPS aprox.
        lastFrameTime = currentTime
        
        // 1. UPDATE: Manda a MiJuego a actualizar su lógica.
        juego.updateGame(deltaTime, inputService) 
        
        // 2. RENDER: Manda al RenderManager a dibujar.
        gameStateManager.render()
    }
}