package org.example.core

// Define el contrato para la lógica central del juego.
// El motor del juego dependerá solo de esta abstracción.
interface GameLogicService {
    fun startGame(playerName: String)
    fun updateScore(points: Int)
    fun getGameInfo(): String
    fun stopGame()
    fun isRunning(): Boolean
    fun getScore(): Int
}
