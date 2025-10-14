package org.example.cli

import org.example.core.*

class CLI_GameStateManager(
    private val renderService: RenderService,
    private var worldState: WorldState
) : GameStateManager {

    override fun changeState(newState: GameState) {
        println("CLI Manager: Nuevo estado solicitado -> $newState")
    }

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