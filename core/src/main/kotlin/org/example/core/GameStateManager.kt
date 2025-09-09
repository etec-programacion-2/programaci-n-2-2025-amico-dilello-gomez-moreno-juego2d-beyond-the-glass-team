// Archivo: GameStateManager.kt

interface GameStateManager {
    /**
     * Cambia el estado actual del juego.
     */
    fun changeState(newState: GameState)

    /**
     * Actualiza el estado actual del juego.
     */
    fun update()

    /**
     * Dibuja el estado actual del juego.
     */
    fun render()
}