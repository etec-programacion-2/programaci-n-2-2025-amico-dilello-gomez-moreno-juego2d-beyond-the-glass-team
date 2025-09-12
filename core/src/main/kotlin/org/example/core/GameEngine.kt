package org.example.core

// El motor del juego que une todas las dependencias.
// Su constructor recibe las interfaces, cumpliendo con la Inversión de Dependencias.
class GameEngine(
    private val renderService: RenderService,
    private val inputService: InputService,
    private val gameLogicService: GameLogicService
) {
    // El bucle principal que controla la ejecución del juego.
    fun run() {
        while (gameLogicService.isRunning()) {
            if (inputService.isKeyPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
                gameLogicService.stopGame()
            }
            renderService.render()
        }
    }
}