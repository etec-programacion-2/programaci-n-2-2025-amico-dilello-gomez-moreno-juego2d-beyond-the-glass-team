package org.example.cli

import org.example.core.GameState
import org.example.core.GameStateManager
import org.example.core.RenderService
import org.example.core.InputService

/**
 * Implementación básica de GameStateManager para el entorno de línea de comandos.
 */
import org.example.core.*

class CLI_GameStateManager(
    private val renderService: RenderService,
    private var worldState: WorldState
) : GameStateManager {

    override fun changeState(newState: GameState) {
        println("CLI Manager: Nuevo estado solicitado -> $newState")
    }
    
    /**
     * El método update ahora coincide con la nueva firma de la interfaz.
     * Aunque los parámetros no se usan en la CLI, son necesarios para cumplir el contrato.
     */
    override fun update(inputService: InputService, deltaTime: Float) {
        println("CLI Manager: Lógica de juego actualizada (simulada). DeltaTime: $deltaTime")

    private var movingRight = true

    override fun update() {
        // Lógica de movimiento de ida y vuelta para una simulación visible
        if (movingRight) {
            if (worldState.player.position.x < 150.0f) {
                worldState.player.position.x += 10.0f
            } else {
                movingRight = false // Al llegar al final, cambia de dirección
            }
        } else {
            if (worldState.player.position.x > 100.0f) {
                worldState.player.position.x -= 10.0f
            } else {
                movingRight = true // Al llegar al principio, cambia de dirección
            }
        }
    }

    override fun render() {
        renderService.renderWorld(worldState)
    }

    fun setWorldState(initialState: WorldState) {
        this.worldState = initialState
    }
}
