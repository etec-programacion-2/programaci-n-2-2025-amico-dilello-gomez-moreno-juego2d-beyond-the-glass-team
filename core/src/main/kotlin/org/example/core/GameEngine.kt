package org.example.core

/**
 * El GameEngine es el orquestador central que ejecuta el Game Loop.
 * Ahora inyecta todas las dependencias para gestionar un frame completo.
 * * @property gameStateManager El gestor encargado de la lógica y la renderización por estados.
 */
class GameEngine(
    val renderService: RenderService,
    val inputService: InputService,
    private val gameLogicService: GameLogicService,
    private val gameStateManager: GameStateManager // Nueva dependencia
) {
    /**
     * Ejecuta una sola iteración (frame) del bucle principal del juego.
     * Este método realiza la secuencia fundamental: PROCESAR -> ACTUALIZAR -> RENDERIZAR.
     */
    fun updateFrame() {
        // 1. PROCESAR ENTRADA (Input) - La entrada puede ser chequeada aquí o dentro del gestor de estados.
        // Aquí solo orquestamos, el GameStateManager se encargará de actuar sobre la entrada.
        
        // 2. ACTUALIZAR ESTADO (Update)
        // Se llama al gestor de estados para actualizar la física, lógica y reglas del juego.
        gameStateManager.update() 
        
        // 3. RENDERIZAR SALIDA (Render)
        // Se llama al gestor de estados para dibujar el frame actual.
        gameStateManager.render()
    }
    
    /**
     * Inicia un bucle continuo que llama a updateFrame(). 
     * Útil para entornos que no tienen un bucle nativo (como la CLI, aunque se recomienda usar updateFrame() allí).
     */
    fun run() {
        while (gameLogicService.isRunning()) {
            updateFrame()
        }
    }

    /**
     * Detiene el bucle de forma controlada a través del GameLogicService.
     */
    fun stop() {
        gameLogicService.stopGame()
    }
}
