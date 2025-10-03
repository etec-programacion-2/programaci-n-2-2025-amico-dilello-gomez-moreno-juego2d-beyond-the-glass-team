package org.example.cli

import org.example.core.*

class CLI_GameStateManager(
    private val renderService: RenderService,
    // Debe gestionar el WorldState para pasarlo al RenderService (Separación de lógica y presentación)
    private var worldState: WorldState 
) : GameStateManager {
    
    override fun changeState(newState: GameState) {
        println("CLI Manager: Nuevo estado solicitado -> $newState")
    }
    
    override fun update() {
        // Lógica de movimiento simulado para demostrar que el loop funciona y se redibuja
        if (worldState.player.position.x < 150.0f) {
            worldState.player.position.x += 10.0f // Movimiento notable
        } else {
            // Simulación de cambio de dimensión
            worldState.player.currentDimension = Dimension.B 
        }
    }
    
    override fun render() {
        // Llama al método avanzado con el estado del mundo
        renderService.renderWorld(worldState)
    }
    
    // No estrictamente necesario si se inyecta en el constructor, pero útil
    fun setWorldState(initialState: WorldState) {
        this.worldState = initialState
    }
}
