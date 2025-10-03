package org.example.core

/**
 * DIP: Define la interfaz para controlar el flujo y el estado de la aplicación.
 */
interface GameStateManager {
    fun changeState(newState: GameState)
    fun update()
    fun render()
}