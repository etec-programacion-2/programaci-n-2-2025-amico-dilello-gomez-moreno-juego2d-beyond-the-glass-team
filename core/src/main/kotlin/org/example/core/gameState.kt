package org.example.core

/**
 * Una clase sellada (sealed class) para representar los diferentes estados
 * posibles del juego (Máquina de Estados).
 * Es más robusto que un 'enum' porque los estados pueden contener datos.
 *
 * ---
 * @see "Issue BTG-008: Bucle de Juego y Gestión de Estado."
 * @see "Issue BTG-012: Estado 'GameOver'."
 * ---
 */
sealed class GameState {
    /** * (MODIFICADO) Estado de Menú Principal.
     * Ahora es el estado por defecto.
     */
    object Menu : GameState()
    
    object Playing : GameState()    // Estado de Jugando
    object Paused : GameState()     // Estado de Pausa
    object GameOver : GameState()   // Estado de Fin de Juego
    
    /** Estado para cuando el jugador gana el juego. */
    object GameWon : GameState()
}