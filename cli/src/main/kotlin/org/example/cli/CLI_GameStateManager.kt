package org.example.cli

import org.example.core.GameState
import org.example.core.GameStateManager
import org.example.core.RenderService
import org.example.core.InputService

/**
 * Implementación básica de GameStateManager para el entorno de línea de comandos.
 */
class CLI_GameStateManager(
    private val renderService: RenderService
) : GameStateManager {
    
    override fun changeState(newState: GameState) {
        println("CLI Manager: Nuevo estado -> $newState")
    }
    
    /**
     * El método update ahora coincide con la nueva firma de la interfaz.
     * Aunque los parámetros no se usan en la CLI, son necesarios para cumplir el contrato.
     */
    override fun update(inputService: InputService, deltaTime: Float) {
        println("CLI Manager: Lógica de juego actualizada (simulada). DeltaTime: $deltaTime")
    }
    
    override fun render() {
        // Delega la tarea de dibujo al servicio de renderizado de la CLI.
        renderService.render()
    }
}
