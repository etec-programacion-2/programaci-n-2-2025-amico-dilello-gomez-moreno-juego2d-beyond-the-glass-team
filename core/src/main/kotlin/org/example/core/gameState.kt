package org.example.core

/**
 * Una clase sellada (sealed class) para representar los diferentes estados
 * posibles del juego (Máquina de Estados).
 * Es más robusto que un 'enum' porque los estados pueden contener datos.
 */
sealed class GameState {
    object Menu : GameState()       // Estado de Menú Principal
    object Playing : GameState()    // Estado de Jugando
    object Paused : GameState()     // Estado de Pausa
    object GameOver : GameState()   // Estado de Fin de Juego
}
