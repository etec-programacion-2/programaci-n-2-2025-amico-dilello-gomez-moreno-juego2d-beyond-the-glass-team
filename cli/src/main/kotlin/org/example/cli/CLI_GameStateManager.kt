package org.example.cli

import org.example.core.*

// CAMBIO CLAVE: El constructor ahora también pide el 'inputService'.
class CLI_GameStateManager(
    private val renderService: RenderService,
    private val inputService: InputService,
    private var worldState: WorldState
) : GameStateManager {

    override fun changeState(newState: GameState) {
        println("CLI Manager: Nuevo estado solicitado -> $newState")
    }

    override fun update() {
        // Esta lógica ahora funcionará porque sí recibe las teclas.
        val action = inputService.getAction()

        worldState = when (action) {
            GameAction.MOVE_LEFT -> {
                val newPlayer = worldState.player.copy(
                    position = worldState.player.position.copy(x = worldState.player.position.x - 10.0f)
                )
                worldState.copy(player = newPlayer)
            }
            GameAction.MOVE_RIGHT -> {
                val newPlayer = worldState.player.copy(
                    position = worldState.player.position.copy(x = worldState.player.position.x + 10.0f)
                )
                worldState.copy(player = newPlayer)
            }
            GameAction.JUMP -> {
                println("¡Acción de SALTO recibida!") // Mapeado a 'w'
                worldState
            }
            GameAction.SWITCH_DIMENSION -> {
                // Mapeado a 's' (shift).
                val newDimension = if (worldState.currentDimension == Dimension.A) Dimension.B else Dimension.A
                worldState.copy(currentDimension = newDimension)
            }
            GameAction.NONE -> {
                worldState
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