package org.example.desktop

import org.example.core.*

/**
 * Implementación de GameStateManager para LibGDX, ahora funcional.
 */
class GdxGameStateManager(
    private val renderService: GdxRenderService, // Usamos la implementación concreta mejorada
    private val juego: MiJuego
) : GameStateManager {
    
    private var currentState: GameState = GameState.Playing
    
    override fun changeState(newState: GameState) {
        currentState = newState
    }
    
    /**
     * El método update ahora tiene la firma correcta.
     * Delega la actualización de la lógica del juego a MiJuego.
     */
    override fun update(inputService: InputService, deltaTime: Float) {
        if (currentState == GameState.Playing) {
            juego.update(inputService, deltaTime)
        }
    }
    
    /**
     * El método render ahora le pide al render service que dibuje
     * el estado actual del juego (jugador y plataformas).
     */
    override fun render() {
        renderService.renderGameState(juego.getPlayer(), juego.getLevelData())
    }
}
