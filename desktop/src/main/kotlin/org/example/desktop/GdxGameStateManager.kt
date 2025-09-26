package org.example.desktop

import org.example.core.GameState
import org.example.core.GameStateManager
import org.example.core.RenderService
import org.example.core.MiJuego

/**
 * Implementación básica de GameStateManager para el entorno de escritorio (LibGDX).
 */
class GdxGameStateManager(
    private val renderService: RenderService,
    private val juego: MiJuego
) : GameStateManager {
    
    private var currentState: GameState = GameState.Menu
    
    override fun changeState(newState: GameState) {
        currentState = newState
        // Lógica de transición de estados
    }
    
    override fun update() {
        // Lógica de actualización del estado actual (p. ej., chequear entrada, mover entidades).
    }
    
    override fun render() {
        // Delega la tarea de dibujo al servicio de renderizado de LibGDX.
        renderService.render()
    }
}
