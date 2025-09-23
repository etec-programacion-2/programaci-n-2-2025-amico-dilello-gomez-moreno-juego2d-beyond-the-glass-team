package org.example.core

// El motor del juego que une todas las dependencias.
// Las propiedades se hacen públicas para que el DesktopGame pueda acceder a ellas directamente.
class GameEngine(
    val renderService: RenderService,
    val inputService: InputService,
    private val gameLogicService: GameLogicService
) {
    // El bucle principal que controla la ejecución del juego.
    fun run() {
        while (gameLogicService.isRunning()) {
            // Se usa el servicio de renderizado para dibujar el estado actual.
            renderService.render()
        }
    }
}
