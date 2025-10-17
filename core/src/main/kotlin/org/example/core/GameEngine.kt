package org.example.core

/**
 * El GameEngine es el orquestador central que agrupa los servicios del juego.
 * En la nueva arquitectura, ya no es responsable de mantener el bucle de juego,
 * esa responsabilidad recae en la plataforma (ej: DesktopGame).
 */
class GameEngine(
    // Mantiene las referencias a los servicios para que puedan ser accedidos.
    val renderService: RenderService,
    val inputService: InputService,
    private val gameLogicService: GameLogicService,
    private val gameStateManager: GameStateManager
) {
    // Los métodos run(), stop() y updateFrame() han sido eliminados
    // porque ya no son necesarios. El bucle de juego ahora es
    // manejado directamente por la implementación de la plataforma,
    // como se ve en 'DesktopGame.kt', que llama a los servicios
    // directamente en su propio método render().
    //
    // Esta clase se mantiene por si en el futuro se quiere añadir lógica
    // que sea común a todas las plataformas.
}
