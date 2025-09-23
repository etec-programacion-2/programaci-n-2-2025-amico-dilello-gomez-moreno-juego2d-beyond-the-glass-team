package org.example.core

// Una clase sellada (sealed class) es perfecta para representar los diferentes estados
// posibles de tu juego, como Menu, Playing, Pause, etc.
sealed class GameState {
    object Menu : GameState()
    object Playing : GameState()
    object Paused : GameState()
    object GameOver : GameState()
}