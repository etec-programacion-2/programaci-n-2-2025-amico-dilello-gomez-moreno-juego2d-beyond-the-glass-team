package org.example.core

/**
 * Define la interfaz para gestionar los estados del juego.
 * El método update ahora recibe el servicio de entrada y el delta time.
 */
interface GameStateManager {
    fun changeState(newState: GameState)
    fun update(inputService: InputService, deltaTime: Float)
    fun render()
}