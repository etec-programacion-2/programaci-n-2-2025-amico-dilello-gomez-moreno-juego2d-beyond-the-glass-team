package org.example.cli

import org.example.core.GameState
import org.example.core.GameStateManager
import org.example.core.RenderService

/**
 * ADAPTADOR CLI para GameStateManager.
 * DIP: Implementa la interfaz de GameStateManager.
 */
class CLI_GameStateManager(
    private val renderService: RenderService
) : GameStateManager {
    
    override fun changeState(newState: GameState) {
        println("CLI Manager: Nuevo estado -> $newState")
    }
    
    override fun update() {
        // En la CLI, el update real lo hace el GameEngine, esto es solo una traza.
        println("CLI Manager: Lógica de juego actualizada (simulada).")
    }
    
    override fun render() {
        renderService.render()
    }
}
