package org.example.core

// Define la interfaz para gestionar los estados del juego.
interface GameStateManager {
    fun changeState(newState: GameState)
    fun update()
    fun render()
}
