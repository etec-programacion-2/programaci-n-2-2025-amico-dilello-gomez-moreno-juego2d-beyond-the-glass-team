package org.example.cli

import org.example.core.GameState
import org.example.core.GameStateManager
import org.example.core.RenderService

/**
 * Implementación básica de GameStateManager para el entorno de línea de comandos.
 * Se enfoca principalmente en registrar eventos y delegar el renderizado a la CLI.
 */
class CLI_GameStateManager(
    private val renderService: RenderService
) : GameStateManager {
    
    override fun changeState(newState: GameState) {
        println("CLI Manager: Nuevo estado -> $newState")
    }
    
    override fun update() {
        println("CLI Manager: Lógica de juego actualizada (simulada).")
    }
    
    override fun render() {
        // Delega la tarea de dibujo al servicio de renderizado de la CLI.
        renderService.render()
    }
}