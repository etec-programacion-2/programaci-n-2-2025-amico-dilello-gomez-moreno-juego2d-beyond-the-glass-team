package org.example.cli

import org.example.core.*

class CLI_GameStateManager(
    private val renderService: RenderService,
    private val juego: MiJuego
) : GameStateManager {

    override fun changeState(newState: GameState) {
        // Por ahora no hace nada, pero cumple el contrato.
    }

    // MÉTODO AÑADIDO: Cumple con el requisito de la interfaz.
    override fun update() {
        // La lógica de actualización real ya se maneja en Main.kt,
        // así que este método puede quedar vacío por ahora.
    }

    // 'override' AÑADIDO: Cumple con el requisito de la interfaz.
    override fun render() {
        val worldState = juego.getWorldState()
        renderService.renderWorld(worldState)
    }

    // Estos son los métodos que usamos en Main.kt, no forman parte de la interfaz.
    fun updateCli(action: GameAction, deltaTime: Float) {
        juego.update(action, deltaTime)
    }
}
